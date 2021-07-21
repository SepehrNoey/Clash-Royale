package shared.model.troops.card;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.State;
import shared.enums.TargetTypes;
import shared.model.Board;
import shared.model.troops.Troop;
import shared.model.troops.timerTasks.CoordinateUpdater;
import java.util.ArrayList;
import java.util.Timer;

public class SoldierCard extends Card{
    private SpeedTypes movingSpeed; // can be other types
    private double hitSpeed;
    private int hp;
    private Image[] walkFrames;
    private Image[] dieFrames;
    private String direction; // will be used for rendering
    private Timer walkTimer;
    private CoordinateUpdater walkTask;
    private Board board;


    public SoldierCard(boolean isServerSide, CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                       int width , int height , double range , TargetTypes target , int count , boolean areaSplash ,
                       Point2D coordinates , String owner, SpeedTypes movingSpeed , double hitSpeed , int hp , String walkFrmPath , int walkFrmNum , String dieFrmPath , int dieFrmNum , String shotPath)
    {
        super(isServerSide, type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count, areaSplash , coordinates , owner,shotPath);
        this.movingSpeed = movingSpeed;
        this.hitSpeed = hitSpeed;
        this.hp = hp;
        walkFrames = new Image[walkFrmNum];
        dieFrames = new Image[dieFrmNum];
        if(!isServerSide)
        {
            for (int i = 0; i < walkFrmNum ; i++)
                walkFrames[i] = new Image(walkFrmPath + i + ".png");
            for (int i = 0 ; i < dieFrmNum ; i++)
                dieFrames[i] = new Image(dieFrmPath + i + ".png");
        }
        direction = "";
    }

    @Override
    public void run() { // attack task
        if (getTargetToDoAct() != null)
        {
            if(isAreaSplash())
            {
                ArrayList<Troop> enemies = board.getNearEnemies(this);
                int each = getDamage() / enemies.size();
                for (Troop troop:enemies)
                {
                    troop.getBeingHit(each);
                }
            }
            else {
                this.getTargetToDoAct().getBeingHit(this.getDamage());
            }
            if (getType() == CardTypes.ARCHER || getType() == CardTypes.BABY_DRAGON || getType() == CardTypes.WIZARD)
                getRender().addForRender(this,true);
        }

    }

    public Image getWalkFrm(double time , double duration){
        int index = (int)((time % (walkFrames.length * duration)) / duration);
        return walkFrames[index];
    }


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

    /**
     * setter
     * @param direction the new direction(to go)
     */
    public synchronized void setDirection(String direction) {
        this.direction = direction;
        notifyAll();
    }

    /**
     * getter
     * @return direction of going
     */
    public synchronized String getDirection() {
        return direction;
    }



    public void decideWhereToGo(Board board){
        if (getTarget() == TargetTypes.BUILDINGS) // giant
        {
            String direction = board.getNearestWay(this);
            setDirection(direction);
        }
        else{
            Card enemyAround = board.isEnemyAround(this,(int)getCoordinates().getX() , (int)getCoordinates().getY());
            if (enemyAround != null && getTarget() == TargetTypes.AIR_GROUND || (getTarget() == TargetTypes.GROUND && !(enemyAround instanceof SpellCard
                    || enemyAround.getType() == CardTypes.BABY_DRAGON) ) ){
                int xEnemy = (int)enemyAround.getCoordinates().getX();
                int yEnemy = (int)enemyAround.getCoordinates().getY();
                int thisX =(int) getCoordinates().getX();
                int thisY =(int) getCoordinates().getY();
                if (thisX == xEnemy)
                {
                    if (yEnemy <= thisY)
                    {
                        setDirection("up");
                    }
                    else
                    {
                        setDirection("down");
                    }
                }
                else if (thisY == yEnemy)
                {
                    if (xEnemy > thisX)
                    {
                        setDirection("right");
                    }
                    else
                    {
                        setDirection("left");
                    }
                }
                else {
                    int slope = (int)((yEnemy - thisY) / (xEnemy - thisX));
                    if (xEnemy > thisX)
                    {
                        setDirection(slope + "," + "right");
                    }
                    else
                    {
                        setDirection(slope + "," + "left");
                    }
                }
            }
            else { // no enemy near
                setDirection(board.getNearestWay(this));
            }
        }
}

    /**
     * to check if can attack anyone or not
     * @return true if can , false if can't
     */
    @Override
    public boolean canAttack(Troop troop){ // can attack is implemented for the new changed troop ,  not all !!!
        if (troop.getOwner().equals(getOwner()))
            return false;
        if (getRange() == 0) // melee
        {
            if ((Math.pow(troop.getCoordinates().getX() - this.getCoordinates().getX() , 2)
                    + Math.pow(troop.getCoordinates().getY() - this.getCoordinates().getY() , 2) <= 2)  &&
                    !(troop instanceof SpellCard))// a square with side 3
            {
                if (troop instanceof Card)
                {
                    Card card = (Card) troop;
                    if (card.getType() == CardTypes.BABY_DRAGON)
                        return false;
                    else
                        return true;
                }
                else // for towers
                    return true;
            }
            else // it's not enough close to it
                return false;
        }
        else if (Math.pow(troop.getCoordinates().getX() - getCoordinates().getX() , 2) + Math.pow(troop.getCoordinates().getY() - getCoordinates().getY() , 2) <= getRange())
        { // range cards
            return true;
        }
        return false;
    }

    /**
     * setter
     * @param walkTask task of walking (coordinate updater)
     */
    public void setWalkTask(CoordinateUpdater walkTask) {
        this.walkTask = walkTask;
    }

    public void updateState(Board board , Troop changedTroop , boolean isDead) {
        this.board = board;
        if (direction.equals("")) // first time using
        {
            ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
            if (nearEnemies.size() > 0)
            {
                setTargetToDoAct(nearEnemies.get(0));
                Timer atcTim = new Timer();
                setActTimer(atcTim);
                atcTim.schedule(this , 0 , (long) (1000 * getHitSpeed()));
                setState(State.ATTACK);
                direction = "attack"; // redundant
            }
            else{
                setState(State.WALK);
                decideWhereToGo(board);
            }
        }
        else if (isDead) // updating state is because of dying some troop
        {
            if (getTargetToDoAct() != null && getTargetToDoAct().getId().equals(changedTroop.getId()))
            {
                getActTimer().cancel();
                setTargetToDoAct(null);
                ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                if (nearEnemies.size() > 0) // still enemy around here to attack
                {
                    setTargetToDoAct(nearEnemies.get(0));
                    setActTimer(new Timer());
                    getActTimer().schedule(this,0,(long) (1000 * getHitSpeed()));
                    direction = "attack";
                    setState(State.ATTACK);
                }
                else
                {
                    setState(State.WALK);
                    decideWhereToGo(board);
                }
            }
        }
        else { // updating is because of changing coordinate of somebody
            if (getTargetToDoAct() == null)
            {
                ArrayList<Troop> nearEnemies = board.getNearEnemies(this);
                if(nearEnemies.size() > 0)
                {
//                    walkTimer.cancel();
//                    setWalkTask(null);
                    setState(State.ATTACK);
                    direction = "attack";
                    setActTimer(new Timer());
                    getActTimer().schedule(this,0,(long) (1000 * getHitSpeed()));
                    setTargetToDoAct(nearEnemies.get(0));
                }
                else
                {
                    setState(State.WALK);
                    decideWhereToGo(board);
                }
            }
        }
    }

    /**s
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
        this.movingSpeed.setValue(newSpeed);
    }

    /**
     * getter
     * @return timer for walking
     */
    public Timer getWalkTimer() {
        return walkTimer;
    }

    /**
     * getter
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * setter
     * @param walkTimer the new walkTimer
     */
    public void setWalkTimer(Timer walkTimer) {
        this.walkTimer = walkTimer;
    }
}
