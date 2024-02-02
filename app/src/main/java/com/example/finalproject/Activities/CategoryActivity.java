package com.example.finalproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.finalproject.Classes.App;
import com.example.finalproject.Classes.AppView;
import com.example.finalproject.Classes.Constants;
import com.example.finalproject.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private ScrollView svCategories;
    private ArrayList<RecyclerView> categoriesRecyclerViews;
    private LinearLayout llCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        llCategories = findViewById(R.id.llCategories);

        svCategories = findViewById(R.id.svCategories); //ScrollView
        AppView appView = new AppView(this, "joe", BitmapFactory.decodeResource(getResources(),
                R.drawable.emptypfp));

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(appView);
        //llCategories.addView(appView);
        //llCategories.addView(tv);
        TextView tv;
        for (int  i = 0; i < 100; i++){
            tv = new TextView(this);
            tv.setText("hello" + i);
            ll.addView(tv);

        }
        svCategories.addView(ll);
        //hsvCategories.addView(appView);
    }

    private void createEverything(){
        categoriesRecyclerViews = new ArrayList<RecyclerView>();
        for (String catName: Constants.APPLICATION_CATEGORIES) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);





            RecyclerView myList = new RecyclerView(this);
            myList.setLayoutManager(layoutManager);

            categoriesRecyclerViews.add(myList);
        }
    }

    private void createItemApp(ScrollView scrollView, App apps){
    }
}