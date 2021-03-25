package com.freesonwill.swipeTo2048.model;

import java.util.ArrayList;
import java.util.List;

public class board {
    private int columnCount = 4;
    private int rowCount = 4;
    private Cell[][] grids = new Cell[rowCount][columnCount];

    void addTwoOrFourToGrids() {
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

    void swipeGrids(Direction dir) {
        int newGrids = this.changeGrids(dir);
        if (newGrids.toString() != grids.toString()) {
            grids = newGrids;
            this.addTwoOrFourToGrids();
            this.drawGrids();

            if (this.isGridsFull() == true && this.isGridsNotMergeable() == true) {
                colors = THEME.faded;
                this.drawGrids();
                this.isShow = true;
            }
        }
    }

    void changeGrids(Direction direction) {


        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            int step = direction == Direction.LEFT ? 1 : -1;
            int column = direction == Direction.LEFT ? 0 : columnCount - 1;
            for (int row = 0; row < rowCount; row++) {
                List<Cell> array = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    if (grids[row][column].value != 0) {
                        array.add(grids[row][column]);
                    }
                    column += step;
                }

                for (int i = 0; i < array.size() - 1; i++) {
                    if (array.get(i).value == array.get(i + 1).value) {
                        array.get(i).value += array.get(i + 1).value;
                        this.updateCurrentScores(array.get(i));
                        array.get(i + 1).value = 0;
                        i++;
                    }
                }
                column = direction == Direction.LEFT ? 0 : columnCount - 1;
                for (Cell a : array) {
                    if (a.value != 0) {
                        grids[row][column] = a;
                        column += step;
                    }
                }
            }
        } else if (direction == 'up' || direction == 'down') {
            let step = 1;
            if (direction == 'down') {
                step = -1;
            }

            for (let column = 0; column < 4; column++) {
                let array = [];

                let row = 0;
                if (direction == 'down') {
                    row = 3;
                }

                for (let i = 0; i < 4; i++) {
                    if (grids[row][column] != 0) {
                        array.push(grids[row][column]);
                    }
                    row += step;
                }

                for (let i = 0; i < array.length - 1; i++) {
                    if (array[i] == array[i + 1]) {
                        array[i] += array[i + 1];
                        this.updateCurrentScores(array[i]);
                        array[i + 1] = 0;
                        i++;
                    }
                }

                row = 0;
                if (direction == 'down') {
                    row = 3;
                }

                for (const elem of array){
                    if (elem != 0) {
                        newGrids[row][column] = elem;
                        row += step;
                    }
                }
            }
        }

        return newGrids;

    }

    private void updateCurrentScores(Cell cell) {

    }

    boolean isGridsFull() {
        int count = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (grids[row][column].value != 0) {
                    count++;
                }
            }
        }
        return count == rowCount * columnCount;
    }

    boolean isGridsNotMergeable() {
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
