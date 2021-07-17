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

    /**
     * constructor and initializer
     * @param player player
     * @param gameMode mode of game (1v1 , bot1 , ...)
     * @param scene scene of the game
     */
    public Manager(Player player, String gameMode , Scene scene){
        this.scene = scene;
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
        board = new Board(gameMode.equals("2v2") ? BoardTypes.FOUR_PLAYERS : BoardTypes.TWO_PLAYERS , false, player.getName());
        addTowers(board.getType());
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

    public void cardDroppingReq(String card , double x , double y){
        elixirUpdater.decrease(5);

        Card chosen = getCardByStr(card); // cards must be allocated before start!!! (4 first card in hand)
        if (!board.isValidAddress(chosen,x,y))
            return;
        if (chosen.getCost() > elixir) // is updated by gui
            return;

        // valid choosing


        // + updating elixir bar
        render.addForRender(chosen);




    }


    public Card getCardByStr(String str){
        return switch (str) {
            case "card1" -> card1;
            case "card2" -> card2;
            case "card3" -> card3;
            case "card4" -> card4;
            default -> null;
        };
    }

    private void addTowers(BoardTypes boardType)
    {
        if (boardType == BoardTypes.TWO_PLAYERS)  // attention !! point2D is addressed by index  0 to MAX !!! - like Board
        {
            // player towers
            board.addTroop(Troop.makeTroop(false,TowerTypes.KING_TOWER.toString() , player.getLevel() ,
                    new Point2D(9,25) , player.getName(),sceneController.getDownBigWeaponBase() , sceneController.getDownBigWeaponHead()));
            board.addTroop(Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , player.getLevel() ,
                    new Point2D(4,24) , player.getName(),sceneController.getDownLeftWeaponBase() , sceneController.getDownLeftWeaponHead()));
            board.addTroop(Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , player.getLevel() ,
                    new Point2D(15,24) , player.getName(),sceneController.getDownRightWeaponBase() , sceneController.getDownRightWeaponHead()));

            // enemy towers
            board.addTroop(Troop.makeTroop(false,TowerTypes.KING_TOWER.toString() , player.getLevel() ,
                    new Point2D(9,3) , gameMode,sceneController.getDownBigWeaponBase() , sceneController.getDownBigWeaponHead()));// attention : !! game mode is set for bot name
            board.addTroop(Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , player.getLevel() ,
                    new Point2D(4,4) , gameMode,sceneController.getDownLeftWeaponBase() , sceneController.getDownLeftWeaponHead()));
            board.addTroop(Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , player.getLevel() ,
                    new Point2D(15,4) , gameMode,sceneController.getDownRightWeaponBase() , sceneController.getDownRightWeaponHead()));

        }
        else {
            // for four player board
        }
    }

    public void updateElixir(int elixir){
        this.elixir = elixir;
    }


}
