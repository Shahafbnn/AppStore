package com.example.finalproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

public class ChosenAppActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAppName, tvAppCreator, tvAppDownloads, tvAppSize, tvAppPerms;
    ImageView ivAppImage;
    ImageButton ibAppDownload, ibAppShare;
    Button btnAppSendUserReview;
    ScrollView svAppPerms, svAppReviews;
    RatingBar rbAppAvgRating, rbAppUserRating;
    EditText etAppReview;
    LinearLayout llAppPerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_app);

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("key");
            if(value == null || value.equals("")) return;
            long id = Long.parseLong(value);
            int curApp;
//            //MyApplication curApp = MyDatabase.getInstance(this).cityDAO().getCityById(id);
//            tvAppName.setText(curApp.getName());
//            tvAppCreator.setText("Created by: " + curApp.getCreator());
//            tvAppDownloads.setText(curApp.getDownloads() + " Downloads");
//            tvAppSize.setText(curApp.getSize() + " mb");
//
//            //the openable and closable scrollView
//            llAppPerms.


        }
    }

    @Override
    public void onClick(View v) {

    }
}