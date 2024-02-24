package com.example.finalproject.Classes.App;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.StorageFunctions;

public class AppView extends LinearLayout {
    private TextView textView;
    private ImageView imageView;
    private App app;
    private OnClickListener clickListener;
    private OnLongClickListener longClickListener;
    public AppView(Context context, String text, Bitmap bitmap) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        setParams(context);

        imageView.setImageBitmap(bitmap);

        textView.setText(text);
    }
    public AppView(Context context, App app) {
        super(context);
        this.app = app;
        setParams(context);

        setData(context, app);
    }
    public AppView(Context context) {
        super(context);
        setParams(context);
    }
    public void setData(Context context, App app){
        StorageFunctions.setImage(context, imageView, app.getAppImagePath());

        textView.setText(app.getAppName());
        this.app = app;
    }



    private void setParams(Context context){
        setOrientation(LinearLayout.VERTICAL);

        setPadding(25,25,25,25);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (100 * scale + 0.5f);
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(pixels, pixels);

        imageView = new ImageView(context);
        imageView.setLayoutParams(imageViewLayoutParams);
        addView(imageView);

        textView = new TextView(context);
        textView.setGravity(CENTER);
        addView(textView);
        setGravity(CENTER);

    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        this.clickListener = l; // Add this line
    }

    public OnClickListener getOnClickListener() { // Add this method
        return clickListener;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        super.setOnLongClickListener(l);
        this.longClickListener = l; // Add this line
    }

    public OnLongClickListener getOnLongClickListener() { // Add this method
        return longClickListener;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
