package shared.model.player;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import shared.enums.TowerTypes;
import shared.model.player.sendGet.Getter;
import shared.model.player.sendGet.Sender;
import shared.model.troops.Tower;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import shared.model.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * a class to store data of a player and also sending and receiving message from server
 *
 * @version 1.0
 */
public class Player implements Serializable {
    private String name;
    private int level;
    private int xp;
    private String password;
    private ArrayList<Troop> allTroops;
    private ArrayList<Card> cards;
    private ArrayList<Card> deck;
    private transient Socket socket;
    private transient Getter getter;
    private transient Sender sender;
    private transient ArrayBlockingQueue<Message> sharedInbox;


    public Player(boolean isServerSide,String name, String password , int level ,int xp, ArrayList<Card> cards , Socket socket
            , ObjectOutputStream outObj , ObjectInputStream inObj){ // password may not be needed
        this.name = name;
        this.password = password;
        this.level = level;
        this.xp = xp;
        this.cards = cards;
        allTroops = new ArrayList<>(cards); // attention !!! imageView should be added later! - and also player is considered in bottom of board! - handle later
        // attention !! point2D is addressed by index  0 to MAX !!! - like Board
        allTroops.add(Troop.makeTroop(isServerSide,TowerTypes.KING_TOWER.toString() , level , new Point2D(8,24) , name,null , null));
        allTroops.add(Troop.makeTroop(isServerSide,TowerTypes.PRINCESS_TOWER.toString() , level , new Point2D(3,23) , name,null , null));
        allTroops.add(Troop.makeTroop(isServerSide,TowerTypes.PRINCESS_TOWER.toString() , level , new Point2D(14,23) , name,null , null));
        deck = new ArrayList<>();
        for(int i = 0 ; i < 8 ; i++)
        {
            deck.add(cards.get(i)); // first 8 cards are deck
        }
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

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setBattleDeck(ImageView[] deckView){ // for updating deck..

        deck.clear();
        for(int i=0;i<8;i++)
            for(Card card : cards)
                if(card.getCardImage().equals(deckView[i].getImage()))
                    deck.add(card);
        Card[] other = new Card[4];
        int i=0;
        for(Card card : cards)
            if (!deck.contains(card))
                other[i++] = card;
        cards.clear();
        i=0;
        for(int j=0;j<8;j++)
            cards.add(deck.get(i++));
        for(int j=8;j<12;j++)
            cards.add(other[j-8]);

        for(Card card : cards)
            System.out.println(card.getType());

    }

    public ArrayList<Card> getDeck() {
        return deck;
    }
}