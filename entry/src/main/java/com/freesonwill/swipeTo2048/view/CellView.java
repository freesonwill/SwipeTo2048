package com.freesonwill.swipeTo2048.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

public class CellView extends Component implements Component.DrawTask, Component.TouchEventListener {

    public CellView(Context context) {
        super(context);
    }

    public CellView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public CellView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    public CellView(Context context, AttrSet attrSet, int resId) {
        super(context, attrSet, resId);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {

        return false;
    }


}
