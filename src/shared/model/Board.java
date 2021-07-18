package shared.model;

import javafx.geometry.Point2D;
import shared.enums.BoardThings;
import shared.enums.BoardTypes;
import shared.enums.CardTypes;
import shared.enums.TowerTypes;
import shared.model.troops.Tower;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class Board implements Runnable {
    private BoardThings[][] board;
    private BoardTypes type;
    private ArrayList<Troop> addedTroops;
    private boolean isServerSide;
    private boolean isLeftUpAreaAllowed;
    private boolean isRightUpAreaAllowed;
    private String humanPlayer;
    private String botName;
    private int humanLevel;
    private LinkedTransferQueue<Card> coordinateUpdateQueue;

    public Board(BoardTypes type , boolean isServerSide , String humanPlayer , int humanLevel , String botName){
        this.type = type;
        this.humanPlayer = humanPlayer;
        this.humanLevel = humanLevel;
        this.botName = botName;
        board = new BoardThings[21][30];
        addedTroops = new ArrayList<>();
        this.isServerSide = isServerSide;
        makeRawBoard();
        coordinateUpdateQueue = new LinkedTransferQueue<>();
    }


    private void makeRawBoard(){
        if (type == BoardTypes.TWO_PLAYERS)
        {
            // roads
            for (int i = 3 ; i < 6 ; i++) // index in board starts from zero!!!
                for (int j = 3 ; j < 27 ; j++)
                    board[i][j] = BoardThings.ROAD;
            for (int i = 15 ; i < 18 ; i++)
                for (int j = 3 ; j < 27 ; j++)
                    board[i][j] = BoardThings.ROAD;
            for (int i = 6 ; i < 16 ; i++)
                for (int j = 3 ; j < 5 ; j++)
                    board[i][j] = BoardThings.ROAD;
            for (int i = 6 ; i < 16 ; i++)
                for (int j = 24 ; j < 26 ; j++)
                    board[i][j] = BoardThings.ROAD;

            board[9][2] = BoardThings.ROAD;
            board[10][2] = BoardThings.ROAD;
            board[11][2] = BoardThings.ROAD;
            board[9][27] = BoardThings.ROAD;
            board[10][27] = BoardThings.ROAD;
            board[11][27] = BoardThings.ROAD;

            // road
            for (int j = 14 ; j < 16 ; j++)
                for (int i = 0 ; i < 21 ; i++)
                    board[i][j] = BoardThings.WATER;
            // bridges
            board[4][14] = BoardThings.BRIDGE;
            board[4][15] = BoardThings.BRIDGE;
            board[16][14] = BoardThings.BRIDGE;
            board[16][15] = BoardThings.BRIDGE;

            // objects
            board[0][6] = BoardThings.OBJECT;
            board[1][6] = BoardThings.OBJECT;
            board[0][7] = BoardThings.OBJECT;
            board[1][7] = BoardThings.OBJECT;

            board[5][5] = BoardThings.OBJECT;
            board[6][5] = BoardThings.OBJECT;
            board[5][6] = BoardThings.OBJECT;
            board[6][6] = BoardThings.OBJECT;

            board[5][11] = BoardThings.OBJECT;
            board[6][11] = BoardThings.OBJECT;
            board[5][12] = BoardThings.OBJECT;
            board[6][12] = BoardThings.OBJECT;

            board[5][16] = BoardThings.OBJECT;
            board[6][16] = BoardThings.OBJECT;
            board[5][17] = BoardThings.OBJECT;
            board[6][17] = BoardThings.OBJECT;

            board[1][20] = BoardThings.OBJECT;

            board[12][9] = BoardThings.OBJECT;

            board[12][19] = BoardThings.OBJECT;
            board[13][19] = BoardThings.OBJECT;
            board[12][20] = BoardThings.OBJECT;
            board[13][20] = BoardThings.OBJECT;

            board[17][2] = BoardThings.OBJECT;

            board[18][10] = BoardThings.OBJECT;
            board[19][10] = BoardThings.OBJECT;
            board[18][11] = BoardThings.OBJECT;
            board[19][11] = BoardThings.OBJECT;

            board[17][25] = BoardThings.OBJECT;
            board[18][25] = BoardThings.OBJECT;
            board[17][26] = BoardThings.OBJECT;
            board[18][26] = BoardThings.OBJECT;

            // grass

            for (int i = 0 ; i < 21 ; i++)
                for (int j = 0 ; j < 30 ; j++)
                    if (board[i][j] == null)
                        board[i][j] = BoardThings.GRASS;


        }
        else { // four player board

        }
    }

    public void addTroop(Troop troop)
    {
        addedTroops.add(troop);
    }

    /**
     * getter
     * @return type of board
     */
    public BoardTypes getType() {
        return type;
    }

    public boolean isValidAddress(Card chosen, int tileX , int tileY){
        if (tileX >= 21 || tileY >= 30) // out of map
            return false;
        if (chosen.getType() == CardTypes.ARROWS || chosen.getType() == CardTypes.FIREBALL // spells are allowed everywhere
                || chosen.getType() == CardTypes.RAGE)
            return true;
        else if ((tileX >= 8 && tileX <= 10 && tileY <= 26 && tileY >= 24) || (   // for soldiers and buildings
        tileX >= 3 && tileX <= 5 && tileY >= 23 && tileY <= 25) || (tileX >= 14 && tileX <= 16 && tileY >= 23 && tileY <= 25))
            return false;

        else if (tileY >= 16 && !(board[tileX][tileY] == BoardThings.OBJECT))
            return true;
        else if (tileY <= 15 && tileY >= 10 && !(board[tileX][tileY] == BoardThings.OBJECT || board[tileX][tileY] == BoardThings.WATER))
        {
            if (tileX <= 10 && isLeftUpAreaAllowed)
                return true;
            if (tileX >= 10 &&  isRightUpAreaAllowed)
                return true;
            return false;
        }
        else return false;
    }

    /**
     * center of towers are set for coordinates
     * @param boardType two or four players board
     * @param playerName who is these towers for
     * @param inGameInbox is used for sending data to gameLoop
     */
    public void addTowers(BoardTypes boardType , String playerName , ArrayBlockingQueue<Message> inGameInbox)
    {
        if (boardType == BoardTypes.TWO_PLAYERS)  // attention !! point2D is addressed by index  0 to MAX !!! - like Board
        {
            // player towers
            Troop troop = null;
            troop = Troop.makeTroop(false, TowerTypes.KING_TOWER.toString() , humanLevel ,
                    new Point2D(10,26) , playerName);
            troop.setInGameInbox(inGameInbox);
            troop.setId();

            this.addTroop(troop);

            troop = Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , humanLevel ,
                    new Point2D(4,25) , playerName);
            troop.setInGameInbox(inGameInbox);
            troop.setId();
            this.addTroop(troop);

            troop = Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , humanLevel ,
                    new Point2D(16,25) , playerName);
            troop.setInGameInbox(inGameInbox);
            troop.setId();
            this.addTroop(troop);

            // bot towers
            troop = Troop.makeTroop(false,TowerTypes.KING_TOWER.toString() , humanLevel ,
                    new Point2D(10,3) , botName);
            troop.setInGameInbox(inGameInbox);
            troop.setId();
            this.addTroop(troop); // attention : !! game mode is set for bot name

            troop = Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , humanLevel ,
                    new Point2D(4,5) , botName);
            troop.setInGameInbox(inGameInbox);
            troop.setId();
            this.addTroop(troop);

            troop = Troop.makeTroop(false,TowerTypes.PRINCESS_TOWER.toString() , humanLevel ,
                    new Point2D(16,5) , botName);
            troop.setInGameInbox(inGameInbox);
            troop.setId();
            this.addTroop(troop);
        }
        else {
            // for four player board
        }
    }

//    public Troop getNearestEnemy(int x , int y){
//        int xMin = 0;
//        int yMin = 0;
//        for (Troop troop:addedTroops)
//        {
//            if (!troop.getOwner().equals(humanPlayer))
//            {
//                xMin = (int)troop.getCoordinates().getX();
//                yMin = (int)troop.getCoordinates().getY();
//                break;
//            }
//        }
//        Troop nearest = null;
//
//        for (Troop troop:addedTroops)
//        {
//            int xEnemy = (int)troop.getCoordinates().getX();
//            int yEnemy = (int)troop.getCoordinates().getY();
//            if (!troop.getOwner().equals(humanPlayer))
//            {
//                if ( (Math.pow(x - xEnemy , 2) + Math.pow(y - yEnemy , 2 )) <= (Math.pow(x - xMin , 2) + Math.pow(y - yMin , 2 )))
//                {
//                    xMin = xEnemy;
//                    yMin = yEnemy;
//                    nearest = troop;
//                }
//            }
//        }
//        return nearest; // nearest enemy
//    }

    @Override
    public void run() {
        Card updatedCard = null;
        while (true) {
            try {
                updatedCard = coordinateUpdateQueue.take();
            } catch (InterruptedException e)
            {
                System.out.println("Exception in getting updated coordinates.");
                e.printStackTrace();
            }
            notifyAllTroops(updatedCard);




        }
    }

    public void notifyAllTroops(Card changedCard){ // the card which its coordinates are updated
        for (Troop troop:addedTroops)
        {
            troop.updateState(this,changedCard,false); // isDead messages will be sent to troops from some other method
        }
    }

    public Card isEnemyAround(int x , int y){ // towers are ignored!!!
        for (Troop troop:addedTroops)  // checking a circle with radius sqrt(72)
        {
            if (Math.pow(x - troop.getCoordinates().getX() , 2) + Math.pow(y - troop.getCoordinates().getY() , 2) <= 72
        && !(troop instanceof Tower ))
            {
                return (Card) troop;
            }
        }
        return null;
    }




    public String getNearestWay(Card card,int x , int y){ // this method gives second destination (when character wants to go to road)
        if (card.getOwner().contains("bot") && y == 25)
        {
            if (x <= 10)
                return "right";
            else
                return "left";
        }
        else if (!card.getOwner().contains("bot") && y == 4)
        {
            if (x <= 10)
                return "right";
            else
                return "left";
        }
        else if ((board[x][y] == BoardThings.ROAD || board[x][y] == BoardThings.BRIDGE) &&
                (board[x][y - 1] == BoardThings.ROAD || board[x][y -1] == BoardThings.BRIDGE))
        {
            if (!card.getOwner().contains("bot"))
                return "down";
            else
                return "up";
        }
        else if (x < 3)
            return "right";
        else if (x > 3 && x <= 10)
            return "left";
        else if (x >= 10 && x <= 14)
            return "right";
        else if (x >= 18)
            return "left";
        return null;
    }

    /**
     * getter
     * @return CoordinateUpdateQueue
     */
    public LinkedTransferQueue<Card> getCoordinateUpdateQueue() {
        return coordinateUpdateQueue;
    }

    /**
     * getter
     * @return added troops
     */
    public ArrayList<Troop> getAddedTroops() {
        return addedTroops;
    }

    public ArrayList<Troop> getNearEnemies(Troop thisTroop){
        ArrayList<Troop> nearEnemies = new ArrayList<>();
        for (Troop troop:addedTroops)
            if (thisTroop.canAttack(troop))
                nearEnemies.add(troop);
        return nearEnemies;
    }

    public Troop getTroopByID(String id){
        for (Troop troop:addedTroops)
        {
            if (troop.getId().equals(id))
                return troop;
        }
        return null;
    }

}
