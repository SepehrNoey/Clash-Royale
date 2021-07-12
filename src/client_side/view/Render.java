package client_side.view;

import shared.enums.BoardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.Troop;
import shared.model.troops.card.BuildingCard;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Render implements Runnable{
    private Board board;
    private String playerName;
    private ArrayBlockingQueue<Message> inGameInbox;
    private ArrayList<Troop> allTroops;

    public Render(String playerName , ArrayBlockingQueue<Message> inGameInbox , BoardTypes type){
        this.playerName = playerName;
        this.inGameInbox = inGameInbox;
        allTroops = new ArrayList<>();
        board = new Board(type);
    }


    @Override
    public void run(){
        while (true)
        {

        }
    }
}
