package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.*;
import static com.example.finalproject.Classes.UserValidations.validate;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.User;
import com.example.finalproject.Classes.UserValidations;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSendData;
    EditText etTextFirstName,etTextLastName,etBirthDate,etDecimalWeight,etPhoneNumber,
            etTextPassword,etTextPasswordConfirm,etTextEmailAddress,etTextHomeCity,etTextHomeAddress;
    LinkedList<User> users;
    ImageView ivGallery,ivImage,ivCamera;
    boolean isFromCamera, isFromGallery;
    private Uri uriPhoto;
    private Bitmap photoBitmap;

    private SharedPreferences sharedPreferences;
    private boolean isSPInitialized;
    private boolean isUserInDB;
    private SharedPreferences.Editor editor;
    private MyDatabase myDatabase;
    private User curUser;
    private boolean isSPValid;
    private String myDirStr;

    public void saveBitmapInFolder(Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
        String foldersPhotos = "ABC";
        File myDir = new File(Environment.getExternalStorageDirectory(), "/" + foldersPhotos);

        if(!myDir.exists())
        {
            myDir.mkdirs();
            myDirStr = myDir.toString();
        }

        File dest = new File(myDir, timeStamp);
        try {
            FileOutputStream out = new FileOutputStream(dest);
        } catch (Exception e){

        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register_shared_preference);
//
//        sp = getSharedPreferences("SharedPreferencesSignUp", 0);
//        editor = sp.edit();
//
//        // Let's say you want to store a string
//        String key = "exampleKey";
//        String value = "exampleValue";
//
//        // Store the string in SharedPreferences
//        editor.putString(key, value);
//        editor.apply();
//
//        // Now let's retrieve the string from SharedPreferences
//        String retrievedValue = sp.getString(key, "default");
//
//        Log.d("SharedPreferences", "Stored value: " + sp.getString(key, null));
//
//        // At this point, 'retrievedValue' should be equal to 'exampleValue'
//    }

    //this object gets the result of the camera/gallery activity and sets the image in ivImage.
    ActivityResultLauncher<Intent> startFile = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        if(isFromCamera){
                            try{
                                photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
                                saveBitmapInFolder(photoBitmap);
                            } catch (IOException e){
                                throw new RuntimeException(e);
                            }
                        } else if (isFromGallery) {
                            uriPhoto = result.getData().getData();
                            try{
                                photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
                            } catch (IOException e){
                                throw new RuntimeException(e);
                            }

                        }
                        ivImage.setImageURI(uriPhoto);

                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDatabase = MyDatabase.getInstance(this);
        Object o = InitiateFunctions.initUserSharedPreferences(sharedPreferences, this, myDatabase);
        if(o instanceof String){
            if(((String)o).equals("SP")) {
                isSPValid = false;
                isUserInDB = false;
            }
            if(((String)o).equals("User")) {
                isSPValid = true;
                isUserInDB = false;
            }
        }
        else curUser = (User)o;



        btnSendData = findViewById(R.id.btnSendData);
        btnSendData.setOnClickListener(this);
        etBirthDate = findViewById(R.id.etBirthDate);
        etBirthDate.setOnClickListener(this);

        etTextFirstName = findViewById(R.id.etTextFirstName);
        etTextLastName = findViewById(R.id.etTextLastName);
        etDecimalWeight = findViewById(R.id.etDecimalWeight);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etTextPassword = findViewById(R.id.etTextPassword);
        etTextPasswordConfirm = findViewById(R.id.etTextPasswordConfirm);
        etTextEmailAddress = findViewById(R.id.etTextEmailAddress);
        etTextHomeCity = findViewById(R.id.etTextHomeCity);
        etTextHomeAddress = findViewById(R.id.etTextHomeAddress);

        ivGallery = findViewById(R.id.ivGallery);
        ivGallery.setOnClickListener(this);
        ivImage = findViewById(R.id.ivImage);

        ivCamera = findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(this);

        if(!PermissionClass.CheckPermission(this)) PermissionClass.RequestPerms(this);

        //shared preferences init:
        sharedPreferences = getSharedPreferences("SharedPreferencesRegister", 0);
        isSPInitialized = sharedPreferences.contains("initialized");
        if(isSPInitialized && isSPValid && isUserInDB){
            //FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL
            final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress};
            final Object[] DATA = {curUser.getFirstName(), curUser.getLastName(), curUser.getWeight(),curUser.getBirthDate(), curUser.getPhoneNumber(), curUser.getPassword(), curUser.getEmail()};
            InitiateFunctions.initUser(DATA, ETS);
        }
        else{
            editor = sharedPreferences.edit();

            //Indicate that the default shared prefs have been set
            editor.putBoolean("initialized", true);

            editor.commit();
        }

    }


    @Override
    public void onClick(View v) {
        if(v==btnSendData){
            sendData();
        }
        else if(v==etBirthDate){
            etBirthDateOnClick();
        }
        else if(v==ivGallery){
            startGallery();
        }
        else if(v==ivCamera){
            startCamera();
        }
    }

    private void sendData() {
        //FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL
        final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress};
        boolean isValid = InitiateFunctions.initUser(ETS);
        if(isValid){
            //long id, String firstName, String lastName, Date birthDate, Double weight, String email, long homeCityId, String homeAddress, String password, String phoneNumber, boolean isAdmin, String imgSrc
            User u = new User(0, etTextFirstName.getText().toString(), etTextLastName.getText().toString(), User.getDateFromString(etTextLastName.getText().toString()), Double.parseDouble(etDecimalWeight.getText().toString()), etTextEmailAddress.getText().toString(), myDatabase.cityDAO().getCityByName(etTextHomeCity.getText().toString()).getCityId()
                    , etTextHomeAddress.getText().toString(), etTextPassword.getText().toString(), etPhoneNumber.getText().toString(), User.isAdmin(etPhoneNumber.getText().toString()), myDirStr);
            myDatabase.userDAO().insert(u);
        }
    }

    private void etBirthDateOnClick(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String time = dayOfMonth + "/" + month + "/" + year;
                etBirthDate.setText(time);
//                    Toast myToast = Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG);
//                    myToast.show();
            }
        }, year, month, day);

        //limits to 16 years
        calendar.add(Calendar.YEAR, -16);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void startCamera(){
        isFromCamera = true;
        isFromGallery = false;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        uriPhoto = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
        startFile.launch(intent);

    }
    public void startGallery(){
        isFromCamera = false;
        isFromGallery = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startFile.launch(intent);
    }
}