package shared.model.card;

import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.TargetTypes;

public abstract class Card {
    private CardTypes type;
    private int cost;
    private int damage; // except rage card
    private int level;
    private Image cardImage;
    private Image[] attackFrames;  // all cards have attack frames
    private int width; // picture size
    private int height;
    private double range; // all cards have range or radius , but they are nearly same - melee means zero!
    private TargetTypes target;
    private int count;
    private boolean areaSplash;

    public Card(CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                int width , int height , double range , TargetTypes target , int count , boolean areaSplash)
    {
        this.type = type;
        this.cost = cost;
        this.damage = damage;
        this.level = level;
        cardImage = new Image(cardImagePath);
        attackFrames = new Image[attackFrmNum];
        for (int i = 0 ; i < attackFrmNum ; i++)
        {
            attackFrames[i] = new Image(attackFrmPath + i + ".png");  // index from zero !!! and also all images should be .png !!
        }
        this.width = width;
        this.height = height;
        this.range = range;
        this.target = target;
        this.count = count;
        this.areaSplash = areaSplash;
    }

//    public Image getAttackFrm(){
//
//    }

    /**
     * getter
     * @return type of card
     */
    public CardTypes getType() {
        return type;
    }

    /**
     * getter
     * @return cost of card
     */
    public int getCost() {
        return cost;
    }

    /**
     * getter
     * @return damage of card
     */
    public int getDamage() {
        return damage;
    }

    /**
     * getter
     * @return level of card
     */
    public int getLevel() {
        return level;
    }

    /**
     * getter
     * @return cardImage for using in deck
     */
    public Image getCardImage() {
        return cardImage;
    }

    /**
     * getter
     * @return picture width
     */
    public int getWidth() {
        return width;
    }

    /**
     * getter
     * @return height width
     */
    public int getHeight() {
        return height;
    }

    /**
     * getter
     * @return range of attacking (if melee -> 0.0)
     */
    public double getRange() {
        return range;
    }

    /**
     * getter
     * @return targetType enum
     */
    public TargetTypes getTarget() {
        return target;
    }

    /**
     * getter
     * @return count of characters placed in map for each card
     */
    public int getCount() {
        return count;
    }

    /**
     * getter
     * @return isAreaSplash or not
     */
    public boolean isAreaSplash() {
        return areaSplash;
    }
}
