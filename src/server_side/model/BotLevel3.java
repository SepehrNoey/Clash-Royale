package server_side.model;

import shared.model.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel3 extends Bot{
    public BotLevel3(ArrayBlockingQueue<Message> inGameInbox){
        super(inGameInbox);
    }

    @Override
    public void act() {

    }
}
