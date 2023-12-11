package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.InitiateFunctions.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Classes.*;
import com.example.finalproject.DatabaseClasses.*;
import com.example.finalproject.R;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MenuItem itemLogIn,itemRegister;
    private TextView tvWelcome;
    private ImageView ivProfilePic;
    private Dialogs dialogs;
    private SharedPreferences sharedPreferences;
    private boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private User curUser;
    private MyDatabase myDatabase;
    private InitiateFunctions initiateFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //the function classes:
        initiateFunctions = new InitiateFunctions(this);

        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        dialogs = new Dialogs(this);
        myDatabase = MyDatabase.getInstance(this);
        City c = new City();
        //c.setCityName("Tel Aviv");
        myDatabase.cityDAO().insert(c);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();

        //checking if the user is saved in the SP and initializing vars if it is.
        MyPair<ValidationData, User> validationPair = initUserSharedPreferences(sharedPreferences, myDatabase);
        isUserSignedIn = validationPair.getFirst().isValid();
        if(!isUserSignedIn) Log.v("SignIn", validationPair.getFirst().getError());
        else curUser = validationPair.getSecond();

        initViewsFromUser(curUser, isUserSignedIn, this, myDatabase, tvWelcome, ivProfilePic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_sign_in_menu, menu);

        itemRegister = menu.findItem(R.id.itemRegister);
        itemLogIn = menu.findItem(R.id.itemLogIn);
        return true;
    }
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        initiateFunctions.initViewsFromUser(curUser, isUserSignedIn, myDatabase, tvWelcome, ivProfilePic);
                    }
                }
            }
    );

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==itemLogIn){
            dialogs.createCustomDialogLogIn(curUser, isUserSignedIn, this, myDatabase, tvWelcome, ivProfilePic);
            return true;
        }
        else if(item==itemRegister){
            Intent intent = new Intent(this, RegisterActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

}