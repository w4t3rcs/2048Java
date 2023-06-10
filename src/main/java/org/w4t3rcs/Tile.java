package org.w4t3rcs;

import java.awt.*;

public class Tile implements Cloneable {
    public static final int EMPTY = 0;
    public static final int WINNING_TILE = 2048;
    private int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
        this(0);
    }

    public boolean isEmpty() {
        return getValue() <= EMPTY;
    }

    public Color getFontColor() {
        if (getValue() < 16) return new Color(0x776e65);
        else return new Color(0xf9f6f2);
    }

    public Color getTileColor() {
        return switch (getValue()) {
            case EMPTY -> new Color(0xcdc1b4);
            case 2 -> new Color(0xeee4da);
            case 4 -> new Color(0xede0c8);
            case 8 -> new Color(0xf2b179);
            case 16 -> new Color(0xf59563);
            case 32 -> new Color(0xf67c5f);
            case 64 -> new Color(0xf65e3b);
            case 128 -> new Color(0xedcf72);
            case 256 -> new Color(0xedcc61);
            case 512 -> new Color(0xedc850);
            case 1024 -> new Color(0xedc53f);
            case WINNING_TILE -> new Color(0xedc22e);
            default -> new Color(0xff0000);
        };
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "value=" + value +
                '}';
    }

    @Override
    public Tile clone() {
        return new Tile(this.getValue());
    }
}
