package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.*;
import static com.example.finalproject.Classes.InitiateFunctions.initUserSharedPreferences;
import static com.example.finalproject.Classes.UserValidations.validate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.MyPair;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.DatabaseClasses.CitiesArray;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

import java.io.IOException;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSendData;
    private EditText etTextFirstName,etTextLastName,etBirthDate,etDecimalWeight,etPhoneNumber,
            etTextPassword,etTextPasswordConfirm,etTextEmailAddress, etTextHomeAddress;
    private AutoCompleteTextView actvTextHomeCity;
    private ImageView ivGallery,ivImage,ivCamera;
    boolean isFromCamera, isFromGallery;
    private Uri uriPhoto;
    private Bitmap photoBitmap;

    private SharedPreferences sharedPreferences;
    private boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private MyDatabase myDatabase;
    private User curUser;
    private String myDirStr;

    ActivityResultLauncher<Intent> startFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDatabase = MyDatabase.getInstance(this);

        startFile  = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            if(isFromCamera){
                                try{
                                    photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
                                    //check if the data was saved correctly
                                    MyPair<Boolean, String> validationPair = StorageFunctions.saveBitmapInPath(photoBitmap);
                                    if(validationPair.getFirst()) myDirStr = validationPair.getSecond();
                                    else {
                                        myDirStr = null;
                                        Log.e("Runtime Exception", "" + "onActivityResult image saving failed.");
                                    }
                                } catch (IOException e){
                                    Log.e("Runtime Exception", "" + e);
                                }
                            } else if (isFromGallery) {
                                uriPhoto = result.getData().getData();
                                try{
                                    photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
                                    //check if the data was saved correctly
                                    MyPair<Boolean, String> validationPair = StorageFunctions.saveBitmapInPath(photoBitmap);
                                    if(validationPair.getFirst()) myDirStr = validationPair.getSecond();
                                    else {
                                        myDirStr = null;
                                        Log.e("Runtime Exception", "" + "onActivityResult image saving failed.");
                                    }
                                } catch (IOException e){
                                    Log.e("Runtime Exception", "" + e);
                                }

                            }
                            ivImage.setImageBitmap(photoBitmap);

                        }
                    }
                });


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();

        //checking if the user is saved in the SP and initializing vars if it is.
        MyPair<ValidationData, User> validationPair = initUserSharedPreferences(sharedPreferences, myDatabase);
        isUserSignedIn = validationPair.getFirst().isValid();
        if(!isUserSignedIn) Log.v("SignIn", validationPair.getFirst().getError());
        else curUser = validationPair.getSecond();

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
        actvTextHomeCity = (AutoCompleteTextView) findViewById(R.id.actvTextHomeCity);
        etTextHomeAddress = findViewById(R.id.etTextHomeAddress);

        ivGallery = findViewById(R.id.ivGallery);
        ivGallery.setOnClickListener(this);
        ivImage = findViewById(R.id.ivImage);

        ivCamera = findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(this);

        if(!PermissionClass.CheckPermission(this)) PermissionClass.RequestPerms(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CitiesArray.getArrCities());

        actvTextHomeCity.setAdapter(adapter);


        if(isUserSignedIn){
            final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress, actvTextHomeCity, etTextHomeAddress};
            final Object[] DATA = {curUser.getFirstName(), curUser.getLastName(), Double.toString(curUser.getWeight()),curUser.birthdateToString(), curUser.getPhoneNumber(), curUser.getPassword(), curUser.getEmail(), myDatabase.cityDAO().getCityById(curUser.getHomeCityId()).getCityName(), curUser.getHomeAddress()};
            //editValue[0] = phoneNumber, editValue[1] = email
            InitiateFunctions.initUser(DATA, ETS, this, new String[]{curUser.getPhoneNumber(), curUser.getEmail()});
            etTextPasswordConfirm.setText(curUser.getPassword());
            if(!User.isPasswordConfirmed(curUser.getPassword(), etTextPasswordConfirm.getText().toString())) etTextPasswordConfirm.setError("password confirm isn't equal to password");
            ivImage.setImageURI(curUser.getImgUri(this));
        }

        // ignore this
        boolean setDevData = false;
        if(setDevData){
            char[] nameStr = "abcdefghijklmnopqrstubwxyz".toCharArray();
            String specialFirstName = "";
            String specialLastName = "";

            Random rand = new Random();
            int num = rand.nextInt(7) + 3;
            for(int i = 0; i < num; i++){
                if(i == 0){
                    specialFirstName += ("" + nameStr[rand.nextInt(nameStr.length)]).toUpperCase();
                    specialLastName += ("" + nameStr[rand.nextInt(nameStr.length)]).toUpperCase();
                }
                specialFirstName += nameStr[rand.nextInt(nameStr.length)];
                specialLastName += nameStr[rand.nextInt(nameStr.length)];
            }
//            User addUser = new User();
//            addUser.setFirstName(specialFirstName);
//            addUser.setLastName(specialLastName);
//            addUser.setBirthDate(User.getDateFromString(etBirthDate.getText().toString()));
//            addUser.setWeight(Double.parseDouble(etDecimalWeight.getText().toString()));
//            addUser.setEmail(etTextEmailAddress.getText().toString());
//            addUser.setHomeCityId(myDatabase.cityDAO().getCityByName(actvTextHomeCity.getText().toString().toUpperCase()).getCityId());
//            addUser.setHomeAddress(etTextHomeAddress.getText().toString());
//            addUser.setPassword(etTextPassword.getText().toString());
//            addUser.setPhoneNumber(etPhoneNumber.getText().toString());
//            addUser.setAdmin(User.isAdmin(etPhoneNumber.getText().toString()));
//            addUser.setImgSrc(myDirStr);

            etTextFirstName.setText(specialFirstName);
            etTextLastName.setText(specialLastName);
            etDecimalWeight.setText("99");
            etPhoneNumber.setText("05867733" + (rand.nextInt(90) + 10));
            etTextPassword.setText("Pass1234!");
            etTextPasswordConfirm.setText(etTextPassword.getText().toString());
            etTextEmailAddress.setText("email"+(rand.nextInt(90) + 10)+"@email.email");
            actvTextHomeCity.setText("Abbirim");
            etTextHomeAddress.setText("Home 1");
            etBirthDate.setText("13/12/2000");

        }
    }


    @Override
    public void onClick(View v) {
        if(v==btnSendData){
            sendData();
        }
        else if(v==etBirthDate){
            etBirthDate.setError(null);
            etBirthDateOnClick();
        }
        else if(v==ivGallery){
            if(!PermissionClass.CheckPermission(this)){
                PermissionClass.RequestPerms(this);
                Log.v("Image", "RequestPerms has failed in ivGallery");
                Toast.makeText(this, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
            }
            else startGallery();
        }
        else if(v==ivCamera){
            if(!PermissionClass.CheckPermission(this)){
                PermissionClass.RequestPerms(this);
                Log.v("Image", "RequestPerms has failed in ivCamera");
                Toast.makeText(this, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
            }
            else startCamera();
        }
    }

    private void sendData() {
        //FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL
        final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress, actvTextHomeCity, etTextHomeAddress};
        boolean allValid;
        final Object[] data = {etTextFirstName.getText().toString(), etTextLastName.getText().toString(), etDecimalWeight.getText().toString(),
                etBirthDate.getText().toString(), etPhoneNumber.getText().toString(), etTextPassword.getText().toString(), etTextEmailAddress.getText().toString(), actvTextHomeCity.getText().toString(), etTextHomeAddress.getText().toString()};
        //for(int i = 0; i < ETS.length; i++) data[i] = ETS[i].getText().toString();
        if(isUserSignedIn){
            //editValue[0] = phoneNumber, editValue[1] = email
            allValid = InitiateFunctions.initUser(data, ETS, this, new String[]{curUser.getPhoneNumber(), curUser.getEmail()});
        }else{
            allValid = InitiateFunctions.initUser(data, ETS, this);
        }
        if(!User.isPasswordConfirmed(etTextPassword.getText().toString(), etTextPasswordConfirm.getText().toString())) {
            etTextPasswordConfirm.setError("password confirm isn't equal to password");
            Toast.makeText(this, "All EditTexts must be correct!", Toast.LENGTH_LONG).show();
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
            u.setHomeCityId(myDatabase.cityDAO().getCityByName(actvTextHomeCity.getText().toString().toUpperCase()).getCityId());
            u.setHomeAddress(etTextHomeAddress.getText().toString());
            u.setPassword(etTextPassword.getText().toString());
            u.setPhoneNumber(etPhoneNumber.getText().toString());
            u.setAdmin(User.isAdmin(etPhoneNumber.getText().toString()));
            u.setImgSrc(myDirStr);


            if(isUserSignedIn) {
                if(myDirStr==null) u.setImgSrc(curUser.getImgSrc());
                u.setId(curUser.getId());
                myDatabase.userDAO().update(u);
                curUser = u;
            }
            else {
                // put the user id in the sharedPreference to stay signed in.
                long id = myDatabase.userDAO().insert(u);
                editor.clear();
                editor.putLong(USER_ID_KEY, id);
                editor.putBoolean(SHARED_PREFERENCES_INITIALIZED_KEY, true);
                editor.commit();
            }
            Log.v("sharedPreferences", "sharedPreferences.getAll(): " + sharedPreferences.getAll().toString());
            //clear all the EditTexts even tho we close the activity.
//            for(int i = 0; i < ETS.length; i++){
//                ETS[i].setText("");
//            }
            //Toast.makeText(this, "Successful!", Toast.LENGTH_LONG).show();
            finishActivity(true);
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
                String time = dayOfMonth + "/" + (month + 1) + "/" + year;
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