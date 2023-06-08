package org.w4t3rcs;

import java.util.Arrays;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;

    public Model() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        Arrays.stream(gameTiles).forEach(gameTile -> Arrays.setAll(gameTile, j -> new Tile()));
    }
}
