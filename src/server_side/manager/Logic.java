package server_side.manager;

import javafx.geometry.Point2D;
import server_side.model.Bot;
import shared.enums.BoardTypes;
import shared.enums.MessageType;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
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
    private int cardUsedNum;

    public Logic(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> toCheckEvents ,String gameMode,Player humanPlayer , Bot botPlayer)
    {
        cardUsedNum = 0;
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
        board.addTowers(board.getType() , humanPlayer.getName() , inGameInbox);
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
                if (event.getType() == MessageType.PICKED_CARD)
                {
                    this.firstTimeHandle(event);
                }
                else if (event.getType() == MessageType.CHARACTER_DIED)
                {
                    
                }
            }


        }
    }

    public void firstTimeHandle(Message event) {
        // calculate where to go and in how much time
        String playerName = event.getSender();
        String[] split = event.getContent().split(","); // cardType , x , y
        String cardType = split[0];
        int tileX = Integer.parseInt(split[1]);
        int tileY = Integer.parseInt(split[2]);
        Card chosen = getCardByStr(cardType, playerName);
        chosen.setInGameInbox(inGameInbox);
        if (chosen.getCount() == 1) // valid is checked before
        {
            chosen.setCoordinates(new Point2D(tileX, tileY));
            cardUsedNum++;
            chosen.setId(cardUsedNum);
            board.addTroop(chosen);
            chosen.updateState(board,null,false);
        }
        else
        { // it may need to be changed for some special cases - it is used for adding troops that their count is more than 1 .
            chosen.setCoordinates(new Point2D(tileX, tileY));
            board.addTroop(chosen);
            chosen.updateState(board,null,false);
            cardUsedNum++;
            chosen.setId(cardUsedNum);
            for (int i = 0 ; i < chosen.getCount() - 1 ; i++)
            {
                if (board.isValidAddress(chosen , tileX - 1 , tileY))
                {
                    Card newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX - 1 , tileY) , chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    board.addTroop(newCard);
                    newCard.updateState(board,null,false);
                }
                else if (board.isValidAddress(chosen,tileX + 1 , tileY))//not valid
                {
                    Card newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX + 1 , tileY) , chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    board.addTroop(newCard);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    newCard.updateState(board,null,false);
                }
                else if (board.isValidAddress(chosen,tileX , tileY -1))
                {
                    Card newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX , tileY - 1) , chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    board.addTroop(newCard);
                    newCard.updateState(board,null,false);
                }
                else if (board.isValidAddress(chosen,tileX , tileY  + 1))
                {
                    Card newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX , tileY + 1) , chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    board.addTroop(newCard);
                    newCard.updateState(board,null,false);
                }
            }
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
