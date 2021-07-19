package client_side.view.render;

import client_side.controller.GameSceneController;
import shared.model.Board;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;

import java.util.ArrayList;

public class Render implements Runnable{
    private Board board;
    private String me;
    private ArrayList<Troop> allTroops; // it may be not important
    private int cardUsedNum;
    private GameSceneController controller;

    public Render(String humanPlayer , Board board , GameSceneController controller){
        cardUsedNum = 0;
        this.me = humanPlayer;
        allTroops = new ArrayList<>();
        this.board = board;
        this.controller = controller;
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
