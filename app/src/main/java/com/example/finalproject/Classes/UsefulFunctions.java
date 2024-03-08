package com.example.finalproject.Classes;

import android.content.Context;

public class UsefulFunctions {
    public static int dpToPixels(Context context, int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
