package com.example.finalproject.Classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.R;

import java.util.ArrayList;

public class Dialogs {

    //create the dialog item's OnClickListeners and others.
    public void createAlertDialog(Context context, DialogInterface.OnClickListener dialogOnClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Photo");
        builder.setMessage("Are you sure?");
        builder.setCancelable(false);
        builder.setPositiveButton("agree", dialogOnClick);
        builder.setNegativeButton("disagree", dialogOnClick);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
    }

    private class AlertDialogClick implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == dialog.BUTTON_POSITIVE) {
                dialog.dismiss();
            }

            if (which == dialog.BUTTON_NEGATIVE) {
                dialog.dismiss();
            }
        }
    }

    public static void createPermsMultiselectDialog(Context context, TextView textView, String[] choices){

        boolean[] selected = new boolean[choices.length];
        // Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // set title
        builder.setTitle("Select Language");

        // set dialog non cancelable
        builder.setCancelable(true);
        ArrayList<String> chosenArrayList = new ArrayList<>();

        builder.setMultiChoiceItems(choices, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                // check condition
                if (b) {
                    // when checkbox selected
                    chosenArrayList.add(choices[i]);

                } else {
                    chosenArrayList.remove(choices[i]);
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // dismiss dialog
            dialogInterface.dismiss();
        });
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // use for loop
                for (int j = 0; j < selected.length; j++) {
                    // remove all selection
                    selected[j] = false;
                    // clear language list
                    chosenArrayList.clear();
                    // clear text view value
                    textView.setText("");
                }
            }
        });
        // show dialog
        builder.show();
    }

    public static void createMultiselectListView(Context context, ListView listView, String[] choices){
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, choices));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemChecked(2, true);
    }





}
