package server_side.model;

import shared.model.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel1 extends Bot implements Runnable{
    public BotLevel1(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level){
        super(inGameInbox , incomingEvents , level);
        setName("Bot 1");
    }

    @Override
    public void act() {

    }

    @Override
    public void run(){

    }
}
