package shared.model.troops;

import javafx.geometry.Point2D;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;
import shared.enums.TowerTypes;
import shared.model.Board;
import shared.model.troops.card.Card;
import shared.model.troops.card.SpellCard;

import java.util.ArrayList;
import java.util.Timer;


public class Tower extends Troop{
    private TowerTypes type;
    private int hp;
    private double hitSpeed;
    private Board board;

    public Tower(boolean isServerSide, int damage, int level, String attackFrmPath, int attackFrmNum, int width,
                 int height, double range, TargetTypes target, TowerTypes type, int hp,
                 double hitSpeed, Point2D coordinates , String owner,String shotPath)
    {
        super(isServerSide, damage, level, attackFrmPath, attackFrmNum, width, height, range, target ,coordinates , owner,shotPath);
        this.type = type;
        this.hp = hp;
        this.hitSpeed = hitSpeed;
    }

    @Override
    public void run() { // attack task
        Troop toAttack = getTargetToDoAct();
        if (toAttack != null) { // target exists
            toAttack.getBeingHit(getDamage());
            getRender().addForRender(this,true);
        }
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
     * to check if can attack anyone or not
     * @return true if can , false if can't
     */

    @Override
    public boolean canAttack(Troop troop) {
        if (troop.getOwner().equals(getOwner()))
            return false;
        if (!(Math.pow(troop.getCoordinates().getX() - this.getCoordinates().getX() , 2) +
                Math.pow(troop.getCoordinates().getY() - this.getCoordinates().getY() , 2) <= getRange()) )
            return false;
        if (troop instanceof SpellCard)
            return false;
        if (getTarget() == TargetTypes.AIR_GROUND)
            return true;
        else {
            if (troop instanceof Card)
            {
                Card card = (Card) troop;
                if (card.getType() == CardTypes.BABY_DRAGON)
                    return false;
                return true;
            }
            return true;
        }
    }

    public void updateState(Board board , Troop changedTroop , boolean isDead) {
        this.board = board;
        if (isDead)
        {
            if (getTargetToDoAct() != null && changedTroop.getId().equals(getTargetToDoAct().getId()))
            {
                getActTimer().cancel();
                setTargetToDoAct(null);
                ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                if (nearEnemies.size() > 0) // still enemy around here to attack
                {
                    setTargetToDoAct(nearEnemies.get(0));
                    setActTimer(new Timer());
                    getActTimer().schedule(this,0 , (long) (1000 * hitSpeed));
                    // no direction for Building cards - state can be recognized by targetToDoAct
                }
            }
        }
        else { // updating is because of coordinate changing of some troop
            if (getTargetToDoAct() == null) // if there is no target , so decide on state changing
            {
                ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                if (nearEnemies.size() > 0)
                {
                    setTargetToDoAct(nearEnemies.get(0));
                    setActTimer(new Timer());
                    getActTimer().schedule(this,0,(long) (1000 * hitSpeed));
                }
            }
        }
    }


        /**
         * setter for hp
         * @param damage to be hit
         */
    public void setHp(int damage) {
        if (damage > hp)
            hp = 0;
        else
            hp -= damage;
    }


    @Override
    public void setHitSpeed(double newHitSpeed) {
        this.hitSpeed = newHitSpeed;
    }

    @Override
    public void setMovingSpeed(double newSpeed) {
        // fake
    }

    @Override
    public SpeedTypes getMovingSpeed() {
        return null; // fake
    }

}
