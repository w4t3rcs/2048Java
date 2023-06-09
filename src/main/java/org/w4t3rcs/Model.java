package org.w4t3rcs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    protected int score = 0;
    protected int maxTile = 0;

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

    public void mergeTiles(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            if (i == tiles.length - 1) return;
            if (tiles[i].getValue() == tiles[i + 1].getValue()) mergeSelectedTiles(tiles, i);
        }
    }

    private void mergeSelectedTiles(Tile[] tiles, int position) {
        int newValue = calculateMergedValue(tiles, position);
        updateScore(newValue);
        updateMaxTile(newValue);
        compressTiles(tiles);
    }

    private int calculateMergedValue(Tile[] tiles, int position) {
        int mergedValue = tiles[position].getValue()*2;
        tiles[position].setValue(mergedValue);
        tiles[position + 1].setValue(Tile.EMPTY);
        return mergedValue;
    }

    private void updateScore(int value) {
        score += value;
    }

    private void updateMaxTile(int value) {
        maxTile = Math.max(maxTile, value);
    }

    private void compressTiles(Tile[] tiles) {
        Arrays.sort(tiles, (tile1, tile2) -> tile2.isEmpty() ? -1 : 0);
    }
}
