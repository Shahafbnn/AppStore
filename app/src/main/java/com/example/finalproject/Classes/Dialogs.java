package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_INITIALIZED_KEY;
import static com.example.finalproject.Classes.Constants.USER_ID_KEY;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Activities.MainActivity;
import com.example.finalproject.Activities.RegisterActivity;
import com.example.finalproject.Activities.UsersListViewActivity;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

public class Dialogs {

    //create the dialog item's OnClickListeners and others.
    public static boolean customDialogLogIn(Dialog dialog, Context context){
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
    private static boolean checkLogIn(Context context, String email, String password){
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






}
