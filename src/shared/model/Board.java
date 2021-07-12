package shared.model;

import shared.enums.BoardThings;
import shared.enums.BoardTypes;
import shared.model.troops.Tower;
import shared.model.troops.Troop;

import java.util.ArrayList;

public class Board {
    private BoardThings[][] board;
    private BoardTypes type;
    private ArrayList<Troop> addedTroops;

    public Board(BoardTypes type){
        this.type = type;
        board = new BoardThings[21][30];

    }


    private void makeRawBoard(){
        if (type == BoardTypes.TWO_PLAYERS)
        {
            // roads
            for (int i = 3 ; i < 6 ; i++) // index in board starts from zero!!!
                for (int j = 3 ; j < 27 ; j++)
                    board[i][j] = BoardThings.ROAD;
            board[3][14] = BoardThings.WATER;
            board[3][15] = BoardThings.WATER;
            board[4][14] = BoardThings.BRIDGE;
            board[4][15] = BoardThings.BRIDGE;
            board[5][14] = BoardThings.WATER;
            board[5][15] = BoardThings.WATER;

            for (int i = 14 ; i < 17 ; i++)
                for (int j = 3 ; j < 27 ; j++)
                    board[i][j] = BoardThings.ROAD;
            board[14][14] = BoardThings.WATER;
            board[14][15] = BoardThings.WATER;
            board[15][14] = BoardThings.BRIDGE;
            board[15][15] = BoardThings.BRIDGE;
            board[16][14] = BoardThings.WATER;
            board[16][15] = BoardThings.WATER;

            for (int i = 6 ; i < 15 ; i++)
                for (int j = 3 ; j < 5 ; j++)
                    board[i][j] = BoardThings.ROAD;

            board[9][2] = BoardThings.ROAD;
            board[10][2] = BoardThings.ROAD;
            board[11][2] = BoardThings.ROAD;


            for (int i = 6 ; i < 15 ; i++)
                for (int j = 24 ; j < 26 ; j++)
                    board[i][j] = BoardThings.ROAD;

            board[9][26] = BoardThings.ROAD;
            board[10][26] = BoardThings.ROAD;
            board[11][26] = BoardThings.ROAD;


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

            // adding towers

//            addedTroops.add(new Tower())
        }
        else {

        }
    }

    public void addTroop(Troop troop)
    {
        addedTroops.add(troop);
    }

}
