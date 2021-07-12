package shared.model.troops.card;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;

public class SoldierCard extends Card{
    private SpeedTypes movingSpeed; // can be other types
    private double hitSpeed;
    private int hp;
    private Image[] walkFrames;
    private Image[] dieFrames;


    public SoldierCard(CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                       int width , int height , double range , TargetTypes target , int count , boolean areaSplash ,
                       Point2D coordinates , String owner, SpeedTypes movingSpeed , double hitSpeed , int hp , String walkFrmPath , int walkFrmNum , String dieFrmPath , int dieFrmNum)
    {
        super(type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count, areaSplash , coordinates , owner);
        this.movingSpeed = movingSpeed;
        this.hitSpeed = hitSpeed;
        this.hp = hp;
        walkFrames = new Image[walkFrmNum];
        for (int i = 0; i < walkFrmNum ; i++)
        {
            walkFrames[i] = new Image(walkFrmPath + i + ".png");
        }
        dieFrames = new Image[dieFrmNum];
        for (int i = 0 ; i < dieFrmNum ; i++)
        {
            dieFrames[i] = new Image(dieFrmPath + i + ".png");
        }
    }

//    public Image getWalkFrm(){
//
//    }

//    public Image getDieFrm(){
//
//    }


    /**
     * getter
     * @return movingSpeed enum
     */
    public SpeedTypes getMovingSpeed() {
        return movingSpeed;
    }

    /**
     * getter
     * @return hitSpeed
     */
    public double getHitSpeed() {
        return hitSpeed;
    }

    /**
     * getter
     * @return hp
     */
    public int getHp() {
        return hp;
    }
}
