package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import test.Tile.Bag;

public class Tile {

    public final char letter;
    public final int score;

    private Tile(char letter,int score)
    {
        this.letter = letter;
        this.score = score;
    }


    @Override
    public boolean equals(Object o)
    {
        if(this==o)
            return true;

        if( o == null || this.getClass() != o.getClass() )
            return false;

        Tile tile = (Tile) o; //casting from o to class Tile
        return (this.letter == tile.letter && this.score == tile.score) ;
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(letter,score);
    }


    public static class Bag
    {
        private final int[] tileCounts; // An array that holds the amount of tiles for each letter
        private final Tile[] tiles; // An array that holds all possible tiles
        private final List<Tile> tileList; // A list that will contain the tiles that can be taken out
        private final int[] initialCounts; // An array that holds the starting amount of each tile

        private static Bag instance = null; 
        //private static final Bag instance = new Bag();

        private Bag(){

            initialCounts = new int[]{ 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            tileCounts = initialCounts.clone();
            

            char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            int[] scores = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};


            tiles = new Tile[26];
            for(int i=0;i<26;i++)
            {
                tiles[i] = new Tile(letters[i], scores[i]);
            }


            tileList = new ArrayList<>();
            for(int i=0;i<26;i++)
            {
                for(int j=0;j<tileCounts[i];j++)
                {
                    tileList.add(tiles[i]);
                }
            }

            Collections.shuffle(tileList);
        }


        public static Bag getBag()
        {
            if( instance == null)
                instance = new Bag();

            return instance;
        }

        public Tile getRand()
        {
            if(tileList.isEmpty())
                return null;

            int randomIndex = ThreadLocalRandom.current().nextInt(tileList.size());
            Tile randomTile = tileList.remove(randomIndex);
            tileCounts[randomTile.letter - 'A']--;
            return randomTile;
        }

        public Tile getTile(char letter)
        {
            int index = letter - 'A';

            if( index < 0 || index > 25 || tileCounts[index] == 0 )
                return null;

            tileCounts[index]--;    
            return tiles[index];    
        }


        public void put(Tile tile)
        {
            int index = tile.letter - 'A'; //ascii 
            
            if(tileCounts[index] < initialCounts[index])
            {
                tileCounts[index]++;
                tileList.add(tile);
                Collections.shuffle(tileList);
            }
            //else throw "cannot return tile to bag";
        }


        public int size()
        {
            return tileList.size();
        }


        public int[] getQuantities()
        {
            return tileCounts.clone();
        }


    }


}
