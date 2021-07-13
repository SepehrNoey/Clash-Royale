package shared.model.troops;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import shared.enums.TargetTypes;
import shared.enums.TowerTypes;

public class Tower extends Troop{
    private TowerTypes type;
    private int hp;
    private double hitSpeed;
    private ImageView base;
    private ImageView head;

    public Tower(int damage, int level, String attackFrmPath, int attackFrmNum, int width,
                 int height, double range, TargetTypes target, TowerTypes type, int hp,
                 double hitSpeed, ImageView base, ImageView head , Point2D coordinates , String owner)
    {
        super(damage, level, attackFrmPath, attackFrmNum, width, height, range, target ,coordinates , owner);
        this.type = type;
        this.hp = hp;
        this.hitSpeed = hitSpeed;
        this.base = base;
        this.head = head;
    }

    /**
     * getter
     * @return type of tower
     */
    public TowerTypes getType() {
        return type;
    }

    /**
     * getter
     * @return hp
     */
    public int getHp() {
        return hp;
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
     * @return base ImageView
     */
    public ImageView getBase() {
        return base;
    }

    /**
     * getter
     * @return head ImageView
     */
    public ImageView getHead() {
        return head;
    }
}
