package shared.model.troops;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;
import shared.enums.TowerTypes;
import shared.model.troops.card.BuildingCard;
import shared.model.troops.card.SoldierCard;
import shared.model.troops.card.SpellCard;


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
     * factory method for troops
     * @param type of troop as a string
     * @param level of troop
     * @param point2D of troop (can be null)
     * @param owner owner of troop
     * @param base is used just when we want to make tower(can be null)
     * @param head is used just when we want to make tower(can be null)
     * @return a created Troop
     */
    public static Troop makeTroop(boolean isServerSide,String type , int level , Point2D point2D , String owner , ImageView base , ImageView head){
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
                    ,level,"client_side/view/pics/barbarian_walk0.png","client_side/view/pics/barbarian_attack",
                    7,40,40,0, TargetTypes.GROUND,4,false,point2D,owner, SpeedTypes.MEDIUM,1.5
                    ,level == 1 ? 300 : level == 2 ? 330 : level == 3 ? 363 : level == 4 ? 438 : 480,"client_side/view/pics/barbarian_walk",6,"",0);
        }
        else if (typeParam == CardTypes.ARCHER)
        {
            return new SoldierCard(isServerSide,typeParam,3,level == 1 ? 33 : level == 2 ? 44 : level == 3? 48 : level == 4 ? 53 : 58
                    ,level,"client_side/view/pics/archer_walk0.png","client_side/view/pics/archer_attack",
                    3,40,40,5, TargetTypes.AIR_GROUND,2,false,point2D,owner, SpeedTypes.MEDIUM,1.2
                    ,level == 1 ? 125 : level == 2 ? 127 : level == 3 ? 151 : level == 4 ? 166 : 182,"client_side/view/pics/archer_walk",20,"",0);
        }
        else if (typeParam == CardTypes.BABY_DRAGON)
        {
            return new SoldierCard(isServerSide,typeParam,4,level == 1 ? 100 : level == 2 ? 110 : level == 3? 121 : level == 4 ? 133 : 146
                    ,level,"client_side/view/pics/heli_walk0.png","client_side/view/pics/heli_walk",
                    1,100,100,3, TargetTypes.AIR_GROUND,1,true,point2D,owner, SpeedTypes.FAST,1.8
                    ,level == 1 ? 800 : level == 2 ? 880 : level == 3 ? 968 : level == 4 ? 1064 : 1168,"client_side/view/pics/heli_walk",1,"",0);
        }
        else if (typeParam == CardTypes.WIZARD)
        {
            return new SoldierCard(isServerSide,CardTypes.WIZARD,5,level == 1 ? 130 : level == 2 ? 143 : level == 3? 157 : level == 4 ? 172 : 189
                    ,level,"client_side/view/pics/wizard_walk0.png","client_side/view/pics/wizard_shot",
                    1,30,30,5, TargetTypes.AIR_GROUND,1,true,point2D,owner,SpeedTypes.MEDIUM,1.7
                    ,level == 1 ? 340 : level == 2 ? 374 : level == 3 ? 411 : level == 4 ? 452 : 496,"client_side/view/pics/wizard_walk",1,"",0);
        }
        else if (typeParam == CardTypes.MINI_PEKKA)
        {
            return new SoldierCard(isServerSide,CardTypes.MINI_PEKKA,4,level == 1 ? 325 : level == 2 ? 357 : level == 3? 393 : level == 4 ? 432 : 474
                    ,level,"client_side/view/pics/pekka_walk0.png","client_side/view/pics/pekka_attack",
                    7,35,35,0, TargetTypes.GROUND,1,false,point2D,owner, SpeedTypes.FAST,1.8
                    ,level == 1 ? 600 : level == 2 ? 660 : level == 3 ? 726 : level == 4 ? 798 : 876,"client_side/view/pics/pekka_walk",6,"",0);
        }
        else if (typeParam == CardTypes.GIANT)
        {
            return new SoldierCard(isServerSide,CardTypes.GIANT,5,level == 1 ? 126 : level == 2 ? 138 : level == 3? 152 : level == 4 ? 167 : 183
                    ,level,"client_side/view/pics/giant_walking0.png","client_side/view/pics/giant_attack",
                    5,60,50,0, TargetTypes.BUILDINGS,1,false,point2D,owner, SpeedTypes.SLOW,1.5
                    ,level == 1 ? 2000 : level == 2 ? 2200 : level == 3 ? 2420 : level == 4 ? 2660 : 2920,"client_side/view/pics/giant_walking",4,"",0);
        }
        else if (typeParam == CardTypes.VALKYRIE)
        {
            return new SoldierCard(isServerSide,typeParam,4,level == 1 ? 120 : level == 2 ? 132 : level == 3? 145 : level == 4 ? 159 : 175
                    ,level,"client_side/view/pics/valkyrie_walk3.png","client_side/view/pics/valkyrie_attack",
                    6,40,50,0, TargetTypes.GROUND,1,true,point2D,owner, SpeedTypes.MEDIUM,1.5
                    ,level == 1 ? 880 : level == 2 ? 968 : level == 3? 1064 : level == 4 ? 1170 : 1284,"client_side/view/pics/valkyrie_walk",8,"client_side/view/pics/valkyrie_die",1);
        }
        else if (typeParam == CardTypes.RAGE) // attention !! : duration is not added in the card
        {
            return new SpellCard(isServerSide,typeParam,3,0 // doesn't have damage
                    ,level, "client_side/view/pics/rage0.png", "client_side/view/pics/rage", // attack frame and card frame are same
                    1,30,45,5,TargetTypes.AIR_GROUND,1,false,point2D,owner,SpeedTypes.FAST // this can be changed later
                    ,40,40,40,false);
        }
        else if (typeParam == CardTypes.FIREBALL)
        {
            return new SpellCard(isServerSide,typeParam,4,level == 1 ? 325 : level == 2 ? 357 : level == 3? 393 : level == 4 ? 432 : 474
                    ,level,"client_side/view/pics/fireball4.png","client_side/view/pics/fireball",
                    6,80,200,2.5,null,1,true,point2D,owner,SpeedTypes.VERY_FAST // this can be changed later
                    ,0,0,0,true);
        }
        else if (typeParam == CardTypes.ARROWS)
        {
            return new SpellCard(isServerSide,typeParam,3,level == 1 ? 144 : level == 2 ? 156 : level == 3? 174 : level == 4 ? 189 : 210
                    ,level,"client_side/view/pics/arrow0.png","client_side/view/pics/arrow",
                    1,30,80,4,null,1,false,point2D,owner,SpeedTypes.VERY_FAST // this can be changed later
                    ,0,0,0,true);
        }
        else if (typeParam == CardTypes.CANNON) // explosion can be added as dieFrm
        {
            return new BuildingCard(isServerSide,typeParam,6,level == 1 ? 60 : level == 2 ? 66 : level == 3? 72 : level == 4 ? 79 : 87
                    ,level,"client_side/view/pics/cannon_walk0.png","client_side/view/pics/cannon_walk",
                    1,50,50,5.5,TargetTypes.GROUND,1,false,point2D,owner,"",0,
                    0.8,30,level == 1 ? 380 : level == 2 ? 418 : level == 3? 459 : level == 4 ? 505 : 554);
        }
        else if (typeParam == CardTypes.INFERNO_TOWER)
        {
            return new BuildingCard(isServerSide,typeParam,5,level == 1 ? 400 : level == 2 ? 440 : level == 3? 484 : level == 4 ? 532 : 584
                    ,level,"client_side/view/pics/inferno_attack0.png","client_side/view/pics/inferno_attack",
                    1,45,45,6,TargetTypes.AIR_GROUND,1,false,point2D,owner,"",0,
                    0.4,40,level == 1 ? 800 : level == 2 ? 880 : level == 3? 968 : level == 4 ? 1064 : 1168);
        }
        else if (towerType == TowerTypes.KING_TOWER)
        {
            return new Tower(isServerSide,level == 1 ? 50 : level == 2 ? 53 : level == 3 ? 57 : level == 4 ? 60 : 64 ,
                    level , "client_side/view/pics/kingTowerHead" , 1 , 90 , 90 ,7 ,TargetTypes.GROUND ,
                    TowerTypes.KING_TOWER , level == 1 ? 2400 : level == 2 ? 2568 : level == 3 ? 2736 : level == 4 ? 2904 : 3096 ,1,base,head,point2D,owner);
        }
        else if (towerType == TowerTypes.PRINCESS_TOWER)
        {
            return new Tower(isServerSide,level == 1 ? 50 : level == 2 ? 54 : level == 3 ? 58 : level == 4 ? 62 : 69 ,
                    level , "client_side/view/pics/princessTowerHead" , 1 , 90 , 90 ,7.5 ,TargetTypes.AIR_GROUND ,
                    TowerTypes.PRINCESS_TOWER , level == 1 ? 1400 : level == 2 ? 1512 : level == 3 ? 1624 : level == 4 ? 1750 : 1890 ,0.8,base,head,point2D,owner);
        }
        else {
            return null;
        }
    }
}
