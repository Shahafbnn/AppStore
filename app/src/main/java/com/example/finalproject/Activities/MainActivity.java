package com.example.finalproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.Dialogs;
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
    private boolean isSpValid;
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
        initUserSharedPreferences();
    }

    //making it so it's repeatable in other classes/activities
    public void initUserSharedPreferences(){
        sharedPreferences = getSharedPreferences("SharedPreferencesRegister", 0);
        if (sharedPreferences==null || sharedPreferences.contains("initialized")) {
            isSpValid = false;
            return;
        }

        long id = sharedPreferences.getLong(Constants.USER_ID_KEY, -1);
        if(id<0){
            isSpValid = false;
            return;
        }
        curUser = myDatabase.userDAO().get(id);


    }

    //uses curUser and turns the
    public void initViewsFromUser(User user, boolean isValid){
        if(isValid){
            String fullName = curUser.getFirstName() + " " + curUser.getLastName();
            boolean isAdmin = isAdmin(curUser.getPhoneNumber());
            if(isAdmin != curUser.isAdmin()) {
                curUser.setAdmin(false);
                myDatabase.userDAO().update(curUser);
            }
            if(isAdmin) fullName += " (Admin)";
            tvWelcome.setText("Welcome " + fullName + "!");
            String imageSrc = curUser.getImgSrc();
            if(new File(imageSrc).exists())
            {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageSrc);
                ivProfilePic.setImageBitmap(imageBitmap);
            }
            else Log.e("Bitmap", "pfp image path does not exist: " + imageSrc);
            throw new RuntimeException("pfp image path does not exist: " + imageSrc);
        }
    }

    public boolean isAdmin(String phoneNumber){
        for (String s:Constants.ADMIN_PHONE_NUMBERS) {
            if (phoneNumber.equals(s)) return true;
        }
        return false;
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