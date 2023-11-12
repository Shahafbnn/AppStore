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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.finalproject.Classes.Dialogs;
import com.example.finalproject.R;

import java.io.IOException;

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
        String fullName = sharedPreferences.getString("firstName", "Guest") + sharedPreferences.getString("lastName", "");
        if(sharedPreferences.getBoolean("isAdmin", false)) fullName += " (Admin)";
        tvWelcome.setText("Welcome " + fullName + "!");

    }
    //    public void saveBitmapInFolder(Bitmap bitmap){
//        String timeStamp = new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
//        String foldersPhotos = "ABC";
//        File myDir = new File(Environment.getExternalStorageDirectory(), "/" + foldersPhotos);
//
//        if(!myDir.exists())
//        {
//            myDir.mkdirs();
//        }
//
//        File dest = new File(myDir, filename);
//        try {
//            FileOutputStream out = new FileOutputStream(dest);
//        } catch (Exception e){
//
//        }
//    }

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