package test;

import java.util.Objects;

public class Board {


    private static Board instance = null;

    // private Tiles[] tiles;

    // private Board()
    // {
    //     tiles = new Tile[15][15];
    //     for(int i=0;i<15;i++)
    //     {
    //         for(int j=0;j<15;j++)
    //         {
    //             tiles[i][j] = null;
    //         } 
    //     }
    // }

    private Tile[][] board;
    private static final int SIZE = 15;
    private static final int CENTER = SIZE / 2 ;
    
    private Board()
    {
        board = new Tile[SIZE][SIZE];
    }


    public static Board getBoard()
    {
        if(instance == null)
            instance = new Board();

        return instance;
    }


    // public Tile[][] getTiles()
    // {
    //     Tile[][] copy = new Tile[SIZE][SIZE];
    //     for(int i=0;i<SIZE;i++)
    //     {
    //         for(int j=0;j<SIZE;j++)
    //         {
    //             copy[i][j] = board[i][j];
    //         }
    //     }
    //     return copy;
    // }

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy[i][j] = board[i][j].clone(); //deep copy
            }
        }
        return copy;
    }
    

    public boolean boardLegal(Word word)
    {
        //Tile[][] board = getTiles();

        boolean hasExitingTile = false;
        //boolean isValidPlacement = true;

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        for(int i=0;i<wordTiles.length;i++)
        {
            int row = isVertical ? startRow + i : startRow;
            int col = isVertical ? startCol : startCol + i ;

            if( row < 0 || row >= 15 || col < 0 || col >= 15 ) 
            return false;

            Tile currenTile = board[row][col];
            Tile wordTile = wordTiles[i];

            if(currenTile == null && wordTile != null)
                isValidPlacement = false;

            else if(currenTile != null && !currenTile.equals(wordTile))
                isValidPlacement = false;

            if(currenTile != null)
                hasExitingTile = true;
        }

        if(!hasExitingTile)
            return false;

        for(int i=0;i<wordTiles.length;i++)
        {
               
            int row = isVertical ? startRow + i : startRow;
            int col = isVertical ? startCol : startCol + i ;

            Tile currenTile = board[row][col];
            Tile wordTile = wordTiles[i];

            if(currenTile != null && !currenTile.equals(wordTile))
                return false;
        }

        return isValidPlacement;
        
    }


    public boolean dictionaryLegal(Word word)
    {
        return true;
    }


    // public int getScore(Word word)
    // {
    //     Tile[][] board = Board.getBoard().getTiles();
    //     Tile[] wordTiles = word.getTiles();

    //     int startRow = word.getRow();
    //     int startCol = word.getColumn();
    //     boolean isVertical = word.isVertical();

    //     int score = 0;



    // } 



}
