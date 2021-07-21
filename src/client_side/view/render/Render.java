package client_side.view.render;

import client_side.controller.GameSceneController;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import shared.enums.CardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.Troop;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.Card;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.card.SpellCard;
import shared.model.troops.timerTasks.ShotAndSpellAnimMaker;
import shared.model.troops.timerTasks.SoldierAnimMaker;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;


public class Render{
    private Board board;
    private String humanPlayer;
    private int cardUsedNum;
    private GameSceneController controller;
    private ArrayBlockingQueue<Troop> renderQueue;
    private HashMap<String , ImageView> cardsImgViews;

    public Render(String humanPlayer , Board board , GameSceneController controller){
        cardUsedNum = 0;
        this.humanPlayer = humanPlayer;
        this.board = board;
        this.controller = controller;
        this.renderQueue = new ArrayBlockingQueue<>(50);
        board.setRenderQueue(renderQueue);
        cardsImgViews = new HashMap<>();
    }

    public void gameFinishedMsg(Message msg) // on javaFX thread
    {

    }

    public void delete(Card card){
        Platform.runLater(() -> {
            ImageView imageView = cardsImgViews.get(card.getId());
            imageView.setImage(null);
            controller.getPane().getChildren().remove(imageView);
            cardsImgViews.remove(card.getId());
        });
    }


    public void addForRender(Troop troop , boolean isShot){
        if (isShot) // buildings and towers just send their shots to render (in addForRender , in BuildingCard run method)
        {
            ShotAndSpellAnimMaker shot = new ShotAndSpellAnimMaker(troop, controller.getPane(), troop.getTargetToDoAct().getCoordinates(),troop.getCoordinates(), troop.getWidth(),
                    troop.getHeight(),true,troop.getShotPath());
            Platform.runLater(shot);
        }
        else if (troop instanceof SoldierCard)
        {
            SoldierCard soldier = (SoldierCard) troop;
            SoldierAnimMaker animMaker = new SoldierAnimMaker(soldier, controller.getPane() , board.getCoordinateUpdateQueue());
            Platform.runLater(animMaker);
        }
        else if (troop instanceof SpellCard && ((SpellCard)troop).getType() == CardTypes.RAGE)
        {
            Troop finalUpdated = troop;
            Platform.runLater(() -> {
                ImageView imageView = new ImageView();
                imageView.setImage(finalUpdated.getAttackFrm(1.0 , 0.100)); // may have bug here
                imageView.setFitWidth(finalUpdated.getWidth());
                imageView.setFitHeight(finalUpdated.getHeight());
                imageView.setTranslateX(finalUpdated.getCoordinates().getX() * 30);
                imageView.setTranslateY(finalUpdated.getCoordinates().getY() * 30);
                controller.getPane().getChildren().add(imageView);
                ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1) ,imageView);
                scaleTransition.setToX(0.2);
                scaleTransition.setToY(0.2);
                RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1),imageView);
                rotateTransition.setByAngle(720); // two rounds
                ParallelTransition parallel = new ParallelTransition(scaleTransition,rotateTransition);
                parallel.setOnFinished(actionEvent -> {
                    controller.getPane().getChildren().remove(imageView);
                    Circle circle = new Circle(finalUpdated.getCoordinates().getX() * 30 , finalUpdated.getCoordinates().getY() * 30, 5 * 30);
                    circle.setFill(Color.PURPLE);
                    controller.getPane().getChildren().add(circle);
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2) , circle);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0);
                    fadeTransition.setOnFinished(actionEvent1 -> {
                        controller.getPane().getChildren().remove(circle);
                    });
                    finalUpdated.run(); // act of rage here!!!
                    fadeTransition.play();

                });
                parallel.play();
            });
        }
        else if (troop instanceof SpellCard) // fireball and arrows
        {
            SpellCard spell = (SpellCard) troop;
            ShotAndSpellAnimMaker animMaker = new ShotAndSpellAnimMaker(spell, controller.getPane(),
                    spell.getDestination(),spell.getCoordinates(),spell.getWidth(),spell.getHeight(),false,null);
            Platform.runLater(animMaker);
        }
        else if (troop instanceof BuildingCard)
        {
            Platform.runLater(() -> {
                ImageView imageView = new ImageView();
                imageView.setImage(troop.getAttackFrm(1.0,0.100)); // fake time and duration
                imageView.setTranslateX(troop.getCoordinates().getX());
                imageView.setTranslateY(troop.getCoordinates().getY());
                imageView.setFitHeight(troop.getHeight());
                imageView.setFitWidth(troop.getWidth());

            });
        }
    }
}
