package client_side.model;

import client_side.model.sendGet.Getter;
import client_side.model.sendGet.Sender;
import shared.Card;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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

    public Player(String name, String password , int level , ArrayList<Card> cards , Socket socket
            , ObjectOutputStream outObj , ObjectInputStream inObj){ // password may not be needed
        this.name = name;
        this.password = password;
        this.level = level;
        this.cards = cards;
        this.socket = socket;
        this.getter = new Getter(inObj);
        this.sender = new Sender(outObj);
    }

    
}
