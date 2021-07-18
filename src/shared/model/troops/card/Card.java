package shared.model.troops.card;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.TargetTypes;
import shared.model.Message;
import shared.model.troops.Troop;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Card extends Troop implements Serializable {
    private CardTypes type;
    private int cost;
    private Image cardImage;
    private int count;
    private boolean areaSplash;

    public Card(boolean isServerSide, CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                int width , int height , double range , TargetTypes target , int count , boolean areaSplash , Point2D coordinates , String owner)
    {
        super(isServerSide,damage, level, attackFrmPath, attackFrmNum, width, height, range, target, coordinates , owner);
        this.type = type;
        this.cost = cost;
        if (!isServerSide)
            cardImage = new Image(cardImagePath);
        this.count = count;
        this.areaSplash = areaSplash;
    }


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
     * @return cardImage for using in deck
     */
    public Image getCardImage() {
        return cardImage;
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
