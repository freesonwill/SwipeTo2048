package com.freesonwill.swipeTo2048.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

public class GridView extends Component implements Component.DrawTask, Component.TouchEventListener {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "GridView");

    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);

    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        HiLog.debug(LABEL,"");

    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        return false;
    }
}
