package com.freesonwill.swipeTo2048.model;

import com.freesonwill.swipeTo2048.model.observable.ObservableField;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int columnCount = 4;
    private int rowCount = 4;
    public ObservableField<Integer> currentScores = new ObservableField<>();
    public ObservableField<Boolean> isGameOver = new ObservableField<>();
    public ObservableField<Cell[][]> gridsObservable = new ObservableField<>();


    public void init() {
        Cell[][] grids = gridsObservable.get();
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                if (grids[i][j] == null) grids[i][j] = new Cell(i, j);
                grids[i][j].value = 0;
            }
        }
        this.currentScores.set(0);
        this.addTwoOrFourToGrids();
        this.addTwoOrFourToGrids();
        gridsObservable.set(grids);
    }


    void addTwoOrFourToGrids() {
        Cell[][] grids = gridsObservable.get();
        List<Cell> array = new ArrayList<>();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (grids[row][column].value == 0) {
                    array.add(grids[row][column]);
                }
            }
        }
        int randomIndex = (int) Math.floor(Math.random() * array.size());
        if (Math.random() < 0.8) {
            array.get(randomIndex).value = 2;
        } else {
            array.get(randomIndex).value = 4;
        }
    }

    public void swipeGrids(Direction dir) {
        this.changeGrids(dir);
        if (!this.isGridsFull()) {
            this.addTwoOrFourToGrids();
            return;
        }
        if (this.isGridsNotMergeable()) {
            gameOver();
            return;
        }
    }

    public void resetGame() {
        init();
    }

    void gameOver() {
        isGameOver.set(true);
    }

    void updateCurrentScores(int gridNum) {
        this.currentScores.set(currentScores.get() + gridNum);
    }

    public void changeGrids(Direction direction) {
        Cell[][] grids = gridsObservable.get();
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            final int step = direction == Direction.LEFT ? 1 : -1;
            int y;
            for (int row = 0; row < rowCount; row++) {
                List<Cell> array = new ArrayList<>();
                y = direction == Direction.LEFT ? 0 : columnCount - 1;
                for (int i = 0; i < columnCount; i++) {
                    if (grids[row][i].value != 0) {
                        array.add(grids[row][y]);
                    }
                    y += step;
                }
                for (int i = 0; i < array.size() - 1; i++) {
                    if (array.get(i).value == array.get(i + 1).value) {
                        array.get(i).value += array.get(i + 1).value;
                        this.updateCurrentScores(array.get(i).value);
                        array.get(i + 1).value = 0;
                        i++;
                    }
                }
                int column = direction == Direction.LEFT ? 0 : columnCount - 1;
                for (Cell a : array) {
                    if (a.value != 0) {
                        grids[row][column].value = a.value;
                        column += step;
                    }
                }
            }
        } else if (direction == Direction.UP || direction == Direction.DOWN) {
            final int step = direction == Direction.UP ? 1 : -1;
            int x;
            for (int column = 0; column < columnCount; column++) {
                List<Cell> array = new ArrayList<>();
                x = direction == Direction.UP ? 0 : rowCount - 1;
                for (int i = 0; i < columnCount; i++) {
                    if (grids[x][column].value != 0) {
                        array.add(grids[x][column]);
                    }
                    x += step;
                }
                for (int i = 0; i < array.size(); i++) {
                    if (array.get(i).value == array.get(i + 1).value) {
                        array.get(i).value += array.get(i + 1).value;
                        this.updateCurrentScores(array.get(i).value);
                        array.get(i + 1).value = 0;
                        i++;
                    }
                }
                x = direction == Direction.UP ? 0 : rowCount - 1;
                for (Cell elem : array) {
                    if (elem.value != 0) {
                        grids[x][column].value = elem.value;
                        x += step;
                    }
                }
            }
        }
    }

    boolean isGridsFull() {
        Cell[][] grids = gridsObservable.get();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (grids[row][column].value != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isGridsNotMergeable() {
        Cell[][] grids = gridsObservable.get();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (column < columnCount - 1) {
                    if (grids[row][column].value == grids[row][column + 1].value) {
                        return false;
                    }
                }
                if (row < rowCount - 1) {
                    if (grids[row][column].value == grids[row + 1][column].value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
