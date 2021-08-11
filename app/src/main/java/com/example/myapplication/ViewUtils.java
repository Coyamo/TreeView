package com.example.myapplication;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {
    
    public static float dp2px(Context ctx, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }
    
}
