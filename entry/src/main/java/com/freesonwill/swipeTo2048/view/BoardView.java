
package com.freesonwill.swipeTo2048.view;

import com.freesonwill.swipeTo2048.common.Utils;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.presenter.BoardPresenter;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentParent;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.DimensFloat;
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

public class BoardView extends Component implements
        Component.TouchEventListener
        , Component.EstimateSizeListener {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201,
            "BoardView");
    private int column = 5, row = 5;
    private float cellSpacing = 0;
    private MmiPoint downPosition, upPosition;
    private Map<String, Object> colorMaps;
    private MyDrawTask drawTask;
    private BoardPresenter presenter;

    public void setPresenter(BoardPresenter presenter) {
        this.presenter = presenter;
    }


    public BoardView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        if (attrSet.getAttr("column").isPresent())
            column = attrSet.getAttr("column").get().getIntegerValue();
        if (attrSet.getAttr("row").isPresent())
            row = attrSet.getAttr("row").get().getIntegerValue();
        if (attrSet.getAttr("cellSpacing").isPresent()) {
            cellSpacing = attrSet.getAttr("cellSpacing").get().getDimensionValue();
        }
        drawTask = new MyDrawTask();
        addDrawTask(drawTask);
        setEstimateSizeListener(this);
        setTouchEventListener(this);

        Point size = Utils.getScreenSize(getContext());
        float W = (size.getPointX() - getMarginLeft() - getMarginRight() - getPaddingLeft() - getPaddingRight());
        float H = (size.getPointY() - 350 * 2 - getMarginTop() - getMarginBottom() - getPaddingTop() - getPaddingBottom());
        int cellSize = (int) Math.min(W / column, H / row);
        row = (int) (H / cellSize);
        column = (int) (W / cellSize);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (touchEvent.getPointerCount() > 1) return true;
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                downPosition = touchEvent.getPointerPosition(0);
                break;
            case TouchEvent.CANCEL:
            case TouchEvent.PRIMARY_POINT_UP:
                upPosition = touchEvent.getPointerPosition(0);
                float moveX = upPosition.getX() - downPosition.getX();
                float moveY = upPosition.getY() - downPosition.getY();
                System.out.println(String.format("you just click,%f,%f,%d", moveX, moveY,
                        touchEvent.getAction()));
                if (Math.abs(moveX) < 10 && Math.abs(moveY) < 10) {
                    return true;
                }
                if (Math.abs(moveX) > Math.abs(moveY)) {
                    presenter.swipeBoard(moveX < 0 ? Direction.LEFT : Direction.RIGHT);
                } else {
                    presenter.swipeBoard(moveY < 0 ? Direction.UP : Direction.DOWN);
                }
                break;
        }

        return true;
    }

    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        if (getComponentParent() == null) return false;
        Point size = Utils.getScreenSize(getContext());
        float W = (size.getPointX() - getMarginLeft() - getMarginRight() - getPaddingLeft() - getPaddingRight());
        float H = (size.getPointY() - 350 * 2 - getMarginTop() - getMarginBottom() - getPaddingTop() - getPaddingBottom());
        int cellSize = (int) Math.min(W / column, H / row);
        int w2 = cellSize * column;
        int h2 = cellSize * row;
        setEstimatedSize(
                EstimateSpec.getSizeWithMode(w2, EstimateSpec.NOT_EXCEED),
                EstimateSpec.getSizeWithMode(h2, EstimateSpec.NOT_EXCEED));
        return true;
    }


    public void refreshBoard(Cell[][] cells) {
        //System.out.println("refreshBoard-->cells");
        drawTask.cells = cells;
        invalidate();
    }

    private class MyDrawTask implements DrawTask {
        private Cell[][] cells;
        private Paint paint = new Paint();

        @Override
        public void onDraw(Component component, Canvas canvas) {
            Cell[][] cells = this.cells;
            int w = component.getWidth();
            int h = component.getHeight();
            int paddingT = component.getPaddingTop();
            int paddingB = component.getPaddingBottom();
            int paddingL = component.getPaddingLeft();
            int paddingR = component.getPaddingRight();
            float cellW = 1f * (w - paddingL - paddingR - cellSpacing * (column - 1)) / column;
            float x, y;
            System.out.println(String.format("w-->%d,%d,%d,%d,%d,%d", w, h, paddingT, paddingB, paddingL, paddingR));
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    Cell cell = cells[i][j];
                    x = paddingL + cellW * j + cellSpacing * j;
                    y = paddingT + cellW * i + cellSpacing * i;
                    paint.reset();
                    paint.setStyle(Paint.Style.FILL_STYLE);
                    String txt = String.valueOf(cell.getValue());
                    Color color = getNumColor(presenter.isGameOver(), txt);
                    paint.setColor(color);
                    canvas.drawRect(x, y, x + cellW, y + cellW, paint);
                    if (cell.getValue() != 0) {
                        paint.reset();
                        paint.setTextSize((int) (cellW * 0.6f));
                        Color color2;
                        if (txt.equals("2") || txt.equals("4")) {
                            color2 = getNumColor(presenter.isGameOver(), "2or4");
                        } else {
                            color2 = getNumColor(presenter.isGameOver(), "others");
                        }
                        paint.setColor(color2);
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
        //System.out.println("colorStr-->" + colorStr);
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