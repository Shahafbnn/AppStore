package com.example.finalproject.Classes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.R;

public class Dialogs {

    private Context context;

    public Dialogs(Context context) {
        this.context = context;
    }

    //create the dialog item's OnClickListeners and others.
    private void customDialogLogIn(Dialog dialog){
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        EditText etTextPassword = dialog.findViewById(R.id.etTextPassword);
        String emailAddress = etEmailAddress.getText().toString();
        String textPassword = etTextPassword.getText().toString();
    }

    public void createCustomDialogLogIn(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.log_in_dialog);

        // Set the custom dialog components - text, image and button
        Button btnFruitSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        btnFruitSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                customDialogLogIn(dialog);
            }
        });
        dialog.show();
    }
}
