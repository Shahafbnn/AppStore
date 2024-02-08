package com.example.finalproject.Classes.Category;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Activities.CategoryActivity;
import com.example.finalproject.Adapters.AppAdapter;
import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;
import com.example.finalproject.Classes.Constants;

import java.util.ArrayList;

public class CategoryView extends LinearLayout implements AdapterView.OnItemClickListener{
    private String categoryName;
    private ArrayList<App> appsArrayList;
    private RecyclerView appsRecyclerView;
    private AppAdapter appAdapter;
    private Context context;
    // This ActivityResultLauncher is used to handle the result from the Activity.
    // It retrieves the App object from the returned Intent and updates the current RecyclerView.
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public CategoryView(Context context, String categoryName, ArrayList<App> appsArrayList, ActivityResultLauncher<Intent> activityResultLauncher) {
        super(context);
        this.categoryName = categoryName;
        this.appsArrayList = appsArrayList;
        this.appsRecyclerView = new RecyclerView(context);
        this.appAdapter = new AppAdapter(context, appsArrayList);
        this.context = context;

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        App app = ((AppView) view).getApp();
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(Constants.INTENT_CURRENT_APP_KEY, app);
        activityResultLauncher.launch(intent);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<App> getAppsArrayList() {
        return appsArrayList;
    }

    public void setAppsArrayList(ArrayList<App> appsArrayList) {
        this.appsArrayList = appsArrayList;
    }

    public RecyclerView getAppsRecyclerView() {
        return appsRecyclerView;
    }

    public void setAppsRecyclerView(RecyclerView appsRecyclerView) {
        this.appsRecyclerView = appsRecyclerView;
    }

    public AppAdapter getAppAdapter() {
        return appAdapter;
    }

    public void setAppAdapter(AppAdapter appAdapter) {
        this.appAdapter = appAdapter;
    }
}
