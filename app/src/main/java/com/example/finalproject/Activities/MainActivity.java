package com.example.finalproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.Dialogs;
import com.example.finalproject.Classes.MyDatabase;
import com.example.finalproject.R;

public class MainActivity extends AppCompatActivity {

    private MenuItem itemLogIn,itemRegister;
    private TextView tvWelcome;
    private Dialogs dialogs;
    private SharedPreferences sharedPreferences;
    private boolean spInitialized;
    private SharedPreferences.Editor editor;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWelcome = findViewById(R.id.tvWelcome);
        dialogs = new Dialogs(this);
        sharedPreferences = getSharedPreferences("SharedPreferencesRegister", 0);
        spInitialized = sharedPreferences.contains("initialized");
        //it will return the default if the sharedPreferences isn't init and the name will be Guest maybe
        String fullName = sharedPreferences.getString(Constants.USER_FIRST_NAME_KEY, "Guest") + sharedPreferences.getString(Constants.USER_LAST_NAME_KEY, "");
        if(sharedPreferences.getBoolean("isAdmin", false)) fullName += " (Admin)";
        tvWelcome.setText("Welcome " + fullName + "!");

        myDatabase = MyDatabase.getInstance(this);

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