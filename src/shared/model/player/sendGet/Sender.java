
package shared.model.player.sendGet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import shared.model.Message;

public class Sender {
    private ObjectOutputStream outObj;

    public Sender(ObjectOutputStream outObj) {
        this.outObj = outObj;
    }

    public void sendMsg(Message msg) {
        try {
            this.outObj.writeObject(msg);
            System.out.println("2) in sendMsg");
        } catch (IOException var3) {
            System.out.println("Can't send message in Sender.");
        }

    }
}