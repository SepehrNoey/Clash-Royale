package server_side.manager;

import shared.model.Message;

import java.util.concurrent.ArrayBlockingQueue;

public class Logic implements Runnable {
    private ArrayBlockingQueue<Message> inGameInbox;

    public Logic(ArrayBlockingQueue<Message> inGameInbox)
    {
        this.inGameInbox = inGameInbox;
    }

    @Override
    public void run() {
        Message event = null;
        while (true)
        {

        }
    }
}
