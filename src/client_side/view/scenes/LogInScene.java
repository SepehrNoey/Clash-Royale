package client_side.view.scenes;


import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.Socket;

public class LogInScene extends EnteranceScene{


    private Image logInBlack;
    private Image logInWhite;

    @Override
    public Group buildScene()
    {
        Group logInPage = new Group();
        ImageView title = new ImageView(new Image("client_side/view/pics/logInPage.png"));

        logInBlack = new Image("client_side/view/pics/logInBlack.png");
        logInWhite = new Image("client_side/view/pics/logInWhite.png");
        op = new ImageView(logInBlack);

        op.setFitWidth(85);
        op.setFitHeight(40);
        op.setTranslateX(280);
        op.setTranslateY(400);

        title.setTranslateX(185);
        title.setTranslateY(100);
        title.setFitWidth(300);
        title.setFitHeight(80);

        logInPage.getChildren().add(back);
        logInPage.getChildren().add(title);
        logInPage.getChildren().add(info);
        logInPage.getChildren().add(usernameLable);
        logInPage.getChildren().add(passwordLable);
        logInPage.getChildren().add(username);
        logInPage.getChildren().add(password);
        logInPage.getChildren().add(op);
        return logInPage;
    }

    @Override
    public void operation(Socket server)
    {
        op.setOnMouseEntered(e->{
            op.setImage(logInWhite);
            op.setCursor(Cursor.HAND);
        });

        op.setOnMouseExited(e->{
            op.setImage(logInBlack);
        });
        op.setOnMouseClicked(e->{
            /*database connection*/
        });
    }
}
