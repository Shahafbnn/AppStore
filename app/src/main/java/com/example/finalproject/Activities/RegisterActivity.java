package com.example.finalproject.Activities;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.User;
import com.example.finalproject.R;

import java.io.IOException;
import java.util.LinkedList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSendData;
    EditText etdBirthDate,ettFirstName,ettLastName,etndWeight;
    LinkedList<User> users;
    ImageView ivGallery,ivImage,ivCamera;
    boolean isFromCamera, isFromGallery;
    private Uri uriPhoto;
    private Bitmap photoBitmap;

    private SharedPreferences sharedPreferences;
    private boolean spInitialized;
    private SharedPreferences.Editor editor;


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
        etdBirthDate = findViewById(R.id.etdBirthDate);
        etdBirthDate.setOnClickListener(this);

        ettFirstName = findViewById(R.id.ettFirstName);
        ettLastName = findViewById(R.id.ettLastName);
        etndWeight = findViewById(R.id.etndWeight);

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
            String firstName = sharedPreferences.getString("firstName", "default");
            Object[] isFirstNameValidated = User.validateFirstName(firstName);
            ettFirstName.setText(firstName);

            String lastName = sharedPreferences.getString("lastName", "default");
            Object[] isLastNameValidated = User.validateFirstName(lastName);
            ettLastName.setText(lastName);

            String birthDate = sharedPreferences.getString("birthDate", "default");
            boolean birthDateValidated = (!birthDate.equals("")) && birthDate != null;
            if(!birthDateValidated) etdBirthDate.setError("The text cannot be empty");

            String[] weight = new String[]{sharedPreferences.getString("weight", "default")};
            Object[] isWeightValidated = User.validateWeight(weight);
            etndWeight.setText(weight[0]);



            if(!(boolean)isFirstNameValidated[0]) ettFirstName.setError("The text is " + isFirstNameValidated[1]);
            if(!(boolean)isLastNameValidated[0]) ettLastName.setError("The text is " + isLastNameValidated[1]);
            if(!(boolean)isWeightValidated[0]) etndWeight.setError("The text is " + isWeightValidated[1]);




        }
        else{
            editor = sharedPreferences.edit();

            //Indicate that the default shared prefs have been set
            editor.putBoolean("initialized", true);

            editor.commit();
        }

        editor.apply();

    }



    @Override
    public void onClick(View v) {
        if(v==btnSendData){
            Object[] firstNameValidated = User.validateFirstName(ettFirstName.getText().toString());
            if(!(boolean)(firstNameValidated[0])){
                ettFirstName.setError("The text is " + firstNameValidated[1]);
            }
            Object[] lastNameValidated = User.validateLastName(ettLastName.getText().toString());
            if(!(boolean)(lastNameValidated[0])){
                ettLastName.setError("The text is " + lastNameValidated[1]);
            }
            boolean birthDateValidated = (!etdBirthDate.getText().toString().equals("")) && etdBirthDate.getText() != null;
            if(!birthDateValidated) etdBirthDate.setError("The text cannot be empty");

            String[] weightString = new String[]{etndWeight.getText().toString()};
            Object[] weightValidated = User.validateWeight(weightString);

            if(!(boolean)weightValidated[0]) etndWeight.setError("The text is " + weightValidated[1]);

            if((boolean)firstNameValidated[0] && (boolean)lastNameValidated[0] && birthDateValidated && (boolean)weightValidated[0]){
                //calendar time stuff
                Calendar calendar = User.setBirthDateFromString(etdBirthDate.getText().toString());

                //weight double stuff

                double weightDouble = Double.parseDouble(weightString[0]);

                //change to the database later
                User user = new User(ettFirstName.getText().toString(), ettLastName.getText().toString(), calendar, Double.parseDouble(etndWeight.getText().toString()));
                User.getUsersList().add(user);
                //clearing them all so u can add another user

                editor.putString("firstName", ettFirstName.getText().toString());
                editor.putString("firstName", ettFirstName.getText().toString());

                ettFirstName.getText().clear();
                ettLastName.getText().clear();
                etdBirthDate.getText().clear();
                etndWeight.getText().clear();

            }

        }
        else if(v==etdBirthDate){
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String time = dayOfMonth + "/" + month + "/" + year;
                    etdBirthDate.setText(time);
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