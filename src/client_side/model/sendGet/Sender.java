package client_side.model.sendGet;

import java.io.ObjectOutputStream;

public class Sender { // no need to be thread
    ObjectOutputStream outObj;
    public Sender(ObjectOutputStream outObj){
        this.outObj = outObj;
    }

}
