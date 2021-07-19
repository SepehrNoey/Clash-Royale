package server_side.model;

import shared.enums.CardTypes;
import shared.model.Board;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import shared.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Bot implements Runnable{
    private String name;
    private int level;
    private ArrayList<Card> deck; // a bot just has a deck
    private ArrayBlockingQueue<Message> inGameInbox; // this is used to put events (events made by bots)
    private ArrayBlockingQueue<Message> incomingEvents; // this is used to get events made by other players or bots and taking action according to them
    private int elixir;
    private Timer elixirTimer;
    private ElixirUpdaterForBot elixirUpdater;
    private Board board;

    public Bot(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level)
    {
        this.level = level;
        this.inGameInbox = inGameInbox;
        this.incomingEvents = incomingEvents;
        deck = new ArrayList<>();
        setDeck();
        elixir = 4;
        elixirTimer = new Timer();
        elixirUpdater = new ElixirUpdaterForBot(this);
        elixirTimer.schedule(elixirUpdater,0,2000);
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

    public ArrayList<Card> getDeck() {
        return deck;
    }

    /**
     * getter
     * @return the object of elixir
     */
    public Integer getElixir() {
        return elixir;
    }

    public synchronized void updateElixir(int elixir) {
        this.elixir += elixir;
        notifyAll();
    }

    /**
     * setter
     * @param board of game
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public abstract String getPlaceForCard(Card chosen);

    /**
     * getter
     * @return board of game
     */
    public Board getBoard() {
        return board;
    }
}
