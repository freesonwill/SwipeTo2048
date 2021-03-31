package com.freesonwill.swipeTo2048.view;

import com.freesonwill.swipeTo2048.common.Utils;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.presenter.GridPresenter;
import com.freesonwill.swipeTo2048.view.base.IGridView;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.Revocable;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.zson.ZSONReader;

import java.io.*;
import java.util.Map;

public class GridView extends Component implements
        Component.TouchEventListener
        , Component.EstimateSizeListener,
        IGridView {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "GridView");
    private int column = 5, row = 5;
    private int cellSpacing = 5;
    private MmiPoint downPosition, upPosition;
    private Map<String, Object> colorMaps;
    private MyDrawTask drawTask;
    private GridPresenter presenter;

    public void setPresenter(GridPresenter presenter) {
        this.presenter = presenter;
    }


    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        if (attrSet.getAttr("column").isPresent())
            column = attrSet.getAttr("column").get().getIntegerValue();
        if (attrSet.getAttr("row").isPresent())
            row = attrSet.getAttr("row").get().getIntegerValue();
        setEstimateSizeListener(this);
        setTouchEventListener(this);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }


    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (touchEvent.getPointerCount() > 1) return false;
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                downPosition = touchEvent.getPointerScreenPosition(0);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                upPosition = touchEvent.getPointerPosition(0);
                float moveX = upPosition.getX() - downPosition.getX();
                float moveY = upPosition.getY() - downPosition.getY();
                if (Math.abs(moveX) > Math.abs(moveY)) {
                    presenter.swipeGrids(moveX < 0 ? Direction.LEFT : Direction.RIGHT);
                } else {
                    presenter.swipeGrids(moveY < 0 ? Direction.UP : Direction.DOWN);
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        Point size = Utils.getScreenSize(getContext());
        float W = (size.getPointX() - getMarginLeft() - getMarginRight() - getPaddingLeft() - getPaddingRight());
        float H = (size.getPointY() - getMarginTop() - getMarginBottom() - getPaddingTop() - getPaddingBottom());
        int cellSize = (int) Math.min(W, H);
        setEstimatedSize(
                EstimateSpec.getSizeWithMode(cellSize, EstimateSpec.PRECISE),
                EstimateSpec.getSizeWithMode(cellSize, EstimateSpec.PRECISE));
        return true;
    }

    @Override
    public void setScore(int score) {

    }

    @Override
    public void showGameOver() {

    }

    public void refreshGrids(Cell[][] cells) {
        System.out.println("refreshGrids-->cells");
        if (drawTask == null) {
            drawTask = new MyDrawTask();
            addDrawTask(drawTask);
        }
        drawTask.cells = cells;
        invalidate();
    }

    @Override
    public void swipeGrids(Direction dir) {

    }

    private class MyDrawTask implements DrawTask {
        private Cell[][] cells;
        private Paint paint = new Paint();

        @Override
        public void onDraw(Component component, Canvas canvas) {
            Cell[][] cells = this.cells;
            int w = component.getWidth();
            int paddingT = component.getPaddingTop();
            int paddingB = component.getPaddingBottom();
            int paddingL = component.getPaddingLeft();
            int paddingR = component.getPaddingRight();
            float cellW = 1f * (w - paddingL - paddingR - cellSpacing * (column - 1)) / column;
            float x = paddingL, y = paddingT;
            System.out.println("cellW-->" + cellW);
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    Cell cell = cells[i][j];
                    x = paddingL + cellW * i + cellSpacing * i;
                    y = paddingT + cellW * j + cellSpacing * j;
                    paint.reset();
                    paint.setColor(Color.GREEN);
                    canvas.drawRect(x, y, x + cellW, y + cellW, paint);
                    if (cell.getValue() != 0) {
                        paint.reset();
                        paint.setTextSize((int) (cellW * 0.6f));
                        String txt = String.valueOf(cell.getValue());
                        paint.setColor(getNumColor(presenter.isGameOver(), txt));
                        System.out.println("Color-->" + cell.getValue());
                        Rect rect = paint.getTextBounds(txt);
                        int[] wh = rect.getRectSize();
                        canvas.drawText(paint,
                                txt,
                                x + cellW / 2f - wh[0] / 2,
                                y + cellW / 2f + wh[1] / 2);
                    }
                }
            }
        }
    }

    private Color getNumColor(boolean isGameOver, String txt) {
        Map<String, String> map = (Map<String, String>) colorMaps.get(!isGameOver ? "normal" : "faded");
        String colorStr = map.get(txt);
        return new Color(Color.getIntColor(colorStr));
    }

    public void loadColorJson() {
        TaskDispatcher dispatcher = getContext().getGlobalTaskDispatcher(TaskPriority.DEFAULT);
        Revocable revocable = dispatcher.asyncDispatch(new Runnable() {
            @Override
            public void run() {
                ResourceManager resourceManager = getContext().getResourceManager();
                RawFileEntry rawFileEntry = resourceManager.getRawFileEntry("resources/rawfile/cell_color.json");
                try (Resource resource = rawFileEntry.openRawFile()) {
                    try (ZSONReader r = new ZSONReader(new InputStreamReader(resource))) {
                        colorMaps = r.read(Map.class, true);
                        //System.out.println("colorMaps-->" + colorMaps);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //System.out.println("colorMaps-->");
    }

}
