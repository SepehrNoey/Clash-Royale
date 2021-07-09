package client_side.view.scenes;


import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
   public void operation()
   {
       op.setOnMouseEntered(e->{
           op.setImage(signUpWhite);
           op.setCursor(Cursor.HAND);
       });

       op.setOnMouseExited(e->{
           op.setImage(signUpBlack);
       });

       op.setOnMouseClicked(e->{
           /* database connection*/
       });
   }

}
