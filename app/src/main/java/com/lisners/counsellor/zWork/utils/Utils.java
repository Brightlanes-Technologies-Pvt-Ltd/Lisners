package com.lisners.counsellor.zWork.utils;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import okhttp3.RequestBody;
import okio.Buffer;

public class Utils {



    public static void setNestedScrollingEnabledFalse(final RecyclerView recyclerView) {
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                recyclerView.dispatchNestedFling(velocityX, velocityY, false);
                return false;
            }
        });
    }

    public static String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f %c",
                count / Math.pow(1000, exp),
                "kMGTPE".charAt(exp - 1));
    }







    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
