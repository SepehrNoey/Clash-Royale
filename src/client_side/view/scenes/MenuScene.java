package client_side.view.scenes;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import shared.model.player.Player;

public class MenuScene {

    private ImageView profile;
    private ImageView deck;
    private ImageView history;
    private ImageView bot1;
    private ImageView bot2;
    private ImageView v1;
    private ImageView v2;
    private ImageView logOut;

    private Image profileBlack;
    private Image profileWhite;
    private Image deckBlack;
    private Image deckWhite;
    private Image historyBlack;
    private Image historyWhite;
    private Image bot1Black;
    private Image bot1White;
    private Image bot2Black;
    private Image bot2White;
    private Image v1Black;
    private Image v1White;
    private Image v2Black;
    private Image v2White;
    private Image logOutBlack;
    private Image logOutWhite;


    public Group buildScene()
    {
        Group menu = new Group();
        ImageView back = new ImageView(new Image("client_side/view/pics/menuBack.png"));
        back.setFitWidth(630);
        back.setFitHeight(900);
        menu.getChildren().add(back);


        profileBlack = new Image("client_side/view/pics/profileBlack.png");
        profileWhite = new Image("client_side/view/pics/profileWhite.png");
        profile = new ImageView(profileBlack);
        profile.setFitWidth(130);
        profile.setFitHeight(85);
        profile.setTranslateX(80);
        profile.setTranslateY(100);
        menu.getChildren().add(profile);




        deckBlack = new Image("client_side/view/pics/deckBlack.png");
        deckWhite = new Image("client_side/view/pics/deckWhite.png");
        deck = new ImageView(deckBlack);
        deck.setFitWidth(130);
        deck.setFitHeight(95);
        deck.setTranslateX(260);
        deck.setTranslateY(100);
        menu.getChildren().add(deck);


        historyBlack = new Image("client_side/view/pics/historyBlack.png");
        historyWhite = new Image("client_side/view/pics/historyWhite.png");
        history = new ImageView(historyBlack);
        history.setFitWidth(130);
        history.setFitHeight(95);
        history.setTranslateX(440);
        history.setTranslateY(100);
        menu.getChildren().add(history);



        bot1Black = new Image("client_side/view/pics/bot1Black.png");
        bot1White = new Image("client_side/view/pics/bot1White.png");
        bot1 = new ImageView(bot1Black);
        bot1.setFitWidth(130);
        bot1.setFitHeight(95);
        bot1.setTranslateX(150);
        bot1.setTranslateY(300);
        menu.getChildren().add(bot1);



        bot2Black = new Image("client_side/view/pics/bot2Black.png");
        bot2White = new Image("client_side/view/pics/bot2White.png");
        bot2 = new ImageView(bot2Black);
        bot2.setFitWidth(130);
        bot2.setFitHeight(95);
        bot2.setTranslateX(350);
        bot2.setTranslateY(300);
        menu.getChildren().add(bot2);



        v1Black = new Image("client_side/view/pics/1v1Black.png");
        v1White = new Image("client_side/view/pics/1v1White.png");
        v1 = new ImageView(v1Black);
        v1.setFitWidth(130);
        v1.setFitHeight(95);
        v1.setTranslateX(150);
        v1.setTranslateY(450);
        menu.getChildren().add(v1);




        v2Black = new Image("client_side/view/pics/2v2Black.png");
        v2White = new Image("client_side/view/pics/2v2White.png");
        v2 = new ImageView(v2Black);
        v2.setFitWidth(130);
        v2.setFitHeight(95);
        v2.setTranslateX(350);
        v2.setTranslateY(450);
        menu.getChildren().add(v2);



        logOutBlack = new Image("client_side/view/pics/logOutBlack.png");
        logOutWhite = new Image("client_side/view/pics/logOutWhite.png");
        logOut = new ImageView(logOutBlack);
        logOut.setFitWidth(80);
        logOut.setFitHeight(45);
        logOut.setTranslateX(50);
        logOut.setTranslateY(600);
        menu.getChildren().add(logOut);


        return menu;
    }

    public void enableController(Player player, Scene scene)
    {
        profile.setOnMouseEntered(e->{
            profile.setImage(profileWhite);
            profile.setCursor(Cursor.HAND);
        });
        profile.setOnMouseExited(e->{
            profile.setImage(profileBlack);
        });
        profile.setOnMouseClicked(e->{

        });

        deck.setOnMouseEntered(e->{
            deck.setImage(deckWhite);
            deck.setCursor(Cursor.HAND);
        });
        deck.setOnMouseExited(e->{
            deck.setImage(deckBlack);
        });
        deck.setOnMouseClicked(e->{

        });

        history.setOnMouseEntered(e->{
            history.setImage(historyWhite);
            history.setCursor(Cursor.HAND);
        });
        history.setOnMouseExited(e->{
            history.setImage(historyBlack);
        });
        history.setOnMouseClicked(e->{

        });

        bot1.setOnMouseEntered(e->{
            bot1.setImage(bot1White);
            bot1.setCursor(Cursor.HAND);
        });
        bot1.setOnMouseExited(e->{
            bot1.setImage(bot1Black);
        });
        bot1.setOnMouseClicked(e->{

        });


        bot2.setOnMouseEntered(e->{
            bot2.setImage(bot2White);
            bot2.setCursor(Cursor.HAND);
        });
        bot2.setOnMouseExited(e->{
            bot2.setImage(bot2Black);
        });
        bot2.setOnMouseClicked(e->{

        });

        v1.setOnMouseEntered(e->{
            v1.setImage(v1White);
            v1.setCursor(Cursor.HAND);
        });
        v1.setOnMouseExited(e->{
            v1.setImage(v1Black);
        });
        v1.setOnMouseClicked(e->{

        });

        v2.setOnMouseEntered(e->{
            v2.setImage(v2White);
            v2.setCursor(Cursor.HAND);
        });
        v2.setOnMouseExited(e->{
            v2.setImage(v2Black);
        });
        v2.setOnMouseClicked(e->{

        });

        logOut.setOnMouseEntered(e->{
            logOut.setImage(logOutWhite);
            logOut.setCursor(Cursor.HAND);
        });
        logOut.setOnMouseExited(e->{
            logOut.setImage(logOutBlack);
        });
        logOut.setOnMouseClicked(e->{

        });
    }

}