package shared.model.troops.timerTasks;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import shared.model.troops.Troop;

public class ShotAndSpellAnimMaker extends AnimationMaker{
    private Point2D destination;
    private Point2D startPoint;
    private int width;
    private int height;
    private String shotPath;

    public ShotAndSpellAnimMaker(Troop troop , Pane pane ,Point2D destination ,Point2D startPoint , int width , int height,boolean isShot , String shotPath){ // destination must be added before
        super(troop, pane);
        this.destination = destination;
        this.startPoint = startPoint;
        this.height = height; // use these fields for all situations
        this.width = width;
        this.shotPath = shotPath;
        setXYChange();
        setAnimationTimer(new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - getStartNanoTime()) / 1000000000.0;
                if (getImageView() == null) // first time
                {
                    setImageView(new ImageView());
                    getImageView().setFitHeight(height);
                    getImageView().setFitWidth(width);
                    if (isShot)
                        getImageView().setImage(new Image(shotPath)); // may have to change here to getting from troop
                    else {
                        getImageView().setImage(troop.getAttackFrm(t,0.100));
                    }


                    if (troop.getOwner().contains("bot"))
                    {
                        getImageView().setRotate(180); // bots from top to down
                    }
                    pane.getChildren().add(getImageView());
                    getImageView().setTranslateX(startPoint.getX());
                    getImageView().setTranslateY(startPoint.getY());
                    setLastPoint(new Point2D(startPoint.getX() * 30 , startPoint.getY() * 30)); // in pixels
                    getImageView().setRotate(getRotationDegree());
                }
                else {
                    setLastPoint(new Point2D(getLastPoint().getX() + getxToChange() ,getLastPoint().getY() + getyToChange())); // pixel moving
                    getImageView().setTranslateX(getLastPoint().getX());
                    getImageView().setTranslateY(getLastPoint().getY());

                    if (!isShot)
                        getImageView().setImage(troop.getAttackFrm(t,0.100));

                    if ((int)getLastPoint().getX() / 30 == (int)destination.getX() || (int)getLastPoint().getY() / 30 == (int)destination.getY())
                    { // reached destination
                        if (!isShot)
                        {
                            troop.run();
                        }
                        pane.getChildren().remove(getImageView());
                        this.stop(); // handle later
                    }
                }
            }
        });
    }

    private double getRotationDegree(){
        if (destination.getY() == startPoint.getY() && destination.getX() > startPoint.getX())
            return 90;
        else if (destination.getY() == startPoint.getY() && destination.getX() < startPoint.getX())
            return 270;
        else{
            double deltaY = startPoint.getY() - destination.getY();
            double deltaX = startPoint.getY() - destination.getY();

            double angle = ((Math.atan(deltaX / deltaY)) * 180)/Math.PI;
            if ( destination.getY() > startPoint.getY() ) // bot shot or arrow or fireball
                return angle + 180;
            else if (angle > 0) // humanPlayer shot or spells(except rage)
                return angle;
            else {
                return 360 + angle; // humanPlayer
            }
        }
    }

    public static double getRotationDegree(Point2D startPoint,Point2D destination){
        if (destination.getY() == startPoint.getY() && destination.getX() > startPoint.getX())
            return 90;
        else if (destination.getY() == startPoint.getY() && destination.getX() < startPoint.getX())
            return 270;
        else{
            double deltaY = startPoint.getY() - destination.getY();
            double deltaX = startPoint.getY() - destination.getY();

            double angle = ((Math.atan(deltaX / deltaY)) * 180)/Math.PI;
            if ( destination.getY() > startPoint.getY() ) // bot shot or arrow or fireball
                return angle + 180;
            else if (angle > 0) // humanPlayer shot or spells(except rage)
                return angle;
            else {
                return 360 + angle; // humanPlayer
            }
        }
    }





    public void setXYChange(){
        if(destination.getY() == startPoint.getY())
        {
            setyToChange(0);
            if (destination.getX() > startPoint.getX())
                setxToChange(1);
            else
                setxToChange(-1);
        }
        else if (destination.getX() == startPoint.getX())
        {
            setxToChange(0);
            if (destination.getY() > startPoint.getY())
                setyToChange(1);
            else setyToChange(-1);
        }
        else {
            int m = (int)((destination.getY() - startPoint.getY()) /(destination.getX() - startPoint.getX()));
            if (m < 0) // the line will be like slash: /
            {
                if (destination.getX() > startPoint.getX()) // at most for human player
                {
                    setxToChange(m * -1);
                    setyToChange(-1);
                }
                else { // for bots
                    setxToChange(m);
                    setyToChange(1);
                }
            }
            else { // the line will be like backSlash : \
                if (destination.getX() > startPoint.getX())
                {
                    setxToChange(m);
                    setyToChange(1);
                }
                else {
                    setxToChange(-1 * m);
                    setyToChange(-1);
                }
            }
        }
    }



    @Override
    public void run() {
        getAnimationTimer().start(); // handle
    }
}
