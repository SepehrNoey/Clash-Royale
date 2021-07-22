package shared.model.troops.card;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.State;
import shared.enums.TargetTypes;
import shared.model.Board;
import shared.model.troops.Troop;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BuildingCard extends Card {
    private Image[] dieFrames; // explosion frames for buildings
    private double hitSpeed;
    private int lifeTime;
    private int hp;
    private Board board;

    public BuildingCard( boolean isServerSide, CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                        int width , int height , double range , TargetTypes target , int count , boolean areaSplash , Point2D coordinates, String owner, String dieFrmPath , int dieFrmNum , double hitSpeed , int lifeTime , int hp ,String shotPath)
    {
        super(isServerSide,type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count , areaSplash ,coordinates , owner,shotPath);
        dieFrames = new Image[dieFrmNum];
        if (!isServerSide)
            for (int i = 0 ; i < dieFrmNum ; i++)
                dieFrames[i] = new Image(dieFrmPath + i + ".png");

        this.hitSpeed = hitSpeed;
        this.lifeTime = lifeTime;
        this.hp = hp;
    }

    @Override
    public void run() { // attack task
        Troop toAttack = getTargetToDoAct();
        if (toAttack != null)
        {
            toAttack.getBeingHit(getDamage());
            if (!isServerSide() && getType() == CardTypes.CANNON)
            {
                getRender().addForRender(this,true);
            }
        }
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

    /**
     * to check if can attack anyone or not
     * @return true if can , false if can't
     */
    @Override
    public boolean canAttack(Troop troop){ // canAttack can be changed later to just one method!
        if (troop.getOwner().equals(getOwner()))
            return false;
        if (!(Math.pow(troop.getCoordinates().getX() - this.getCoordinates().getX() , 2) +
                Math.pow(troop.getCoordinates().getY() - this.getCoordinates().getY() , 2) <= getRange()) )
            return false;
        if (troop instanceof SpellCard)
            return false;
        if (getTarget() == TargetTypes.AIR_GROUND)
            return true;
        else { // ground
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
                getExec().shutdownNow();
                setTargetToDoAct(null);
                setState(State.STOP);
                ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                if (nearEnemies.size() > 0) // still enemy around here to attack
                {
                    setState(State.ATTACK);
                    setTargetToDoAct(nearEnemies.get(0));
                    setExec(Executors.newScheduledThreadPool(1));
                    getExec().scheduleAtFixedRate(this,0 , (long) (1000 * hitSpeed), TimeUnit.MILLISECONDS);
                    // no direction for Building cards - state can be recognized by targetToDoAct
                }
            }
            else {
                if (getTargetToDoAct() == null) // if there is no target , so decide on state changing
                {
                    ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                    setState(State.STOP);
                    if (nearEnemies.size() > 0)
                    {
                        setState(State.ATTACK);
                        setTargetToDoAct(nearEnemies.get(0));
                        setExec(Executors.newScheduledThreadPool(1));
                        getExec().scheduleAtFixedRate(this,0,(long) (1000 * hitSpeed),TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
        else { // updating is because of changing coordinate of somebody
            if (getTargetToDoAct() == null)
            {
                ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                if (nearEnemies.size() > 0)
                {
                    setState(State.ATTACK);
                    setExec(Executors.newScheduledThreadPool(1));
                    getExec().scheduleAtFixedRate(this,0 ,(long) (1000 * hitSpeed),TimeUnit.MILLISECONDS);
                    setTargetToDoAct(nearEnemies.get(0));
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
    public void setHitSpeed(double newHitSpeed)
    {
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
