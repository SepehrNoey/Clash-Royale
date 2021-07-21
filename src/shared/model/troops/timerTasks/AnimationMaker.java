package shared.model.troops.timerTasks;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import shared.model.troops.Troop;

public abstract class AnimationMaker implements Runnable{
    private Troop troop;
    private AnimationTimer animationTimer;
    private final long startNanoTime;
    private Pane pane;
    private int xToChange;
    private int yToChange;
    private Point2D lastPoint;
    private ImageView imageView;

    public AnimationMaker(Troop troop , Pane pane){
        this.troop = troop;
        startNanoTime = System.nanoTime();
        this.pane = pane;
    }

    public abstract void setXYChange();

    public void setAnimationTimer(AnimationTimer animationTimer) {
        this.animationTimer = animationTimer;
    }

    public void setxToChange(int xToChange) {
        this.xToChange = xToChange;
    }

    public void setyToChange(int yToChange) {
        this.yToChange = yToChange;
    }

    public void setLastPoint(Point2D lastPoint) {
        this.lastPoint = lastPoint;
    }

    public long getStartNanoTime() {
        return startNanoTime;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public AnimationTimer getAnimationTimer() {
        return animationTimer;
    }

    public Point2D getLastPoint() {
        return lastPoint;
    }

    public int getxToChange() {
        return xToChange;
    }

    public int getyToChange() {
        return yToChange;
    }

}
