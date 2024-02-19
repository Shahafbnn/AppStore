package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.StorageFunctions.getUriFromImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.Review;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ChosenAppActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAppName, tvAppCreator, tvAppDownloads, tvAppSize, tvAppPerms;
    private ImageView ivAppImage;
    private ImageButton ibAppDownload, ibAppShare;
    private Button btnAppSendUserReview;
    private ScrollView svAppPerms, svAppReviews;
    private RatingBar rbAppAvgRating, rbAppUserRating;
    private EditText etAppReview;
    private FirebaseFirestore db;

    private LinearLayout llAppPerms, llReviews, llAppReviews;
    private App curApp;
    private User curUser;
    private boolean isUserSignedIn;
    private boolean isAppDownloadedByUser;
    private ArrayList<Review> appReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_app);

        db = FirebaseFirestore.getInstance();

        //getting the app from the intent
        curApp = (App) getIntent().getSerializableExtra(INTENT_CURRENT_APP_KEY);
        if(curApp == null) {
            Toast.makeText(this, "null app was sent, try again!", Toast.LENGTH_LONG).show();
            finishActivity(Activity.RESULT_CANCELED);
        }

        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;

        initViews();
        initAppDataFromCurApp();


    }

    private void initViews(){
        tvAppName = (TextView) findViewById(R.id.tvAppName);
        tvAppCreator = (TextView) findViewById(R.id.tvAppCreator);
        tvAppCreator.setOnClickListener(this);
        tvAppDownloads = (TextView) findViewById(R.id.tvAppDownloads);
        tvAppSize = (TextView) findViewById(R.id.tvAppSize);
        tvAppPerms = (TextView) findViewById(R.id.tvAppPerms);
        tvAppPerms.setOnClickListener(this);

        ivAppImage = (ImageView) findViewById(R.id.ivAppImage);

        ibAppDownload = (ImageButton) findViewById(R.id.ibAppDownload);
        ibAppDownload.setOnClickListener(this);
        ibAppShare = (ImageButton) findViewById(R.id.ibAppShare);
        ibAppShare.setOnClickListener(this);

        btnAppSendUserReview = (Button) findViewById(R.id.btnAppSendUserReview);
        btnAppSendUserReview.setOnClickListener(this);

        svAppPerms = (ScrollView) findViewById(R.id.svAppPerms);
        svAppReviews = (ScrollView) findViewById(R.id.svAppReviews);

        llAppReviews = findViewById(R.id.llAppReviews);
        svAppReviews.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                int scrollHeight = svAppReviews.getChildAt(0).getHeight();
                float ratio = (float) scrollY / (scrollHeight - screenHeight);

                ViewGroup.LayoutParams params = llAppReviews.getLayoutParams();
                params.height = (int) (screenHeight * ratio);
                llAppReviews.setLayoutParams(params);
            }
        });

        rbAppAvgRating = (RatingBar) findViewById(R.id.rbAppAvgRating);
        //so it won't be clickable!
        rbAppAvgRating.setOnTouchListener((v, event) -> true);
        rbAppUserRating = (RatingBar) findViewById(R.id.rbAppUserRating);

        etAppReview = (EditText) findViewById(R.id.etAppReview);

        llAppPerms = findViewById(R.id.llAppPerms);
        llReviews = findViewById(R.id.llReviews);
        if(!isUserSignedIn){
            llReviews.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initAppDataFromCurApp(){
        tvAppName.setText(curApp.getAppName());
        tvAppCreator.setText("Created by: " + curApp.getAppCreator().getFullNameAdmin());
        tvAppSize.setText(curApp.getAppSize() + " MB in size");
        StorageFunctions.setImage(this, ivAppImage, curApp.getAppImagePath());

        // add perms, getAppDownloads  later!
        db.collection("apps").document(curApp.getAppId()).collection("reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    appReviews = new ArrayList<>();
                    Review review;

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.exists()){
                            review = document.toObject(Review.class);
                            review.setReviewId(document.getId());
                            appReviews.add(review);
                        }
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==tvAppCreator){}
        else if(v==tvAppPerms){
            if(svAppPerms.getVisibility() == View.GONE){
                svAppPerms.setVisibility(View.VISIBLE);
                tvAppPerms.setText("v Perms:");
            }
            else {
                svAppPerms.setVisibility(View.GONE);
                tvAppPerms.setText("> Perms:");
            }
        }
        else if(v==ibAppDownload){
            if(isUserSignedIn){

            }
            else Toast.makeText(this, "You must be signed in to download!", Toast.LENGTH_LONG).show();

        }
        else if(v==ibAppShare){
            shareApp();



        }
        else if(v==btnAppSendUserReview){}
    }

    private boolean shareApp(){ //doesn't display the image nor the title
        Uri uri = getUriFromImageView(this, ivAppImage, curApp.getAppId() + "" + new SimpleDateFormat("ddMyy-HHmmss").format(new Date()));

        if (uri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file

            // Add the text to the intent
            String shareText = "Your Text Here";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            // Create a new ClipData.Item from the URI
            ClipData.Item item = new ClipData.Item(uri);

            // Create a new ClipData using the item
            String[] mimeTypes = {getContentResolver().getType(uri), "text/plain"};
            ClipData clipData = new ClipData(new ClipDescription("Search for the " + curApp.getAppName() + " App on App Store!", mimeTypes), item);

            // Set the ClipData
            shareIntent.setClipData(clipData);

            // Set the type
            shareIntent.setType("*/*");

            // Start the activity
            startActivity(Intent.createChooser(shareIntent, null));
            return true;
        }
        return false;

    }
}