package com.mecury.circlelibrary;

import android.content.res.Resources;

/**
 * Created by 海飞 on 2016/7/13.
 */
public class Utils {

    public static float dp2px(Resources resources, float dp){
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().density;
        return sp * scale;
    }
}
