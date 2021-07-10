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

import java.io.IOException;
import java.net.Socket;

public class Starter extends Application {


    @Override
    public void start(Stage stage){

        // connecting to server
        try {
            Socket server = new Socket("127.0.0.1",7000);

            MainScene mainPage = new MainScene();
            EnteranceScene signUpScene = new SignUpScene();
            EnteranceScene logInScene = new LogInScene();
            Group mainScene = mainPage.buildScene();
            Scene scene = new Scene(mainScene,630,900,Color.GREENYELLOW);
            mainPage.enableController(scene,signUpScene.buildScene(), logInScene.buildScene());
            signUpScene.enableController(scene,mainScene);
            logInScene.enableController(scene,mainScene);
            signUpScene.operation(server);
            logInScene.operation(server);

            stage.setTitle("Button Graphics");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            System.out.println("Cannot connect to server...");
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
