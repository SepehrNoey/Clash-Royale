package shared.model.card;

import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;

public class SpellCard extends Card{
    private SpeedTypes movingSpeed; // the speed of moving or transitions on its node
    private int damageBoost; // if exists
    private int speedBoost;
    private int hitSpeedBoost;
    private boolean reducedDmg; // reduced damage to crown towers

    public SpellCard(CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                     int width , int height , double range , TargetTypes target , int count , boolean areaSplash , SpeedTypes movingSpeed ,
                     int damageBoost , int speedBoost , int hitSpeedBoost , boolean reducedDmg)
    {
        super(type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count, areaSplash);
        this.movingSpeed = movingSpeed;
        this.damageBoost = damageBoost;
        this.speedBoost = speedBoost;
        this.hitSpeedBoost = hitSpeedBoost;
        this.reducedDmg = reducedDmg;
    }

    /**
     * getter
     * @return movingSpeed - the speed of moving or transitions on this node
     */
    public SpeedTypes getMovingSpeed() {
        return movingSpeed;
    }

    /**
     * getter
     * @return damage boost value
     */
    public int getDamageBoost() {
        return damageBoost;
    }

    /**
     * getter
     * @return speed boost value
     */
    public int getSpeedBoost() {
        return speedBoost;
    }

    /**
     * getter
     * @return hit speed boost value
     */
    public int getHitSpeedBoost() {
        return hitSpeedBoost;
    }

    /**
     * getter
     * @return true if it damages crown towers less than others , else false
     */
    public boolean isReducedDmg() {
        return reducedDmg;
    }
}
