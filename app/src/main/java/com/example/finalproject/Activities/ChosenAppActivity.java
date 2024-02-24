package com.example.finalproject.Activities;

import static android.view.View.GONE;
import static com.example.finalproject.Classes.Constants.FIRESTORE_APP_REVIEWS_KEY;
import static com.example.finalproject.Classes.Constants.FIRESTORE_RECEIPT_KEY;
import static com.example.finalproject.Classes.Constants.FIRESTORE_USER_CREATED_APPS_KEY;
import static com.example.finalproject.Classes.Constants.FIRESTORE_USER_DOWNLOADED_APPS_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_ACTIVITY_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CHOSEN_APP_ACTIVITY_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_SCROLL_VIEW_KEY;
import static com.example.finalproject.Classes.Constants.getAppTypes;
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
import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.PermissionChoiceView;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.Receipt;
import com.example.finalproject.Classes.Review;
import com.example.finalproject.Classes.SimpleListView;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.Classes.User.Validations;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ChosenAppActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAppName, tvAppCreator, tvAppDownloads, tvAppSize, tvAppPerms, tvAppMainCategory, tvAppFullPrice, tvAppUploadDate;
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
    private Integer downloadCount;
    private Boolean alreadyDownloadedTheApp;
    private PermissionClass perms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_app);

        db = FirebaseFirestore.getInstance();
        perms = new PermissionClass(this);

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
        fetchDownloadCountFromFireStore();

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
    private void fetchDownloadCountFromFireStore(){
        db.collection("apps").document(curApp.getAppId()).collection(FIRESTORE_RECEIPT_KEY).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        int counter = 0;
                        Receipt receipt;
                        alreadyDownloadedTheApp = false;
                        for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                            receipt = documentSnapshot.toObject(Receipt.class);
                            counter++;
                            if(isUserSignedIn && receipt.getReceiptBuyer().getUserId().equals(curUser.getUserId())) alreadyDownloadedTheApp = true;
                        }
                        downloadCount = counter;
                        if(downloadCount == 1) tvAppDownloads.setText(downloadCount + " Download!");
                        else tvAppDownloads.setText(downloadCount + " Downloads!");

                        db.collection("apps").document(curApp.getAppId()).update("appDownloadCount", downloadCount);
                        db.collection("users").document(curApp.getAppCreator().getUserId()).collection(FIRESTORE_USER_CREATED_APPS_KEY).document(curApp.getAppId()).update("appDownloadCount", downloadCount);
                    }
                }
            }
        });
    }
    private void fetchAverageRatingFromFireStore(){
        db.collection("apps").document(curApp.getAppId()).collection(FIRESTORE_APP_REVIEWS_KEY).aggregate(AggregateField.average("reviewAppScore")).get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Aggregate fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    Double data  = snapshot.get(AggregateField.average("reviewAppScore"));
                    if(data != null){
                        rbAppAvgRating.setRating(data.floatValue());
                        db.collection("apps").document(curApp.getAppId()).update("appAvgRating", data.floatValue());
                        db.collection("users").document(curApp.getAppCreator().getUserId()).collection(FIRESTORE_USER_CREATED_APPS_KEY).document(curApp.getAppId()).update("appAvgRating", data.floatValue());
                    }
                }
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
    private void setAppScoreFromReviewArrayList(){
        float score = 0;
        int i = 0;
        for(Review review: appReviews){
            i++;
            score += review.getReviewAppScore();
        }
        rbAppAvgRating.setRating(score/i);
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
        tvAppUploadDate = findViewById(R.id.tvAppUploadDate);

        ivAppImage = (ImageView) findViewById(R.id.ivAppImage);

        ibAppDownload = (ImageButton) findViewById(R.id.ibAppDownload);
        ibAppDownload.setOnClickListener(this);
        ibAppShare = (ImageButton) findViewById(R.id.ibAppShare);
        ibAppShare.setOnClickListener(this);




        rbAppAvgRating = (RatingBar) findViewById(R.id.rbAppAvgRating);
        //so it won't be clickable!
        rbAppAvgRating.setOnTouchListener((v, event) -> true);




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
        if(!isUserSignedIn){
            llReviews.setVisibility(GONE);
        }
        btnAppSendUserReview = reviewsDialog.findViewById(R.id.btnAppSendUserReview);
        btnAppSendUserReview.setOnClickListener(this);



    }

    @SuppressLint("SetTextI18n")
    private void initAppDataFromCurApp(){
        fetchAverageRatingFromFireStore();
        if(curApp.getAppUploadDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
            String str = sdf.format(curApp.getAppUploadDate()); // formats to 09/23/2009 13:53:28.238
            tvAppUploadDate.setText("Upload Date: " + str);
        }


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
                fullPrice = "<strike><font color='red'>" + String.format("%.2f", price) + "</font></strike> " + "<font color='green'><b>" + String.format("%.2f", priceAndDiscount) + "</b></font>" + " (" + String.format("%.2f", discount) + "% OFF!)";
            }
            tvAppFullPrice.setText(Html.fromHtml(fullPrice));

        }

        lvAppReviews.setAdapter(appReviewsAdapter);

        //reviews dialog
        if(!isUserSignedIn) llReviews.setVisibility(GONE);


    }

    private void finishActivity(boolean result, App app){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_SCROLL_VIEW_KEY, getIntent().getStringExtra(Constants.INTENT_SCROLL_VIEW_KEY));
        returnIntent.putExtra(INTENT_CURRENT_APP_KEY, app);
        returnIntent.putExtra(INTENT_ACTIVITY_KEY, INTENT_CHOSEN_APP_ACTIVITY_KEY);
        if(result) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(v==tvAppCreator){
            Intent intent = new Intent(getApplicationContext(), UserDataActivity.class);
            intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curApp.getAppCreator());
            startActivity(intent);
        }
        else if(v==ibAppDownload){
            if(isUserSignedIn){
                if(perms.CheckPermission(this)){
                    if(alreadyDownloadedTheApp == null || !alreadyDownloadedTheApp){
                        boolean downloadedSuccessfully = StorageFunctions.downloadApkFileFromFireExternal(ChosenAppActivity.this, curApp.getAppApkPath(), curApp.getAppName(), perms);
                        if(downloadedSuccessfully) {
                            Receipt receipt = new Receipt(curUser, curUser.getUserId(), curApp, Calendar.getInstance().getTime());
                            db.collection("apps").document(curApp.getAppId()).collection(FIRESTORE_RECEIPT_KEY).add(receipt);
                            db.collection("users").document(curUser.getUserId()).collection(FIRESTORE_USER_DOWNLOADED_APPS_KEY).add(receipt);
                            if(downloadCount != null) {
                                if(downloadCount==1)tvAppDownloads.setText(downloadCount + " Download!");
                                else tvAppDownloads.setText(downloadCount + " Downloads!");
                            }
                            dataChanged = true;
                        }
                    }
                    else Toast.makeText(getApplicationContext(), "You have already downloaded it!", Toast.LENGTH_LONG).show();


                }
                else perms.RequestPerms(this);
            }
            else Toast.makeText(this, "You must be signed in to download!", Toast.LENGTH_LONG).show();

        }
        else if(v==ibAppShare){
            shareApp();



        }
        else if(v==btnAppSendUserReview){
            if(isUserSignedIn){
                if(appCurUserReviews.size() < 5){
                    String text = etAppReview.getText().toString();
                    ValidationData validated = Validations.validateAppDescription(text);
                    if(validated.isValid()){
                        Review review = new Review(rbAppUserRating.getRating(), text, curUser);
                        appReviews.add(review);
                        appCurUserReviews.add(review);
                        addReviewToFireBase(review);
                        appReviewsAdapter.notifyDataSetChanged();
                        setAppScoreFromReviewArrayList();
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

        // Create the text you want to share
        String shareText = "Look at this app in App: " + curApp.getAppName();

// Create a new intent and set its action to ACTION_SEND
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

// Put the text into the intent
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

// Set the type of the content to be shared
        shareIntent.setType("text/plain");

// Start the activity with the intent
        startActivity(Intent.createChooser(shareIntent, null));

        return true;

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
        db.collection("users").document(curUser.getUserId()).collection(FIRESTORE_APP_REVIEWS_KEY).add(review);
    }
}