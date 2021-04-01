package com.freesonwill.swipeTo2048.view.slice;

import com.freesonwill.swipeTo2048.ResourceTable;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.presenter.BoardPresenter;
import com.freesonwill.swipeTo2048.view.BoardView;
import com.freesonwill.swipeTo2048.view.base.IBoardView;
import com.freesonwill.swipeTo2048.view.base.IView;
import ohos.aafwk.ability.AbilityForm;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.OnClickListener;
import ohos.aafwk.ability.ViewsStatus;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;

public class MainAbilitySlice extends AbilitySlice implements IBoardView {
    private BoardPresenter presenter;
    private BoardView cellView;
    private Text scoreView;
    private Button resetView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        cellView = (BoardView) findComponentById(ResourceTable.Id_board);
        scoreView = (Text) findComponentById(ResourceTable.Id_score);
        resetView = (Button) findComponentById(ResourceTable.Id_reset);
        presenter = new BoardPresenter(cellView.getRow(), cellView.getColumn());
        presenter.attachView(this);
        cellView.setPresenter(presenter);
        cellView.loadColorJson();
        cellView.refreshBoard(presenter.getCells());
        resetView.setClickedListener(resetViewClick);
    }

    private Component.ClickedListener resetViewClick = new Component.ClickedListener() {
        @Override
        public void onClick(Component component) {
            presenter.resetGame();
        }
    };

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void showGameOver() {

    }

    @Override
    public void refreshBoard(Cell[][] cells) {
        cellView.refreshBoard(cells);
    }

    @Override
    public void swipeBoard(Direction dir) {
        cellView.swipeBoard(dir);
    }

    @Override
    public void setScore(int score) {
        scoreView.setText(String.format("总分数：%d", score));
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
        presenter = null;
    }
}
