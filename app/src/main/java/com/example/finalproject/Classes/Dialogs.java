package com.example.finalproject.Classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;

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






}
