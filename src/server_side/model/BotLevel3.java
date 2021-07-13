package server_side.model;

import shared.model.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel3 extends Bot implements Runnable{
    public BotLevel3(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level){
        super(inGameInbox , incomingEvents , level);
        setName("Bot 3");
    }

    @Override
    public void act() {

    }

    @Override
    public void run(){

    }
}
