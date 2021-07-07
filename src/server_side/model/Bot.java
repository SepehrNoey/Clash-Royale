package server_side.model;

import shared.model.card.Card;
import shared.model.Message;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Bot {
    private String name;
    private ArrayList<Card> cards;
    private ArrayBlockingQueue<Message> inGameInbox;

    public Bot(ArrayBlockingQueue<Message> inGameInbox)
    {
        name = "Bot";
        this.inGameInbox = inGameInbox;
        cards = new ArrayList<>();
        setCards();
    }

    public void setCards(){

    }

    public abstract void act();

}
