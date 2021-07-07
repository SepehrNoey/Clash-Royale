package scenes;


import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainScene {

    private ImageView signUp;
    private ImageView logIn;
    private Image signUpWhite;
    private Image signUpBlack;
    private Image logInBlack;
    private Image logInWhite;

    public Group buildScene()
    {
        Group mainPage = new Group();

        signUpBlack = new Image("./pics/signUpBlack.png");
        signUpWhite = new Image("./pics/signUpWhite.png");
        ImageView background = new ImageView(new Image("./pics/back.jpg"));
        logInBlack = new Image("./pics/logInBlack.png");
        logInWhite = new Image("./pics/logInWhite.png");
        signUp = new ImageView(signUpBlack);
        logIn = new ImageView(logInBlack);
        mainPage.getChildren().add(background);
        mainPage.getChildren().add(signUp);
        mainPage.getChildren().add(logIn);
        background.setFitHeight(600);
        background.setFitWidth(512);
        signUp.setFitHeight(70);
        signUp.setFitWidth(140);
        logIn.setFitHeight(70);
        logIn.setFitWidth(130);
        signUp.setTranslateX(150);
        signUp.setTranslateY(400);
        logIn.setTranslateX(155);
        logIn.setTranslateY(480);




        return mainPage;
    }

    public void enableController(Scene scene, Group signUpPage, Group logInPage)
    {
        signUp.setOnMouseEntered(e->{
            signUp.setCursor(Cursor.HAND);
            signUp.setImage(signUpWhite);
        });

        signUp.setOnMouseExited(e->{
            signUp.setImage(signUpBlack);
        });

        signUp.setOnMouseClicked(e->{
            scene.setRoot(signUpPage);
        });

        logIn.setOnMouseEntered(e->{
            logIn.setCursor(Cursor.HAND);
            logIn.setImage(logInWhite);
        });

        logIn.setOnMouseExited(e->{
            logIn.setImage(logInBlack);
        });

        logIn.setOnMouseClicked(e->{
            scene.setRoot(logInPage);
        });
    }

}
