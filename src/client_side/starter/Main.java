package client_side.starter;

import client_side.view.scenes.MainScene;
import client_side.view.scenes.EnteranceScene;
import client_side.view.scenes.SignUpScene;
import client_side.view.scenes.LogInScene;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage){




        MainScene mainPage = new MainScene();
        EnteranceScene signUpScene = new SignUpScene();
        EnteranceScene logInScene = new LogInScene();
        Group mainScene = mainPage.buildScene();
        Scene scene = new Scene(mainScene,630,900,Color.GREENYELLOW);
        mainPage.enableController(scene,signUpScene.buildScene(), logInScene.buildScene());
        signUpScene.enableController(scene,mainScene);
        logInScene.enableController(scene,mainScene);
        signUpScene.operation();
        logInScene.operation();

        stage.setTitle("Button Graphics");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
