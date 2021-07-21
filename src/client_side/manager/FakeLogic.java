package client_side.manager;

import client_side.view.render.Render;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import shared.enums.MessageType;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.Troop;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.Card;

import java.util.concurrent.ArrayBlockingQueue;

public class FakeLogic implements Runnable{
    private ArrayBlockingQueue<Message> inGameInbox;
    private Player humanPlayer;
    private String botName;
    private int cardUsedNum;
    private String winner; // can be changed later
    private String gameMode;
    private Manager manager;
    private Board board;
    private Render render;


    public FakeLogic(Player humanPlayer,
                     String botName, String gameMode, Manager manager, Board board ,Render render) {
        this.humanPlayer = humanPlayer;
        this.inGameInbox = humanPlayer.getSharedInbox();
        this.botName = botName;
        this.cardUsedNum = 0;
        this.gameMode = gameMode;
        this.manager = manager;
        this.board = board;
        this.render = render;
    }


    private void addAndInitCard(Message event) {
        String playerName = event.getSender();
        String[] split = event.getContent().split(","); // cardType , x , y
        String cardType = split[0];
        int tileX = Integer.parseInt(split[1]);
        int tileY = Integer.parseInt(split[2]);
        Card chosen = getCardByStr(cardType, playerName);
        chosen.setInGameInbox(inGameInbox);
        if (chosen.getCount() == 1) {
            chosen.setCoordinates(new Point2D(tileX, tileY));
            cardUsedNum++;
            chosen.setId(cardUsedNum);
            board.addTroop(chosen);
            chosen.updateState(board, null, false);
            chosen.setRender(render);
            render.addForRender(chosen , false);
        } else {
            chosen.setCoordinates(new Point2D(tileX, tileY));
            board.addTroop(chosen);
            chosen.updateState(board, null, false);
            cardUsedNum++;
            chosen.setId(cardUsedNum);
            chosen.setRender(render);
            render.addForRender(chosen,false);
            for (int i = 0; i < chosen.getCount() - 1; i++) {
                Card newCard = null;
                if (board.isValidAddress(chosen, tileX - 1, tileY)) {
                    newCard = (Card) Troop.makeTroop(true, chosen.getType().toString(), chosen.getLevel(), new Point2D(tileX - 1, tileY), chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    board.addTroop(newCard);
                    newCard.updateState(board, null, false);
                } else if (board.isValidAddress(chosen, tileX + 1, tileY))//not valid
                {
                    newCard = (Card) Troop.makeTroop(true, chosen.getType().toString(), chosen.getLevel(), new Point2D(tileX + 1, tileY), chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    board.addTroop(newCard);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    newCard.updateState(board, null, false);
                } else if (board.isValidAddress(chosen, tileX, tileY - 1)) {
                    newCard = (Card) Troop.makeTroop(true, chosen.getType().toString(), chosen.getLevel(), new Point2D(tileX, tileY - 1), chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    board.addTroop(newCard);
                    newCard.updateState(board, null, false);
                } else if (board.isValidAddress(chosen, tileX, tileY + 1)) {
                    newCard = (Card) Troop.makeTroop(true, chosen.getType().toString(), chosen.getLevel(), new Point2D(tileX, tileY + 1), chosen.getOwner());
                    newCard.setInGameInbox(inGameInbox);
                    cardUsedNum++;
                    newCard.setId(cardUsedNum);
                    board.addTroop(newCard);
                    newCard.updateState(board, null, false);
                }
                newCard.setRender(render);
                render.addForRender(newCard , false);
            }
        }
    }

    public void addEvent(Message newEvent){
        try {
            inGameInbox.put(newEvent);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    public Card getCardByStr(String cardType , String owner) {
        Card[] card = new Card[1];
        Platform.runLater(() -> {
            card[0] = (Card) Troop.makeTroop(false,cardType,humanPlayer.getLevel(),null,owner); // serverSide is true because it's not javaFX thread
        });
        while (card[0] == null)
        {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return card[0];
    }


    @Override
    public void run() {
        Message event = null;
        try {
            event = inGameInbox.take();
            if (event.getType() == MessageType.PICKED_CARD)
            {
                addAndInitCard(event);
            }
            else if (event.getType() == MessageType.CHARACTER_DIED)
            {
                Troop died = board.getTroopByID(event.getContent());
                if (died == null)
                    System.out.println("Couldn't find the died troop in logic. Shouldn't happen . Error!");
                else {
                    board.destroy(died);
                    if (died instanceof Card)
                        render.delete((Card) died);
                    for (Troop troop :board.getAddedTroops())
                    {
                        troop.updateState(board,died,true);
                    }
                }
            }
            else { // GAME_RESULT message
                manager.getExecutor().shutdownNow();
                Message finalEvent = event;
                Platform.runLater(() -> render.gameFinishedMsg(finalEvent)); // handle later
            }
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
