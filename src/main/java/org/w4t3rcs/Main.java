package org.w4t3rcs;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Tile[] tiles1 = new Tile[]{new Tile(4), new Tile(4), new Tile(4), new Tile(0)};
        Tile[] tiles2 = new Tile[]{new Tile(2), new Tile(2), new Tile(4), new Tile(0)};
        Tile[] tiles3 = new Tile[]{new Tile(4), new Tile(4), new Tile(4), new Tile(4)};

        model.mergeTiles(tiles1);
        model.mergeTiles(tiles2);
        model.mergeTiles(tiles3);

        Arrays.stream(tiles1).forEach(System.out::print);
        System.out.println();
        Arrays.stream(tiles2).forEach(System.out::print);
        System.out.println();
        Arrays.stream(tiles3).forEach(System.out::print);
    }
}