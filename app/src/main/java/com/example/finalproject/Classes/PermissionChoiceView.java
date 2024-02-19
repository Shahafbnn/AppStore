package com.example.finalproject.Classes;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PermissionChoiceView extends LinearLayout {
    private ListView listView;
    private String[] choices;
    private Context context;
    private ArrayList<String> result;
    private Dialog dialog;
    private TextView tvOutData;
    private int maxCheckedResults;
    public PermissionChoiceView(Context context, String[] choices,  ArrayList<String> result, Dialog dialog, TextView outData, int maxCheckedResults) {
        super(context);
        inflate(getContext(), R.layout.permission_choice_view, this);
        this.choices = choices;
        this.context = context;
        this.result = result;
        this.dialog = dialog;
        this.tvOutData = outData;
        this.maxCheckedResults = maxCheckedResults;

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
        TextView tv = findViewById(R.id.tvPermissionChoice);
        tv.setText("Please choose your permissions (max " + maxCheckedResults + "):");

        Button btn = findViewById(R.id.btnPermissionChoiceSubmit);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //because it's a pointer
                result.clear();
                result.addAll(getCheckedItems());
                dialog.dismiss();
            }
        });
//        addView(btn);
    }

    public ArrayList<String> getCheckedItems(){
        SparseBooleanArray checkedItemsArray = listView.getCheckedItemPositions();
        ArrayList<String> chosen = new ArrayList<String>();
        int len = checkedItemsArray.size();
        tvOutData.setText("");
        String str = "Perms: ";
        int checkedCount = 0;
        for(int i = 0; i < len && i < maxCheckedResults && checkedCount < maxCheckedResults; i++){
            if(checkedItemsArray.valueAt(i)){
                int position = checkedItemsArray.keyAt(i); // Get the actual position of the item in the list
                if(checkedItemsArray.valueAt(i)) checkedCount++;
                chosen.add(choices[position]);
                str += choices[position] + ", ";
            }
        }
        if(checkedCount==maxCheckedResults) Toast.makeText(context, "You cannot have over "+maxCheckedResults+" permissions!", Toast.LENGTH_LONG).show();
        str = str.substring(0, str.length()-2);
        //Toast.makeText(context, "The string: " + str, Toast.LENGTH_LONG).show();
        tvOutData.setText(str);
        return chosen;
    }
    public void setCheckedItems(ArrayList<String> checkedItems){
        if(!(checkedItems==null || checkedItems.isEmpty())){for(int i = 0; i < choices.length; i++){
            if(checkedItems.contains(choices[i])){
                listView.setItemChecked(i, true);
            } else {
                listView.setItemChecked(i, false);
            }
        }}
        getCheckedItems();
    }

    private void clear(){
        result.clear();
    }

}
