package com.example.finalproject.Activities;

import static android.view.View.GONE;
import static com.example.finalproject.Classes.Constants.FIRESTORE_APP_REVIEWS_KEY;
import static com.example.finalproject.Classes.Constants.FIRESTORE_USER_CREATED_APPS_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_ACTIVITY_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CHOSEN_APP_ACTIVITY_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.StorageFunctions.getUriFromImageView;

import static java.security.AccessController.getContext;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Adapters.ReviewAdapter;
import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.PermissionChoiceView;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.Review;
import com.example.finalproject.Classes.SimpleListView;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.Classes.User.Validations;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ChosenAppActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAppName, tvAppCreator, tvAppDownloads, tvAppSize, tvAppPerms, tvAppMainCategory, tvAppFullPrice;
    private ImageView ivAppImage;
    private ImageButton ibAppDownload, ibAppShare;
    private Button btnAppSendUserReview, btnViewReviews;
    private RatingBar rbAppAvgRating, rbAppUserRating;
    private EditText etAppReview;
    private FirebaseFirestore db;

    private LinearLayout llReviews;
    private App curApp;
    private User curUser;
    private boolean isUserSignedIn;
    private ArrayList<Review> appReviews;
    private ArrayList<Review> appCurUserReviews;
    private ReviewAdapter appReviewsAdapter;

    private ListView lvAppReviews;

    private Dialog permsDialog;
    private PermissionChoiceView permsDialogChoiceView;
    private boolean isAppFree;
    private Boolean dataChanged;
    private Dialog reviewsDialog;


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

        appReviews = new ArrayList<>();
        appCurUserReviews = new ArrayList<>();

        //addRandomReviews(10);

        fetchReviewsFromFireStore();

        dataChanged = false;

        initViews();
        initAppDataFromCurApp();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean changed = dataChanged != null && dataChanged;
                //if the data changed then I send the changed data, else I don't
                finishActivity(changed, changed?curApp:null);
            }
        });
    }

    private void fetchReviewsFromFireStore(){
        db.collection("apps").document(curApp.getAppId()).collection(FIRESTORE_APP_REVIEWS_KEY).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Review review;
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    review = documentSnapshot.toObject(Review.class);
                    review.setReviewId(documentSnapshot.getId());
                    if(isUserSignedIn && review.getReviewReviewer().getUserId().equals(curUser.getUserId())) appCurUserReviews.add(review);
                    appReviews.add(review);
                }
                appReviewsAdapter.notifyDataSetChanged();
            } else {
                Log.d("debug", "Error getting documents: ", task.getException());
            }
        });
    }

    private void initViews(){
        tvAppName = (TextView) findViewById(R.id.tvAppName);
        tvAppCreator = (TextView) findViewById(R.id.tvAppCreator);
        tvAppCreator.setOnClickListener(this);
        tvAppDownloads = (TextView) findViewById(R.id.tvAppDownloads);
        tvAppSize = (TextView) findViewById(R.id.tvAppSize);
        tvAppPerms = (TextView) findViewById(R.id.tvAppPerms);
        tvAppPerms.setOnClickListener(this);
        tvAppMainCategory = findViewById(R.id.tvAppMainCategory);
        tvAppFullPrice = findViewById(R.id.tvAppFullPrice);

        ivAppImage = (ImageView) findViewById(R.id.ivAppImage);

        ibAppDownload = (ImageButton) findViewById(R.id.ibAppDownload);
        ibAppDownload.setOnClickListener(this);
        ibAppShare = (ImageButton) findViewById(R.id.ibAppShare);
        ibAppShare.setOnClickListener(this);




        rbAppAvgRating = (RatingBar) findViewById(R.id.rbAppAvgRating);
        //so it won't be clickable!
        rbAppAvgRating.setOnTouchListener((v, event) -> true);


        if(!isUserSignedIn){
            llReviews.setVisibility(GONE);
        }


        btnViewReviews = findViewById(R.id.btnViewReviews);
        btnViewReviews.setOnClickListener(this);



        //reviews dialog
        reviewsDialog = new Dialog(this);
        appReviewsAdapter = new ReviewAdapter(this, appReviews);
        SimpleListView simpleListView = new SimpleListView(this, reviewsDialog, appReviewsAdapter, this);
        reviewsDialog.setContentView(simpleListView);

        // reviews variables
        etAppReview = reviewsDialog.findViewById(R.id.etAppReview);
        rbAppUserRating = reviewsDialog.findViewById(R.id.rbAppUserRating);
        lvAppReviews = reviewsDialog.findViewById(R.id.lvAppReviews);
        llReviews = reviewsDialog.findViewById(R.id.llReviews);
        btnAppSendUserReview = reviewsDialog.findViewById(R.id.btnAppSendUserReview);
        btnAppSendUserReview.setOnClickListener(this);


        // Get the screen height
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        final int screenHeight = displayMetrics.heightPixels;
//        Toast.makeText(getApplicationContext(), "screenHeight: " + screenHeight, Toast.LENGTH_LONG).show();
//
//        // Set an OnScrollChangedListener
//        final int maxScrollY = svAppData.getChildAt(0).getBottom() - svAppData.getHeight();
//
//        svAppData.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                int scrollY = svAppData.getScrollY(); // For ScrollView
//                // Check if we have reached the bottom of the ScrollView
//                if (scrollY >= maxScrollY) {
//                    // Calculate the extra scroll amount
//                    int extraScrollY = scrollY - maxScrollY;
//                    // Ensure extraScrollY is non-negative
//                    extraScrollY = Math.max(0, extraScrollY);
//                    // Reduce the height of the ListView
//                    ViewGroup.LayoutParams params = lvAppReviews.getLayoutParams();
//                    params.height = screenHeight - extraScrollY;
//                    lvAppReviews.setLayoutParams(params);
//                    Log.v("ScrollChangedApp", "svY = " + scrollY + ", lv height = " + params.height);
//                }
//            }
//        });



    }


    @SuppressLint("SetTextI18n")
    private void initAppDataFromCurApp(){
        tvAppName.setText(Html.fromHtml("<u>" + curApp.getAppName() + "</u>"));
        tvAppMainCategory.setText(curApp.getAppMainCategory());
        tvAppCreator.setText("Created by: " + curApp.getAppCreator().getFullNameAdmin());
        tvAppSize.setText(curApp.getAppSize() + " in size");
        StorageFunctions.setImage(this, ivAppImage, curApp.getAppImagePath());

        permsDialog = new Dialog(this);
        permsDialogChoiceView = new PermissionChoiceView(this, PermissionClass.getAllPermsStrings(), new ArrayList<>(), permsDialog, null, 50, ListView.CHOICE_MODE_MULTIPLE, true);
        permsDialog.setContentView(permsDialogChoiceView);
        tvAppPerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permsDialog.show();
            }
        });
        permsDialogChoiceView.setCheckedItems(curApp.getAppPerms());




        //determine the price and colors
        double price = curApp.getAppPrice();
        double discount = curApp.getAppDiscountPercentage();
        isAppFree = price == 0 || discount >= 100;
        //I may add a "get money for downloading the app" thing later
        if(isAppFree){
            tvAppFullPrice.setVisibility(GONE);
        }
        else{
            ibAppDownload.setImageDrawable(getDrawable(R.drawable.buy_button));
            String fullPrice = "";
            // No discount
            if(curApp.getAppDiscountPercentage() == 0 || curApp.getAppDiscountPercentage() == 0.0){
                fullPrice = "<font color='green'><b>" + String.format("%.2f", price) + "</b></font>";
            }
            else{
                double priceAndDiscount = price * (1 - discount/100);
                fullPrice = "<strike><font color='red'>" + String.format("%.2f", price) + "</font></strike> " + "<font color='green'><b>" + String.format("%.2f", priceAndDiscount) + "</b></font>" + " (" + discount + "% OFF!)";
            }
            tvAppFullPrice.setText(Html.fromHtml(fullPrice));

        }

        lvAppReviews.setAdapter(appReviewsAdapter);

        //reviews dialog
        if(!isUserSignedIn) llReviews.setVisibility(GONE);
    }

    private void finishActivity(boolean result, App app){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_CURRENT_APP_KEY, app);
        returnIntent.putExtra(INTENT_ACTIVITY_KEY, INTENT_CHOSEN_APP_ACTIVITY_KEY);
        if(result) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


    @Override
    public void onClick(View v) {
        if(v==tvAppCreator){}
        else if(v==ibAppDownload){
            if(isUserSignedIn){
                if(PermissionClass.CheckPermission(this)){
                    boolean downloaded = StorageFunctions.downloadApkFileFromFireExternal(this, curApp.getAppApkPath(), curApp.getAppName());
                    if(downloaded){
                        db.collection("apps").document(curApp.getAppId()).update("appDownloadCount", curApp.getAppDownloadCount() + 1);
                        curApp.setAppDownloadCount(curApp.getAppDownloadCount() + 1);
                        dataChanged = true;
                    }
                }
                else PermissionClass.RequestPerms(this);
            }
            else Toast.makeText(this, "You must be signed in to download!", Toast.LENGTH_LONG).show();

        }
        else if(v==ibAppShare){
            shareApp();



        }
        else if(v==btnAppSendUserReview){
            if(isUserSignedIn){
                if(appCurUserReviews.size() <= 5){
                    String text = etAppReview.getText().toString();
                    ValidationData validated = Validations.validateAppDescription(text);
                    if(validated.isValid()){
                        Review review = new Review(rbAppUserRating.getRating(), text, curUser);
                        appReviews.add(review);
                        appCurUserReviews.add(review);
                        addReviewToFireBase(review);
                        appReviewsAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Review added successfully!", Toast.LENGTH_LONG).show();
                    }else etAppReview.setError(validated.getError());
                }
                else Toast.makeText(this, "You can only have 5 reviews!", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this, "You must be signed in to review!", Toast.LENGTH_LONG).show();
        }
        else if(v==btnViewReviews){
            reviewsDialog.show();
        }
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

    private void addRandomReviews(int num){
        for(int i = 0; i < num; i++) addRandomReview();
    }
    private void addRandomReview(){
        Review review;
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
        assert curApp != null;
        review = new Review(num, specialFirstName + specialLastName, curUser);
        addReviewToFireBase(review);
    }
    private void addReviewToFireBase(Review review){
        db.collection("apps").document(curApp.getAppId()).collection(FIRESTORE_APP_REVIEWS_KEY).add(review);
        db.collection("users").document(curUser.getUserId()).collection(FIRESTORE_USER_CREATED_APPS_KEY).document(curApp.getAppId()).collection(FIRESTORE_APP_REVIEWS_KEY).add(review);
    }
}