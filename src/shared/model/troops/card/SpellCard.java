package shared.model.troops.card;

import javafx.geometry.Point2D;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.Troop;
import shared.model.troops.timerTasks.CoordinateUpdater;
import shared.model.troops.timerTasks.RageTask;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;

public class SpellCard extends Card{
    private SpeedTypes movingSpeed; // the speed of moving or transitions on its node
    private int damageBoost; // if exists
    private int speedBoost;
    private int hitSpeedBoost;
    private boolean reducedDmg; // reduced damage to crown towers
    private Timer walkTimer;
    private CoordinateUpdater walkTask;
    private ArrayList<Troop> targets;
    private String direction; // will be used for rendering
    private int hp = 10; // fake
    private Board board;
    private double durationForRage;


    public SpellCard(boolean isServerSide, CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                     int width , int height , double range , TargetTypes target , int count , boolean areaSplash , Point2D coordinates,
                     String owner, SpeedTypes movingSpeed ,
                     int damageBoost , int speedBoost , int hitSpeedBoost , boolean reducedDmg , double durationForRage)
    {
        super(isServerSide,type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count, areaSplash , coordinates , owner);
        this.movingSpeed = movingSpeed;
        this.damageBoost = damageBoost;
        this.speedBoost = speedBoost;
        this.hitSpeedBoost = hitSpeedBoost;
        this.reducedDmg = reducedDmg;
        this.durationForRage = durationForRage;
    }

    @Override
    public void run() { // act
        if (this.getType() == CardTypes.RAGE)
        {
            ArrayList<Troop> inRange = board.getNearEnemies(this);
            for (Troop troop:inRange)
            {
                troop.setHitSpeed(troop.getHitSpeed() + troop.getHitSpeed() * hitSpeedBoost / 100);
                troop.setDamage(troop.getDamage() + troop.getDamage() * damageBoost / 100);
                troop.setMovingSpeed(troop.getMovingSpeed().getValue() + troop.getMovingSpeed().getValue() * speedBoost / 100);
            }
            Timer rageTimer = new Timer();
            rageTimer.schedule(new RageTask(inRange ,5 / 7.0) , (long) durationForRage * 1000);
        }
        else
        {
            ArrayList<Troop> inRange = board.getNearEnemies(this);
            int each = this.getDamage() / inRange.size();
            for (Troop troop:inRange)
            {
                troop.getBeingHit(each);
            }
        }
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


    /**
     * to check if can attack anyone or not
     * @return true if can , false if can't
     */
    @Override
    public boolean canAttack(Troop troop){
        if(this.getType() == CardTypes.RAGE)
        {
            if (!troop.getOwner().equals(this.getOwner()))
                return false;
        }
        else { // fireball and arrow
            if (troop.getOwner().equals(this.getOwner()))
                return false;
        }
        return Math.pow(troop.getCoordinates().getX() - this.getCoordinates().getX(), 2) +
            Math.pow(troop.getCoordinates().getY() - this.getCoordinates().getY(), 2) <= getRange();
    }


    /**
     * setter
     * @param walkTimer timer for walking
     */
    public void setWalkTimer(Timer walkTimer) {
        this.walkTimer = walkTimer;
    }

    /**
     * setter
     * @param walkTask task of walking (coordinate updater)
     */
    public void setWalkTask(CoordinateUpdater walkTask) {
        this.walkTask = walkTask;
    }

    /**
     * updateState for spellCards is just used at the beginning
     * @param board of play
     * @param changedCard this parameter is not used for spellCards
     * @param isDead this parameter is not used for spellCards
     */
    public void updateState(Board board , Card changedCard , boolean isDead){
        this.board = board;
        setWalkTimer(new Timer());
        decideWhereToGo();
        setWalkTask(new CoordinateUpdater(this,direction, board.getCoordinateUpdateQueue()));
        walkTimer.schedule(walkTask , 0 ,(int)(1000 / getMovingSpeed().getValue()) );
    }

    private void decideWhereToGo(){
        int xEnemy = (int)getCoordinates().getX();
        int yEnemy = (int)getCoordinates().getY();
        int thisX = 10;
        int thisY =26;
        if (this.getType() == CardTypes.RAGE)
            direction = "rage"; // just for rage
        else if (thisX == xEnemy)
        {
            if (yEnemy <= thisY)
                direction = "up";
            else
                direction = "down";
        }
        else if (thisY == yEnemy)
        {
            if (xEnemy > thisX)
                direction = "right";
            else
                direction = "left";
        }
        else {
            int slope = (int)((yEnemy - thisY) / (xEnemy - thisX));
            if (xEnemy > thisX)
                direction = slope + "," + "right";
            else
                direction = slope + "," + "left";
        }

    }

    /**
     * setter for hp
     * @param damage to be hit
     */
    public void setHp(int damage) {

    }

    public int getHp() {
        return hp;
    }

    @Override
    public void setHitSpeed(double newHitSpeed) {

    }

    @Override
    public double getHitSpeed() {
        return 0;
    }

    @Override
    public void setMovingSpeed(double newSpeed) {
        // fake
    }

    /**
     * getter
     * @return walking timer
     */
    public Timer getWalkTimer() {
        return walkTimer;
    }
}
