package shared.model.troops.timerTasks;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import shared.enums.State;
import shared.model.troops.card.Card;
import shared.model.troops.card.SoldierCard;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

public class CoordinateUpdater implements Runnable { // is used for soldierCard
    private SoldierCard soldierCard;
    private int xToChange;
    private int yToChange;
    private ArrayBlockingQueue<Card> coordinateUpdateQueue;
    private Point2D lastPoint;
    private ScheduledExecutorService itsExec;

    public CoordinateUpdater(SoldierCard soldierCard ,ArrayBlockingQueue<Card> coordinateUpdateQueue ,ScheduledExecutorService itsExec) {
        this.itsExec = itsExec;
        this.soldierCard = soldierCard;
        this.coordinateUpdateQueue = coordinateUpdateQueue;
        lastPoint = new Point2D(soldierCard.getCoordinates().getX() * 30, soldierCard.getCoordinates().getY() * 30); // first time
    }




//        this.animationTimer = new AnimationTimer() {
//            @Override
//            public void handle(long currentNanoTime) {
//                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
//                if (soldierCard.getState() == State.WALK)
//                {
//                    setXYChange();
//                    lastPoint = soldierCard.getCoordinates().add(xToChange,yToChange);
//                    if ((int)lastPoint.getX() != (int)soldierCard.getCoordinates().getX() ||
//                            (int)lastPoint.getY() != (int)soldierCard.getCoordinates().getY())
//                    {
//                        soldierCard.setCoordinates(new Point2D((int)(lastPoint.getX() / 30) , (int)(lastPoint.getY() / 30))); // addressing in tiles
//                        // communicating with board
//                        try{
//                            System.out.println("coordinate changed: " + soldierCard.getCoordinates().getX() + ","+  soldierCard.getCoordinates().getY());
//                            coordinateUpdateQueue.put(soldierCard);
//                        }catch (InterruptedException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                else if (soldierCard.getState() == State.DEAD){
//                    this.stop();
//                }
//            }
//        };



    @Override
    public void run() {
        if (soldierCard.getState() == State.WALK)
        {
            setXYChange();
            lastPoint = lastPoint.add(xToChange,yToChange);
            if ((int)(lastPoint.getX() / 30) != (int)soldierCard.getCoordinates().getX() ||
                            (int)(lastPoint.getY() / 30) != (int)soldierCard.getCoordinates().getY())
                {
                    soldierCard.setCoordinates(new Point2D((int)(lastPoint.getX() / 30) , (int)(lastPoint.getY() / 30))); // addressing in tiles
                    // communicating with board
                    try{
                        System.out.println("coordinate changed: " + soldierCard.getCoordinates().getX() + ","+  soldierCard.getCoordinates().getY());
                        coordinateUpdateQueue.put(soldierCard);
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

        }
        else if (soldierCard.getState() == State.ATTACK)
        {
            /// nothing
        }
        else if (soldierCard.getState() == State.DEAD){
            itsExec.shutdownNow();
        }
    }



    private void setXYChange(){
        if (soldierCard.getDirection().equals("rage"))
        {
            xToChange = 0;
            yToChange = 0;
        }
        else if (soldierCard.getDirection().equals("up"))
        {
            yToChange = -1;
            xToChange = 0;
        }
        else if (soldierCard.getDirection().equals("down"))
        {
            xToChange = 0;
            yToChange = 1;
        }
        else if (soldierCard.getDirection().equals("right"))
        {
            xToChange = 1;
            yToChange = 0;
        }
        else if (soldierCard.getDirection().equals("left"))
        {
            xToChange = -1;
            yToChange = 0;
        }
        else {
            String[] split = soldierCard.getDirection().split(","); // first part number(int) , second part (right or left)
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
