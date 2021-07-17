package shared.model.troops;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import shared.enums.TargetTypes;
import shared.enums.TowerTypes;

public class Tower extends Troop{
    private TowerTypes type;
    private int hp;
    private double hitSpeed;

    public Tower(boolean isServerSide,int damage, int level, String attackFrmPath, int attackFrmNum, int width,
                 int height, double range, TargetTypes target, TowerTypes type, int hp,
                 double hitSpeed, Point2D coordinates , String owner)
    {
        super(isServerSide, damage, level, attackFrmPath, attackFrmNum, width, height, range, target ,coordinates , owner);
        this.type = type;
        this.hp = hp;
        this.hitSpeed = hitSpeed;
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
}
