package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_INITIALIZED_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.Constants.USER_ID_KEY;
import static com.example.finalproject.Classes.InitiateFunctions.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.example.finalproject.DatabaseClasses.*;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.*;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MenuItem itemLogIn,itemRegister, itemLogOut, itemDataUpdate, itemAboutMe;
    private TextView tvWelcome;
    private ImageView ivProfilePic;
    private SharedPreferences sharedPreferences;
    private boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private User curUser;
    private FirebaseFirestore db;
    private InitiateFunctions initiateFunctions;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //the function classes:
        initiateFunctions = new InitiateFunctions(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        db = FirebaseFirestore.getInstance();
        boolean[] isEmpty = new boolean[1];
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            isEmpty[0] = task.getResult().isEmpty();
                            // Now you can use 'isEmpty' to check if the collection is empty
                        } else {
                            Log.w("city", "Error getting documents.", task.getException());
                        }
                    }
                });
        if(isEmpty[0]) {
            CitiesArray.addCities(db);
        }

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();

        //checking if the user is saved in the SP and initializing vars if it is.
        MyPair<ValidationData, User> validationPair = initUserSharedPreferences(sharedPreferences, db);
        isUserSignedIn = validationPair.getFirst().isValid();
        if(!isUserSignedIn) Log.v("SignIn", validationPair.getFirst().getError());
        else curUser = validationPair.getSecond();

        initiateFunctions.initViewsFromUser(curUser, isUserSignedIn, this, db, tvWelcome, ivProfilePic);
        Context c = this;
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //checking if the user is saved in the SP and initializing vars if it is.
                            MyPair<ValidationData, User> validationPair = initUserSharedPreferences(sharedPreferences, db);
                            isUserSignedIn = validationPair.getFirst().isValid();
                            if(!isUserSignedIn) Log.v("SignIn", validationPair.getFirst().getError());
                            else {
                                curUser = validationPair.getSecond();
                                ivProfilePic.setImageURI(curUser.getImgUri(c));
                            }
                            initiateFunctions.initViewsFromUser(curUser, isUserSignedIn, db, tvWelcome, ivProfilePic);
                            invalidateOptionsMenu();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_sign_in_menu, menu);
        this.menu = menu;

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

        changeOptionMenuItemsVisibility();
        return true;
    }

    private void changeOptionMenuItemsVisibility(){
        if(isUserSignedIn)
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
    }
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==itemLogIn){
            //rember to update the user, DO IT!
            createCustomDialogLogIn(db, tvWelcome, ivProfilePic, editor);
            return true;
        }
        else if(item==itemRegister){
            Intent intent = new Intent(this, RegisterActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        else if(item==itemLogOut){
            editor.clear();
            editor.commit();
            isUserSignedIn = false;
            curUser = null;
            initiateFunctions.invalidateViews(tvWelcome, ivProfilePic);
            invalidateOptionsMenu();
            return true;
        }
        else if(item==itemDataUpdate){
            Intent intent = new Intent(this, UsersListViewActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        else if(item==itemAboutMe){
            createAlertDialog();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

    public void createAlertDialog(){
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
//        builder.setNegativeButton("Cancel", new AlertDialogClick());
        AlertDialog dialog = builder.create();
        dialog.show();
//        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
//        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    private class AlertDialogClick implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == dialog.BUTTON_POSITIVE) {
                dialog.dismiss();
            }
        }
    }

    public void createCustomDialogLogIn(FirebaseFirestore db, TextView tvWelcome, ImageView ivProfilePic, SharedPreferences.Editor editor){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_dialog);
        Context context = this;
        // Set the custom dialog components - text, image and button
        Button btnFruitSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        btnFruitSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Dialogs.customDialogLogIn(dialog, context)) {

                    final User[] user = new User[1];
                    db.collection("users")
                            .whereEqualTo("userEmail", etEmailAddress.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            user[0] = document.toObject(User.class);
                                            // You can convert the document into a User object here
                                        }
                                    } else {
                                        Log.w("user", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                    String id = user[0].getId();
                    editor.clear();
                    editor.putString(USER_ID_KEY, id);
                    editor.putBoolean(SHARED_PREFERENCES_INITIALIZED_KEY, true);
                    editor.commit();
                    InitiateFunctions.initViewsFromUser(user[0], true, context, db, tvWelcome, ivProfilePic);
                    //checking if the user is saved in the SP and initializing vars if it is.
                    MyPair<ValidationData, User> validationPair = initUserSharedPreferences(sharedPreferences, db);
                    isUserSignedIn = validationPair.getFirst().isValid();
                    if(!isUserSignedIn) Log.v("SignIn", validationPair.getFirst().getError());
                    else curUser = validationPair.getSecond();
                    onPrepareOptionsMenu(menu);
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

}