package client_side.model.sendGet;

import shared.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class Getter implements Runnable { // must be thread
    private ObjectInputStream inObj;
    private ArrayBlockingQueue<Message> sharedInbox;

    public Getter(ObjectInputStream inObj, ArrayBlockingQueue<Message> sharedInbox){
        this.inObj = inObj;
        this.sharedInbox = sharedInbox;
    }

    public Message getMsg(){
        try {
            return (Message) inObj.readObject();
        }catch (ClassNotFoundException | IOException e){
            System.out.println("Exception in getter!");
            e.printStackTrace();
        }
        return null;
    }

    public void run(){
        while (true) // handle later !!!
        {
            Message msg = getMsg();
            try {
                sharedInbox.put(msg);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
