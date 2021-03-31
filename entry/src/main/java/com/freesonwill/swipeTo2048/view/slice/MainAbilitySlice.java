package com.freesonwill.swipeTo2048.view.slice;

import com.freesonwill.swipeTo2048.ResourceTable;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.presenter.GridPresenter;
import com.freesonwill.swipeTo2048.view.GridView;
import com.freesonwill.swipeTo2048.view.base.IGridView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class MainAbilitySlice extends AbilitySlice {
    private GridPresenter presenter;
    private GridView cellView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        presenter = new GridPresenter(cellView.getRow(), cellView.getColumn());
        cellView = (GridView) findComponentById(ResourceTable.Id_grid);
        cellView.setPresenter(presenter);
        cellView.loadColorJson();
        cellView.refreshGrids(presenter.getCells());
        presenter.attachView(cellView);
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
    protected void onStop() {
        super.onStop();
        presenter.detachView();
        presenter = null;
    }
}
