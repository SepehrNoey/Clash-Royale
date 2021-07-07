package server_side.model;

import shared.model.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel1 extends Bot{
    public BotLevel1(ArrayBlockingQueue<Message> inGameInbox){
        super(inGameInbox);
    }

    @Override
    public void act() {

    }
}
