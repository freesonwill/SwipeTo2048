package com.freesonwill.swipeTo2048.model;

import com.freesonwill.swipeTo2048.model.observable.ObservableField;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int columnCount;
    private final int rowCount;
    public ObservableField<Integer> currentScores = new ObservableField<>(0);
    public ObservableField<Boolean> isGameOver = new ObservableField<>(false);
    public ObservableField<Cell[][]> boardObservable = new ObservableField<>(null);

    public Board(int rowCount, int columnCount) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        init();
    }

    public void init() {
        Cell[][] board = boardObservable.get();
        if (board == null) {
            board = new Cell[rowCount][columnCount];
            boardObservable.set(board, false);
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null) board[i][j] = new Cell(i, j);
                board[i][j].value = 0;
            }
        }
        this.currentScores.set(0);
        this.addTwoOrFourToBoard();
        this.addTwoOrFourToBoard();
        boardObservable.set(board, true);
    }


    void addTwoOrFourToBoard() {
        Cell[][] board = boardObservable.get();
        List<Cell> array = new ArrayList<>();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (board[row][column].value == 0) {
                    array.add(board[row][column]);
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

    public void swipeBoard(Direction dir) {
        this.changeBoard(dir);
        if (!this.isBoardFull()) {
            this.addTwoOrFourToBoard();
            return;
        }
        if (this.isBoardNotMergeable()) {
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

    void updateCurrentScores(int score) {
        this.currentScores.set(currentScores.get() + score);
    }

    public void changeBoard(Direction direction) {
        System.out.println("direction-->" + direction);
        Cell[][] board = boardObservable.get();
        boolean changed = false;
        if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            final int step = direction == Direction.LEFT ? 1 : -1;
            int y = direction == Direction.LEFT ? 0 : columnCount - 1;
            for (int row = 0; row < rowCount; row++) {
                List<Cell> array = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    if (board[row][i].value != 0) {
                        array.add(board[row][y]);
                    }
                    y += step;
                }
                for (int i = 0; i < array.size() - 1; i++) {
                    if (array.get(i).value == array.get(i + 1).value) {
                        array.get(i).value += array.get(i + 1).value;
                        this.updateCurrentScores(array.get(i).value);
                        array.get(i + 1).value = 0;
                        i++;
                        changed = true;
                    }
                }
                int column = direction == Direction.LEFT ? 0 : columnCount - 1;
                System.out.println("before-->" + Cell.getCellsString(board));
                for (Cell a : array) {
                    if (a.value != 0) {
                        board[row][column].value = a.value;
                        a.value = 0;
                        column += step;
                        changed = true;
                    }
                }
                System.out.println("end-->" + Cell.getCellsString(board));
            }
        } else if (direction == Direction.UP || direction == Direction.DOWN) {
            final int step = direction == Direction.UP ? 1 : -1;
            int x;
            for (int column = 0; column < columnCount; column++) {
                List<Cell> array = new ArrayList<>();
                x = direction == Direction.UP ? 0 : rowCount - 1;
                for (int i = 0; i < columnCount; i++) {
                    if (board[x][column].value != 0) {
                        array.add(board[x][column]);
                    }
                    x += step;
                }
                for (int i = 0; i < array.size() - 1; i++) {
                    if (array.get(i).value == array.get(i + 1).value) {
                        array.get(i).value += array.get(i + 1).value;
                        this.updateCurrentScores(array.get(i).value);
                        array.get(i + 1).value = 0;
                        i++;
                        changed = true;
                    }
                }
                x = direction == Direction.UP ? 0 : rowCount - 1;
                for (Cell a : array) {
                    if (a.value != 0) {
                        board[x][column].value = a.value;
                        a.value = 0;
                        x += step;
                        changed = true;
                    }
                }
            }
        }
        if (changed) boardObservable.set(board);
    }

    boolean isBoardFull() { //格子是否满了
        Cell[][] Board = boardObservable.get();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (Board[row][column].value != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isBoardNotMergeable() {//是否可以合并
        Cell[][] Board = boardObservable.get();
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (column < columnCount - 1) {
                    if (Board[row][column].value == Board[row][column + 1].value) {
                        return false;
                    }
                }
                if (row < rowCount - 1) {
                    if (Board[row][column].value == Board[row + 1][column].value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
