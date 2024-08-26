package test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class Word {

    private final Tile[] tiles;
    private final int row;
    private final int column;
    private final boolean vertical;

    public Word(Tile[] tiles, int row, int column, boolean vertical)// Tiles[]
    {
        this.tiles = tiles;
        this.row = row;
        this.column = column;
        this.vertical = vertical;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean isVertical() {
        return vertical;
    }

    ////////////////////////////////////////////////equals
    @Override
    public boolean equals(Object w) {

        if (this == w)
            return true;

        if (w == null || this.getClass() != w.getClass())
            return false;

        Word word = (Word) w; // casting from o to class Tile
        return (this.row == word.row && this.column == word.column && this.vertical == word.vertical
                && Arrays.equals(tiles, word.tiles));// Array

    }

    ////////////////////////////////////////////////hashCode
    @Override
    public int hashCode() {
        int result = Objects.hash(row, column, vertical);
        result = (31 * result) + Arrays.hashCode(tiles); // multipie by a high prime num (Optimization of scatters in a
                                                         // hash code) //Array
        return result;
    }
}
