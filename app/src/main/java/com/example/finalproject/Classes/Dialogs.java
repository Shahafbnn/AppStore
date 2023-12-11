package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_INITIALIZED_KEY;
import static com.example.finalproject.Classes.Constants.USER_ID_KEY;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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



    public void createCustomDialogLogIn(MyDatabase myDatabase, TextView tvWelcome, ImageView ivProfilePic, SharedPreferences.Editor editor){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.log_in_dialog);

        // Set the custom dialog components - text, image and button
        Button btnFruitSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        btnFruitSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customDialogLogIn(dialog, context)) {
                    User u = myDatabase.userDAO().getUserByEmail(etEmailAddress.getText().toString());
                    long id = u.getId();
                    editor.clear();
                    editor.putLong(USER_ID_KEY, id);
                    editor.putBoolean(SHARED_PREFERENCES_INITIALIZED_KEY, true);
                    editor.commit();
                    InitiateFunctions.initViewsFromUser(u, true, context, myDatabase, tvWelcome, ivProfilePic);
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }
}
