package server_side.manager;

import javafx.geometry.Point2D;
import server_side.model.Bot;
import shared.enums.BoardTypes;
import shared.enums.CardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.Card;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.card.SpellCard;
import shared.model.troops.timerTasks.CoordinateUpdater;

import java.util.ArrayList;
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
    private Bot botPlayer;

    public Logic(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> toCheckEvents ,String gameMode,Player humanPlayer , Bot botPlayer)
    {
        this.humanPlayer = humanPlayer;
        this.botPlayer = botPlayer;
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
                }catch (InterruptedException e)
                {
                    System.out.println("Interrupted in getting new event in logic (toCheckEvents). Shouldn't happen. Error!");
                    e.printStackTrace();
                }
                this.firstTimeHandle(event);
            }


        }
    }

    public void firstTimeHandle(Message event){
        // calculate where to go and in how much time
        String playerName = event.getSender();
        String[] split = event.getContent().split(","); // cardType , x , y
        String cardType = split[0];
        int tileX = Integer.parseInt(split[1]);
        int tileY = Integer.parseInt(split[2]);
        Card chosen = getCardByStr(cardType , playerName);
        chosen.setCoordinates(new Point2D(tileX , tileY));
        board.addTroop(chosen);
        if (chosen instanceof SoldierCard)
        {
            SoldierCard soldier = (SoldierCard) chosen;
            String direction = soldier.decideWhereToGo(board);
            Timer timer = new Timer();
            timer.schedule(new CoordinateUpdater(soldier,direction, board.getCoordinateUpdateQueue()) , 0 ,  1000 / soldier.getMovingSpeed().getValue());
            board.getTroopsTimer().put(soldier ,timer);
        }
        else if (chosen instanceof BuildingCard)
        {
            BuildingCard buildingCard = (BuildingCard) chosen;
        }
        else if (chosen.getType() == CardTypes.RAGE)
        {
            


        }
        else if (chosen instanceof SpellCard) // arrows and fireball
        {

        }


    }



    public Card getCardByStr(String cardType , String owner){
        ArrayList<Card> toGetFrom = owner.contains("bot") ? botPlayer.getDeck() : humanPlayer.getCards(); // check is according to owner name
        for (Card card:toGetFrom)
        {
            if (cardType.equals(card.getType().toString()))
                return card;
        }
        return null;
    }


}
