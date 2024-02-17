package com.example.finalproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Adapters.AppAdapter;
import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;
import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.User.User;


import java.util.ArrayList;

public class CategoryView extends LinearLayout {
    private String categoryName;
    private ArrayList<App> appsArrayList;
    private RecyclerView appsRecyclerView;
    private AppAdapter appAdapter;
    private Context context;
    // This ActivityResultLauncher is used to handle the result from the Activity.
    // It retrieves the App object from the returned Intent and updates the current RecyclerView.
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private User curUser;
    private Boolean isUserSignedIn;

    public CategoryView(Context context, String categoryName, ArrayList<App> appsArrayList, ActivityResultLauncher<Intent> activityResultLauncher, User curUser) {
        super(context);
        this.categoryName = categoryName;
        this.appsArrayList = appsArrayList;
        this.appsRecyclerView = new RecyclerView(context);
        this.context = context;
        this.curUser = curUser;
        this.appAdapter = new AppAdapter(context, appsArrayList, activityResultLauncher, curUser);


        setAdapter();

        this.activityResultLauncher = activityResultLauncher;

        TextView tvCategory = new TextView(context);
        tvCategory.setText(categoryName);
        setOrientation(LinearLayout.VERTICAL);
        addView(tvCategory);

        addView(appsRecyclerView);

    }

    private void setAdapter(){
        // set up the RecyclerView
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        appsRecyclerView.setLayoutManager(layoutManager);

        appsRecyclerView.setAdapter(appAdapter);
    }


    public String getCategoryName() {
        return categoryName;
    }

    public ArrayList<App> getAppsArrayList() {
        return appsArrayList;
    }


    public RecyclerView getAppsRecyclerView() {
        return appsRecyclerView;
    }


    public AppAdapter getAppAdapter() {
        return appAdapter;
    }
}
