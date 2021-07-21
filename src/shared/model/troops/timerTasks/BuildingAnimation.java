package shared.model.troops.timerTasks;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

                    }
                }
                else if (buildingCard.getState() == State.ATTACK)
                {
                    // rotate
                }else { // DEAD STATE

                }
            }
        });


    }

    @Override
    public void run() {

    }

    @Override
    public void setXYChange() {

    }
}
