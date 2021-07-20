package shared.model.troops.timerTasks;

import shared.model.troops.card.Card;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

public class CoordinateUpdater extends TimerTask {
    private Card card;
    private String slope;
    private int xToChange;
    private int yToChange;
    private ArrayBlockingQueue<Card> coordinateUpdateQueue;

    public CoordinateUpdater(Card card ,String slope ,ArrayBlockingQueue<Card> coordinateUpdateQueue) {
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
            System.out.println("coordinate updated for " + card.getType().toString() + ": " + card.getCoordinates().getX() + "," + card.getCoordinates().getY());
            try {
                coordinateUpdateQueue.put(card);
            }catch (InterruptedException e)
            {
                System.out.println("interrupted in putting updating coordinate queue.");
                e.printStackTrace();
            }
        }
    }

    private void setXYChange(){
        if (slope.equals("rage"))
        {
            xToChange = 0;
            yToChange = 0;
        }
        else if (slope.equals("up"))
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
                yToChange = -1 * shib;
            }
            else{
                xToChange = -1;
                yToChange = shib;
            }
        }
    }

}
