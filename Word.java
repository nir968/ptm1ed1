package test;

import java.lang.reflect.Array;
import java.util.Objects;

public class Word {

    private final Tile[] tiles;
    private final int row;
    private final int column;
    private final boolean vertical;


    private Word(Tiles[] tiles, int row, int column, boolean vertical)
    {
        this.tiles = tiles;
        this.row = row;
        this.column = column;
        this.vertical = vertical;
    }

    public Tile[] getTiles() 
    {
        return tiles;
    }

    public int getColumn() 
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }

    public boolean isVertical() 
    {
        return vertical;
    }


    @Override
    public boolean equals(Obj o)
    {

        if(this==o)
            return true;

        if( o == null || this.getClass() != o.getClass() )
            return false;

        Word word = (Word) w; //casting from o to class Tile
        return ( this.row == word.row && this.column == word.column && this.vertical == word.vertical && Array.equals(tiles,word.tiles) ) ;

    }

    
    @Override
    public int hashCode()
    {
        int result = Objects.hash(row,column,vertical);
        result = (31 * result) + Array.hashCode(tiles); //multipie by a high prime num (Optimization of scatters in a hash code)
        return result;
    }
}
