package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;
import com.example.finalproject.Classes.Category.Categories;
import com.example.finalproject.Classes.Category.CategoryView;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity {

    private ScrollView svCategories;
    private ArrayList<RecyclerView> categoriesRecyclerViews;
    private LinearLayout llCategories;
    private FirebaseFirestore db;
    private Categories categories;
    private LinearLayout linearLayout;
    private HashMap<String, CategoryView> categoryViewDictionary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = FirebaseFirestore.getInstance();

        llCategories = findViewById(R.id.llCategories);

        categoryViewDictionary = new HashMap<String, CategoryView>();


        svCategories = findViewById(R.id.svCategories); //ScrollView
//        AppView appView = new AppView(this, "joe", BitmapFactory.decodeResource(getResources(),
//                R.drawable.emptypfp));

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.addView(appView);
//        ArrayList<String> cats = new ArrayList<String>();
//        cats.add("Gaming");
//        cats.add("Fitness");
//        cats.add("Work");
//        db.collection("apps").document("categories").set(new Categories(cats));

        createEverything();

        svCategories.addView(linearLayout);

//        for(int i = 0; i < 3; i++) addRandomApp("Gaming");
//        for(int i = 0; i < 3; i++) addRandomApp("Fitness");
//        for(int i = 0; i < 3; i++) addRandomApp("Work");


    }
    private void addRandomApp(String category){
        App myNewApp;
        char[] nameStr = "abcdefghijklmnopqrstubwxyz".toCharArray();
        String specialFirstName = "";
        String specialLastName = "";

        Random rand = new Random();
        int num = rand.nextInt(7) + 3;
        for(int i = 0; i < num; i++){
            if(i == 0){
                specialFirstName += ("" + nameStr[rand.nextInt(nameStr.length)]).toUpperCase();
                specialLastName += ("" + nameStr[rand.nextInt(nameStr.length)]).toUpperCase();
            }
            specialFirstName += nameStr[rand.nextInt(nameStr.length)];
            specialLastName += nameStr[rand.nextInt(nameStr.length)];
        }
        myNewApp = new App(specialFirstName, "Photos/0535622719050224-094546.0970.jpg", specialLastName, rand.nextInt(20), "None", (double) rand.nextInt(20), (double) rand.nextInt(100), category);
        db.collection("apps").add(myNewApp);
    }

    private void createEverything(){

        // Get a list of all subcollections under the "categories" collection
        db.collection("apps").document("categories").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    categories = documentSnapshot.toObject(Categories.class);
                    ArrayList<String> arrayListCategories = categories.getCategories();

                    //i'll add the user's suggested apps by their most downloaded categories later,
                    // for now i'll do the first 3 categories.
                    int size = arrayListCategories.size();

                    for(int i = 0; i < 3 && i < size; i++){
                        addCategory(arrayListCategories.get(i));
                    }
                }
            }
        });


    }

    private int getListIndexOf(ArrayList<App> list, App app){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if(app.getAppId().equals(list.get(i).getAppId())) return i;
        }
        return -1;
    }

    private void addCategory(String category){
        // add .orderBy("appAvgRating") later
        db.collection("apps").whereEqualTo("appMainCategory", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<App> apps = new ArrayList<>();
                    App app;

                    ActivityResultLauncher<Intent> activityResultLauncher = null;
//                    activityResultLauncher = registerForActivityResult(
//                            new ActivityResultContracts.StartActivityForResult(),
//                            new ActivityResultCallback<ActivityResult>() {
//                                @Override
//                                public void onActivityResult(ActivityResult result) {
//                                    if(result.getResultCode()== Activity.RESULT_OK){
//                                        if(result.getData() != null){
//                                            App intentApp = (App)result.getData().getSerializableExtra(INTENT_CURRENT_APP_KEY);
//                                            if(intentApp != null){
//                                                int index = getListIndexOf(apps, intentApp);
//                                                apps.set(index, intentApp);
//
//                                                categoryViewDictionary.get(category).getAppAdapter().notifyDataSetChanged();
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                    );

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.exists()){
                            app = document.toObject(App.class);
                            app.setAppId(document.getId());
                            apps.add(app);
                        }
                    }
                    CategoryView categoryView = new CategoryView(getApplicationContext(), category, apps, activityResultLauncher);

                    categoryViewDictionary.put(category, categoryView);

                    linearLayout.addView(categoryView);
                }
            }
        });
    }
}