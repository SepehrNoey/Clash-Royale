package shared.model.troops.card;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.TargetTypes;

public class BuildingCard extends Card {

    private Image[] dieFrames; // explosion frames for buildings
    private double hitSpeed;
    private int lifeTime;
    private int hp;

    public BuildingCard(CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                        int width , int height , double range , TargetTypes target , int count , boolean areaSplash , Point2D coordinates,String owner, String dieFrmPath , int dieFrmNum , double hitSpeed , int lifeTime , int hp)
    {
        super(type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count , areaSplash ,coordinates , owner);
        dieFrames = new Image[dieFrmNum];
        for (int i = 0 ; i < dieFrmNum ; i++)
        {
            dieFrames[i] = new Image(dieFrmPath + i + ".png");
        }
        this.hitSpeed = hitSpeed;
        this.lifeTime = lifeTime;
        this.hp = hp;
    }

//    public Image getDieFrm(){
//
//    }


    /**
     * getter
     * @return hitSpeed
     */
    public double getHitSpeed() {
        return hitSpeed;
    }

    /**
     * getter
     * @return lifeTime
     */
    public int getLifeTime() {
        return lifeTime;
    }

    /**
     * getter
     * @return hp
     */
    public int getHp() {
        return hp;
    }
}
