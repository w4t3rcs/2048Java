package org.w4t3rcs;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    private int score = 0;
    private int maxTile = 0;
    private Stack<Tile[][]> previousStates;
    private Stack<Integer> previousScores;
    private boolean isSaveNeeded;

    public Model() {
        resetGameTiles();
    }

    protected void resetGameTiles() {
        this.gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        Arrays.stream(gameTiles).forEach(gameTile -> Arrays.setAll(gameTile, j -> new Tile()));
        for (int i = 0; i < 2; i++) addTile();
        previousStates = new Stack<>();
        previousScores = new Stack<>();
        isSaveNeeded = true;
    }

    private void addTile() {
        final List<Tile> emptyList = getEmptyTiles();
        if (!emptyList.isEmpty()) {
            final int index = (int) (Math.random() * emptyList.size()) % emptyList.size();
            Tile emptyTile = emptyList.get(index);
            emptyTile.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    protected void randomMove() {
        switch ((int) (Math.random()*100) % 4) {
            case 0 -> left();
            case 1 -> right();
            case 2 -> up();
            case 3 -> down();
        }
    }

    protected void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::right));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));
        Objects.requireNonNull(priorityQueue.peek()).getMove().move();
    }

    private MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
        move.move();
        if (hasBoardChanged()) moveEfficiency = new MoveEfficiency(getEmptyTilesCount(), getScore(), move);
        rollback();
        return moveEfficiency;
    }

    private boolean hasBoardChanged() {
        Tile[][] previousTile = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].getValue() != previousTile[i][j].getValue()) return true;
            }
        }

        return false;
    }

    protected void up() {
        saveState(gameTiles);
        for (int i = 0; i < 3; i++) gameTiles = rotateClockwise(gameTiles);
        left();
        gameTiles = rotateClockwise(gameTiles);
    }

    protected void down() {
        saveState(gameTiles);
        gameTiles = rotateClockwise(gameTiles);
        left();
        for (int i = 0; i < 3; i++) gameTiles = rotateClockwise(gameTiles);
    }

    protected void right() {
        saveState(gameTiles);
        for (int i = 0; i < 2; i++) gameTiles = rotateClockwise(gameTiles);
        left();
        for (int i = 0; i < 2; i++) gameTiles = rotateClockwise(gameTiles);
    }

    protected void left() {
        if (isSaveNeeded) saveState(gameTiles);
        boolean moveRequired = false;
        for (Tile[] tiles : gameTiles)
            if (compressTiles(tiles) | mergeTiles(tiles)) moveRequired = true;
        if (moveRequired) addTile();
        isSaveNeeded = true;
    }

    private Tile[][] rotateClockwise(Tile[][] tiles) {
        final int length = tiles.length;
        Tile[][] result = new Tile[length][length];
        for (int row = 0; row < length; row++) {
            for (int column = 0; column < length; column++) {
                result[column][length - 1 - row] = tiles[row][column];
            }
        }

        return result;
    }

    private boolean compressTiles(Tile[] tiles) {
        Tile[] copyTiles = getTilesCopy(tiles);
        Arrays.sort(tiles, (tile1, tile2) -> tile2.isEmpty() ? -1 : 0);
        return isTilesUpdated(copyTiles, tiles);
    }

    private boolean mergeTiles(Tile[] tiles) {
        Tile[] copyTiles = getTilesCopy(tiles);
        performMergingTiles(tiles);
        return isTilesUpdated(copyTiles, tiles);
    }

    private Tile[] getTilesCopy(Tile[] tiles) {
        return Arrays.copyOf(tiles, tiles.length);
    }

    private void performMergingTiles(Tile[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            if (i == tiles.length - 1) break;
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
        setScore(getScore() + value);
    }

    private void updateMaxTile(int value) {
        setMaxTile(Math.max(getMaxTile(), value));
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] tempTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempTiles[i][j] = tiles[i][j].clone();
            }
        }

        previousStates.push(tempTiles);
        previousScores.push(getScore());
        isSaveNeeded = false;
    }

    protected void rollback() {
        if (!previousStates.isEmpty()) {
            gameTiles = previousStates.pop();
            setScore(previousScores.pop());
        }
    }

    private boolean isTilesUpdated(Tile[] oldTiles, Tile[] newTiles) {
        final boolean updated = !Arrays.equals(oldTiles, newTiles);
        return updated;
    }

    protected boolean canMove() {
        if (!isFull()) {
            return true;
        }

        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                Tile tile = gameTiles[x][y];
                if ((x < FIELD_WIDTH - 1 && tile.getValue() == gameTiles[x + 1][y].getValue())
                        || ((y < FIELD_WIDTH - 1) && tile.getValue() == gameTiles[x][y + 1].getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isFull() {
        return getEmptyTilesCount() == 0;
    }

    private int getEmptyTilesCount() {
        return getEmptyTiles().size();
    }

    private List<Tile> getEmptyTiles() {
        final List<Tile> emptyList = new ArrayList<>();
        Arrays.stream(gameTiles).flatMap(Arrays::stream).filter(Tile::isEmpty).forEach(emptyList::add);
        return emptyList;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMaxTile() {
        return maxTile;
    }

    public void setMaxTile(int maxTile) {
        this.maxTile = maxTile;
    }
}
