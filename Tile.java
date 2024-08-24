package test;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import test.Tile.Bag;

public class Tile {

    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public char getLetter() {
        return letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || this.getClass() != o.getClass())
            return false;

        Tile tile = (Tile) o; // casting from o to class Tile
        return (this.letter == tile.letter && this.score == tile.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag {
        private final int[] tileCounts; // An array that holds the amount of tiles for each letter
        private final Tile[] tiles; // An array that holds all possible tiles
        private final int[] initialCounts; // An array that holds the starting amount of each tile
        private int totalTiles;

        private static Bag instance = null;

        private final Random random;

        private Bag() {

            initialCounts = new int[] { 9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1 };
            tileCounts = initialCounts.clone();

            char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();// shorter
            int[] scores = { 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10 };

            tiles = new Tile[initialCounts.length];
            for (int i = 0; i < initialCounts.length; i++) {
                tiles[i] = new Tile(letters[i], scores[i]);
            }

            totalTiles = 0;
            for (int i = 0; i < initialCounts.length; i++) {
                totalTiles += initialCounts[i];
            }

            random = new Random();
        }

        public static Bag getBag() {
            if (instance == null)
                instance = new Bag();

            return instance;
        }

        public Tile getRand() {

            if (totalTiles == 0)
                return null;

            int randomIndex;
            Tile randomTile;

            do {
                randomIndex = random.nextInt(tiles.length);
                randomTile = tiles[randomIndex];
            } while (tileCounts[randomTile.getLetter() - 'A'] == 0);

            int index = randomTile.letter - 'A';

            tileCounts[index]--;
            totalTiles--;

            return randomTile;
        }

        public Tile getTile(char letter) 
        {
            int index = letter - 'A';

            if (index < 0 || index > 25 || tileCounts[index] == 0)
                return null;

            tileCounts[index]--;
            totalTiles--;
            return tiles[index];
        }

        public void put(Tile tile) {
            int index = tile.letter - 'A';
 
            if (tileCounts[index] < initialCounts[index]) {
                tileCounts[index]++;
                totalTiles++;
            }

        }

        public int size() {
            return totalTiles;
        }

        public int[] getQuantities() {
            return tileCounts.clone();
        }

    }

}
