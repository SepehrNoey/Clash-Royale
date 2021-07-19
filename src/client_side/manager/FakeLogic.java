package client_side.manager;

import client_side.view.render.Render;
import javafx.geometry.Point2D;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
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


    public void addAndInitCard(Card chosen , int tileX , int tileY)
    {
        chosen.setCoordinates(new Point2D(tileX , tileY));
        
    }



    @Override
    public void run() {

    }
}
