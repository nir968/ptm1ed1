package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Board {

    private static Board instance = null;

    private Tile[][] board;
    private static final int SIZE = 15;
    private static final int CENTER = SIZE / 2;
    private int firstTime = 0;

    private Board() {
        board = new Tile[SIZE][SIZE];
    }

    // singleton
    public static Board getBoard() {
        if (instance == null)
            instance = new Board();

        return instance;
    }

    public Tile[][] getTiles() {
        Tile[][] copy = new Tile[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    //////////////////////////////////////////////////////////
    public boolean boardLegal(Word word) {

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        int legalCounter = 0;
        boolean legalForAll = true;
        boolean forTheRest = false;
        boolean firstWord = false; // (startRow == CENTER && startCol == CENTER && board[CENTER][CENTER] == null);

        // boolean isConnected = false;

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {
                legalForAll = false;
                return false;
            }

            if ((row == CENTER && column == CENTER) && (board[CENTER][CENTER] == null)) {
                firstWord = true;
            }

            else if ((wordTiles[i] != null) && (wordTiles[i].letter == '_')) {
                if (board[row][column] != null) {
                    wordTiles[i] = board[row][column];

                    if (dictionaryLegal(word))
                        forTheRest = true;
                }
            }

            else {
                boolean adjacent = (row > 0 && board[row - 1][column] != null)
                        || (row < SIZE - 1 && board[row + 1][column] != null)
                        || (column > 0 && board[row][column - 1] != null)
                        || (column < SIZE - 1 && board[row][column + 1] != null);
                if (adjacent) {
                    forTheRest = true; // ?
                }
            }

        }

        if (forTheRest || firstWord){
            if (legalForAll) {
                return true;
            }
        }
        return false;
    }

    //////////////////////////////////////////////////////////

    public boolean dictionaryLegal(Word word) {
        return true;
    }
    //////////////////////////////////////////////////////////

    public int getTotalScore(Word word) {
        int wordMulti = 1;
        int score = 0;

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            Tile tile = wordTiles[i];

            if (tile == null)
                tile = board[row][column];

            if (tile == null)
                continue;// to the next iteration

            int letterScore = tile.score;

            int letterMultiplier = getLetterMultiplier(row, column);

            int wordMultiplier = getWordMultiplier(row, column);

            score += letterScore * letterMultiplier;

            if ((row == CENTER && column == CENTER) && (firstTime == 0)) {
                firstTime++;
                wordMultiplier = 2;
            }
            wordMulti *= wordMultiplier;
        }

        int totalScore = 0;
        totalScore = score * wordMulti; // ?
        return totalScore;
    }

    public int getLetterMultiplier(int row, int column) {
        // blue TLS
        if ((row == 1 && column == 5) || (row == 1 && column == 9) || (row == 5 && column == 1) ||
                (row == 5 && column == 5) || (row == 5 && column == 9) || (row == 5 && column == 13) ||
                (row == 9 && column == 1) || (row == 9 && column == 5) || (row == 9 && column == 9) ||
                (row == 9 && column == 13) || (row == 13 && column == 5) || (row == 13 && column == 9)) {
            return 3;

        }
        // light blue DLS
        else if ((row == 0 && column == 3) || (row == 0 && column == 11) || (row == 2 && column == 6) ||
                (row == 2 && column == 8) || (row == 3 && column == 0) || (row == 3 && column == 7) ||
                (row == 3 && column == 14) || (row == 6 && column == 2) || (row == 6 && column == 6) ||
                (row == 6 && column == 8) || (row == 6 && column == 12) || (row == 7 && column == 3) ||
                (row == 7 && column == 11) || (row == 8 && column == 2) || (row == 8 && column == 6) ||
                (row == 8 && column == 8) || (row == 8 && column == 12) || (row == 11 && column == 0) ||
                (row == 11 && column == 7) || (row == 11 && column == 14) || (row == 12 && column == 6) ||
                (row == 12 && column == 8) || (row == 14 && column == 3) || (row == 14 && column == 11)) {
            return 2;

        } else {
            return 1; // regular
        }
    }

    public int getWordMultiplier(int row, int column) {
        // red TWS
        if ((row == 0 && column == 0) || (row == 0 && column == 7) || (row == 0 && column == 14) ||
                (row == 7 && column == 0) || (row == 7 && column == 14) || (row == 14 && column == 0) ||
                (row == 14 && column == 7) || (row == 14 && column == 14)) {
            return 3;

        } // yellow DWS
        else if ((row == 1 && column == 1) || (row == 2 && column == 2) || (row == 3 && column == 3) ||
                (row == 4 && column == 4) || (row == 13 && column == 1) || (row == 12 && column == 2) ||
                (row == 11 && column == 3) || (row == 10 && column == 4) || (row == 1 && column == 13) ||
                (row == 2 && column == 12) || (row == 3 && column == 11) || (row == 4 && column == 10) ||
                (row == 10 && column == 10) || (row == 11 && column == 11) || (row == 12 && column == 12) ||
                (row == 13 && column == 13)) {
            return 2;
        } else {
            return 1; // regular
        }
    }
    //////////////////////////////////////////////////////////

    

    private ArrayList<Word> getAllWords(Tile[][] ts) {
        ArrayList<Word> ws = new ArrayList<>();
    
        // מציאת מילים אופקיות
        for (int i = 0; i < ts.length; i++) {
            int j = 0;
            while (j < ts[i].length) {
                ArrayList<Tile> horizontal = new ArrayList<>();
                int startCol = j;
                while (j < ts[i].length && ts[i][j] != null) {
                    horizontal.add(ts[i][j]);
                    j++;
                }
                if (horizontal.size() > 1) {
                    Tile[] tiles = new Tile[horizontal.size()];
                    ws.add(new Word(horizontal.toArray(tiles), i, startCol, false));
                }
                j++;
            }
        }
    
        // מציאת מילים אנכיות
        for (int j = 0; j < ts[0].length; j++) {
            int i = 0;
            while (i < ts.length) {
                ArrayList<Tile> vertical = new ArrayList<>();
                int startRow = i;
                while (i < ts.length && ts[i][j] != null) {
                    vertical.add(ts[i][j]);
                    i++;
                }
                if (vertical.size() > 1) {
                    Tile[] tiles = new Tile[vertical.size()];
                    ws.add(new Word(vertical.toArray(tiles), startRow, j, true));
                }
                i++;
            }
        }
    
        return ws;
    }



    private Set<Word> getWords(Word word) {
        Tile[][] ts = this.getTiles();
        Set<Word> before = new HashSet<>(getAllWords(ts));

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;
            ts[row][column] = wordTiles[i];
        }

        Set<Word> after = new HashSet<>(getAllWords(ts));
        after.removeAll(before);

        return after;
    }

    public int tryPlaceWord(Word word) {
        int totalScore = 0;
        if (!boardLegal(word))// not legal!
            return 0;

        Word fixedWord = fixWord(word);
        Set<Word> newWords = this.getWords(fixedWord);
        newWords.add(fixedWord);

        placeWord(word);
        for (Word w : newWords) {
            totalScore += this.getTotalScore(w);
        }
        return totalScore;

    }

    public Word fixWord(Word word) {
        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();
        Tile[] fixedTiles = new Tile[wordTiles.length];

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            if (wordTiles[i] == null)
                fixedTiles[i] = board[row][column];
            else
                fixedTiles[i] = wordTiles[i];
        }

        return new Word(fixedTiles, startRow, startCol, isVertical);
    }

    public void placeWord(Word word) {
        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;
            if ((wordTiles[i] != null) && (wordTiles[i].getLetter() == '_') && (board[row][column] != null))
                wordTiles[i] = board[row][column];

            if (board[row][column] == null && wordTiles[i] != null)
                board[row][column] = wordTiles[i];
        }
        //printBoard();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // public void printBoard() {
    //     for (int i = 0; i < SIZE; i++) {
    //         for (int j = 0; j < SIZE; j++) {
    //             if (board[i][j] != null) {
    //                 System.out.print(board[i][j].getLetter() + " ");
    //             } else {
    //                 System.out.print(". ");
    //             }
    //         }
    //         System.out.println();
    //     }
    // }

}