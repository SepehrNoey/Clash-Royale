package server_side.model;

import shared.model.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel2 extends Bot{
    public BotLevel2(ArrayBlockingQueue<Message> inGameInbox){
        super(inGameInbox);
    }

    @Override
    public void act() {

    }
}
