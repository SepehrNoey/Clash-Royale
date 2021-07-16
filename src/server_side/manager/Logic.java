package server_side.manager;

import shared.model.Message;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

public class Logic implements Runnable {
    private ArrayBlockingQueue<Message> inGameInbox; // for adding new events
    private ArrayBlockingQueue<Message> toCheckEvents; // the events to check - the resultant of these events will cause to make a new event
    private Timer timer;
    private TimerTask endOfGame;


    public Logic(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> toCheckEvents)
    {
        this.inGameInbox = inGameInbox;
        endOfGame = new TimerTask(){
            public void run()
            {
                timeEnded();
            }
        };
        timer = new Timer();
        this.toCheckEvents = toCheckEvents;
    }

//    public boolean isFinished(){
//        // if finished , then cancel the timer
//    }

    public void timeEnded(){

    }

    public void addToCheckEvent(Message newEvent){
        try {
            toCheckEvents.put(newEvent);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        timer.schedule(endOfGame , 3 * 60 * 1000); // timer for 3 minutes
        while (true)
        {
            Message event = null;
            if (!toCheckEvents.isEmpty())
            {
                try {
                    event = toCheckEvents.take();
                    this.handle(event);
                }catch (InterruptedException e)
                {
                    System.out.println("Interrupted in getting new event in logic (toCheckEvents). Shouldn't happen. Error!");
                    e.printStackTrace();
                }
            }


        }
    }

    public void handle(Message event){
        // calculate where to go and in how much time


    }


}
