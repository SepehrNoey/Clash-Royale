package client_side.manager;

import client_side.controller.GameSceneController;
import client_side.starter.Starter;
import client_side.view.render.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import shared.enums.BoardTypes;
import shared.enums.MessageType;
import shared.enums.TowerTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.player.Player;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Manager {
    private Render render;
    private String gameMode; // can be enum .. later
    private Player player;
    private String[] names_levels; // other players' name and levels
    private GameSceneController sceneController;
    private Parent gameRoot;
    private Scene scene;
    private FXMLLoader loader;
    private Board board;
    private int elixir;
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;
    private ElixirUpdater elixirUpdater;
    private Timer elixirTimer;
    private String botName;

    /**
     * constructor and initializer
     * @param player player
     * @param gameMode mode of game (1v1 , bot1 , ...)
     * @param scene scene of the game
     */
    public Manager(Player player, String gameMode,String botName, Scene scene){
        this.scene = scene;
        this.botName = botName;
        this.player = player;
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
        board = new Board(gameMode.equals("2v2") ? BoardTypes.FOUR_PLAYERS : BoardTypes.TWO_PLAYERS , false, player.getName() ,player.getLevel(), botName);
        board.addTowers(board.getType() , player.getName());
        render = new Render(player.getName() , player.getSharedInbox(), board); // the only messages that will be sent to render must be inGameMessages!!!
        scene.setRoot(gameRoot);
        Starter.stage.setWidth(814);

        // for bot mode
        try {
            Message msg = player.getSharedInbox().take(); // here just for bot implemented
            names_levels[0] = msg.getContent();
        }catch(InterruptedException e)
        {
            System.out.println("Interrupted while taking bot or players info.");
            e.printStackTrace();
        }
        sceneController.getEnemyNameLabel().setText(getPlayerName(0) + "  Lvl " + getPlayerLevel(0));
        Timer timeTimer = new Timer();
        TimeUpdater timeUpdater = new TimeUpdater(sceneController.getTimeLabel());
        TaskForTime taskForTime = new TaskForTime(timeUpdater);
        timeTimer.schedule(taskForTime, 0 , 1000);

        elixir = 0; // handled for elixir updater
        elixirTimer = new Timer();
        elixirUpdater = new ElixirUpdater(this , elixirTimer ,85, sceneController.getBarsPane());
        elixirUpdater.fastUpdate((int)sceneController.getBarsPane().getChildren().get(6).getLayoutY());

        TaskForElixir taskForElixir = new TaskForElixir(elixirUpdater);
        elixirUpdater.setItsTask(taskForElixir);
        elixirTimer.schedule(taskForElixir,0,27);
        card1 = player.getDeck().get(0);
        card2 = player.getDeck().get(1);
        card3 = player.getDeck().get(2);
        card4 = player.getDeck().get(3);
        sceneController.getCard1().setImage(card1.getCardImage());
        sceneController.getCard2().setImage(card2.getCardImage());
        sceneController.getCard3().setImage(card3.getCardImage());
        sceneController.getCard4().setImage(card4.getCardImage());

    }


    public void start(){




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

    public void cardDroppingReq(String card , int tileX , int tileY){
        Card chosen = getCardByStr(card); // cards must be allocated before start!!! (4 first card in hand)
        if (!board.isValidAddress(chosen,tileX,tileY))
            return;
        if (chosen.getCost() > elixir) // is updated by gui
            return;
        elixirUpdater.decrease(chosen.getCost());
        // valid choosing

        player.getSender().sendMsg(new Message(MessageType.PICKED_CARD , player.getName() , chosen.getType() + "," + tileX + "," + tileY));
        chosen.setCoordinates(new Point2D(tileX , tileY));
        // + updating elixir bar
        render.addForRender(chosen);




    }


    public Card getCardByStr(String str){
        if (str.equals("card1"))
            return card1;
        else if (str.equals("card2"))
            return card2;
        else if (str.equals("card3"))
            return card3;
        else if (str.equals("card4"))
            return card4;

        return null;
    }



    public void updateElixir(int elixir){
        this.elixir = elixir;
    }


}
