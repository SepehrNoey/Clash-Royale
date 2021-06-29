package server_side.manager;

import client_side.model.Player;

import java.util.ArrayList;

public class GameLoop {
    private ArrayList<Player> players;

    public GameLoop(ArrayList<Player> players){
        this.players = players;
    }

    public void play(){
        // first sending gameConfigs...
    }
}
