package com.example.finalproject.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.Dialogs;
import com.example.finalproject.Classes.User;
import com.example.finalproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MenuItem itemLogIn,itemRegister;
    private TextView tvWelcome;
    private Dialogs dialogs;
    private SharedPreferences sharedPreferences;
    private boolean spInitialized;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWelcome = findViewById(R.id.tvWelcome);
        dialogs = new Dialogs(this);
        sharedPreferences = getSharedPreferences("SharedPreferencesRegister", 0);
        spInitialized = sharedPreferences.contains("initialized");
        //it will return the default if the sharedPreferences isn't init and the name will be Guest maybe
        String fullName = sharedPreferences.getString(Constants.FIRST_NAME_KEY, "Guest") + sharedPreferences.getString(Constants.LAST_NAME_KEY, "");
        if(sharedPreferences.getBoolean("isAdmin", false)) fullName += " (Admin)";
        tvWelcome.setText("Welcome " + fullName + "!");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_sign_in_menu, menu);

        itemRegister = menu.findItem(R.id.itemRegister);
        itemLogIn = menu.findItem(R.id.itemLogIn);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==itemLogIn){
            dialogs.createCustomDialogLogIn();
            return true;
        }
        else if(item==itemRegister){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }

}