package shared.model.player.sendGet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import shared.model.Message;

public class Getter implements Runnable {
    private ObjectInputStream inObj;
    private ArrayBlockingQueue<Message> sharedInbox;

    public Getter(ObjectInputStream inObj, ArrayBlockingQueue<Message> sharedInbox) {
        this.inObj = inObj;
        this.sharedInbox = sharedInbox;
    }

    public Message getMsg() {
        try {
            return (Message)this.inObj.readObject();
        } catch (IOException | ClassNotFoundException var2) {
            System.out.println("Exception in getter!");
            var2.printStackTrace();
            return null;
        }
    }

    public void run() {
        while(true) {
            Message msg = this.getMsg();

            try {
                this.sharedInbox.put(msg);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
        }
    }
}