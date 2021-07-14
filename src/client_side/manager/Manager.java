package client_side.manager;

import client_side.controller.GameSceneController;
import client_side.starter.Starter;
import client_side.view.Render;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import shared.enums.BoardTypes;
import shared.model.Message;
import shared.model.Task;
import shared.model.TimeUpdater;
import shared.model.player.Player;

import java.io.IOException;
import java.util.*;

public class Manager {
    private Render render;
    private String gameMode; // can be enum .. later
    private Player me;
    private String[] names_levels; // other players' name and levels
    private GameSceneController sceneController;
    private Parent gameRoot;
    private Scene scene;
    private FXMLLoader loader;


    public Manager(Player me,String gameMode , Scene scene){
        this.scene = scene;
        this.me = me;
        this.gameMode = gameMode;
        names_levels = new String[gameMode.equals("2v2") ? 3 : 1];
        this.loader = new FXMLLoader(getClass().getResource("../view/scenes/GameScene.fxml"));
        try{
            gameRoot = loader.load();
        }catch (IOException e)
        {
            System.out.println("Exception in Manager.");
            e.printStackTrace();
        }
        sceneController = loader.getController(); // shouldn't be null !
        sceneController.setManager(this); // connecting manager and fxmlController

        render = new Render(me.getName() ,me.getSharedInbox() , gameMode.equals("2v2") ? BoardTypes.FOUR_PLAYERS : BoardTypes.TWO_PLAYERS); // the only messages that will be sent to render must be inGameMessages!!!
    }


    public void start(){
        scene.setRoot(gameRoot);
        Starter.stage.setWidth(832);

        // for bot mode
        try {
            Message msg = me.getSharedInbox().take(); // here just for bot implemented
            names_levels[0] = msg.getContent();
        }catch(InterruptedException e)
        {
            System.out.println("Interrupted while taking bot or players info.");
            e.printStackTrace();
        }
        sceneController.getEnemyNameLabel().setText(getPlayerName(0) + "  Lvl " + getPlayerLevel(0));
        Timer timer = new Timer();
        TimeUpdater timeUpdater = new TimeUpdater(sceneController.getTimeLabel());
        Task task = new Task(timeUpdater);
        timer.schedule(task, 0 , 1000);



    }

    private String getPlayerName(int index)
    {
        String[] split = names_levels[index].split(",");
        return split[0];
    }
    private int getPlayerLevel(int index)
    {
        String[] split = names_levels[index].split(",");
        return Integer.parseInt(split[1]);
    }


}
