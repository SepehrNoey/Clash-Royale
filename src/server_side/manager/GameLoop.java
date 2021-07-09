package server_side.manager;

import shared.model.Player;
import server_side.model.Bot;
import shared.model.Message;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class GameLoop {
    private ArrayList<Player> players;
    private Bot bot;
    private ArrayBlockingQueue<Message> inGameInbox;

    public GameLoop(ArrayList<Player> players, Bot bot){
        this.players = players;
        if (bot != null)
            this.bot = bot;
    }

    public void play(){
        // first sending gameConfigs...
    }
}
