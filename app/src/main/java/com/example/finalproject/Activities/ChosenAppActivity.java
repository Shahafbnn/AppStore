package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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
import com.example.finalproject.R;

public class ChosenAppActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvAppName, tvAppCreator, tvAppDownloads, tvAppSize, tvAppPerms;
    private ImageView ivAppImage;
    private ImageButton ibAppDownload, ibAppShare;
    private Button btnAppSendUserReview;
    private ScrollView svAppPerms, svAppReviews;
    private RatingBar rbAppAvgRating, rbAppUserRating;
    private EditText etAppReview;
    private LinearLayout llAppPerms;
    private App curApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_app);

        //getting the app from the intent
        curApp = (App) getIntent().getSerializableExtra(INTENT_CURRENT_APP_KEY);
        if(curApp == null) {
            Toast.makeText(this, "null app was sent, try again!", Toast.LENGTH_LONG).show();
            finishActivity(Activity.RESULT_CANCELED);
        }


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

        rbAppAvgRating = (RatingBar) findViewById(R.id.rbAppAvgRating);
        rbAppUserRating = (RatingBar) findViewById(R.id.rbAppUserRating);

        etAppReview = (EditText) findViewById(R.id.etAppReview);

        llAppPerms = findViewById(R.id.llAppPerms);

    }

    private void initAppFromExtras(){
        tvAppName.setText(curApp.getAppName());
        tvAppCreator = (TextView) findViewById(R.id.tvAppCreator);
        tvAppCreator.setOnClickListener(this);
        tvAppDownloads = (TextView) findViewById(R.id.tvAppDownloads);
        tvAppSize = (TextView) findViewById(R.id.tvAppSize);
        tvAppPerms = (TextView) findViewById(R.id.tvAppPerms);
    }

    @Override
    public void onClick(View v) {

    }
}