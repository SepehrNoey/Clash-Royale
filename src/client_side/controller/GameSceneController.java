package client_side.controller;

import client_side.manager.Manager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;

public class GameSceneController {

    @FXML private HBox hBox;
    @FXML private Pane pane;
    @FXML private ImageView upLeftWeaponBase;
    @FXML private ImageView upLeftWeaponHead;
    @FXML private ImageView downLeftWeaponBase;
    @FXML private ImageView downLeftWeaponHead;
    @FXML private ImageView upBigWeaponBase;
    @FXML private ImageView upBigWeaponHead;
    @FXML private ImageView upRightWeaponBase;
    @FXML private ImageView upRightWeaponHead;
    @FXML private ImageView downBigWeaponBase;
    @FXML private ImageView downBigWeaponHead;
    @FXML private ImageView downRightWeaponBase;
    @FXML private ImageView downRightWeaponHead;
    @FXML private ImageView card1;
    @FXML private ImageView card2;
    @FXML private ImageView card3;
    @FXML private ImageView card4;
    @FXML private ImageView nextCard;
    @FXML private ImageView bar1;
    @FXML private ImageView bar2;
    @FXML private ImageView bar3;
    @FXML private ImageView bar4;
    @FXML private ImageView bar5;
    @FXML private ImageView bar6;
    @FXML private ImageView bar7;
    @FXML private ImageView bar8;
    @FXML private ImageView bar9;
    @FXML private ImageView bar10;
    @FXML private Label enemyNameLabel;
    @FXML private Label timeLabel;
    @FXML private Pane cardPane;
    @FXML private Pane barsPane;

    private Manager manager;


    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Pane getPane() {
        return pane;
    }

    public HBox getHBox() {
        return hBox;
    }

    public ImageView getCard1() {
        return card1;
    }

    public ImageView getCard2() {
        return card2;
    }

    public ImageView getCard3() {
        return card3;
    }

    public ImageView getCard4() {
        return card4;
    }

    public ImageView getNextCard() {
        return nextCard;
    }

    public ImageView getUpLeftWeaponBase() {
        return upLeftWeaponBase;
    }

    public ImageView getUpLeftWeaponHead() {
        return upLeftWeaponHead;
    }

    public ImageView getDownLeftWeaponBase() {
        return downLeftWeaponBase;
    }

    public ImageView getDownLeftWeaponHead() {
        return downLeftWeaponHead;
    }

    public ImageView getUpBigWeaponBase() {
        return upBigWeaponBase;
    }

    public ImageView getUpBigWeaponHead() {
        return upBigWeaponHead;
    }

    public ImageView getUpRightWeaponBase() {
        return upRightWeaponBase;
    }

    public ImageView getUpRightWeaponHead() {
        return upRightWeaponHead;
    }

    public ImageView getDownBigWeaponBase() {
        return downBigWeaponBase;
    }

    public ImageView getDownBigWeaponHead() {
        return downBigWeaponHead;
    }

    public ImageView getDownRightWeaponBase() {
        return downRightWeaponBase;
    }

    public ImageView getDownRightWeaponHead() {
        return downRightWeaponHead;
    }


    public ImageView getBar1() {
        return bar1;
    }

    public ImageView getBar2() {
        return bar2;
    }

    public ImageView getBar3() {
        return bar3;
    }

    public ImageView getBar4() {
        return bar4;
    }

    public ImageView getBar5() {
        return bar5;
    }

    public ImageView getBar6() {
        return bar6;
    }

    public ImageView getBar7() {
        return bar7;
    }

    public ImageView getBar8() {
        return bar8;
    }

    public ImageView getBar9() {
        return bar9;
    }

    public ImageView getBar10() {
        return bar10;
    }

    public Label getEnemyNameLabel() {
        return enemyNameLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Manager getManager() {
        return manager;
    }

    public Pane getBarsPane() {
        return barsPane;
    }

    @FXML
    void dragDetected(MouseEvent event) {
        Dragboard db = cardPane.startDragAndDrop(TransferMode.ANY);
        ClipboardContent cb = new ClipboardContent();
        cb.putString(getCard(event));
        db.setContent(cb);
        event.consume();
    }

    @FXML
    void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasString())
        {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    void handleDragDropped(DragEvent event) {
        manager.cardDroppingReq(event.getDragboard().getString(),(int)Math.round(event.getSceneX() / 30.0) - 1,(int)Math.round(event.getSceneY() / 30.0) - 1);
        event.consume();
    }

    private String getCard(MouseEvent event){
        if (event.getSource().toString().contains("card1"))
            return "card1";
        else if (event.getSource().toString().contains("card2"))
            return "card2";
        else if (event.getSource().toString().contains("card3"))
            return "card3";
        else return "card4";
    }

}


