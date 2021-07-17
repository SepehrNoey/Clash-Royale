package shared.model.troops.card;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import shared.enums.CardTypes;
import shared.enums.SpeedTypes;
import shared.enums.TargetTypes;
import shared.model.Board;

public class SoldierCard extends Card{
    private SpeedTypes movingSpeed; // can be other types
    private double hitSpeed;
    private int hp;
    private Image[] walkFrames;
    private Image[] dieFrames;
    private String direction; // will be used for rendering


    public SoldierCard(boolean isServerSide,CardTypes type , int cost , int damage , int level , String cardImagePath , String attackFrmPath , int attackFrmNum ,
                       int width , int height , double range , TargetTypes target , int count , boolean areaSplash ,
                       Point2D coordinates , String owner, SpeedTypes movingSpeed , double hitSpeed , int hp , String walkFrmPath , int walkFrmNum , String dieFrmPath , int dieFrmNum)
    {
        super(isServerSide, type, cost, damage, level, cardImagePath, attackFrmPath, attackFrmNum, width, height, range, target, count, areaSplash , coordinates , owner);
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
    }

//    public Image getWalkFrm(){
//
//    }

//    public Image getDieFrm(){
//
//    }


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
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * getter
     * @return direction of going
     */
    public String getDirection() {
        return direction;
    }



    public String decideWhereToGo(Board board){
        if (getTarget() == TargetTypes.BUILDINGS) // giant
        {
            String direction = board.getNearestWay(this , (int)getCoordinates().getX() , (int)getCoordinates().getY());
            setDirection(direction);
            return direction;
        }
        else{
            Card enemyAround = board.isEnemyAround((int)getCoordinates().getX() , (int)getCoordinates().getY());
            String direction = "";
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
                        direction = "up";
                        setDirection(direction);
                    }
                    else
                    {
                        direction = "down";
                        setDirection(direction);
                    }
                }
                else if (thisY == yEnemy)
                {
                    if (xEnemy > thisX)
                    {
                        direction = "right";
                        setDirection(direction);
                    }
                    else
                    {
                        direction = "left";
                        setDirection(direction);
                    }
                }
                else {
                    int slope = (int)((yEnemy - thisY) / (xEnemy - thisX));
                    if (xEnemy > thisX)
                    {
                        direction = slope + "," + "right";
                        setDirection(direction);
                    }
                    else
                    {
                        direction = slope + "," + "left";
                        setDirection(direction);
                    }
                }
                return direction;
            }

            return null;
        }
    }
}
