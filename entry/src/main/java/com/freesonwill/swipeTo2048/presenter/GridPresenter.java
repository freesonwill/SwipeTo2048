package com.freesonwill.swipeTo2048.presenter;

import com.freesonwill.swipeTo2048.model.Board;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.model.observable.ObservableField;
import com.freesonwill.swipeTo2048.model.observable.OnFieldChangeListener;
import com.freesonwill.swipeTo2048.view.base.IGridView;

public class GridPresenter extends BasePresenter<IGridView> {
    private Board board;

    public GridPresenter(int row, int column) {
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
        board.gridsObservable.addListener(new OnFieldChangeListener<Cell[][]>() {
            @Override
            public void onDataChange(ObservableField<Cell[][]> field, Cell[][] oldVal, Cell[][] newVal) {
                getView().refreshGrids(newVal);
            }
        });
    }

    public void swipeGrids(Direction dir) {
        board.swipeGrids(dir);
    }

    public void resetGame() {
        board.resetGame();
    }

    public Cell[][] getCells() {
        return board.gridsObservable.get();
    }

    public boolean isGameOver() {
        return board.isGameOver.get();
    }
}
