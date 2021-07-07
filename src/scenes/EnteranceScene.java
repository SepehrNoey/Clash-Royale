package scenes;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public EnteranceScene()
    {
        backBlack = new Image("./pics/backBlack.png");
        backWhite = new Image("./pics/backWhite.png");
        back = new ImageView(backBlack);
        back.setFitHeight(35);
        back.setFitWidth(60);
        back.setTranslateX(30);
        back.setTranslateY(35);

        info = new ImageView(new Image("./pics/information.png"));
        info.setFitWidth(400);
        info.setFitHeight(50);
        info.setTranslateX(60);
        info.setTranslateY(200);

        usernameLable = new Label();
        usernameLable.setText("User Name : ");
        usernameLable.setTranslateX(100);
        usernameLable.setTranslateY(303);

        passwordLable = new Label();
        passwordLable.setText("Password : ");
        passwordLable.setTranslateX(100);
        passwordLable.setTranslateY(353);

        username = new TextField();
        username.setPromptText("User Name");
        username.setFocusTraversable(false);
        username.setTranslateX(200);
        username.setTranslateY(300);
        password = new PasswordField();
        password.setPromptText("Password");
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

    public abstract void operation();
}
