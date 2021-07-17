package server_side.manager;

import shared.enums.BoardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.card.Card;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

public class Logic implements Runnable {
    private ArrayBlockingQueue<Message> inGameInbox; // for adding new events
    private ArrayBlockingQueue<Message> toCheckEvents; // the events to check - the resultant of these events will cause to make a new event
    private Timer timer;
    private TimerTask endOfGame;
    private Board board;
    private String gameMode;
    private Player humanPlayer;

    public Logic(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> toCheckEvents ,String gameMode,Player humanPlayer)
    {
        this.humanPlayer = humanPlayer;
        this.inGameInbox = inGameInbox;
        this.gameMode = gameMode;
        endOfGame = new TimerTask(){
            public void run()
            {
                timeEnded();
            }
        };
        timer = new Timer();
        this.toCheckEvents = toCheckEvents;
        this.board = new Board(gameMode.equals("2v2") ? BoardTypes.FOUR_PLAYERS : BoardTypes.TWO_PLAYERS , true , humanPlayer.getName() , humanPlayer.getLevel() , gameMode);
        board.addTowers(board.getType() , humanPlayer.getName());
    }

//    public boolean isFinished(){
//        // if finished , then cancel the timer
//    }

    public void timeEnded(){

    }

    public void addToCheckEvent(Message newEvent){
        try {
            toCheckEvents.put(newEvent);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        timer.schedule(endOfGame , 3 * 60 * 1000); // timer for 3 minutes
        while (true)
        {
            Message event = null;
            if (!toCheckEvents.isEmpty())
            {
                try {
                    event = toCheckEvents.take();
                    this.handle(event);
                }catch (InterruptedException e)
                {
                    System.out.println("Interrupted in getting new event in logic (toCheckEvents). Shouldn't happen. Error!");
                    e.printStackTrace();
                }
            }


        }
    }

    public void handle(Message event){
        // calculate where to go and in how much time
        String playerName = event.getSender();
        String[] split = event.getContent().split(",");
        String cardType = split[0];
        int tileX = Integer.parseInt(split[1]);
        int tileY = Integer.parseInt(split[2]);
        Card chosen = getCardByStr(cardType);




    }



    public Card getCardByStr(String cardType){
        for (Card card:humanPlayer.getCards())
        {
            if (cardType.equals(card.getType().toString()))
                return card;
        }
        return null;
    }


}
