package client_side.model.sendGet;

import shared.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class Sender { // no need to be thread
    private ObjectOutputStream outObj;
    public Sender(ObjectOutputStream outObj){
        this.outObj = outObj;
    }

    /**
     * sending message
     * @param msg to be sent
     */
    public void sendMsg(Message msg){
        try {
            outObj.writeObject(msg);
        }catch (IOException e)
        {
            System.out.println("Can't send message in Sender.");
        }
    }
}
