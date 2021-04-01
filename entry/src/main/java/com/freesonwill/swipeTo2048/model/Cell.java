package com.freesonwill.swipeTo2048.model;

public class Cell {
    protected final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    protected int value;

    public static String getCellsString(Cell[][] cells) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getValue() == 0) continue;
                if (builder.length() > 0) builder.append(",");
                builder.append("[").append(i).append(",").append(j)
                        .append(":").append(cells[i][j].getValue())
                        .append("]");
            }
        }
        return builder.toString();
    }
}
