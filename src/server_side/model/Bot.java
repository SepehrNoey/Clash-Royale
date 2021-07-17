package server_side.model;

import shared.enums.CardTypes;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import shared.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Bot implements Runnable{
    private String name;
    private int level;
    private ArrayList<Card> deck; // a bot just has a deck
    private ArrayBlockingQueue<Message> inGameInbox; // this is used to put events (events made by bots)
    private ArrayBlockingQueue<Message> incomingEvents; // this is used to get events made by other players or bots and taking action according to them


    public Bot(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level)
    {
        this.level = level;
        this.inGameInbox = inGameInbox;
        this.incomingEvents = incomingEvents;
        deck = new ArrayList<>();
        setDeck();
    }

    public void setDeck(){
        ArrayList<CardTypes> notUsed = new ArrayList<>(Arrays.asList(CardTypes.values()));
        Random random = new Random();
        for (int i = 0 ; i < 8 ; i++) // making 8 random cards
        {
            int rand = random.nextInt(notUsed.size());
            deck.add((Card) Troop.makeTroop(true,notUsed.get(rand).toString(), level,null,name)); // is name null ?
            notUsed.remove(rand);
        }
    }

    public abstract void act();

    public ArrayBlockingQueue<Message> getIncomingEvents() {
        return incomingEvents;
    }

    public ArrayBlockingQueue<Message> getInGameInbox() {
        return inGameInbox;
    }

    public int getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
