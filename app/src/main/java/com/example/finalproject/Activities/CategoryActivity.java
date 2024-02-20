package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.INTENT_CATEGORIES_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.User.Validations.validateCategorySearch;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.Category.Categories;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.Classes.User.Validations;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.DatabaseClasses.CitiesArray;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollView svCategories;
    private AutoCompleteTextView actvSearchCategoryActivity;
    private Button btnSendDataCategoryActivity;
    private ArrayList<RecyclerView> categoriesRecyclerViews;
    private LinearLayout llCategories;
    private FirebaseFirestore db;
    private Categories categories;
    private LinearLayout linearLayout;
    // because we need to set the  ActivityResultLauncher<Intent>> before the onCreate or in it
    //since LifecycleOwner is attempting to register while current state is RESUMED. LifecycleOwners must call register before they are STARTED.
    private HashMap<String, ActivityResultLauncher<Intent>> activityResultLaunchers;
    private HashMap<String, CategoryView> categoryViewHashMap;
    private User curUser;
    private Boolean isUserSignedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = FirebaseFirestore.getInstance();

        llCategories = findViewById(R.id.llCategories);
        actvSearchCategoryActivity = findViewById(R.id.actvSearchCategoryActivity);



        btnSendDataCategoryActivity = findViewById(R.id.btnSendDataCategoryActivity);
        btnSendDataCategoryActivity.setOnClickListener(this);

        activityResultLaunchers = new HashMap<>();
        categoryViewHashMap = new HashMap<>();

        //getting the user categories the intent
        categories = (Categories) getIntent().getSerializableExtra(INTENT_CATEGORIES_KEY);
        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;

        svCategories = findViewById(R.id.svCategories); //ScrollView

        if(categories != null){
            ArrayList<String> cats = categories.getCategories();
            if(!cats.isEmpty()){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cats);
                actvSearchCategoryActivity.setAdapter(adapter);
            }
        }

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        svCategories.addView(linearLayout);

        createEverything();


        //createRandomApps();

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
        myNewApp = new App(specialFirstName, "Photos/0535622719050224-094546.0970.jpg", curUser, StorageFunctions.humanReadableByte(rand.nextInt(200000)), new ArrayList<String>(), (double) rand.nextInt(20), (double) rand.nextInt(100), category);
        db.collection("apps").add(myNewApp);
    }
    private void createRandomApps(){
        for(int i = 0; i < 10; i++){
            addRandomApp("Gaming");
        }
        for(int i = 0; i < 10; i++){
            addRandomApp("Fitness");
        }
        for(int i = 0; i < 10; i++){
            addRandomApp("Work");
        }
    }

    private void createEverything(){
        if(categories != null){
            ArrayList<String> arrayListCategories = categories.getCategories();

            for (String category : arrayListCategories) {
                ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if(result.getResultCode()== Activity.RESULT_OK){
                                    if(result.getData() != null){
                                        App intentApp = (App)result.getData().getSerializableExtra(INTENT_CURRENT_APP_KEY);
                                        if(intentApp != null){
                                            ArrayList<App> apps = categoryViewHashMap.get(category).getAppsArrayList();
                                            int index = getListIndexOf(apps, intentApp);
                                            apps.set(index, intentApp);

                                            categoryViewHashMap.get(category).getAppAdapter().notifyDataSetChanged();
                                        }
                                    }
                                }
                            }
                        }
                );
                activityResultLaunchers.put(category, activityResultLauncher);
            }

            //i'll add the user's suggested apps by their most downloaded categories later,
            // for now i'll do the first 3 categories.
            int size = arrayListCategories.size();

            for(int i = 0; i < 3 && i < size; i++){
                addCategory(arrayListCategories.get(i));
            }
        }

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
                    QuerySnapshot snapshots = task.getResult();
                    //int count = 0;
                    for (QueryDocumentSnapshot document : snapshots) {
                        //Log.v("debug", "document " + count + ": " + document.toString());
                        if(document.exists()){
                            app = document.toObject(App.class);
                            app.setAppId(document.getId());
                            apps.add(app);
                        }
                        //count++;
                    }
                    CategoryView categoryView = new CategoryView(getApplicationContext(), category, LinearLayout.VERTICAL, apps, activityResultLaunchers.get(category), curUser);
                    categoryViewHashMap.put(category, categoryView);
                    linearLayout.addView(categoryView);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==btnSendDataCategoryActivity){
            String text = actvSearchCategoryActivity.getText().toString();
            ValidationData validation = validateCategorySearch(text);

            if(validation.isValid()){
                ArrayList<String> cats = categories.getCategories();
                boolean exists = false;
                int size = cats.size();
                String curCat;
                for(int i = 0; i < size && !exists; i++){
                    curCat = cats.get(i);
                    if(curCat.equals(text)) exists = true;
                }
                if(exists){
                    if(!categoryViewHashMap.containsKey(text)){
                        addCategory(text);
                        Toast.makeText(this, "Adding category...", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(this, "Category already exists!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(this, "Category doesn't exist!", Toast.LENGTH_SHORT).show();

            }
            else{
                actvSearchCategoryActivity.setError(validation.getError());
            }
        }
    }


}