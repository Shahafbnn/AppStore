package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.*;

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
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.DatabaseClasses.CitiesArray;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private User curUser;
    private boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore db;

    private MenuItem registerActivityMenuItemRandomData;
    private PermissionClass perms;


    ActivityResultLauncher<Intent> startFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();
        perms = new PermissionClass(this);


        startFile  = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            if(isFromCamera){
                                try{
                                    photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
                                } catch (IOException e){
                                    Log.e("Runtime Exception", "" + e);
                                }
                            } else if (isFromGallery) {
                                uriPhoto = result.getData().getData();
                                try{
                                    photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhoto);
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

        //getting the user from the intent
        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;

        btnSendData = findViewById(R.id.btnUploadAppSendData);
        btnSendData.setOnClickListener(this);
        etBirthDate = findViewById(R.id.etBirthDate);
        etBirthDate.setOnClickListener(this);

        etTextFirstName = findViewById(R.id.etUploadAppName);
        etTextLastName = findViewById(R.id.etUploadAppDescription);
        etDecimalWeight = findViewById(R.id.etUploadAppPrice);
        etPhoneNumber = findViewById(R.id.etUploadAppDiscountPercentage);
        etTextPassword = findViewById(R.id.etTextPassword);
        etTextPasswordConfirm = findViewById(R.id.etTextPasswordConfirm);
        etTextEmailAddress = findViewById(R.id.etTextEmailAddress);
        actvTextHomeCity = (AutoCompleteTextView) findViewById(R.id.actvUploadAppMainCategory);
        etTextHomeAddress = findViewById(R.id.etTextHomeAddress);

        ivGallery = findViewById(R.id.ivUploadAppGallery);
        ivGallery.setOnClickListener(this);
        ivImage = findViewById(R.id.ivUploadAppImage);

        ivCamera = findViewById(R.id.ivUploadAppCamera);
        ivCamera.setOnClickListener(this);

        if(!perms.CheckPermission(this)) perms.RequestPerms(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, CitiesArray.getArrCities());

        actvTextHomeCity.setAdapter(adapter);


        if(isUserSignedIn){
            final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress, actvTextHomeCity, etTextHomeAddress};
            final Object[] DATA = {curUser.getUserFirstName(), curUser.getUserLastName(), Double.toString(curUser.getUserWeight()),curUser.birthdateToString(), curUser.getUserPhoneNumber(), curUser.getUserPassword(), curUser.getUserEmail(), curUser.getHomeCityName(), curUser.getUserHomeAddress()};
            //editValue[0] = phoneNumber, editValue[1] = email
            //to check if the data is good and set the ET errors.
            InitiateFunctions.initUser(DATA, ETS);
            etTextPasswordConfirm.setText(curUser.getUserPassword());
            if(!User.isPasswordConfirmed(curUser.getUserPassword(), etTextPasswordConfirm.getText().toString())) etTextPasswordConfirm.setError("password confirm isn't equal to password");

            StorageFunctions.setImage(this, ivImage, curUser.getUserImagePath());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_register_menu, menu);

        registerActivityMenuItemRandomData = menu.findItem(R.id.registerActivityMenuItemRandomData);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==registerActivityMenuItemRandomData){
            fillWithRandomData();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
    private void fillWithRandomData(){
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
    private String getFullPath(User user){
        String specialPath = "" + user.getUserPhoneNumber() + new SimpleDateFormat("ddMMyy-HHmmss.SSSS").format(new Date()) + ".jpg";
        String foldersPhotos = "Photos/";
        return foldersPhotos + specialPath;
    }
    private void saveBitmapAndFinish(User user){
        if(!isUserSignedIn || photoBitmap != null){
            if (photoBitmap == null) {
                photoBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.emptypfp);
            }

            String fullPath = user.getUserImagePath();

            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef = storage.getReference();
            StorageReference imagesRef = storageRef.child(fullPath);


            StorageFunctions.uploadAndCompressBitmapToFirestore(this, photoBitmap, imagesRef, task -> {
                if (task.isSuccessful()) {
                    finishActivity(true, user);
                } else {
                    Toast.makeText(getApplicationContext(), "Image upload failed, please try again!", Toast.LENGTH_LONG).show();
                    btnSendData.setClickable(true);
                }
            }, null);

        }
        else finishActivity(true, user);
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
            if(!perms.CheckPermission(this)){
                perms.RequestPerms(this);
                Log.v("Image", "RequestPerms has failed in ivGallery");
                Toast.makeText(this, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
            }
            else startGallery();
        }
        else if(v==ivCamera){
            if(!perms.CheckPermission(this)){
                perms.RequestPerms(this);
                Log.v("Image", "RequestPerms has failed in ivCamera");
                Toast.makeText(this, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
            }
            else startCamera();
        }
    }

    private void sendData() {
        //FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL
        //using my validation system to validate all the ETs
        final EditText[] ETS = {etTextFirstName, etTextLastName, etDecimalWeight, etBirthDate, etPhoneNumber, etTextPassword, etTextEmailAddress, actvTextHomeCity, etTextHomeAddress};
        boolean allValid;
        final Object[] data = {etTextFirstName.getText().toString(), etTextLastName.getText().toString(), etDecimalWeight.getText().toString(),
                etBirthDate.getText().toString(), etPhoneNumber.getText().toString(), etTextPassword.getText().toString(), etTextEmailAddress.getText().toString(), actvTextHomeCity.getText().toString(), etTextHomeAddress.getText().toString()};
        //for(int i = 0; i < ETS.length; i++) data[i] = ETS[i].getText().toString();
        allValid = InitiateFunctions.initUser(data, ETS);

        if(!User.isPasswordConfirmed(etTextPassword.getText().toString(), etTextPasswordConfirm.getText().toString())) {
            etTextPasswordConfirm.setError("password confirm isn't equal to password");
            Toast.makeText(this, "All must be correct!", Toast.LENGTH_LONG).show();
            allValid = false;
        }
        if(allValid){
            btnSendData.setClickable(false);
            db.collection("users")
                    .whereEqualTo("userEmail", etTextEmailAddress.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            boolean isValid = true;
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    if(documentSnapshot.exists()){
                                        if(!(isUserSignedIn && (documentSnapshot.getId().equals(curUser.getUserId())))){
                                            etTextEmailAddress.setError("Email is already in use!");
                                            isValid = false;
                                            btnSendData.setClickable(true);
                                        }
                                    }
                                }

                            }
                            else btnSendData.setClickable(true);
                            if(isValid){
                                db.collection("users")
                                        .whereEqualTo("userPhoneNumber", etPhoneNumber.getText().toString())
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            boolean isValid1 = true;
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                                    if(documentSnapshot.exists()){
                                                        if(!(isUserSignedIn && (documentSnapshot.getId().equals(curUser.getUserId())))){
                                                            etPhoneNumber.setError("Phone number is already in use!");
                                                            isValid1 = false;
                                                            btnSendData.setClickable(true);
                                                        }
                                                    }
                                                }

                                            } else btnSendData.setClickable(true);
                                            if(isValid1){
                                                db.collection("cities")
                                                        .whereEqualTo("cityName", actvTextHomeCity.getText().toString().toUpperCase())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                                if (task1.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                                                        if(documentSnapshot.exists()){
                                                                            User u = new User();
                                                                            u.setUserFirstName(etTextFirstName.getText().toString());
                                                                            u.setUserLastName(etTextLastName.getText().toString());
                                                                            u.setUserBirthDate(User.getDateFromString(etBirthDate.getText().toString()));
                                                                            u.setUserWeight(Double.parseDouble(etDecimalWeight.getText().toString()));
                                                                            u.setUserEmail(etTextEmailAddress.getText().toString());
//            u.setHomeCityId(db.collection("cities").whereEqualTo("cityName", actvTextHomeCity.getText().toString().toUpperCase()));
                                                                            u.setHomeCityName(actvTextHomeCity.getText().toString().toUpperCase());
                                                                            u.setUserHomeAddress(etTextHomeAddress.getText().toString());
                                                                            u.setUserPassword(etTextPassword.getText().toString());
                                                                            u.setUserPhoneNumber(etPhoneNumber.getText().toString());
                                                                            // update the user's admin-ability
                                                                            u.setUserIsAdmin(User.isAdmin(etPhoneNumber.getText().toString()));
                                                                            if(!isUserSignedIn){
                                                                                u.setUserImagePath(getFullPath(u));
                                                                            }
                                                                            if(isUserSignedIn){
                                                                                u.setUserImagePath(curUser.getUserImagePath());
                                                                                u.setUserId(curUser.getUserId());
                                                                                db.collection("users").document(curUser.getUserId()).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                                                        if (task1.isSuccessful()) {
                                                                                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_LONG).show();
                                                                                            try {
                                                                                                saveBitmapAndFinish(u);
                                                                                            } catch (NullPointerException e){
                                                                                                Log.e("debug", "NullPointerException for OnComplete RegisterActivity: " + e);
                                                                                            }
                                                                                        } else {
                                                                                            Toast.makeText(getApplicationContext(), "Unsuccessful! Please try again!", Toast.LENGTH_LONG).show();
                                                                                            Log.d("debug", "set failed with ", task1.getException());
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                            else{
                                                                                db.collection("users").add(u).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentReference> task1) {
                                                                                        if (task1.isSuccessful()) {
                                                                                            DocumentReference documentReference = task1.getResult();
                                                                                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_LONG).show();
                                                                                            // You can get the ID of the newly added document using documentReference.getId()
                                                                                            String docId = documentReference.getId();
                                                                                            InitiateFunctions.setSharedPreferencesData(editor, docId);
                                                                                            u.setUserId(docId);
//                                                                                                // attaching MainActivity's snapshotListener: userDocRef.addSnapshotListener(MainActivity.this, snapshotListener); they can't be null since you can't get to RegisterActivity before MainActivity's OnCreate.
//                                                                                                Activity act = InitiateFunctions.getUpdateUserSnapshotListenerActivity();
//                                                                                                EventListener<DocumentSnapshot> snap = InitiateFunctions.getUpdateUserSnapshotListener();
//                                                                                                db.collection("users").document(docId).addSnapshotListener(act, snap);
                                                                                            try {
                                                                                                saveBitmapAndFinish(u);
                                                                                            } catch (NullPointerException e){
                                                                                                Log.e("debug", "NullPointerException for OnComplete RegisterActivity: " + e);
                                                                                            }
                                                                                        } else {
                                                                                            Toast.makeText(getApplicationContext(), "Unsuccessful! Please try again!", Toast.LENGTH_LONG).show();
                                                                                            Log.d("debug", "add failed with ", task1.getException());
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                        else {
                                                                            actvTextHomeCity.setError("City does not exist!");
                                                                            Toast.makeText(getApplicationContext(), "City does not exist!", Toast.LENGTH_LONG).show();
                                                                            btnSendData.setClickable(true);
                                                                        }
                                                                    }
                                                                } else {
                                                                    Log.w("debug", "Task unsuccessful", task1.getException());
                                                                    Toast.makeText(getApplicationContext(), "Task unsuccessful!", Toast.LENGTH_LONG).show();
                                                                    btnSendData.setClickable(true);
                                                                }
                                                            }
                                                        });
                                            }
                                        });
                            }
                        }
                    });
        }
        else Toast.makeText(this, "All EditTexts must be correct!", Toast.LENGTH_LONG).show();
    }

    private void finishActivity(boolean result, User user){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.INTENT_CURRENT_USER_KEY, user);
        if(result) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void etBirthDateOnClick(){
        String lastDate = etBirthDate.getText().toString();
        boolean isLastDate = !lastDate.equals("");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if(isLastDate){
            Calendar lastCal = Calendar.getInstance();
            lastCal.setTime((Objects.requireNonNull(User.getDateFromString(lastDate))));
            day = lastCal.get(Calendar.DAY_OF_MONTH);
            month = lastCal.get(Calendar.MONTH);
            year = lastCal.get(Calendar.YEAR);
        }


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