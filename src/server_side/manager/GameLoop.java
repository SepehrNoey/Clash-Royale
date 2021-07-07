package server_side.manager;

import client_side.model.Player;
import server_side.model.Bot;

import java.util.ArrayList;

public class GameLoop {
    private ArrayList<Player> players;
    private Bot bot;

    public GameLoop(ArrayList<Player> players, Bot bot){
        this.players = players;
        if (bot != null)
            this.bot = bot;
    }

    public void play(){
        // first sending gameConfigs...
    }
}
