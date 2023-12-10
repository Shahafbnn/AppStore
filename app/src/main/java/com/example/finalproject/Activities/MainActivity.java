package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;

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

import com.example.finalproject.Classes.Dialogs;
import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.DatabaseClasses.City;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.Classes.User;
import com.example.finalproject.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private MenuItem itemLogIn,itemRegister;
    private TextView tvWelcome;
    private ImageView ivProfilePic;
    private Dialogs dialogs;
    private SharedPreferences sharedPreferences;
    private boolean isSPValid;
    private boolean isSPInitialized;
    private boolean isUserInDB;
    private SharedPreferences.Editor editor;
    private User curUser;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        dialogs = new Dialogs(this);
        myDatabase = MyDatabase.getInstance(this);
        City c = new City();
        //c.setCityName("Tel Aviv");
        myDatabase.cityDAO().insert(c);

        //shared preferences init:
        //vals[0] = isSPValid, vals[1] = isUserInDB, vals[2] = isSPInitialized
        //the bool array is like a c pointer, to change the actual value;
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        curUser = InitiateFunctions.initUserSharedPreferences(sharedPreferences, myDatabase, new Boolean[]{isSPValid, isUserInDB, isSPInitialized});
        editor = sharedPreferences.edit();
        InitiateFunctions.initViewsFromUser(curUser, isUserInDB, this, myDatabase, tvWelcome, ivProfilePic);
    }


    //making it so it's repeatable in other classes/activities


    //uses curUser and turns the




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
                        recreate();
                    }
                }
            }
    );

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==itemLogIn){
            dialogs.createCustomDialogLogIn(curUser, isUserInDB, this, myDatabase, tvWelcome, ivProfilePic);
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