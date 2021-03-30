package com.freesonwill.swipeTo2048.view.base;

import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;

public interface IGridView extends IView {
    void setScore(int score);
    void showGameOver();
    void refreshGrids(Cell[][] cells);
    void swipeGrids(Direction dir);
}
