

package com.lisners.counsellor.zWork.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.core.math.MathUtils;

import java.text.DecimalFormat;

public class NumberUtil {

    public static String decimal2PointFormat(double number){
        DecimalFormat df = new DecimalFormat("#.##");
        return  df.format(number);
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }


    public static int calculateGridSpanCount(Context context) {
        final int GRID_SPAN_COUNT_MIN = 1;
        final int GRID_SPAN_COUNT_MAX = 3;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int itemSize = dpToPx(context,120);
        //   itemSize = (int)(itemSize*0.75);
        int gridSpanCount = displayWidth / itemSize;
        return MathUtils.clamp(gridSpanCount, GRID_SPAN_COUNT_MIN, GRID_SPAN_COUNT_MAX);
    }
}
