package client_side.view.scenes;


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

        signUpBlack = new Image("client_side/view/pics/signUpBlack.png");
        signUpWhite = new Image("client_side/view/pics/signUpWhite.png");
        ImageView background = new ImageView(new Image("client_side/view/pics/back.jpg"));
        logInBlack = new Image("client_side/view/pics/logInBlack.png");
        logInWhite = new Image("client_side/view/pics/logInWhite.png");
        signUp = new ImageView(signUpBlack);
        logIn = new ImageView(logInBlack);
        mainPage.getChildren().add(background);
        mainPage.getChildren().add(signUp);
        mainPage.getChildren().add(logIn);
        background.setFitHeight(900);
        background.setFitWidth(630);
        signUp.setFitHeight(75);
        signUp.setFitWidth(150);
        logIn.setFitHeight(75);
        logIn.setFitWidth(137);
        signUp.setTranslateX(170);
        signUp.setTranslateY(600);
        logIn.setTranslateX(175);
        logIn.setTranslateY(680);




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
