package shared.model;

import shared.enums.BoardThings;
import shared.enums.BoardTypes;
import shared.enums.CardTypes;
import shared.model.troops.Troop;
import shared.model.troops.card.Card;

import java.util.ArrayList;

public class Board {
    private BoardThings[][] board;
    private BoardTypes type;
    private ArrayList<Troop> addedTroops;
    private boolean isServerSide;
    private boolean isLeftUpAreaAllowed;
    private boolean isRightUpAreaAllowed;

    public Board(BoardTypes type , boolean isServerSide , String humanPlayer){
        this.type = type;
        board = new BoardThings[21][30];
        addedTroops = new ArrayList<>();
        this.isServerSide = isServerSide;
        makeRawBoard();
    }


    private void makeRawBoard(){
        if (type == BoardTypes.TWO_PLAYERS)
        {
            // roads
            for (int i = 3 ; i < 6 ; i++) // index in board starts from zero!!!
                for (int j = 3 ; j < 27 ; j++)
                    board[i][j] = BoardThings.ROAD;
            for (int i = 14 ; i < 17 ; i++)
                for (int j = 3 ; j < 27 ; j++)
                    board[i][j] = BoardThings.ROAD;
            for (int i = 6 ; i < 15 ; i++)
                for (int j = 3 ; j < 5 ; j++)
                    board[i][j] = BoardThings.ROAD;
            for (int i = 6 ; i < 15 ; i++)
                for (int j = 24 ; j < 26 ; j++)
                    board[i][j] = BoardThings.ROAD;

            board[9][2] = BoardThings.ROAD;
            board[10][2] = BoardThings.ROAD;
            board[11][2] = BoardThings.ROAD;
            board[9][26] = BoardThings.ROAD;
            board[10][26] = BoardThings.ROAD;
            board[11][26] = BoardThings.ROAD;

            // road
            for (int j = 14 ; j < 16 ; j++)
                for (int i = 0 ; i < 21 ; i++)
                    board[i][j] = BoardThings.WATER;
            // bridges
            board[4][14] = BoardThings.BRIDGE;
            board[4][15] = BoardThings.BRIDGE;
            board[15][14] = BoardThings.BRIDGE;
            board[15][15] = BoardThings.BRIDGE;

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

    public boolean isValidAddress(Card chosen, double x , double y){
        int tileX = (int)Math.round(x);
        int tileY = (int)Math.round(y);
        tileX--; // changing to array index
        tileY--;
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


}
