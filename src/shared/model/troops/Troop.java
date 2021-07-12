package shared.model.troops;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.TargetTypes;

public abstract class Troop {
    private int damage; // except rage card
    private int level;
    private Image[] attackFrames; // all troops have attack frames
    private int width; // picture size
    private int height;
    private double range; // all cards have range or radius , but they are nearly same - melee means zero!
    private TargetTypes target;
    private Point2D coordinates;
    private String owner;

    public Troop(int damage , int level , String attackFrmPath , int attackFrmNum , int width ,
                 int height , double range , TargetTypes target , Point2D coordinates , String owner)
    {
        this.damage = damage;
        this.level = level;
        attackFrames = new Image[attackFrmNum];
        for (int i = 0 ; i < attackFrmNum ; i++)
        {
            attackFrames[i] = new Image(attackFrmPath + i + ".png");  // index from zero !!! and also all images should be .png !!
        }
        this.width = width;
        this.height = height;
        this.range = range;
        this.target = target;
        this.coordinates = coordinates;
        this.owner = owner;
    }


//    public Image getAttackFrm(){
//
//    }

    /**
     * getter
     * @return damage of card
     */
    public final int getDamage() {
        return damage;
    }

    /**
     * getter
     * @return level of card
     */
    public final int getLevel() {
        return level;
    }

    /**
     * getter
     * @return picture width
     */
    public final int getWidth() {
        return width;
    }

    /**
     * getter
     * @return height width
     */
    public final int getHeight() {
        return height;
    }

    /**
     * getter
     * @return range of attacking (if melee -> 0.0)
     */
    public final double getRange() {
        return range;
    }

    /**
     * getter
     * @return targetType enum
     */
    public final TargetTypes getTarget() {
        return target;
    }

    /**
     * getter
     * @return coordinates
     */
    public final Point2D getCoordinates(){
        return coordinates;
    }
}
