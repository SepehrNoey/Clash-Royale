package client_side.view.render;

import client_side.controller.GameSceneController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import shared.enums.CardTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.Tower;
import shared.model.troops.Troop;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.card.SpellCard;
import shared.model.troops.timerTasks.BuildingAnimation;
import shared.model.troops.timerTasks.ShotAndSpellAnimMaker;
import shared.model.troops.timerTasks.SoldierAnimMaker;


public class Render{
    private Board board;
    private String humanPlayer;
    private int cardUsedNum;
    private GameSceneController controller;

    public Render(String humanPlayer , Board board , GameSceneController controller){
        cardUsedNum = 0;
        this.humanPlayer = humanPlayer;
        this.board = board;
        this.controller = controller;
    }

    public void gameFinishedMsg(Message msg) // on javaFX thread
    {
        Label label = new Label();
        label.setFont(new Font(25));
        label.setPrefWidth(300);
        label.setTranslateX(165);
        label.setTranslateY(440);
        String text = "Game Over\nWinner: " + msg.getContent();
        label.setText(text);
        controller.getPane().getChildren().add(label);
    }

    public void delete(Tower tower) // this method is used just for tower deleting , others , they delete themself
    {
        Platform.runLater(()->{
            ImageView imageView = null;
            if (tower.getId().contains("bot"))
            {
                if (tower.getId().contains("right"))
                    imageView = controller.getUpRightWeaponHead();
                else if (tower.getId().contains("middle"))
                    imageView = controller.getUpBigWeaponHead();
                else
                    imageView = controller.getUpLeftWeaponHead();
            }
            else {
                if (tower.getId().contains("right"))
                    imageView = controller.getDownRightWeaponHead();
                else if (tower.getId().contains("middle"))
                    imageView = controller.getDownBigWeaponHead();
                else
                    imageView = controller.getDownLeftWeaponHead();
            }
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2),imageView);
            ImageView finalImageView = imageView;
            fadeTransition.setOnFinished((ActionEvent event)->{
                finalImageView.setImage(null);
                controller.getPane().getChildren().remove(finalImageView);
            });
            fadeTransition.play();
        });
    }


    public void addForRender(Troop troop , boolean isShot){
        System.out.println("5) in start of addForRender");
        if (isShot) // buildings and towers just send their shots to render (in addForRender , in BuildingCard run method)
        {
            if (troop instanceof BuildingCard && ((BuildingCard) troop).getType() == CardTypes.INFERNO_TOWER)
            {
                Platform.runLater(()->{
                    ImageView shot1 = new ImageView(troop.getShotPath());
                    shot1.setTranslateX(troop.getTargetToDoAct().getCoordinates().getX() * 30);
                    shot1.setTranslateY(troop.getTargetToDoAct().getCoordinates().getY() * 30);
                    double deltaY = troop.getTargetToDoAct().getCoordinates().getY() - troop.getCoordinates().getY();
                    double deltaX = troop.getTargetToDoAct().getCoordinates().getX() - troop.getCoordinates().getX();
                    shot1.setFitWidth(deltaX);
                    shot1.setFitHeight(deltaY);
                    shot1.setRotate(ShotAndSpellAnimMaker.getRotationDegree(troop.getCoordinates(),troop.getTargetToDoAct().getCoordinates()));
                    controller.getPane().getChildren().add(shot1);
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.75),shot1);
                    fadeTransition.setCycleCount(4);
                    fadeTransition.setOnFinished((ActionEvent event)->{
                        shot1.setImage(null);
                        controller.getPane().getChildren().remove(shot1);
                    } );
                    fadeTransition.play();
                });
            }
            else {
                Platform.runLater(() ->{
                    ImageView imageView = new ImageView();
                    imageView.setImage(new Image(troop.getShotPath()));
                    if (troop.getOwner().contains("bot"))
                        imageView.setRotate(180);
                    if (!(troop instanceof Tower))
                    {
                        imageView.setFitHeight(22);
                        imageView.setFitWidth(10);
                        imageView.setTranslateX((troop.getCoordinates().getX() + 1) * 30);
                    }else {
                        imageView.setFitHeight(90); /// have to resize here for wizard archer ....
                        imageView.setFitWidth(60);
                        imageView.setTranslateX(troop.getCoordinates().getX() * 30);
                    }
                    imageView.setTranslateY(troop.getCoordinates().getY() * 30);
                    controller.getPane().getChildren().add(imageView);
                    TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5),imageView);
                    transition.setToX(troop.getTargetToDoAct().getCoordinates().getX() * 30);
                    transition.setToY(troop.getTargetToDoAct().getCoordinates().getY() * 30);
                    transition.setOnFinished((ActionEvent event) ->{
                        imageView.setImage(null);
                        controller.getPane().getChildren().remove(imageView);
                    });
                    transition.play();
                });
            }
        }
        else if (troop instanceof SoldierCard)
        {
            System.out.println("got in render in soldierCard part");
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
            Platform.runLater(()->{
                ImageView imageView = new ImageView(troop.getAttackFrm(1.0,0.100));
                imageView.setFitWidth(troop.getWidth());
                imageView.setFitHeight(troop.getHeight());
                imageView.setTranslateX(troop.getCoordinates().getX() * 30);
                imageView.setTranslateY(troop.getCoordinates().getY() * 30);
                controller.getPane().getChildren().add(imageView);
                if (troop.getOwner().contains("bot"))
                    imageView.setRotate(180);
                TranslateTransition transition = new TranslateTransition(Duration.seconds(2) , imageView);
                transition.setToX(((SpellCard) troop).getDestination().getX() * 30);
                transition.setToY(((SpellCard) troop).getDestination().getY() * 30);
                transition.setOnFinished(actionEvent -> {
                    troop.run();
                    imageView.setImage(null);
                    controller.getPane().getChildren().remove(imageView);
                });
                transition.play();
            });

//            SpellCard spell = (SpellCard) troop;
//            ShotAndSpellAnimMaker animMaker = new ShotAndSpellAnimMaker(spell, controller.getPane(),
//                    spell.getDestination(),spell.getCoordinates(),spell.getWidth(),spell.getHeight(),false,null);
//            Platform.runLater(animMaker);
        }
        else if (troop instanceof BuildingCard)
        {
            BuildingAnimation animation = new BuildingAnimation((BuildingCard) troop, controller.getPane());
            Platform.runLater(animation);
        }
        System.out.println("6) in end of addForRender");
    }
}
