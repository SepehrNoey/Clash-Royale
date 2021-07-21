package shared.model.troops.timerTasks;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import shared.enums.State;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import shared.model.troops.card.SoldierCard;
import java.util.concurrent.ArrayBlockingQueue;

public class SoldierAnimMaker extends AnimationMaker{
    private SoldierCard soldierCard;
    private ArrayBlockingQueue<Card> coordinateUpdateQueue;
    private int num;

    public SoldierAnimMaker(Troop troop, Pane pane, ArrayBlockingQueue<Card> coordinateUpdateQueue){
        super(troop, pane);
        this.coordinateUpdateQueue = coordinateUpdateQueue;
        this.soldierCard = (SoldierCard)troop;
        setAnimationTimer(new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - getStartNanoTime()) / 1000000000.0;
                if (soldierCard.getState() == State.WALK)
                {
                    if (getImageView() == null) // first time
                    {
                        setImageView(new ImageView());
                        getImageView().setImage(soldierCard.getWalkFrm(t,0.100)); // change duration later
                        getImageView().setTranslateX(soldierCard.getCoordinates().getX());
                        getImageView().setTranslateY(soldierCard.getCoordinates().getY());
                        getImageView().setFitWidth(soldierCard.getWidth());
                        getImageView().setFitHeight(soldierCard.getHeight());
                        if (soldierCard.getOwner().contains("bot"))
                            getImageView().setRotate(180); // bots from top
                        pane.getChildren().add(getImageView());
                        setLastPoint(new Point2D(soldierCard.getCoordinates().getX() * 30 , soldierCard.getCoordinates().getY() * 30)); // in pixels
                        setXYChange();
                    }
                    else {
                        setXYChange();
                        setLastPoint(getLastPoint().add(getxToChange(), getyToChange()));
                        getImageView().setImage(soldierCard.getWalkFrm(t,0.300 / troop.getMovingSpeed().getValue()));
                        getImageView().setTranslateX(getLastPoint().getX() + getxToChange());
                        getImageView().setTranslateY(getLastPoint().getY() + getyToChange());
                        if ((int)(getLastPoint().getX() / 30) != (int)soldierCard.getCoordinates().getX() ||
                                (int)(getLastPoint().getY() / 30) != (int)soldierCard.getCoordinates().getY())
                        {
                            soldierCard.setCoordinates(new Point2D((int)(getLastPoint().getX() / 30) , (int)(getLastPoint().getY() / 30))); // addressing in tiles

                            // communicating with board
                            try{
                                coordinateUpdateQueue.put(soldierCard);
                            }catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                else if (soldierCard.getState() == State.ATTACK) // translate shouldn't happen except first time
                {
                    if (getImageView() == null) // first time
                    {
                        setImageView(new ImageView());
                        getImageView().setImage(soldierCard.getAttackFrm(t,0.100)); // change duration later
                        getImageView().setFitWidth(soldierCard.getWidth());
                        getImageView().setFitHeight(soldierCard.getHeight());
                        getImageView().setTranslateX(soldierCard.getCoordinates().getX());
                        getImageView().setTranslateY(soldierCard.getCoordinates().getY());
                        if (soldierCard.getOwner().contains("bot"))
                            getImageView().setRotate(180); // rotate for bots
                        pane.getChildren().add(getImageView());
                        setLastPoint(new Point2D(soldierCard.getCoordinates().getX() * 30 , soldierCard.getCoordinates().getY() * 30));

                    }
                    else {
                        getImageView().setImage(soldierCard.getAttackFrm(t,0.100));

                        // may need to communicate

                    }
                }
                else { // DEAD STATE
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2),getImageView());
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0);
                    fadeTransition.setOnFinished((ActionEvent event) ->{
                        pane.getChildren().remove(getImageView());
                        this.stop();
                    });
                    fadeTransition.play();
                }
            }
        });
    }

    @Override
    public void setXYChange() {
        if (soldierCard.getDirection().equals("up"))
        {
            setyToChange(-1);
            setxToChange(0);
        }
        else if (soldierCard.getDirection().equals("down"))
        {
            setyToChange(1);
            setxToChange(0);
        }
        else if (soldierCard.getDirection().equals("right"))
        {
            setxToChange(1);
            setyToChange(0);
        }
        else if (soldierCard.getDirection().equals("left"))
        {
            setxToChange(-1);
            setyToChange(0);
        }
        else {
            String[] split = soldierCard.getDirection().split(","); // first part number(int) , second part (right or left)
            int shib = Integer.parseInt(split[0]); // should be two part
            String direction = split[1];
            if (direction.equals("right"))
            {
                setxToChange(1);
                setyToChange(-1 * shib);
            }
            else{
                setxToChange(-1);
                setyToChange(shib);
            }
        }
    }

    @Override
    public void run() {
        getAnimationTimer().start();
    }
}
