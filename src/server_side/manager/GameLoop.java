package server_side.manager;

import shared.enums.MessageType;
import shared.model.player.Player;
import server_side.model.Bot;
import shared.model.Message;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

public class GameLoop {
    private ArrayList<Player> players;
    private Bot bot;
    private ArrayBlockingQueue<Message> inGameInbox;
    private ArrayBlockingQueue<Message> incomingEventsForBots;
    private Logic logic;
    private ExecutorService executor;
    private String gameMode;
    private TimerTask timeEnded;
    private Timer timer;


    public GameLoop(String gameMode,ArrayList<Player> players, Bot bot , ArrayBlockingQueue<Message> inGameInbox ,
                    ArrayBlockingQueue<Message> incomingEventsForBots ,ExecutorService executor){
        this.gameMode = gameMode;
        this.players = players;
        this.timer = new Timer();
        if (bot != null)
            this.bot = bot;
        this.inGameInbox = inGameInbox;
        logic = new Logic(inGameInbox, new ArrayBlockingQueue<>(50) ,gameMode,players.get(0) , bot , executor);
        this.executor = executor;
        timeEnded = new TimerTask() {
            @Override
            public void run() {
                executor.shutdownNow();
            }
        };
        this.incomingEventsForBots = incomingEventsForBots;

    }

    public void play(){
        if (bot != null){
            bot.setBoard(logic.getBoard());
            executor.execute(bot);
        }
        executor.execute(logic.getBoard());
        executor.execute(logic);
        timer.schedule(timeEnded , 3 * 60 * 1000); // timer for 3 minutes
        Message event = null;
        for (Player player: players)
        {
            player.getSender().sendMsg(new Message(MessageType.DATA , "Server" , bot.getName() + "," + bot.getLevel())); // just implemented for bot!!!
        }

        while (true)
        {
            try {
                event = inGameInbox.take();
            }catch (InterruptedException e)
            {
                System.out.println("Interrupted in taking message from inGameInbox. Shouldn't happen. Error!"); // handle later
                e.printStackTrace();
            }
            notifyPlayers(event);
            if (event.getType() != MessageType.GAME_RESULT)
                logic.addToCheckEvent(event);
            else { // game is finished
                System.out.println("Game finished.");
            }
        }
    }

    /**
     * sending message to all players and bots
     * @param newEvent the newEvent
     */
    private void notifyPlayers(Message newEvent){
        if ((bot != null && !newEvent.getSender().contains("bot")) || (bot!= null && (newEvent.getType() == MessageType.GAME_RESULT || newEvent.getType() == MessageType.CHARACTER_DIED)  ) )
        {
            try {
                bot.getIncomingEvents().put(newEvent);
            }catch (InterruptedException e)
            {
                System.out.println("Interrupted in putting message to incomingEvents for bots!");
                e.printStackTrace();
            }
        }
        for (Player ply:players)
        {
            if (newEvent.getType() == MessageType.GAME_RESULT || newEvent.getType() == MessageType.CHARACTER_DIED)
            {
                System.out.println("sent " + newEvent.getType() + "for " + ply.getName());
                ply.getSender().sendMsg(newEvent);
            }
            else if (!ply.getName().equals(newEvent.getSender()))
            {
                ply.getSender().sendMsg(newEvent);
            }
        }
        System.out.println("end of notify players of gameLoop");
    }
}
