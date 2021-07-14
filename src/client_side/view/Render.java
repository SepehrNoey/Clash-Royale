package client_side.view;

import shared.enums.BoardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.Troop;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Render implements Runnable{
    private Board board;
    private String me;
    private ArrayBlockingQueue<Message> inGameInbox;
    private ArrayList<Troop> allTroops; // it may be not important

    public Render(String me , ArrayBlockingQueue<Message> inGameInbox , BoardTypes type){
        this.me = me;
        this.inGameInbox = inGameInbox;
        allTroops = new ArrayList<>();
        board = new Board(type , false , me);
    }


    @Override
    public void run(){
        while (true)
        {

        }
    }
}
