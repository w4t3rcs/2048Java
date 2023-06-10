package org.w4t3rcs;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    @Override
    public int compareTo(MoveEfficiency o) {
        if (this.getNumberOfEmptyTiles() > o.getNumberOfEmptyTiles()) return 1;
        else if (this.getNumberOfEmptyTiles() < o.getNumberOfEmptyTiles()) return -1;
        else return Integer.compare(this.getScore(), o.getScore());
    }

    protected int getNumberOfEmptyTiles() {
        return numberOfEmptyTiles;
    }

    protected int getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }
}
