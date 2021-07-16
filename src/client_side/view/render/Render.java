package client_side.view.render;

import shared.enums.BoardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Render implements Runnable{
    private Board board;
    private String me;
    private ArrayBlockingQueue<Message> inGameInbox;
    private ArrayList<Troop> allTroops; // it may be not important

    public Render(String me , ArrayBlockingQueue<Message> inGameInbox , Board board){
        this.me = me;
        this.inGameInbox = inGameInbox;
        allTroops = new ArrayList<>();
        this.board = board;
    }

    public void addForRender(Card card){

    }




    @Override
    public void run(){
        while (true)
        {

        }
    }
}
