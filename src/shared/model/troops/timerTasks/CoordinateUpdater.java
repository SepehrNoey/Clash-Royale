package shared.model.troops.timerTasks;

import shared.model.troops.card.Card;
import java.util.TimerTask;
import java.util.concurrent.LinkedTransferQueue;

public class CoordinateUpdater extends TimerTask {
    private Card card;
    private String slope;
    private int xToChange;
    private int yToChange;
    private LinkedTransferQueue<Card> coordinateUpdateQueue;

    public CoordinateUpdater(Card card ,String slope ,LinkedTransferQueue<Card> coordinateUpdateQueue) {
        this.card = card;
        this.slope = slope;
        this.coordinateUpdateQueue = coordinateUpdateQueue;
        setXYChange();
    }

    @Override
    public void run() {
        synchronized (this)
        {
            card.setCoordinates(card.getCoordinates().add(xToChange ,yToChange));
            try {
                coordinateUpdateQueue.transfer(card);
            }catch (InterruptedException e)
            {
                System.out.println("interrupted in putting updating coordinate queue.");
                e.printStackTrace();
            }
        }
    }

    private void setXYChange(){
        if (slope.equals("up"))
        {
            yToChange = -1;
            xToChange = 0;
        }
        else if (slope.equals("down"))
        {
            xToChange = 0;
            yToChange = 1;
        }
        else if (slope.equals("right"))
        {
            xToChange = 1;
            yToChange = 0;
        }
        else if (slope.equals("left"))
        {
            xToChange = -1;
            yToChange = 0;
        }
        else {
            String[] split = slope.split(","); // first part number(int) , second part (right or left)
            int shib = Integer.parseInt(split[0]); // should be two part
            String direction = split[1];
            if (direction.equals("right"))
            {
                xToChange = 1;
                if (shib > 0)
                {
                    yToChange = -1 * shib;
                }
                else if (shib < 0)
                {
                    yToChange = shib;
                }
            }
            else{
                xToChange = -1;
                if (shib > 0)
                {
                    yToChange = shib;
                }
                else {
                    yToChange = -1 * shib;
                }
            }
        }
    }

}
