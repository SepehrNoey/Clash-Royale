package server_side.model;

import shared.enums.MessageType;
import shared.model.Message;
import shared.model.troops.card.Card;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class BotLevel1 extends Bot implements Runnable{
    public BotLevel1(ArrayBlockingQueue<Message> inGameInbox , ArrayBlockingQueue<Message> incomingEvents , int level){
        super(inGameInbox , incomingEvents , level);
        setName("bot1");
    }

    @Override
    public void run(){
        Random random = new Random();
        while (true)
        {
            int rand = random.nextInt(getDeck().size());
            Card chosen = getDeck().get(rand);
            if (getElixir() >= chosen.getCost())
            {
                String[] xy = getPlaceForCard(chosen).split(",");
                try {
                    getInGameInbox().put(new Message(MessageType.PICKED_CARD , getName(),chosen.getType() + "," + xy[0] + "," + xy[1]));
                    updateElixir(-1 * chosen.getCost());
                }catch (InterruptedException e)
                {
                    System.out.println(getName() + " interrupted while putting message to inGameInbox");
                    e.printStackTrace();
                }
            }
            else {
                try {
                    this.wait();
                }catch (InterruptedException e){}
            }
        }
    }

    @Override
    public String getPlaceForCard(Card chosen) {
        Random random = new Random();
        int tileX = 0;
        int tileY = 0;
        do {
            tileX = random.nextInt(21);
            tileY = random.nextInt(14);
        } while (!getBoard().isValidAddress(chosen, tileX, tileY));
        return tileX + "," + tileY;

    }
}
