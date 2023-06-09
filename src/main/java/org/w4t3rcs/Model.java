package org.w4t3rcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;

    public Model() {
        resetGameTiles();
    }

    protected void resetGameTiles() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        Arrays.stream(gameTiles).forEach(gameTile -> Arrays.setAll(gameTile, j -> new Tile()));
        for (int i = 0; i < 2; i++) addTile();
    }

    private void addTile() {
        final List<Tile> emptyList = getEmptyTiles();
        if (!emptyList.isEmpty()) {
            final int index = (int) (Math.random() * emptyList.size()) % emptyList.size();
            Tile emptyTile = emptyList.get(index);
            emptyTile.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    private List<Tile> getEmptyTiles() {
        final List<Tile> emptyList = new ArrayList<>();
        Arrays.stream(gameTiles).flatMap(Arrays::stream).filter(Tile::isEmpty).forEach(emptyList::add);
        return emptyList;
    }
}
