package com.example.finalproject.Classes;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.MainActivity;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

public class Dialogs {

    private Context context;

    public Dialogs(Context context) {
        this.context = context;
    }

    //create the dialog item's OnClickListeners and others.
    private boolean customDialogLogIn(Dialog dialog, Context context){
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        EditText etTextPassword = dialog.findViewById(R.id.etTextPassword);
        //Button btnLogInSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        String emailAddress = etEmailAddress.getText().toString();
        String textPassword = etTextPassword.getText().toString();

        ValidationData emailValidation = UserValidations.validateEmail(emailAddress);
        ValidationData passwordValidation = UserValidations.validatePassword(textPassword);

        ValidationData.changeEditTextByValidationData(emailValidation, etEmailAddress);
        ValidationData.changeEditTextByValidationData(passwordValidation, etTextPassword);

        if((emailValidation.isValid() || passwordValidation.isValid())) return checkLogIn(context, emailAddress, textPassword);
        else {
            Toast.makeText(context, "All EditTexts must be correct!", Toast.LENGTH_LONG).show();
        }
        return false;
    }
    private boolean checkLogIn(Context context, String email, String password){
        User u = MyDatabase.getInstance(context).userDAO().getUserByEmail(email);
        if(u == null) {
            Toast.makeText(context, "User doesn't exist!", Toast.LENGTH_LONG).show();
            return false;
        }
        boolean isLogInCorrect = u.getPassword().equals(password);
        if(isLogInCorrect){
            User.addUserToSharedPreferences(u, context);
        }
        else Toast.makeText(context, "Password is incorrect!", Toast.LENGTH_LONG).show();
        return isLogInCorrect;
    }



    public void createCustomDialogLogIn(User user, boolean isValid, Context context, MyDatabase myDatabase, TextView tvWelcome, ImageView ivProfilePic){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.log_in_dialog);

        // Set the custom dialog components - text, image and button
        Button btnFruitSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        btnFruitSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customDialogLogIn(dialog, context)) {
                    InitiateFunctions.initViewsFromUser(user, isValid, context, myDatabase, tvWelcome, ivProfilePic);
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }
}
