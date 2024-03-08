package com.example.finalproject.Classes;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class UsefulFunctions {
    public static int dpToPixels(Context context, int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    public static void setTextViewUnderlineAndColor(TextView tv, String start, String colored, int color){
        String full = start + colored;
        SpannableString spannableString = new SpannableString(full);

        setSpannableStringUnderline(spannableString);

        setSpannableStringColor(spannableString, start.length(), full.length(), color);

        tv.setText(spannableString);
    }
    public static void setSpannableStringUnderline(SpannableString spannableString){
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    public static void setSpannableStringColor(SpannableString spannableString, int start, int end, int color){
        // Color part of the string
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    public static void setTextViewUnderline(TextView tv, String str){
        SpannableString spannableString = new SpannableString(str);

        setSpannableStringUnderline(spannableString);

        tv.setText(spannableString);
    }
}
