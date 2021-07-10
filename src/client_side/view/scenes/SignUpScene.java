package client_side.view.scenes;


import shared.model.player.Player;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.enums.MessageType;
import shared.model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SignUpScene extends EnteranceScene{

    private Image signUpBlack;
    private Image signUpWhite;

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
    public void operation(Socket server)
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

                oos.writeObject(new Message(MessageType.SIGNUP_REQ,"player","req"));

                String usr = username.getText();
                String pass = password.getText();

                oos.writeObject(usr);
                oos.writeObject(pass);

                Player serverPlayer = (Player) ois.readObject();

                Player player = new Player(serverPlayer.getName(), serverPlayer.getPassword(), serverPlayer.getLevel(),
                        serverPlayer.getXp(), serverPlayer.getCards(), server,oos,ois);

            } catch (IOException ioException) {
                System.out.println("Cannot make streams in operation...");
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
    }

}
