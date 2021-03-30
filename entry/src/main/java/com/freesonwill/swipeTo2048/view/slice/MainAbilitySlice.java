package com.freesonwill.swipeTo2048.view.slice;

import com.freesonwill.swipeTo2048.ResourceTable;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.presenter.GridPresenter;
import com.freesonwill.swipeTo2048.view.GridView;
import com.freesonwill.swipeTo2048.view.base.IGridView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class MainAbilitySlice extends AbilitySlice implements IGridView {
    private GridPresenter presenter;
    private GridView cellView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        cellView = (GridView) findComponentById(ResourceTable.Id_grid);
        presenter = new GridPresenter();
        presenter.attachView(this);
        cellView.loadColorJson();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void setScore(int score) {

    }

    @Override
    public void showGameOver() {

    }

    @Override
    public void refreshGrids(Cell[][] cells) {
        cellView.refreshGrids(cells);
    }

    @Override
    public void swipeGrids(Direction dir) {
        presenter.swipeGrids(dir);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
        presenter = null;
    }
}
