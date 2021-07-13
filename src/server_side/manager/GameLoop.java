package server_side.manager;

import shared.model.player.Player;
import server_side.model.Bot;
import shared.model.Message;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

public class GameLoop {
    private ArrayList<Player> players;
    private Bot bot;
    private ArrayBlockingQueue<Message> inGameInbox;
    private ArrayBlockingQueue<Message> incomingEventsForBots;
    private Logic logic;
    private ExecutorService executor;


    public GameLoop(ArrayList<Player> players, Bot bot , ArrayBlockingQueue<Message> inGameInbox ,
                    ArrayBlockingQueue<Message> incomingEventsForBots ,ExecutorService executor){
        this.players = players;
        if (bot != null)
            this.bot = bot;
        this.inGameInbox = inGameInbox;
        logic = new Logic(inGameInbox, new ArrayBlockingQueue<>(50));
        this.executor = executor;
        this.incomingEventsForBots = incomingEventsForBots;
    }

    public void play(){
        executor.execute(logic);
        Message event = null;

        // we have to send configs here(for 1v1 or 2v2 ...) !!! (level 3 tower , level 2 barbarian ...)

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
            logic.addToCheckEvent(event);
//            if (logic.isFinished()) // handle later
//                break;
        }
    }

    /**
     * sending message to all players and bots
     * @param newEvent the newEvent
     */
    private void notifyPlayers(Message newEvent){
        if (bot != null)
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
            ply.getSender().sendMsg(newEvent);
        }
    }
}
