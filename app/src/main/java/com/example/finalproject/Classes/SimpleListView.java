package com.example.finalproject.Classes;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

import java.util.ArrayList;

public class SimpleListView extends LinearLayout {
    private ListView listView;
    private LinearLayout llReviews;
    private EditText editText;
    private RatingBar rbAppUserRating;
    private Button button;
    private Context context;
    private Dialog dialog;
    private BaseAdapter adapter;
    private OnClickListener listener;
    public SimpleListView(Context context, Dialog dialog, BaseAdapter adapter, OnClickListener listener) {
        super(context);
        inflate(getContext(), R.layout.app_reviews_dialog, this);
        this.context = context;
        this.dialog = dialog;
        this.adapter = adapter;
        this.listener = listener;

        setParams();
    }

    private void setParams(){
        setOrientation(LinearLayout.VERTICAL);

        llReviews = findViewById(R.id.llReviews);
        listView = findViewById(R.id.lvAppReviews);
        listView.setAdapter(adapter);
        button = findViewById(R.id.btnAppSendUserReview);
        editText = findViewById(R.id.etAppReview);
        rbAppUserRating = findViewById(R.id.rbAppUserRating);
        //if there is no listener that means there is no review, so we make it invisible
        if(listener != null){
            button.setOnClickListener(listener);
        }
        else {
            llReviews.setVisibility(GONE);
        }

    }

}
