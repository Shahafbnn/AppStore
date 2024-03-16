package com.example.finalproject.Activities;

import static android.view.View.GONE;
import static com.example.finalproject.Classes.Constants.FIRESTORE_APP_REVIEWS_KEY;
import static com.example.finalproject.Classes.Constants.FIRESTORE_USER_CREATED_APPS_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_CREATOR_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Adapters.ReviewAdapter;
import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;
import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.Review;
import com.example.finalproject.Classes.SimpleListView;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDataActivity extends AppCompatActivity {

    private User curUser;
    private boolean isUserSignedIn;
    private User curAppCreator;
    private boolean isAppCreatorSignedIn;
    private LinearLayout llUserDataScrollView;
    private ImageView ivUserDataImage;
    private TextView tvUserDataName;
    private Button btnUserDataAllAppReviewsDialog, btnUserDataAllCurUserAppReviewsDialog;
    private HashMap<String,LinearLayout> stringLinearLayoutHashMap;
    private ArrayList<LinearLayout> scrollViewLinearLayouts;
    private FirebaseFirestore db;

    private ArrayList<Review> allAppReviews;
    private ReviewAdapter allAppReviewsAdapter;
    private Dialog allAppReviewsDialog;


    private ArrayList<Review> curUserAppReviews;
    private ReviewAdapter curUserAppReviewsAdapter;
    private Dialog curUserAppReviewsDialog;



//TODO: fix the fact that the data doesn't update when you add a review to an app in your reviews page for example when I go to one of the creator's apps, add a review, and return, then it doesn't show up once I click the Reviews Button!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        db = FirebaseFirestore.getInstance();

        btnUserDataAllAppReviewsDialog = findViewById(R.id.btnUserDataAllAppReviewsDialog);
        btnUserDataAllCurUserAppReviewsDialog = findViewById(R.id.btnUserDataAllCurUserAppReviewsDialog);

        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;

        curAppCreator = (User) getIntent().getSerializableExtra(INTENT_CURRENT_APP_CREATOR_KEY);
        isAppCreatorSignedIn = curAppCreator != null;

        llUserDataScrollView = findViewById(R.id.llUserDataScrollView);
        ivUserDataImage = findViewById(R.id.ivUserDataImage);
        tvUserDataName = findViewById(R.id.tvUserDataName);
        stringLinearLayoutHashMap = new HashMap<>();
        scrollViewLinearLayouts = new ArrayList<>();

        allAppReviews = new ArrayList<>();
        curUserAppReviews = new ArrayList<>();


        if(isAppCreatorSignedIn) {
            StorageFunctions.setImage(this, ivUserDataImage, curAppCreator.getUserImagePath());
            tvUserDataName.setText(curAppCreator.getFullNameAdmin());
            createScrollView("Uploaded Apps:", db.collection("users").document(curAppCreator.getUserId()).collection(FIRESTORE_USER_CREATED_APPS_KEY));
            createReviews();
            fetchReviewsFromFireStore();
            btnUserDataAllAppReviewsDialog.setOnClickListener(v -> allAppReviewsDialog.show());
        }
        else {
            Toast.makeText(this, "Please try again!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void createReviews(){
        //reviews dialog
        allAppReviewsDialog = new Dialog(this);
        allAppReviewsAdapter = new ReviewAdapter(this, allAppReviews);
        SimpleListView allAppReviewsListView = new SimpleListView(this, allAppReviewsDialog, allAppReviewsAdapter, null);
        allAppReviewsDialog.setContentView(allAppReviewsListView);


        if(isUserSignedIn){
            //reviews dialog
            curUserAppReviewsDialog = new Dialog(this);
            curUserAppReviewsAdapter = new ReviewAdapter(this, curUserAppReviews);
            SimpleListView curUserAppReviewsListView = new SimpleListView(this, curUserAppReviewsDialog, curUserAppReviewsAdapter, null);
            allAppReviewsDialog.setContentView(curUserAppReviewsListView);

        }
        else btnUserDataAllCurUserAppReviewsDialog.setVisibility(GONE);
    }
    private void fetchReviewsFromFireStore(){
        db.collection("users").document(curAppCreator.getUserId()).collection(FIRESTORE_APP_REVIEWS_KEY).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Review review;
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    review = documentSnapshot.toObject(Review.class);
                    review.setReviewId(documentSnapshot.getId());
                    //adding the signed in user's reviews separately for a later feature
                    if(isUserSignedIn && review.getReviewReviewer().getUserId().equals(curUser.getUserId())) curUserAppReviews.add(review);
                    allAppReviews.add(review);
                }
                allAppReviewsAdapter.notifyDataSetChanged();
                if(isUserSignedIn) curUserAppReviewsAdapter.notifyDataSetChanged();
            } else {
                Log.d("debug", "Error getting documents: ", task.getException());
            }
        });
    }

    private void createScrollView(String name, Query query){
        final ActivityResultLauncher<Intent> arlScrollViews = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //for changes done to apps in ChosenAppActivity and UploadAppActivity
                        //handle returned data
                        if(result.getResultCode()== Activity.RESULT_OK){
                            if(result.getData() != null){
                                //get the returned app from UploadAppActivity
                                String key = result.getData().getStringExtra(Constants.INTENT_SCROLL_VIEW_KEY);
                                LinearLayout returnedLayout = stringLinearLayoutHashMap.get(key);
                                App app = (App)result.getData().getSerializableExtra(INTENT_CURRENT_APP_KEY);
                                if(app!=null){
                                    boolean found = false;
                                    //if the array is null we fetch it.

                                    //we find the app in createdAppsArrayList, if it's there we update it, else we add it
                                    for (int i = 0; i < returnedLayout.getChildCount() && !found; i++) {
                                        if (((AppView) returnedLayout.getChildAt(i)).getApp().getAppId().equals(app.getAppId())) {
                                            returnedLayout.addView(new AppView(getApplicationContext(), app), i);
                                            found = true;
                                        }
                                    }
                                    if (!found) {
                                        returnedLayout.addView(new AppView(getApplicationContext(), app));
                                    }
                                }

                                //reload activity if isMenuPrepared is true;
                                // needs to be conditional
                                // because what if there are delays on the onCreate get fetch?
                                //conditionalReloadActivity();
                            }
                        }
                    }
                }
        );

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    App app;
                    AppView appView;
                    if(task.getResult().size() > 0){
                        HorizontalScrollView scrollView = new HorizontalScrollView(getApplicationContext());
                        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                        scrollView.addView(linearLayout);
                        stringLinearLayoutHashMap.put(name, linearLayout);
                        scrollViewLinearLayouts.add(linearLayout);

                        TextView tvName = new TextView(getApplicationContext());
                        tvName.setText(name);

                        llUserDataScrollView.addView(tvName);
                        llUserDataScrollView.addView(scrollView);

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            app = documentSnapshot.toObject(App.class);
                            app.setAppId(documentSnapshot.getId());
                            appView = new AppView(getApplicationContext(), app);
                            appView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    AppView view = (AppView)v;
                                    linearLayout.removeView(view);
                                    Toast.makeText(getApplicationContext(), "Removed " + view.getApp().getAppName(), Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            });
                            appView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ChosenAppActivity.class);
                                    intent.putExtra(Constants.INTENT_CURRENT_APP_KEY, ((AppView)v).getApp());
                                    intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curUser);
                                    intent.putExtra(Constants.INTENT_SCROLL_VIEW_KEY, name);

                                    arlScrollViews.launch(intent);
                                }
                            });
                            linearLayout.addView(appView);
                        }
                    }
                }
            }
        });

    }
}