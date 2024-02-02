package com.example.finalproject.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AppView extends LinearLayout {
    private TextView textView;
    private ImageView imageView;

    public AppView(Context context, String text, Bitmap bitmap) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (100 * scale + 0.5f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pixels, pixels);

        imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(layoutParams);
        addView(imageView);

        textView = new TextView(context);
        textView.setText(text);
        addView(textView);
    }
}
