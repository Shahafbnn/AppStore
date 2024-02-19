package com.example.finalproject.Classes;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.finalproject.R;

import java.util.ArrayList;

public class PermissionChoiceView extends LinearLayout {
    private ListView listView;
    private TextView textView;
    private String[] choices;
    private Context context;
    private ArrayList<String> result;
    private Dialog dialog;
    private TextView tvOutData;
    public PermissionChoiceView(Context context, String[] choices,  ArrayList<String> result, Dialog dialog, TextView outData) {
        super(context);
        inflate(getContext(), R.layout.permission_choice_view, this);
        this.choices = choices;
        this.context = context;
        this.result = result;
        this.dialog = dialog;
        this.tvOutData = outData;
        setParams();

    }

    private void setParams(){
        setOrientation(LinearLayout.VERTICAL);

//        textView.setGravity(Gravity.CENTER);
//        textView.setText("> Perms:");
//        addView(textView);
//
//        textView.setOnClickListener(v -> {
//            if(listView.getVisibility() == View.GONE){
//                listView.setVisibility(View.VISIBLE);
//                textView.setText("v Perms:");
//            }
//            else {
//                listView.setVisibility(View.GONE);
//                textView.setText("> Perms:");
//            }
//        });

        listView = findViewById(R.id.lvPermissionChoice);
//        listView.setVisibility(VISIBLE);
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, choices));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setItemChecked(2, true);
//        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        listView.setLayoutParams(layoutParams);
//        addView(listView);

        Button btn = findViewById(R.id.btnPermissionChoiceSubmit);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                result = getCheckedItems();
                dialog.dismiss();
            }
        });
//        addView(btn);
    }

    public ArrayList<String> getCheckedItems(){
        android.util.SparseBooleanArray checkedItemsArray = listView.getCheckedItemPositions();
        ArrayList<String> chosen = new ArrayList<String>();
        tvOutData.setText("");
        String str = "Perms: ";
        for(int i = 0; i < checkedItemsArray.size(); i++){
            if(checkedItemsArray.valueAt(i)){
                chosen.add(choices[i]);
                str += choices[i] + ", ";
            }
        }
        str = str.substring(0, str.length()-2);
        tvOutData.setText(str);
        return chosen;
    }

}
