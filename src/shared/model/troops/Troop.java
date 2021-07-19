package shared.model.troops;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.*;
import shared.model.Board;
import shared.model.Message;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.card.SpellCard;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;


public abstract class Troop extends TimerTask{
    private int damage; // except rage card
    private int level;
    private Image[] attackFrames; // all troops have attack frames
    private int width; // picture size
    private int height;
    private double range; // all cards have range or radius , but they are nearly same - melee means zero!
    private TargetTypes target;
    private Point2D coordinates;
    private String owner;
    private Troop targetToDoAct; // the point where target is - if this target is null , then it means there is no target - can be used for rendering
    private Timer actTimer;
    private ArrayBlockingQueue<Message> inGameInbox;
    private String id; // an special distinct id for each troop

    public Troop(boolean isServerSide,int damage , int level , String attackFrmPath , int attackFrmNum , int width ,
                 int height , double range , TargetTypes target , Point2D coordinates , String owner)
    {
        this.damage = damage;
        this.level = level;
        attackFrames = new Image[attackFrmNum];
        if(!isServerSide)
            for (int i = 0 ; i < attackFrmNum ; i++)
                attackFrames[i] = new Image(attackFrmPath + i + ".png");  // index from zero !!! and also all images should be .png !!

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

    /**
     * getter
     * @return owner name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * factory method for troops
     * @param type of troop as a string
     * @param level of troop
     * @param point2D of troop (can be null)
     * @param owner owner of troop
     * @return a created Troop
     */
    public static Troop makeTroop(boolean isServerSide,String type , int level , Point2D point2D , String owner){
        CardTypes typeParam  = null;
        TowerTypes towerType = null;
        if (type.equals(TowerTypes.PRINCESS_TOWER.toString()) || type.equals(TowerTypes.KING_TOWER.toString()))
            towerType = TowerTypes.valueOf(type);
        else
            typeParam = CardTypes.valueOf(type);

        // when using in database , point2d is null but it's not important

        if(typeParam == CardTypes.BARBARIAN)
        {
            return new SoldierCard(isServerSide,typeParam,5,level == 1 ? 75 : level == 2 ? 82 : level == 3? 90 : level == 4 ? 99 : 109
                    ,level,"client_side/view/pics/babarian.png","client_side/view/pics/barbarian_attack",
                    7,40,40,0, TargetTypes.GROUND,4,false,point2D,owner, SpeedTypes.MEDIUM,1.5
                    ,level == 1 ? 300 : level == 2 ? 330 : level == 3 ? 363 : level == 4 ? 438 : 480,"client_side/view/pics/barbarian_walk",6,"",0);
        }
        else if (typeParam == CardTypes.ARCHER)
        {
            return new SoldierCard(isServerSide,typeParam,3,level == 1 ? 33 : level == 2 ? 44 : level == 3? 48 : level == 4 ? 53 : 58
                    ,level,"client_side/view/pics/archer.png","client_side/view/pics/archer_attack",
                    3,40,40,5, TargetTypes.AIR_GROUND,2,false,point2D,owner, SpeedTypes.MEDIUM,1.2
                    ,level == 1 ? 125 : level == 2 ? 127 : level == 3 ? 151 : level == 4 ? 166 : 182,"client_side/view/pics/archer_walk",20,"",0);
        }
        else if (typeParam == CardTypes.BABY_DRAGON)
        {
            return new SoldierCard(isServerSide,typeParam,4,level == 1 ? 100 : level == 2 ? 110 : level == 3? 121 : level == 4 ? 133 : 146
                    ,level,"client_side/view/pics/heli.png","client_side/view/pics/heli_walk",
                    1,100,100,3, TargetTypes.AIR_GROUND,1,true,point2D,owner, SpeedTypes.FAST,1.8
                    ,level == 1 ? 800 : level == 2 ? 880 : level == 3 ? 968 : level == 4 ? 1064 : 1168,"client_side/view/pics/heli_walk",1,"",0);
        }
        else if (typeParam == CardTypes.WIZARD)
        {
            return new SoldierCard(isServerSide,CardTypes.WIZARD,5,level == 1 ? 130 : level == 2 ? 143 : level == 3? 157 : level == 4 ? 172 : 189
                    ,level,"client_side/view/pics/wizard.png","client_side/view/pics/wizard_shot",
                    1,30,30,5, TargetTypes.AIR_GROUND,1,true,point2D,owner,SpeedTypes.MEDIUM,1.7
                    ,level == 1 ? 340 : level == 2 ? 374 : level == 3 ? 411 : level == 4 ? 452 : 496,"client_side/view/pics/wizard_walk",1,"",0);
        }
        else if (typeParam == CardTypes.MINI_PEKKA)
        {
            return new SoldierCard(isServerSide,CardTypes.MINI_PEKKA,4,level == 1 ? 325 : level == 2 ? 357 : level == 3? 393 : level == 4 ? 432 : 474
                    ,level,"client_side/view/pics/pekka.png","client_side/view/pics/pekka_attack",
                    7,35,35,0, TargetTypes.GROUND,1,false,point2D,owner, SpeedTypes.FAST,1.8
                    ,level == 1 ? 600 : level == 2 ? 660 : level == 3 ? 726 : level == 4 ? 798 : 876,"client_side/view/pics/pekka_walk",6,"",0);
        }
        else if (typeParam == CardTypes.GIANT)
        {
            return new SoldierCard(isServerSide,CardTypes.GIANT,5,level == 1 ? 126 : level == 2 ? 138 : level == 3? 152 : level == 4 ? 167 : 183
                    ,level,"client_side/view/pics/giant.png","client_side/view/pics/giant_attack",
                    5,60,50,0, TargetTypes.BUILDINGS,1,false,point2D,owner, SpeedTypes.SLOW,1.5
                    ,level == 1 ? 2000 : level == 2 ? 2200 : level == 3 ? 2420 : level == 4 ? 2660 : 2920,"client_side/view/pics/giant_walking",4,"",0);
        }
        else if (typeParam == CardTypes.VALKYRIE)
        {
            return new SoldierCard(isServerSide,typeParam,4,level == 1 ? 120 : level == 2 ? 132 : level == 3? 145 : level == 4 ? 159 : 175
                    ,level,"client_side/view/pics/valkyrie.png","client_side/view/pics/valkyrie_attack",
                    6,40,50,0, TargetTypes.GROUND,1,true,point2D,owner, SpeedTypes.MEDIUM,1.5
                    ,level == 1 ? 880 : level == 2 ? 968 : level == 3? 1064 : level == 4 ? 1170 : 1284,"client_side/view/pics/valkyrie_walk",8,"client_side/view/pics/valkyrie_die",1);
        }
        else if (typeParam == CardTypes.RAGE) // attention !! : duration is not added in the card
        {
            return new SpellCard(isServerSide,typeParam,3,0 // doesn't have damage
                    ,level, "client_side/view/pics/rage.png", "client_side/view/pics/rage", // attack frame and card frame are same
                    1,30,45,5,TargetTypes.AIR_GROUND,1,false,point2D,owner,SpeedTypes.FAST // this can be changed later
                    ,40,40,40,false , level == 1 ? 6: level == 2 ? 6.5 : level == 3 ? 7 : level == 4 ? 7.5 : 8 );
        }
        else if (typeParam == CardTypes.FIREBALL)
        {
            return new SpellCard(isServerSide,typeParam,4,level == 1 ? 325 : level == 2 ? 357 : level == 3? 393 : level == 4 ? 432 : 474
                    ,level,"client_side/view/pics/fireball.png","client_side/view/pics/fireball",
                    6,80,200,2.5,null,1,true,point2D,owner,SpeedTypes.VERY_FAST // this can be changed later
                    ,0,0,0,true , 0);
        }
        else if (typeParam == CardTypes.ARROWS)
        {
            return new SpellCard(isServerSide,typeParam,3,level == 1 ? 144 : level == 2 ? 156 : level == 3? 174 : level == 4 ? 189 : 210
                    ,level,"client_side/view/pics/arrow.png","client_side/view/pics/arrow",
                    1,30,80,4,null,1,false,point2D,owner,SpeedTypes.VERY_FAST // this can be changed later
                    ,0,0,0,true , 0);
        }
        else if (typeParam == CardTypes.CANNON) // explosion can be added as dieFrm
        {
            return new BuildingCard(isServerSide,typeParam,6,level == 1 ? 60 : level == 2 ? 66 : level == 3? 72 : level == 4 ? 79 : 87
                    ,level,"client_side/view/pics/cannon.png","client_side/view/pics/cannon_walk",
                    1,50,50,5.5,TargetTypes.GROUND,1,false,point2D,owner,"",0,
                    0.8,30,level == 1 ? 380 : level == 2 ? 418 : level == 3? 459 : level == 4 ? 505 : 554);
        }
        else if (typeParam == CardTypes.INFERNO_TOWER)
        {
            return new BuildingCard(isServerSide,typeParam,5,level == 1 ? 400 : level == 2 ? 440 : level == 3? 484 : level == 4 ? 532 : 584
                    ,level,"client_side/view/pics/inferno.png","client_side/view/pics/inferno_attack",
                    1,45,45,6,TargetTypes.AIR_GROUND,1,false,point2D,owner,"",0,
                    0.4,40,level == 1 ? 800 : level == 2 ? 880 : level == 3? 968 : level == 4 ? 1064 : 1168);
        }
        else if (towerType == TowerTypes.KING_TOWER)
        {
            return new Tower(isServerSide,level == 1 ? 50 : level == 2 ? 53 : level == 3 ? 57 : level == 4 ? 60 : 64 ,
                    level , "client_side/view/pics/kingTowerHead" , 1 , 90 , 90 ,7 ,TargetTypes.GROUND ,
                    TowerTypes.KING_TOWER , level == 1 ? 2400 : level == 2 ? 2568 : level == 3 ? 2736 : level == 4 ? 2904 : 3096 ,1,point2D,owner);
        }
        else if (towerType == TowerTypes.PRINCESS_TOWER)
        {
            return new Tower(isServerSide,level == 1 ? 50 : level == 2 ? 54 : level == 3 ? 58 : level == 4 ? 62 : 69 ,
                    level , "client_side/view/pics/princessTowerHead" , 1 , 90 , 90 ,7.5 ,TargetTypes.AIR_GROUND ,
                    TowerTypes.PRINCESS_TOWER , level == 1 ? 1400 : level == 2 ? 1512 : level == 3 ? 1624 : level == 4 ? 1750 : 1890 ,0.8,point2D,owner);
        }
        else {
            return null;
        }
    }

    /**
     * setter
     * @param coordinates the new coordinates
     */
    public void setCoordinates(Point2D coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * to check if can attack anyone or not
     * @return true if can , false if can't
     */
    public abstract boolean canAttack(Troop troop);

    /**
     * setter
     * @param targetToDoAct the point of target
     */
    public void setTargetToDoAct(Troop targetToDoAct){
        this.targetToDoAct = targetToDoAct;
    }

    /**
     * getter
     * @return point of target - if no target , then returns null
     */
    public Troop getTargetToDoAct() {
        return targetToDoAct;
    }

    /**
     * setter
     * @param actTimer timer for doing act
     */
    public void setActTimer(Timer actTimer) {
        this.actTimer = actTimer;
    }

    /**
     * getter
     * @return timer of acting
     */
    public Timer getActTimer() {
        return actTimer;
    }

    public abstract void setHp(int damage);
    public abstract int getHp();

    public void getBeingHit(int damage) // attention ! deleting from board happens in other methods ...
    {
        this.setHp(damage);
        if (getHp() == 0) // sending die message to logic or board
        {
            try {
                inGameInbox.put(new Message(MessageType.CHARACTER_DIED ,getOwner(),getId()));
                actTimer.cancel();
                this.setTargetToDoAct(null);
                if (this instanceof SoldierCard)
                {
                    SoldierCard soldierCard = (SoldierCard) this;
                    soldierCard.getWalkTimer().cancel();
                    soldierCard.setWalkTask(null);
                    soldierCard.setWalkTimer(null);
                }
                else if (this instanceof SpellCard)
                {
                    SpellCard spellCard = (SpellCard) this;
                    spellCard.getWalkTimer().cancel();
                    spellCard.setWalkTask(null);
                    spellCard.setWalkTimer(null);
                }
            }
            catch (InterruptedException e)
            {
                System.out.println( this.getOwner()+ " interrupted while sending character died message.");
                e.printStackTrace();
            }
        }
    }

    public abstract void setHitSpeed(double newHitSpeed);
    public abstract void updateState(Board board , Troop changedTroop , boolean isDead) throws InterruptedException;
    public abstract double getHitSpeed();

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public abstract void setMovingSpeed(double newSpeed);

    public abstract SpeedTypes getMovingSpeed();

    /**
     * getter
     * @return arrayBlockingQueue of inGameInbox
     */
    public ArrayBlockingQueue<Message> getInGameInbox() {
        return inGameInbox;
    }

    /**
     * setter - this setter must be called! before using Troops
     * @param inGameInbox the inGameInbox of GameLoop
     */
    public void setInGameInbox(ArrayBlockingQueue<Message> inGameInbox) {
        this.inGameInbox = inGameInbox;
    }

    /**
     * setter - this must be called ! when this card is used for first time - just for cards
     * @param cardUsedNum a parameter for making id
     */
    public void setId(int cardUsedNum)
    {
        this.id = getOwner() + "," + cardUsedNum;
    }

    /**
     * setter - this must be called ! when this tower is used for first time - just for towers
     *  for towers id is like this : ownerName,towerType(left or middle or right)
     */
    public void setId()
    {
        Tower tower = (Tower) this;
        String type = "";
        if (tower.getType() == TowerTypes.KING_TOWER)
            type = "middle";
        else {
            if ((int)tower.getCoordinates().getX() == 3) // the left tower
                type = "left";
            else
                type = "right";
        }
        id = tower.getOwner() + "," + type;
    }

    /**
     * getter
     * @return id of this troop
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o){
        if (o == this)
            return true;
        if (!(o instanceof Troop))
            return false;

        Troop troop = (Troop) o;
        return troop.getId().equals(this.getId());
    }



}
