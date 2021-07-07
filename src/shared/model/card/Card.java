package shared.model.card;

import javafx.scene.image.Image;
import shared.CardTypes;

public abstract class Card {
    private CardTypes type;
    private int cost;
    private int damage; // except rage card
    private Image[] walkFrames;
    private Image[] attackFrames;
    private Image[] dieFrames;


    public Card(CardTypes type , int cost , int damage , String walkFrmPath ,int walkFrmNum, String attackFrmPath, int attackFrmNum, String dieFrmPath , int dieFrmNum){
        this.type = type;
        this.cost = cost;
        this.damage = damage;
        walkFrames = new Image[walkFrmNum];
        attackFrames = new Image[attackFrmNum];
        dieFrames = new Image[dieFrmNum];
        for (int i = 0 ; i < walkFrmNum ; i++)
        {
            walkFrames[i] = new Image( walkFrmPath + i + ".png"); // index from zero !!! and also all images should be .png !!
        }
        for (int i = 0 ; i < attackFrmNum ; i++)
        {
            attackFrames[i] = new Image(attackFrmPath + i + ".png");
        }
        for (int i = 0 ; i < dieFrmNum ; i++)
        {
            dieFrames[i] = new Image(dieFrmPath + i + ".png");
        }
    }

}
