package com.freesonwill.swipeTo2048.view.base;

import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;

public interface IBoardView extends IView {
    void showGameOver();
    void refreshBoard(Cell[][] cells);
    void setScore(int score);
}
