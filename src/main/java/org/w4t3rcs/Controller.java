package org.w4t3rcs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private final Model model;
    private final View view;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        resetGameByEscapeKey(e);
        checkIfLost();
        performMoveKeyPressing(e);
        checkIfWon();
        view.repaint();
    }

    private void resetGameByEscapeKey(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) resetGame();
    }

    public void resetGame() {
        model.setScore(0);
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    private void checkIfLost() {
        if (!model.canMove()) view.isGameLost = true;
    }

    private void performMoveKeyPressing(KeyEvent keyEvent) {
        if (!view.isGameLost && !view.isGameWon) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> model.left();
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> model.right();
                case KeyEvent.VK_UP, KeyEvent.VK_W -> model.up();
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> model.down();
                case KeyEvent.VK_Z, KeyEvent.VK_CONTROL + KeyEvent.VK_Z -> model.rollback();
                case KeyEvent.VK_R -> model.randomMove();
                case KeyEvent.VK_E-> model.autoMove();
            }
        }
    }

    private void checkIfWon() {
        if (model.getMaxTile() == Tile.WINNING_TILE) view.isGameWon = true;
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.getScore();
    }

    public View getView() {
        return view;
    }
}
