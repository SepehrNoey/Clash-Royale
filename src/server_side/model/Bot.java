package server_side.model;

import shared.model.troops.card.Card;
import shared.model.Message;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Bot implements Runnable{
    private String name;
    private ArrayList<Card> cards;
    private ArrayBlockingQueue<Message> inGameInbox; // this is used to put events (events made by bots)
    private ArrayBlockingQueue<Message> incomingEvents; // this is used to get events made by other players or bots and taking action according to them

    public Bot(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents)
    {
        name = "Bot";
        this.inGameInbox = inGameInbox;
        this.incomingEvents = incomingEvents;
        cards = new ArrayList<>();
        setCards();
    }

    public void setCards(){

    }

    public abstract void act();

    public ArrayBlockingQueue<Message> getIncomingEvents() {
        return incomingEvents;
    }

    public ArrayBlockingQueue<Message> getInGameInbox() {
        return inGameInbox;
    }
}
