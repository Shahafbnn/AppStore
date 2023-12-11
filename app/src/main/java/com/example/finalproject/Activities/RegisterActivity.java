package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.*;
import static com.example.finalproject.Classes.UserValidations.validate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSendData;
    private EditText etTextFirstName,etTextLastName,etBirthDate,etDecimalWeight,etPhoneNumber,
            etTextPassword,etTextPasswordConfirm,etTextEmailAddress,etTextHomeCity,etTextHomeAddress;
    private ImageView ivGallery,ivImage,ivCamera;
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
    private Date curUserDate;





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
                                StorageFunctions.saveBitmapInFolder(photoBitmap);
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
        //shared preferences init:
        //vals[0] = isSPValid, vals[1] = isUserInDB, vals[2] = isSPInitialized
        //the bool array is like a c pointer, to change the actual value;
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();
        curUser = InitiateFunctions.initUserSharedPreferences(sharedPreferences, myDatabase, new Boolean[]{isSPValid, isUserInDB, isSPInitialized});

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

        if(isSPInitialized && isSPValid && isUserInDB){
            //FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL
            final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress};
            final Object[] DATA = {curUser.getFirstName(), curUser.getLastName(), curUser.getWeight(),curUser.getBirthDate(), curUser.getPhoneNumber(), curUser.getPassword(), curUser.getEmail()};
            //editValue[0] = phoneNumber, editValue[1] = email
            InitiateFunctions.initUser(DATA, ETS, this, new String[]{curUser.getPhoneNumber(), curUser.getEmail()});
            etTextPasswordConfirm.setText(curUser.getPassword());
            if(User.isPasswordConfirmed(curUser.getPassword(), etTextPasswordConfirm.getText().toString())) etTextPasswordConfirm.setError("password confirm isn't equal to password");
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
        final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress, etTextHomeCity, etTextHomeAddress};
        boolean allValid = InitiateFunctions.initUser(ETS);
        if(!User.isPasswordConfirmed(etTextPassword.getText().toString(), etTextPasswordConfirm.getText().toString())) {
            etTextPasswordConfirm.setError("password confirm isn't equal to password");
            allValid = false;
        }
        else if(allValid){
            //long id, String firstName, String lastName, Date birthDate, Double weight, String email, long homeCityId, String homeAddress, String password, String phoneNumber, boolean isAdmin, String imgSrc
            User u = new User();
            u.setFirstName(etTextFirstName.getText().toString());
            u.setLastName(etTextLastName.getText().toString());
            u.setBirthDate(User.getDateFromString(etBirthDate.getText().toString()));
            u.setWeight(Double.parseDouble(etDecimalWeight.getText().toString()));
            u.setEmail(etTextEmailAddress.getText().toString());
            u.setHomeCityId(myDatabase.cityDAO().getCityByName(etTextHomeCity.getText().toString()).getCityId());
            u.setHomeAddress(etTextHomeAddress.getText().toString());
            u.setPassword(etTextPassword.getText().toString());
            u.setPhoneNumber(etPhoneNumber.getText().toString());
            u.setAdmin(User.isAdmin(etPhoneNumber.getText().toString()));
            u.setImgSrc(myDirStr);

            // put the user id in the sharedPreference to stay signed in.
            long id = myDatabase.userDAO().insert(u);
            editor.clear();
            editor.putLong(USER_ID_KEY, id);
            editor.putBoolean(SHARED_PREFERENCES_KEY, true);
            editor.commit();

            //clear all the EditTexts
            for(int i = 0; i < ETS.length; i++){
                ETS[i].setText("");
            }
            Toast.makeText(this, "Successful!", Toast.LENGTH_LONG).show();
            finish();

        }
        else Toast.makeText(this, "All EditTexts must be correct!", Toast.LENGTH_LONG).show();
    }

    private void finishActivity(boolean result){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(REGISTER_ACTIVITY_RETURN_DATA_KEY, result);
        if(result) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
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