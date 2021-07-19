package server_side.model;

import shared.model.Message;
import shared.model.troops.card.Card;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel3 extends Bot implements Runnable{
    public BotLevel3(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level){
        super(inGameInbox , incomingEvents , level);
        setName("bot3");
    }

    @Override
    public void run(){

    }

    @Override
    public String getPlaceForCard(Card chosen) {
        return null;
    }
}
