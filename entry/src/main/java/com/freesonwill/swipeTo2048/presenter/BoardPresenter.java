package com.freesonwill.swipeTo2048.presenter;

import com.freesonwill.swipeTo2048.model.Board;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.model.observable.ObservableField;
import com.freesonwill.swipeTo2048.model.observable.OnFieldChangeListener;
import com.freesonwill.swipeTo2048.view.base.IBoardView;

public class BoardPresenter extends BasePresenter<IBoardView> {
    private Board board;

    public BoardPresenter(int row, int column) {
        board = new Board(row, column);
        board.currentScores.addListener(new OnFieldChangeListener<Integer>() {
            @Override
            public void onDataChange(ObservableField<Integer> field, Integer oldVal, Integer newVal) {
                getView().setScore(newVal);
            }
        });
        board.isGameOver.addListener(new OnFieldChangeListener<Boolean>() {
            @Override
            public void onDataChange(ObservableField<Boolean> field, Boolean oldVal, Boolean newVal) {
                if (newVal) {
                    getView().showGameOver();
                }
            }
        });
        board.boardObservable.addListener(new OnFieldChangeListener<Cell[][]>() {
            @Override
            public void onDataChange(ObservableField<Cell[][]> field, Cell[][] oldVal, Cell[][] newVal) {
                getView().refreshBoard(newVal);
            }
        });
    }

    public void swipeBoard(Direction dir) {
        board.swipeBoard(dir);
    }

    public void resetGame() {
        board.resetGame();
    }

    public Cell[][] getCells() {
        return board.boardObservable.get();
    }

    public boolean isGameOver() {
        return board.isGameOver.get();
    }
    public int getScore(){
        return board.currentScores.get();
    }
}
