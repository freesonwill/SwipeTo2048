package com.freesonwill.swipeTo2048.view.slice;

import com.freesonwill.swipeTo2048.ResourceTable;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.presenter.BoardPresenter;
import com.freesonwill.swipeTo2048.view.BoardView;
import com.freesonwill.swipeTo2048.view.base.IBoardView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;

public class MainAbilitySlice extends AbilitySlice implements IBoardView {
    private BoardPresenter presenter;
    private BoardView cellView;
    private Text highestScoreView;
    private Text scoreView;
    private Button resetView;
    private Preferences preference;
    private boolean hasThumbsup = false;
    private static final String key_highestScore = "key_highestScore";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        cellView = (BoardView) findComponentById(ResourceTable.Id_board);
        highestScoreView = (Text) findComponentById(ResourceTable.Id_highest_score);
        scoreView = (Text) findComponentById(ResourceTable.Id_score);
        resetView = (Button) findComponentById(ResourceTable.Id_reset);
        presenter = new BoardPresenter(cellView.getRow(), cellView.getColumn());
        presenter.attachView(this);
        cellView.setPresenter(presenter);
        cellView.loadColorJson();
        cellView.refreshBoard(presenter.getCells());
        resetView.setClickedListener(resetViewClick);

        DatabaseHelper helper = new DatabaseHelper(this);
        preference = helper.getPreferences("preference2048");
        preferencesObserver.onChange(preference, key_highestScore);
        preference.registerObserver(preferencesObserver);
    }

    private Preferences.PreferencesObserver preferencesObserver = new Preferences.PreferencesObserver() {
        @Override
        public void onChange(Preferences preferences, String s) {
            if (key_highestScore.equals(s)) {
                int highestScore = preferences.getInt(s, 0);
                //new ToastDialog(getContext()).setText("highestScore："+highestScore).show();
                highestScoreView.setText(String.format("最高纪录:%d", highestScore));
            }
        }
    };

    @Override
    public void requestPermissionsFromUser(String[] permissions, int requestCode) {
        super.requestPermissionsFromUser(permissions, requestCode);
    }

    private Component.ClickedListener resetViewClick = new Component.ClickedListener() {
        @Override
        public void onClick(Component component) {
            saveHighestScore();
            presenter.resetGame();
            new ToastDialog(getContext()).setText("游戏已重置").show();
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
//        System.out.println("gameOver");
        resetView.invalidate();
        new ToastDialog(getContext()).setText("游戏结束，可点击重玩").show();
        saveHighestScore();
    }

    private void saveHighestScore() {
        int highestScore = preference.getInt(key_highestScore, 0);
        int score = presenter.getScore();
//        System.out.println(String.format("saveHighestScore-->%d,%d", score, highestScore));
//        System.out.printf("saveHighestScore2-->%d,%d", score, highestScore);
//        new ToastDialog(getContext()).setText(String.format("saveHighestScore-->%d,%d", score, highestScore)).show();
        if (highestScore < score) {
            preference.putInt(key_highestScore, score);
            preference.flush();
        }
    }

    @Override
    public void refreshBoard(Cell[][] cells) {
        cellView.refreshBoard(cells);
    }

    @Override
    public void setScore(int score) {
        scoreView.setText(String.format("总分数：%d", score));
        if (score >= 2048 && !hasThumbsup) {
            hasThumbsup = true;
            new ToastDialog(getContext()).setText("恭喜你，分数突破2048").show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveHighestScore();
        presenter.detachView();
        presenter = null;
        preference.unregisterObserver(preferencesObserver);
    }
}
