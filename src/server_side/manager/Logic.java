package server_side.manager;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import server_side.database.DBUtil;
import server_side.model.Bot;
import shared.enums.BoardTypes;
import shared.enums.MessageType;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.Tower;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.timerTasks.CoordinateUpdater;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.*;

public class Logic implements Runnable {
    private ArrayBlockingQueue<Message> inGameInbox; // for adding new events
    private ArrayBlockingQueue<Message> toCheckEvents; // the events to check - the resultant of these events will cause to make a new event
    private Board board;
    private String gameMode;
    private Player humanPlayer;
    private Bot botPlayer;
    private int cardUsedNum;
    private String winner;
    private ExecutorService executor;

    public Logic(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> toCheckEvents , String gameMode, Player humanPlayer , Bot botPlayer , ExecutorService executor)
    {
        this.executor = executor;
        cardUsedNum = 0;
        this.humanPlayer = humanPlayer;
        this.botPlayer = botPlayer;
        this.inGameInbox = inGameInbox;
        this.gameMode = gameMode;
        this.toCheckEvents = toCheckEvents;
        this.board = new Board(gameMode.equals("2v2") ? BoardTypes.FOUR_PLAYERS : BoardTypes.TWO_PLAYERS , true , humanPlayer.getName() , humanPlayer.getLevel() , gameMode);
        board.addTowers(true,board.getType() , humanPlayer.getName() , inGameInbox , null);
    }

    public boolean isFinished(boolean isTimerFinished) throws InterruptedException{
        ArrayList<Tower> playerTowers = board.getOfTowers(humanPlayer.getName());
        ArrayList<Tower> botTowers = board.getOfTowers(botPlayer.getName());
        if (isTimerFinished)
        {
            if (playerTowers.size() == 0)
            {
                winner = botPlayer.getName();
                return true;
            }
            else if (botTowers.size() == 0)
            {
                winner = humanPlayer.getName();
                return true;
            }
            else {
                int playerHP = 0;
                int botHP = 0;
                for (Tower tower:playerTowers)
                    playerHP += tower.getHp();
                for (Tower tower:botTowers)
                    botHP += tower.getHp();

                winner = playerHP > botHP ? humanPlayer.getName() : botPlayer.getName();
                return true;
            }
        }else {
            if (playerTowers.size() != 0 && botTowers.size() != 0)
                return false;
            winner = playerTowers.size() == 0 ? botPlayer.getName() : humanPlayer.getName();
            return true;
        }
    }
    public void winnerUpdate(){
        if(winner.contains("bot"))
        {
            humanPlayer.updateProperty(70);
        }
        else {
            humanPlayer.updateProperty(200);
        }
        try {
            System.out.println("in winner update going to send game result...");
            inGameInbox.put(new Message(MessageType.GAME_RESULT, "Server",winner));
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        DBUtil dbUtil = new DBUtil();
        dbUtil.updateLevelXp(humanPlayer);
        dbUtil.updateHistory(humanPlayer, botPlayer.getName(), winner);
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
        try {
            while (!isFinished(false))
            {
                Message event = null;
                if (!toCheckEvents.isEmpty())
                {
                    try {
                        event = toCheckEvents.take();
                        if (event.getType() == MessageType.PICKED_CARD && !isFinished(false))
                        {
                            System.out.println("Got picked card");
                            this.firstTimeHandle(event);
                        }
                        else if (event.getType() == MessageType.CHARACTER_DIED && !isFinished(false))
                        {
                            Troop died = board.getTroopByID(event.getContent());
                            if (died == null)
                                System.out.println("Couldn't find the died troop in logic. Shouldn't happen . Error!");
                            else {
                                board.destroy(died);
                                for (Troop troop :board.getAddedTroops())
                                {
                                    troop.updateState(board,died,true);
                                }
                                if (isFinished(false))
                                    break;
                            }
                        }
                        else { // finished
                            winnerUpdate();
                        }
                    }catch (InterruptedException e)
                    {
                        boolean isFinished = isFinished(true);
                        System.out.println("Interrupted in getting new event in logic (toCheckEvents).");
                        if (isFinished)
                            break;
                    }
                }
            }
            winnerUpdate();
        }catch (InterruptedException e)
        {
            try {
                boolean isFinished = isFinished(true);
                if (isFinished)
                    winnerUpdate();
                else
                    System.out.println("the game isn't finished but it interrupted because of time");
            }catch (InterruptedException e2)
            {
                System.out.println("interrupted in checking is finished.");
                e2.printStackTrace();
            }
        }
    }

    public void firstTimeHandle(Message event) throws InterruptedException {
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
            if (chosen instanceof SoldierCard)
            {
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                CoordinateUpdater coordinateUpdater = new CoordinateUpdater((SoldierCard) chosen , getBoard().getCoordinateUpdateQueue(),scheduledExecutorService);
                scheduledExecutorService.scheduleAtFixedRate(coordinateUpdater,0,16,TimeUnit.MILLISECONDS);
            }
        }
        else
        { // it may need to be changed for some special cases - it is used for adding troops that their count is more than 1 .
            chosen.setCoordinates(new Point2D(tileX, tileY));
            board.addTroop(chosen);
            cardUsedNum++;
            chosen.setId(cardUsedNum);
            chosen.updateState(board,null,false);
            // just barbarian and archer are more than one troop

            if (chosen instanceof SoldierCard)
            {
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                CoordinateUpdater coordinateUpdater = new CoordinateUpdater((SoldierCard) chosen , getBoard().getCoordinateUpdateQueue(),scheduledExecutorService);
                scheduledExecutorService.scheduleAtFixedRate(coordinateUpdater,0,16,TimeUnit.MILLISECONDS);
            }

            for (int i = 0 ; i < chosen.getCount() - 1 ; i++)
            {
                Card newCard = null;
                if (board.isValidAddress(chosen , tileX - 1 , tileY))

                    newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX - 1 , tileY) , chosen.getOwner());

                else if (board.isValidAddress(chosen,tileX + 1 , tileY))//not valid
                    newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX + 1 , tileY) , chosen.getOwner());
                else if (board.isValidAddress(chosen,tileX , tileY -1))
                    newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX , tileY - 1) , chosen.getOwner());
                else if (board.isValidAddress(chosen,tileX , tileY  + 1))
                    newCard = (Card) Troop.makeTroop(true,chosen.getType().toString(),chosen.getLevel(),new Point2D(tileX , tileY + 1) , chosen.getOwner());
                newCard.setInGameInbox(inGameInbox);
                cardUsedNum++;
                newCard.setId(cardUsedNum);
                board.addTroop(newCard);
                newCard.updateState(board,null,false);
                if (newCard instanceof SoldierCard)
                {
                    ScheduledExecutorService scheduledExecutorService1 = Executors.newScheduledThreadPool(1);
                    CoordinateUpdater coordinateUpdater1 = new CoordinateUpdater((SoldierCard) newCard, board.getCoordinateUpdateQueue(),scheduledExecutorService1);
                    scheduledExecutorService1.scheduleAtFixedRate(coordinateUpdater1,0,16,TimeUnit.MILLISECONDS);
                }

            }
        }
    }


    public Card getCardByStr(String cardType , String owner){
        return (Card)Troop.makeTroop(true,cardType, humanPlayer.getLevel(), null,owner);
    }

    /**
     * getter
     * @return board of game
     */
    public Board getBoard() {
        return board;
    }
}
