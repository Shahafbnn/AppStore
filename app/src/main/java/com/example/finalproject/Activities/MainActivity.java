package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_INITIALIZED_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.Constants.USER_ID_KEY;
import static com.example.finalproject.Classes.InitiateFunctions.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Classes.*;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.*;

public class MainActivity extends AppCompatActivity {

    private MenuItem itemLogIn,itemRegister, itemLogOut, itemDataUpdate, itemAboutMe;
    private TextView tvWelcome;
    private ImageView ivProfilePic;
    private SharedPreferences sharedPreferences;
    private Boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private User curUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        db = FirebaseFirestore.getInstance();


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();

        //we set it when we get the user from firestore
        isUserSignedIn = null;
        curUser = null;

        //if the user DOESN'T exist in the Shared Preferences, we set the activity for a guest.
        if (sharedPreferences==null || !sharedPreferences.contains(Constants.SHARED_PREFERENCES_INITIALIZED_KEY)) {
            isUserSignedIn = false;
            reloadActivity();
        }
        String id = sharedPreferences.getString(Constants.USER_ID_KEY, "");
        if(id.isEmpty()){
            isUserSignedIn = false;
            reloadActivity();
        }
        else {
            db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            //set the user data
                            curUser = documentSnapshot.toObject(User.class);
                            curUser.setId(documentSnapshot.getId());

                            isUserSignedIn = true;
                            setSharedPreferencesData(id);

                        } else {
                            isUserSignedIn = false;
                        }
                    } else {
                        isUserSignedIn = false;
                        Log.d("debug", "initUserSharedPreferences get failed with ", task.getException());
                    }
                    //reload the activity based on the new data
                    reloadActivity();
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_sign_in_menu, menu);

        itemRegister = menu.findItem(R.id.itemRegister);
        itemLogIn = menu.findItem(R.id.itemLogIn);
        itemLogOut = menu.findItem(R.id.itemLogOut);
        itemDataUpdate = menu.findItem(R.id.itemDataUpdate);
        itemAboutMe = menu.findItem(R.id.itemAboutMe);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //if the user is logged in the Register and Log In options disappear and a Log Out option appears.
        itemRegister = menu.findItem(R.id.itemRegister);
        itemLogIn = menu.findItem(R.id.itemLogIn);
        itemLogOut = menu.findItem(R.id.itemLogOut);
        itemDataUpdate = menu.findItem(R.id.itemDataUpdate);

        itemRegister.setVisible(false);
        itemLogIn.setVisible(false);
        itemLogOut.setVisible(false);
        itemDataUpdate.setVisible(false);
        Toast.makeText(this, "Loading logged in data, please wait", Toast.LENGTH_LONG).show();
        return true;
    }

    private void changeOptionMenuItemsVisibility(Boolean isUserSignedIn){
        if(isUserSignedIn == null){
            itemRegister.setVisible(false);
            itemLogIn.setVisible(false);
            itemLogOut.setVisible(false);
            itemDataUpdate.setVisible(false);
        }
        else if(isUserSignedIn)
        {
            itemRegister.setVisible(false);
            itemLogIn.setVisible(false);
            itemLogOut.setVisible(true);
            itemDataUpdate.setVisible(true);
        }
        else
        {
            itemRegister.setVisible(true);
            itemLogIn.setVisible(true);
            itemLogOut.setVisible(false);
            itemDataUpdate.setVisible(false);
        }
        invalidateOptionsMenu();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==itemLogIn){
            createCustomDialogLogIn();
            return true;
        }
        else if(item==itemRegister){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item==itemLogOut){
            editor.clear();
            editor.commit();
            isUserSignedIn = false;
            curUser = null;
            invalidateViews(tvWelcome, ivProfilePic);
            invalidateOptionsMenu();
            return true;
        }
        else if(item==itemDataUpdate){
            Intent intent = new Intent(this, UsersListViewActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item==itemAboutMe){
            createAlertDialog();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
    private void reloadActivity(){
        changeOptionMenuItemsVisibility(isUserSignedIn);
        initViewsFromUser(curUser, isUserSignedIn, getApplicationContext(), db, tvWelcome, ivProfilePic);
    }
    public void setSharedPreferencesData(String id){
        editor.clear();
        editor.putString(USER_ID_KEY, id);
        editor.putBoolean(SHARED_PREFERENCES_INITIALIZED_KEY, true);
        editor.commit();
    }
    private void createAlertDialog(){
        class AlertDialogClick implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == dialog.BUTTON_POSITIVE) {
                    dialog.dismiss();
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About: ");
        String userName = "Guest";
        String aboutTheApp = "About my app store -\n" +
                "This application will be a digital app store for downloading apps on the phone.\n" +
                "Options:\n" +
                "1. The Log In options which are: Log in, Register, Log out, Update data, user deletion, and the about page! \n" +
                "2. Search apps by name\n" +
                "3. Search for apps using voice search\n" +
                "4. Search history\n" +
                "5. A category page that includes: news, games, social networks, entertainment, work and more.\n" +
                "6. Uploading apps to the store.\n" +
                "7. Installing apps from the store.\n" +
                "8. Sharing apps to another app such as sharing a message to WhatsApp.\n" +
                "9. Displaying application information to users including: application name, application creator, amount of downloads, application size, application phone permissions such as camera permissions.\n" +
                "10. The app creator's page where all the apps they uploaded are displayed.\n" +
                "11. Average rating of a creator's apps.\n" +
                "12 Public app rating shown to all users on the app's download page.\n" +
                "13. Public comments about the app from users who have installed it.\n" +
                "14. The rating of the responses by the users.\n" +
                "15. A page of \"more apps\" in the same category as the app you selected.\n" +
                "16. App settings which include: changing the color of the background and buttons and more.\n" +
                "17. Recently added apps page.\n" +
                "18. Popular apps page with the highest rating and number of installation apps.\n" +
                "19. Suggested apps page based on your search history and app rating.";
        if(isUserSignedIn) userName = curUser.getFullNameAdmin();
        builder.setMessage("Hello " + userName + "!\nI am a computer science student, I live in Israel, and for this project I chose to make an app store!\n" +
                aboutTheApp);
        builder.setCancelable(true);
        builder.setPositiveButton("Cancel", new AlertDialogClick());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void createCustomDialogLogIn(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_dialog);

        Button btnLogInSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        EditText etTextPassword = dialog.findViewById(R.id.etTextPassword);

        btnLogInSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateCustomDialogLogInData(dialog)) {

                    db.collection("users")
                            .whereEqualTo("userEmail", etEmailAddress.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            if(documentSnapshot.exists()){
                                                User user = documentSnapshot.toObject(User.class);
                                                if(user.getPassword().equals(etTextPassword.getText().toString())){
                                                    isUserSignedIn = true;
                                                    curUser = user;
                                                    curUser.setId(documentSnapshot.getId());
                                                    setSharedPreferencesData(documentSnapshot.getId());
                                                    reloadActivity();
                                                    Toast.makeText(getApplicationContext(), "Logging in!", Toast.LENGTH_LONG).show();
                                                    dialog.cancel();
                                                }
                                                else {
                                                    etTextPassword.setError("Incorrect password!");
                                                }

                                            }
                                            else{
                                                etEmailAddress.setError("User does not exist!");
                                            }
                                        }
                                    } else {
                                        Log.w("user", "Task unsuccessful", task.getException());
                                    }
                                }
                            });
                }
            }
        });
        dialog.show();
    }
    private static boolean validateCustomDialogLogInData(Dialog dialog){
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        EditText etTextPassword = dialog.findViewById(R.id.etTextPassword);
        //Button btnLogInSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        String emailAddress = etEmailAddress.getText().toString();
        String textPassword = etTextPassword.getText().toString();

        //validates if the fields correspond to the rules.
        ValidationData emailValidation = UserValidations.validateEmail(emailAddress);
        ValidationData passwordValidation = UserValidations.validatePassword(textPassword);

        //changes the ET error based on the validation.
        if(!emailValidation.isValid()) etEmailAddress.setError(emailValidation.getError());
        if(!passwordValidation.isValid()) etTextPassword.setError(passwordValidation.getError());

        return emailValidation.isValid() && passwordValidation.isValid();
    }


}