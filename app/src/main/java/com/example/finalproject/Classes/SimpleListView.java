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
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

import java.util.ArrayList;

public class SimpleListView extends LinearLayout {
    private ListView listView;
    private EditText editText;
    private Button button;
    private String[] choices;
    private Context context;
    private ArrayList<String> result;
    private Dialog dialog;
    private BaseAdapter adapter;
    private OnClickListener listener;
    public SimpleListView(Context context, Dialog dialog, BaseAdapter adapter, OnClickListener listener) {
        super(context);
        inflate(getContext(), R.layout.app_reviews_dialog, this);
        this.choices = choices;
        this.context = context;
        this.dialog = dialog;
        this.adapter = adapter;
        this.listener = listener;

        setParams();
    }

    private void setParams(){
        setOrientation(LinearLayout.VERTICAL);

        listView = findViewById(R.id.lvAppReviews);
        listView.setAdapter(adapter);

        button = findViewById(R.id.btnAppSendUserReview);
        editText = findViewById(R.id.etAppReview);

        button.setOnClickListener(listener);

    }

}
