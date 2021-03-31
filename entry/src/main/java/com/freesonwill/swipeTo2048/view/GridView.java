package com.freesonwill.swipeTo2048.view;

import com.freesonwill.swipeTo2048.common.Utils;
import com.freesonwill.swipeTo2048.model.Cell;
import com.freesonwill.swipeTo2048.model.Direction;
import com.freesonwill.swipeTo2048.view.base.IGridView;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.zson.ZSONReader;
import ohos.utils.zson.ZSONType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class GridView extends Component implements Component.DrawTask
        , Component.TouchEventListener, Component.EstimateSizeListener {
    private int column = 1, row = 1;
    private MmiPoint downPosition, upPostion;
    private IGridView delegate;
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "GridView");

    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        if (attrSet.getAttr("column").isPresent())
            column = attrSet.getAttr("column").get().getIntegerValue();
        if (attrSet.getAttr("row").isPresent())
            row = attrSet.getAttr("row").get().getIntegerValue();
        setEstimateSizeListener(this);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        HiLog.debug(LABEL, "");

    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (touchEvent.getPointerCount() > 1) return false;
        switch (touchEvent.getAction()) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                downPosition = touchEvent.getPointerScreenPosition(0);
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                MmiPoint upPosition = touchEvent.getPointerPosition(0);
                float moveX = upPosition.getX() - downPosition.getX();
                float moveY = upPosition.getY() - downPosition.getY();
                if (Math.abs(moveX) > Math.abs(moveY)) {
                    delegate.swipeGrids(moveX < 0 ? Direction.LEFT : Direction.RIGHT);
                } else {
                    delegate.swipeGrids(moveY < 0 ? Direction.UP : Direction.DOWN);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onEstimateSize(int widthEstimateConfig, int heightEstimateConfig) {
        Point size = Utils.getScreenSize(getContext());
        float cellSizeX = (size.getPointX() - getMarginLeft() - getMarginRight() - getPaddingLeft() - getPaddingRight()) / column;
        float cellSizeY = (size.getPointY() - getMarginTop() - getMarginBottom() - getPaddingTop() - getPaddingBottom()) / row;
        float cellSize = Math.min(cellSizeX, cellSizeY);
        int w = (int) (cellSize * column + getPaddingLeft() + getPaddingRight());
        int h = (int) (cellSize * row + getPaddingTop() + getPaddingBottom());

        int wideMode = EstimateSpec.getMode(widthEstimateConfig);
        int wideSize = EstimateSpec.getSize(widthEstimateConfig);
        int heightMode = EstimateSpec.getMode(heightEstimateConfig);
        int heightSize = EstimateSpec.getSize(heightEstimateConfig);
        setEstimatedSize(
                EstimateSpec.getSizeWithMode(w, EstimateSpec.PRECISE),
                EstimateSpec.getSizeWithMode(h, EstimateSpec.PRECISE));
        System.out.println(String.format("onEstimateSize:%d,%d,%d,%d", wideMode, heightMode, wideSize, heightSize));
        return true;
    }

    public void refreshGrids(Cell[][] cells) {
        invalidate();
    }

    public void loadColorJson() {
        ResourceManager resourceManager = getContext().getResourceManager();
        RawFileEntry rawFileEntry = resourceManager.getRawFileEntry("resources/rawfile/cell_color.json");
        try (Resource resource = rawFileEntry.openRawFile()) {
            try (ZSONReader r = new ZSONReader(new InputStreamReader(resource))) {
                Map<String, Object> map = new HashMap<>();
                ZSONType type = r.peek();
                System.out.println("type-->" + type);
                switch (type){
                    case NAME:
                        System.out.println("NAME-->" + r.readName());
                        break;
                    case STRING:
                        System.out.println("STRING-->" + r.readString());
                        break;
                    case OBJECT:
                        System.out.println("OBJECT-->");
                        break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
