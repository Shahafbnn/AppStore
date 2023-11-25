package com.example.finalproject.Activities;

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
import android.util.Log;
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

import com.example.finalproject.Classes.LocationAddress;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.User;
import com.example.finalproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.function.Function;

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
    private boolean spInitialized;
    private SharedPreferences.Editor editor;

    public void saveBitmapInFolder(Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
        String foldersPhotos = "ABC";
        File myDir = new File(Environment.getExternalStorageDirectory(), "/" + foldersPhotos);

        if(!myDir.exists())
        {
            myDir.mkdirs();
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

        if(!PermissionClass.CheckPremission(this)) PermissionClass.RequestPerms(this);

        //shared preferences init:
        sharedPreferences = getSharedPreferences("SharedPreferencesRegister", 0);
        spInitialized = sharedPreferences.contains("initialized");
        if(spInitialized){
            String firstName = sharedPreferences.getString(User.FIRST_NAME_KEY, "Guest");
            Object[] isFirstNameValidated = User.validateFirstName(firstName);
            etTextFirstName.setText(firstName);

            String lastName = sharedPreferences.getString(User.LAST_NAME_KEY, "Guest");
            Object[] isLastNameValidated = User.validateFirstName(lastName);
            etTextLastName.setText(lastName);

            String birthDate = sharedPreferences.getString(User.BIRTH_DATE_KEY, "1/1/1991");
            Object[] isBirthDayValidated = User.validateBirthDate(User.getBirthDateFromString(birthDate));
            if(!(boolean)isBirthDayValidated[0]) etBirthDate.setError((String)isBirthDayValidated[1]);

            double weight = sharedPreferences.getFloat(User.WEIGHT_KEY, 50.5f);
            Object[] isWeightValidated = User.validateWeight(weight);
            etDecimalWeight.setText(""+weight);
            if(!(boolean)isWeightValidated[0]) etDecimalWeight.setError((String)isWeightValidated[1]);

            String email = sharedPreferences.getString(User.EMAIL_ADDRESS_KEY, "example@email.com");
            Object[] isEmailValidated = User.validateEmail(email);
            if(!(boolean)isEmailValidated[0]) etTextEmailAddress.setError((String)isEmailValidated[1]);

            String password = sharedPreferences.getString(User.PASSWORD_KEY, "Pass123!");
            Object[] isPasswordValidated = User.validatePassword(password);
            if(!(boolean)isPasswordValidated[0]) etTextPassword.setError((String)isPasswordValidated[1]);

            String passwordConfirm = sharedPreferences.getString(User.PASSWORD_CONFIRM_KEY, "Pass123!");
            Object[] isPasswordConfirmValidated = User.validatePassword(passwordConfirm);
            if(!(boolean)isPasswordConfirmValidated[0]) etTextPasswordConfirm.setError((String)isPasswordConfirmValidated[1]);


            if(!(boolean)isFirstNameValidated[0]) etTextFirstName.setError("The text is " + isFirstNameValidated[1]);
            if(!(boolean)isLastNameValidated[0]) etTextLastName.setError("The text is " + isLastNameValidated[1]);
            if(!(boolean)isWeightValidated[0]) etDecimalWeight.setError("The text is " + isWeightValidated[1]);




        }
        else{
            editor = sharedPreferences.edit();

            //Indicate that the default shared prefs have been set
            editor.putBoolean("initialized", true);

            editor.commit();
        }

        editor.apply();

    }
    public static void initUserFromSharedPreferences(Object[] isValidated, Runnable userSet, EditText et){
        //Object[] isPasswordConfirmValidated = User.validatePassword(passwordConfirm);
        if((boolean)isValidated[0]){
            userSet.run();
        }
        else {
            et.setError((String)isValidated[1]);
        }
    }


    @Override
    public void onClick(View v) {
        if(v==btnSendData){
            Object[] firstNameValidated = User.validateFirstName(etTextFirstName.getText().toString());
            if(!(boolean)(firstNameValidated[0])){
                etTextFirstName.setError("The text is " + firstNameValidated[1]);
            }
            Object[] lastNameValidated = User.validateLastName(etTextLastName.getText().toString());
            if(!(boolean)(lastNameValidated[0])){
                etTextLastName.setError("The text is " + lastNameValidated[1]);
            }
            boolean birthDateValidated = (!etBirthDate.getText().toString().equals("")) && etBirthDate.getText() != null;
            if(!birthDateValidated) etBirthDate.setError("The text cannot be empty");

            String[] weightString = new String[]{etDecimalWeight.getText().toString()};
            Object[] weightValidated = User.validateWeight(weightString);

            if(!(boolean)weightValidated[0]) etDecimalWeight.setError("The text is " + weightValidated[1]);

            if((boolean)firstNameValidated[0] && (boolean)lastNameValidated[0] && birthDateValidated && (boolean)weightValidated[0]){
                //calendar time stuff
                Calendar calendar = User.getBirthDateFromString(etBirthDate.getText().toString());

                //weight double stuff

                double weightDouble = Double.parseDouble(weightString[0]);
                LocationAddress location = LocationAddress.getLocationAddressFromString(etTextHomeCity.getText().toString(), etTextHomeAddress.getText().toString());
                //change to the database later
                User user = new User(etTextFirstName.getText().toString(), etTextLastName.getText().toString(), calendar, Double.parseDouble(etDecimalWeight.getText().toString()), etTextEmailAddress.getText().toString(), location, etTextPassword.getText().toString(), etPhoneNumber.getText().toString());
                User.getUsersList().add(user);
                //clearing them all so u can add another user

                editor.putString("firstName", etTextFirstName.getText().toString());
                editor.putString("firstName", etTextFirstName.getText().toString());

                etTextFirstName.getText().clear();
                etTextLastName.getText().clear();
                etBirthDate.getText().clear();
                etDecimalWeight.getText().clear();

            }

        }
        else if(v==etBirthDate){
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
        else if(v==ivGallery){
            startGallery();
        }
        else if(v==ivCamera){
            startCamera();
        }
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