package test;

//vertical = אנכי
//horizontal = אופקי

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {

    private static Board instance = null;

    private Tile[][] board;
    private static final int SIZE = 15;
    private static final int CENTER = SIZE / 2;

    private Board() {
        board = new Tile[SIZE][SIZE];
    }

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

    //////////////////////////////////////////////////////////////////////////////////////// boardLegal()

    public boolean isBoardEmpty() // the board is empty?
    {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null) {
                    return false;
                }
            }
        }
        return true; // yessss
    }

    public boolean boardLegal(Word word) {

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        boolean isFirstWord = isBoardEmpty();
        boolean crossesCenter = false;
        boolean isConnected = false;

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            if (row < 0 || row >= SIZE || column < 0 || column >= SIZE)
                return false;

            if (row == CENTER && column == CENTER)
                crossesCenter = true;

            if (board[row][column] != null) {
                if (!board[row][column].equals(wordTiles[i]))
                    return false;
                isConnected = true;
            }

            else if (!isFirstWord) {
                boolean adjacent = (row > 0 && board[row - 1][column] != null)
                        || (row < SIZE - 1 && board[row + 1][column] != null)
                        || (column > 0 && board[row][column - 1] != null)
                        || (column < SIZE - 1 && board[row][column + 1] != null);
                if (adjacent)
                    isConnected = true;
            }

        }

        if (!isFirstWord && !isConnected)
            return false;

        if (isFirstWord && !crossesCenter)
            return false;

        return true;
    }

    //////////////////////////////////////////////////////////////////////////////////////// end

    //////////////////////////////////////////////////////////////////////////////////////// getWords()

    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> words = new ArrayList<>();
        words.add(word);

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            checkForNewWord(row, column, isVertical, words);
        }

        return words;
    }

    public void checkForNewWord(int row, int column, boolean isVertical, ArrayList<Word> words) {
        if (isVertical) {
            Word newWord = buildHorizontalWord(row, column);
            if (newWord != null && !words.contains(newWord) && isValidWord(newWord))
                words.add(newWord);
        }

        if (!isVertical) {
            Word newWord = buildVerticalWord(row, column);
            if (newWord != null && !words.contains(newWord) && isValidWord(newWord))
                words.add(newWord);
        }
    }

    private Word buildHorizontalWord(int row, int column)// אופקי
    {
        ArrayList<Tile> tiles = new ArrayList<>();
        int startCol = column;

        while (startCol >= 0 && board[row][startCol] != null) {
            tiles.add(0, board[row][startCol]);

            startCol--; // while..
        }

        startCol = column + 1;
        while (startCol < SIZE && board[row][startCol] != null) {
            tiles.add(board[row][startCol]);

            startCol++; // while
        }

        if (tiles.size() > 1) {
            Tile[] tilesArray = tiles.toArray(new Tile[0]); // convert arrayList to array
            return new Word(tilesArray, row, column, false);
        } else
            return null;

    }

    private Word buildVerticalWord(int row, int column)// אנכי
    {
        ArrayList<Tile> tiles = new ArrayList<>();
        int startRow = row;

        while (startRow >= 0 && board[startRow][column] != null) {
            tiles.add(0, board[startRow][column]);
            startRow--; // while..
        }

        startRow = row + 1;
        while (startRow < SIZE && board[startRow][column] != null) {
            tiles.add(board[startRow][column]);
            startRow++; // while
        }

        if (tiles.size() > 1) {
            Tile[] tilesArray = tiles.toArray(new Tile[0]); // convert arrayList to array
            return new Word(tilesArray, row, column, true);
        } else
            return null;
    }

    private boolean isValidWord(Word word) {
        return (dictionaryLegal(word));
    }
    ////////////////////////////////////////////////////////////////// end

    public boolean dictionaryLegal(Word word) {
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////// getScore()

    public int getScore(Word word) {
        int wordMulti = 1;
        int totalScore = 0;

        Tile[] wordTiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        for (int i = 0; i < wordTiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            Tile tile = wordTiles[i];
            if (tile == null)
                continue;// to the next iteration

            int letterScore = tile.getTileScore();
            String squareType = getSquareType(row, column);

            switch (squareType) {
                case "DLS":
                    letterScore *= 2;
                    break;
                case "TLS":
                    letterScore *= 3;
                    break;
                case "DWS":
                    wordMulti *= 2;
                    break;
                case "TWS":
                    wordMulti *= 3;
                    break;

                default:
                    break;
            }

            totalScore += letterScore;
        }

        totalScore *= wordMulti;

        return totalScore;
    }

    private String getSquareType(int row, int column) {
        if ((row == 0 && column == 0) || (row == 0 && column == 7) || (row == 0 && column == 14)
                || (row == 7 && column == 0) ||
                (row == 7 && column == 14) || (row == 14 && column == 0) || (row == 14 && column == 7)
                || (row == 14 && column == 14))
            return "TWS";

        if ((row == 1 || row == 13) && (column == 1 || column == 13) ||
                (row == 2 || row == 12) && (column == 2 || column == 12) ||
                (row == 3 || row == 11) && (column == 3 || column == 11) ||
                (row == 4 || row == 10) && (column == 4 || column == 10) || (row == 7 && column == 7))
            return "DWS";

        if ((row == 1 || row == 13) && (column == 5 || column == 9)
                || (row == 5 || row == 9) && (column == 1 || column == 5 || column == 9 || column == 13))
            return "TLS";

        if ((row == 0 || row == 14) && (column == 3 || column == 11) ||
                (row == 2 || row == 12) && (column == 6 || column == 8) ||
                (row == 3 || row == 11) && (column == 0 || column == 7 || column == 14) ||
                (row == 6 || row == 8) && (column == 2 || column == 6 || column == 8 || column == 12) ||
                (row == 7 && (column == 3 || column == 11)))
            return "DLS";

        return "";
    }
    ////////////////////////////////////////////////////////////////// end

    ////////////////////////////////////////////////////////////////// tryPlaceWord()
    public int tryPlaceWord(Word word) {

        if (!boardLegal(word)) {

            return 0;
        }

        ArrayList<Word> newWords = getWords(word);

        for (Word w : newWords) {
            if (!dictionaryLegal(w)) {

                return 0;
            }
        }

        int totalScore = 0;
        for (Word w : newWords) {
            totalScore += getScore(w);
        }

        placeWord(word);

        return totalScore;
    }

    // public void placeWord(Word word) {
    // Tile[] tiles = word.getTiles();
    // int startRow = word.getRow();
    // int startCol = word.getColumn();
    // boolean isVertical = word.isVertical();

    // for (int i = 0; i < tiles.length; i++) {
    // int row = isVertical ? startRow + i : startRow;
    // int column = isVertical ? startCol : startCol + i;

    // if( row < 0 || row >= SIZE || column < 0 || column >= SIZE )
    // return;

    // Tile currenTile = board[row][column];
    // Tile newTile = tiles[i];

    // if(newTile == null)
    // continue;

    // if(currenTile == null)
    // {
    // if(newTile.getTileLetter() != '_')
    // board[row][column] = newTile;
    // }
    // else
    // {
    // if(newTile.getTileLetter() == '_')
    // continue;

    // if(!currenTile.equals(newTile))
    // throw new IllegalArgumentException("Invalid placement: existing tile does not
    // match.");
    // }
    // }
    // printBoard();
    // }

    public void placeWord(Word word) {
        Tile[] tiles = word.getTiles();
        int startRow = word.getRow();
        int startCol = word.getColumn();
        boolean isVertical = word.isVertical();

        // הדפסת הלוח לפני השמת המילה
        System.out.println("Before placing word:");
        printBoard();

        // תהליך הנחת המילה
        for (int i = 0; i < tiles.length; i++) {
            int row = isVertical ? startRow + i : startRow;
            int column = isVertical ? startCol : startCol + i;

            if (row < 0 || row >= SIZE || column < 0 || column >= SIZE) {
                System.out.println("Out of bounds: (" + row + ", " + column + ")");
                return; // מחוץ לטווח הלוח
            }

            Tile currentTile = board[row][column];
            Tile newTile = tiles[i];

            System.out.println("Processing tile: " + (newTile == null ? "null" : newTile.getTileLetter()) +
                    " at position: (" + row + ", " + column + ")");

            if (newTile == null) {
                continue; // אין צורך לטפל באריח ריק
            }

            if (currentTile == null) {
                // במקרה של תא ריק, הנח את האריח החדש בתנאי שהוא לא תו `_`
                if (newTile.getTileLetter() != '_') {
                    board[row][column] = newTile;
                    System.out.println("Placing new tile: " + newTile.getTileLetter() +
                            " at position: (" + row + ", " + column + ")");
                }
            } else {
                if (newTile.getTileLetter() == '_') {
                    continue; // תו `_` יכול להיות כל תו חוקי
                }

                // בדוק אם האריחים תואמים, אחרת זרוק שגיאה
                if (!currentTile.equals(newTile)) {
                    System.out.println("Mismatch found: existing tile is " + currentTile.getTileLetter() +
                            ", trying to place " + newTile.getTileLetter());
                    throw new IllegalArgumentException("Invalid placement: existing tile does not match.");
                }
            }
        }

        // הדפסת הלוח אחרי השמת המילה
        System.out.println("After placing word:");
        printBoard();
    }

    ////////////////////////////////////////////////////////////////// end

    // print the board for check

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != null)
                    System.out.print(board[i][j].getTileLetter() + " ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

}
