package shared.model.troops.timerTasks;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import shared.enums.State;
import shared.model.troops.card.BuildingCard;

public class BuildingAnimation extends AnimationMaker{

    public BuildingAnimation(BuildingCard buildingCard , Pane pane){
        super(buildingCard,pane);
        setAnimationTimer(new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - getStartNanoTime()) / 1000000000.0;
                if (buildingCard.getState() == State.STOP)
                {
                    if (getImageView() == null) // first time
                    {
                        setImageView(new ImageView());
                        getImageView().setImage(buildingCard.getAttackFrm(t,0.100));
                        getImageView().setFitWidth(buildingCard.getWidth());
                        getImageView().setFitHeight(buildingCard.getHeight());
                        getImageView().setTranslateX(buildingCard.getCoordinates().getX() * 30);
                        getImageView().setTranslateY(buildingCard.getCoordinates().getY() * 30);
                        pane.getChildren().add(getImageView());
                    }
                    else {
                        // nothing
                    }
                }
                else if (buildingCard.getState() == State.ATTACK) // rotating
                {
                    if (buildingCard.getTargetToDoAct().getCoordinates().getY() == buildingCard.getCoordinates().getY())
                    {
                        if(buildingCard.getTargetToDoAct().getCoordinates().getX() >= buildingCard.getCoordinates().getX())
                            getImageView().setRotate(90);
                        else if (buildingCard.getTargetToDoAct().getCoordinates().getX() < buildingCard.getCoordinates().getX()){
                            getImageView().setRotate(270);
                        }
                    }
                }else { // DEAD STATE
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2),getImageView());
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0);
                    fadeTransition.setOnFinished((ActionEvent event) -> {
                        pane.getChildren().remove(getImageView());
                        this.stop();
                    });
                    fadeTransition.play();
                }
            }
        });


    }

    @Override
    public void run() {
        getAnimationTimer().start();
    }

    @Override
    public void setXYChange() {
        // none
    }
}
