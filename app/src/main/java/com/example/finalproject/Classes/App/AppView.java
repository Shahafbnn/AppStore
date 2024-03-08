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
import com.example.finalproject.Classes.UsefulFunctions;

public class AppView extends LinearLayout {
    private TextView textView;
    private ImageView imageView;
    private App app;
    // we save the listeners so we can reuse them when we update the app!
    private OnClickListener clickListener;
    private OnLongClickListener longClickListener;
    private Context context;
    public AppView(Context context, String text, Bitmap bitmap) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.context = context;
        setParams();

        imageView.setImageBitmap(bitmap);

        textView.setText(text);
    }
    public AppView(Context context, App app) {
        super(context);
        this.app = app;
        this.context = context;
        setParams();

        setData(app);
    }
    public AppView(Context context) {
        super(context);
        this.context = context;
        setParams();
    }
    public void setData(Context context, App app){
        StorageFunctions.setImage(context, imageView, app.getAppImagePath());

        textView.setText(app.getAppName());
        this.app = app;
    }

    public void setData(App app){
        setData(context, app);
    }



    private void setParams(){
        setOrientation(LinearLayout.VERTICAL);

        setPadding(25,25,25,25);

        int pixels = UsefulFunctions.dpToPixels(context, 100);
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
        this.clickListener = l;
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
