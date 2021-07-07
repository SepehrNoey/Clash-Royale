package scenes;


import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LogInScene extends EnteranceScene{


    private Image logInBlack;
    private Image logInWhite;

    @Override
    public Group buildScene()
    {
        Group logInPage = new Group();
        ImageView title = new ImageView(new Image("./pics/logInPage.png"));

        logInBlack = new Image("./pics/logInBlack.png");
        logInWhite = new Image("./pics/logInWhite.png");
        op = new ImageView(logInBlack);

        op.setFitWidth(75);
        op.setFitHeight(35);
        op.setTranslateX(250);
        op.setTranslateY(400);

        title.setTranslateX(100);
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
    public void operation()
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
