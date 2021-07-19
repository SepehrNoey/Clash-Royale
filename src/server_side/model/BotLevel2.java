package server_side.model;

import shared.model.Message;
import shared.model.troops.card.Card;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel2 extends Bot implements Runnable{
    public BotLevel2(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level){
        super(inGameInbox, incomingEvents , level);
        setName("bot2");

    }

    @Override
    public void run(){
//        if (getIncomingEvents().isEmpty())
        // in this level bot or upper if elixir exists , then checking the incomingEvents and also making decision happens


    }

    @Override
    public String getPlaceForCard(Card chosen) {
        return null;
    }
}
