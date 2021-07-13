package client_side.view.scenes;


import javafx.scene.Scene;
import shared.model.player.Player;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.enums.MessageType;
import shared.model.Message;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class LogInScene extends EnteranceScene{


    private Image logInBlack;
    private Image logInWhite;

    public LogInScene(ExecutorService executor)
    {
        super(executor);
    }

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
    public void operation(Socket server, Scene scene)
    {
        op.setOnMouseEntered(e->{
            op.setImage(logInWhite);
            op.setCursor(Cursor.HAND);
        });

        op.setOnMouseExited(e->{
            op.setImage(logInBlack);
        });
        op.setOnMouseClicked(e->{ // password correctness must be handled later
            try {
                ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(server.getInputStream());

                oos.writeObject(new Message(MessageType.LOGIN_REQ,username.getText(),password.getText()));

                Message result = (Message) ois.readObject();
                Player player = null;
                if (result.getType() == MessageType.REFUSED)
                {
                    username.setPromptText("Username or password incorrect!");
                    username.selectAll();
                    username.requestFocus();
                }
                else { // accepted
                    String[] split = result.getContent().split(",");
                    ArrayList<Card> cards = new ArrayList<>();
                    for(int i = 0 ; i < 12 ; i++)
                    {
                        cards.add((Card) Troop.makeTroop(false,split[i + 4] , Integer.parseInt(split[1]) , null,split[0],null,null));
                    }
                    player = new Player(false,split[0] , split[3] , Integer.parseInt(split[1]) , Integer.parseInt(split[2]) , cards,server,oos,ois);
                    executor.execute(player.getGetter());
                }

                // now a player is created

                loadMenu(player,scene);

            } catch (IOException ioException) {
                System.out.println("Cannot make streams in operation...");
            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("Casting failed in operation");
            }

        });
    }
}