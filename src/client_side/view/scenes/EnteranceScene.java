package client_side.view.scenes;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import shared.model.player.Player;

public abstract class EnteranceScene {

    protected Image backBlack;
    protected Image backWhite;
    protected ImageView back;
    protected ImageView info;
    protected TextField username;
    protected PasswordField password;
    protected Label usernameLable;
    protected Label passwordLable;
    protected ImageView op;
    protected ImageView king;
    protected ImageView backView;
    protected ExecutorService executor;

    public EnteranceScene(ExecutorService executor)
    {
        this.executor = executor;
        backView = new ImageView(new Image("client_side/view/pics/back3.png"));
        backView.setFitWidth(630);
        backView.setFitHeight(900);

        backBlack = new Image("client_side/view/pics/backBlack.png");
        backWhite = new Image("client_side/view/pics/backWhite.png");
        back = new ImageView(backBlack);
        back.setFitHeight(40);
        back.setFitWidth(70);
        back.setTranslateX(30);
        back.setTranslateY(35);

        info = new ImageView(new Image("client_side/view/pics/information.png"));
        info.setFitWidth(400);
        info.setFitHeight(50);
        info.setTranslateX(130); // changed
        info.setTranslateY(200);


        king = new ImageView(new Image("client_side/view/pics/king.png"));
        king.setFitWidth(200);
        king.setFitHeight(380);
        king.setTranslateX(400);
        king.setTranslateY(500);


        usernameLable = new Label();
        usernameLable.setText("User Name : ");
        usernameLable.setTranslateX(100);
        usernameLable.setTranslateY(303);
        usernameLable.setFont(new Font(14));

        passwordLable = new Label();
        passwordLable.setText("Password : ");
        passwordLable.setTranslateX(100);
        passwordLable.setTranslateY(353);
        passwordLable.setFont(new Font(14));

        username = new TextField();
        username.setPromptText("User Name");
        username.setFocusTraversable(false);
        username.setTranslateX(200);
        username.setTranslateY(300);
        username.setPrefSize(250, Region.USE_PREF_SIZE);
        password = new PasswordField();
        password.setPromptText("Password");
        password.setPrefSize(250 , Region.USE_PREF_SIZE);
        password.setFocusTraversable(false);
        password.setTranslateX(200);
        password.setTranslateY(350);
    }
    public abstract Group buildScene();

    public void enableController(Scene scene, Group mainPage)
    {
        back.setOnMouseEntered(e->{
            back.setCursor(Cursor.HAND);
            back.setImage(backWhite);
        });

        back.setOnMouseExited(e->{
            back.setImage(backBlack);
        });

        back.setOnMouseClicked(e->{
            scene.setRoot(mainPage);
        });
    }

    public abstract void operation(Socket server,Scene scene);

    public void loadMenu(Player player, Scene scene)
    {
        MenuScene menu = new MenuScene(executor);

        scene.setRoot(menu.buildScene());
        menu.enableController(player,scene);
    }
}