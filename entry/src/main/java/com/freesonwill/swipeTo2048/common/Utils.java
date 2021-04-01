package com.freesonwill.swipeTo2048.common;

import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;

import java.util.Optional;

public class Utils {

    public static Point getScreenSize(Context ctx) {
        Optional<Display> display = DisplayManager.getInstance().getDefaultDisplay(ctx);
        Point pt = new Point();
        display.get().getSize(pt);
        return pt;
    }



}
