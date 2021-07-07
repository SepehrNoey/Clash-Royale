package client_side.model;

import client_side.model.sendGet.Getter;
import client_side.model.sendGet.Sender;
import shared.Card;
import shared.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * a class to store data of a player and also sending and receiving message from server
 *
 * @version 1.0
 */
public class Player {
    private String name;
    private int level;
    private String password;
    private ArrayList<Card> cards;
    private Socket socket;
    private Getter getter;
    private Sender sender;
    private ArrayBlockingQueue<Message> sharedInbox;

    public Player(String name, String password , int level , ArrayList<Card> cards , Socket socket
            , ObjectOutputStream outObj , ObjectInputStream inObj){ // password may not be needed
        this.name = name;
        this.password = password;
        this.level = level;
        this.cards = cards;
        this.socket = socket;
        this.sharedInbox = new ArrayBlockingQueue<>(50);
        this.getter = new Getter(inObj , sharedInbox);
        this.sender = new Sender(outObj);
    }

    /**
     * getter
     * @return getter for player's sharedInbox
     */
    public ArrayBlockingQueue<Message> getSharedInbox() {
        return sharedInbox;
    }

    /**
     * getter
     * @return created instance of class Getter (Runnable)
     */
    public Getter getGetter() {
        return getter;
    }

    /**
     * getter
     * @return messageSender of this player
     */
    public Sender getSender() {
        return sender;
    }

    /**
     * getter
     * @return name of player
     */
    public String getName() {
        return name;
    }
}
