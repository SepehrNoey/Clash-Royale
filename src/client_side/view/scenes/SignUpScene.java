package client_side.view.scenes;


import javafx.scene.Scene;
import server_side.starter.MsgSeparator;
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

public class SignUpScene extends EnteranceScene{

    private Image signUpBlack;
    private Image signUpWhite;

    public SignUpScene(ExecutorService executor)
    {
        super(executor);
    }

    @Override
    public Group buildScene()
    {
        Group signUpPage = new Group();
        ImageView title = new ImageView(new Image("client_side/view/pics/signUpPage.png"));
        signUpBlack = new Image("client_side/view/pics/signUpBlack.png");
        signUpWhite = new Image("client_side/view/pics/signUpWhite.png");
        op = new ImageView(signUpBlack);

        op.setFitWidth(85);
        op.setFitHeight(40);
        op.setTranslateX(280);
        op.setTranslateY(400);

        title.setTranslateX(172);
        title.setTranslateY(100);
        title.setFitWidth(300);
        title.setFitHeight(80);


        signUpPage.getChildren().add(back);
        signUpPage.getChildren().add(title);
        signUpPage.getChildren().add(info);
        signUpPage.getChildren().add(usernameLable);
        signUpPage.getChildren().add(passwordLable);
        signUpPage.getChildren().add(username);
        signUpPage.getChildren().add(password);
        signUpPage.getChildren().add(op);
        return signUpPage;
    }

    @Override
    public void operation(Socket server, Scene scene)
    {
        op.setOnMouseEntered(e->{
            op.setImage(signUpWhite);
            op.setCursor(Cursor.HAND);
        });

        op.setOnMouseExited(e->{
            op.setImage(signUpBlack);
        });

        op.setOnMouseClicked(e->{
            try {
                ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(server.getInputStream());

                oos.writeObject(new Message(MessageType.SIGNUP_REQ,username.getText(),password.getText()));

                Message result = (Message) ois.readObject();
                Player player = null;
                if (result.getType() == MessageType.REFUSED)
                {
                    username.setPromptText("Already exists!");
                    username.selectAll();
                    username.requestFocus();
                }
                else { // accepted
                    String[] split = result.getContent().split(",");
                    ArrayList<Card> cards = new ArrayList<>();
                    for(int i = 0 ; i < 12 ; i++)
                    {
                        cards.add((Card) Troop.makeTroop(false,split[i + 4] , 1 , null,split[0],null,null));
                    }
                    player = new Player(false,split[0] , split[3] , Integer.parseInt(split[1]) , Integer.parseInt(split[2]) , cards,server,oos,ois);
                    executor.execute(player.getGetter());
                }

                // now a player is created

                loadMenu(player,scene);

            } catch (IOException ioException) {
                System.out.println("Cannot make streams in operation...");
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
    }

}