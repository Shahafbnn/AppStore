package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.*;
import static com.example.finalproject.Classes.StorageFunctions.humanReadableByte;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.Category.Categories;
import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.PermissionChoiceView;
import com.example.finalproject.Classes.PermissionClass;
import com.example.finalproject.Classes.StorageFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadAppActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnUploadAppSendData;
    private EditText etUploadAppName, etUploadAppDescription, etUploadAppPrice, etUploadAppDiscountPercentage, etAPKPath;
    private AutoCompleteTextView actvUploadAppMainCategory;
    private ImageView ivUploadAppGallery,ivUploadAppImage,ivUploadAppCamera;
    private TextView tvPerms;
    boolean isFromCamera, isFromGallery;
    private Uri uriPhoto;
    private Bitmap photoBitmap;

    private SharedPreferences sharedPreferences;
    private User curUser;
    private boolean isUserSignedIn;
    private App curApp;
    private boolean isEditingApp;
    private Categories categories;
    private ArrayList<String> categoriesArrayList;
    private FirebaseFirestore db;

    private MenuItem registerActivityMenuItemRandomData;
    ActivityResultLauncher<Intent> activityResultLauncherFileExplorer;
    private Uri apkUri;
    private Dialog permsDialog;
    private ArrayList<String> permsDialogResult;


    ActivityResultLauncher<Intent> startFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_app);
        db = FirebaseFirestore.getInstance();


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
                            ivUploadAppImage.setImageBitmap(photoBitmap);

                        }
                    }
                });

        activityResultLauncherFileExplorer = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                apkUri = data.getData();
                                String filePath = apkUri.getPath();
                                etAPKPath.setText(filePath);
                            }
                        }
                    }
                }
        );


        //getting the user from the intent
        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;

        curApp = (App) getIntent().getSerializableExtra(INTENT_CURRENT_APP_KEY);
        isEditingApp = curApp != null;

        categories = (Categories) getIntent().getSerializableExtra(INTENT_CATEGORIES_KEY);
        categoriesArrayList = categories.getCategories();

        btnUploadAppSendData = findViewById(R.id.btnUploadAppSendData);
        btnUploadAppSendData.setOnClickListener(this);

        etUploadAppName = findViewById(R.id.etUploadAppName);
        etUploadAppDescription = findViewById(R.id.etUploadAppDescription);
        etUploadAppPrice = findViewById(R.id.etUploadAppPrice);
        etUploadAppDiscountPercentage = findViewById(R.id.etUploadAppDiscountPercentage);
        etAPKPath = findViewById(R.id.etAPKPath);
        etAPKPath.setOnClickListener(this);
        actvUploadAppMainCategory = findViewById(R.id.actvUploadAppMainCategory);

        ivUploadAppGallery = findViewById(R.id.ivUploadAppGallery);
        ivUploadAppGallery.setOnClickListener(this);
        ivUploadAppImage = findViewById(R.id.ivUploadAppImage);

        ivUploadAppCamera = findViewById(R.id.ivUploadAppCamera);
        ivUploadAppCamera.setOnClickListener(this);

        tvPerms = findViewById(R.id.tvPerms);
        permsDialog = new Dialog(this);
        permsDialog.setContentView(new PermissionChoiceView(this, PermissionClass.getAllPermsStrings(), permsDialogResult, permsDialog, tvPerms, 50));
        tvPerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permsDialog.show();
            }
        });

        if(!PermissionClass.CheckPermission(this)) PermissionClass.RequestPerms(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, categoriesArrayList);

        actvUploadAppMainCategory.setAdapter(adapter);


        if(isEditingApp){
            setDataFromCurApp();
        }
    }
    private void setDataFromCurApp(){
        StorageFunctions.setImage(this, ivUploadAppImage, curApp.getAppImagePath());
        etUploadAppName.setText(curApp.getAppName());
        etUploadAppDescription.setText(curApp.getAppDescription());
        etUploadAppPrice.setText(Double.toString(curApp.getAppPrice()));
        etUploadAppDiscountPercentage.setText(Double.toString(curApp.getAppDiscountPercentage()));
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

        etUploadAppName.setText(specialFirstName);
        etUploadAppDescription.setText("This is a nice description laddie, i would appreciate if you gave me some of it!");
        etUploadAppPrice.setText("99");
        etUploadAppDiscountPercentage.setText("2");
        actvUploadAppMainCategory.setText(categoriesArrayList.get(rand.nextInt(3)));

    }
    private String getFullPath(User user, App app, String fileTypeFolder, String fileEnding){
        String specialPath = "" + user.getUserPhoneNumber() + "" + app.getAppName() + new SimpleDateFormat("ddMMyy-HHmmss.SSSS").format(new Date()) + "." + fileEnding;
        String foldersPhotos = fileTypeFolder + "/";
        return foldersPhotos + specialPath;
    }
    private void saveBitmapAndFinish(App app){
        if (photoBitmap == null) {
            photoBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.emptypfp);
        }

        String fullPath = app.getAppImagePath();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(fullPath);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        imagesRef.putBytes(data).addOnCompleteListener(UploadAppActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    finishActivity(true, app);
                } else {
                    Toast.makeText(getApplicationContext(), "Image upload failed, please try again!", Toast.LENGTH_LONG).show();
                    btnUploadAppSendData.setClickable(true);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v==btnUploadAppSendData){
            sendData();
        }
        else if(v==etAPKPath){
            if(PermissionClass.CheckPermission(this)){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.android.package-archive"); //it's an apk
                activityResultLauncherFileExplorer.launch(intent);
            }
            else PermissionClass.RequestPerms(this);
        }
        else if(v==ivUploadAppGallery){
            if(!PermissionClass.CheckPermission(this)){
                PermissionClass.RequestPerms(this);
                Log.v("Image", "RequestPerms has failed in ivGallery");
                Toast.makeText(this, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
            }
            else startGallery();
        }
        else if(v==ivUploadAppCamera){
            if(!PermissionClass.CheckPermission(this)){
                PermissionClass.RequestPerms(this);
                Log.v("Image", "RequestPerms has failed in ivCamera");
                Toast.makeText(this, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
            }
            else startCamera();
        }
    }
    private boolean validateETData(){
        //FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL
        //using my validation system to validate all the ETs
        boolean allValid;

        final EditText[] ETS = {etUploadAppName, etUploadAppDescription,actvUploadAppMainCategory, etUploadAppPrice, etUploadAppDiscountPercentage};
        final Object[] data = {etUploadAppName.getText().toString(), etUploadAppDescription.getText().toString(), actvUploadAppMainCategory.getText().toString(),
                etUploadAppPrice.getText().toString(), etUploadAppDiscountPercentage.getText().toString()};
        allValid = InitiateFunctions.initApp(data, ETS);

        return allValid;
    }

    private void sendData() {
        if(validateETData()){
            App app = new App();
            app.setAppName(etUploadAppName.getText().toString());
            app.setAppDescription(etUploadAppDescription.getText().toString());
            app.setAppMainCategory(actvUploadAppMainCategory.getText().toString());
            app.setAppPrice(Double.parseDouble(etUploadAppPrice.getText().toString()));
            app.setAppDiscountPercentage(Double.parseDouble(etUploadAppDiscountPercentage.getText().toString()));
            app.setAppCreator(curUser);
            app.setAppPerms(permsDialogResult);
            if(apkUri==null){
                if(isEditingApp) {
                    app.setAppApkPath(curApp.getAppApkPath());
                    app.setAppSize(curApp.getAppSize());
                }
                else {
                    Toast.makeText(this, "You must choose an APK path!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            else{
                String path = getFullPath(curUser, app, FIRESTORE_STORAGE_APK_FOLDER, "app");

                app.setAppApkPath(path);
                String size = humanReadableByte(apkUri);
                app.setAppSize(size);

                StorageFunctions.saveFileInFireStore(apkUri, path);
            }

            // if the image is null, it's set to the default, if it's null and isEditingApp that means the image hasn't been changed when editing.
            if(isEditingApp && uriPhoto==null){
                app.setAppImagePath(curApp.getAppImagePath());
            }
            else app.setAppImagePath(getFullPath(curUser, app, FIRESTORE_STORAGE_IMAGE_FOLDER, "jpg"));


            btnUploadAppSendData.setClickable(false);
            if(isEditingApp){
                app.setAppImagePath(curApp.getAppImagePath());
                app.setAppId(curApp.getAppId());
                db.collection("apps").document(curApp.getAppId()).set(app).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if (task1.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_LONG).show();
                            db.collection("users").document(curUser.getUserId()).collection(FIRESTORE_USER_CREATED_APPS_KEY).document(app.getAppId()).set(app);
                            try {
                                saveBitmapAndFinish(app);
                            } catch (NullPointerException e){
                                Log.e("debug", "NullPointerException for OnComplete RegisterActivity: " + e);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Unsuccessful! Please try again!", Toast.LENGTH_LONG).show();
                            Log.d("debug", "set failed with ", task1.getException());
                            btnUploadAppSendData.setClickable(true);
                        }
                    }
                });
            }
            else{
                db.collection("apps").add(app).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task1) {
                        if (task1.isSuccessful()) {
                            DocumentReference documentReference = task1.getResult();
                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_LONG).show();

                            // You can get the ID of the newly added document using documentReference.getId()
                            String docId = documentReference.getId();
                            app.setAppId(docId);

                            db.collection("users").document(curUser.getUserId()).collection(FIRESTORE_USER_CREATED_APPS_KEY).add(app);

                            try {
                                saveBitmapAndFinish(app);
                            } catch (NullPointerException e){
                                Log.e("debug", "NullPointerException for OnComplete RegisterActivity: " + e);
                            }
                            // we now add the app to the user's created apps subcollection

                        } else {
                            Toast.makeText(getApplicationContext(), "Unsuccessful! Please try again!", Toast.LENGTH_LONG).show();
                            Log.d("debug", "add failed with ", task1.getException());
                            btnUploadAppSendData.setClickable(true);
                        }
                    }
                });
            }



        }
        else Toast.makeText(this, "All EditTexts must be correct!", Toast.LENGTH_LONG).show();
    }

    private void finishActivity(boolean result, App app){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_CURRENT_APP_KEY, app);
        if(result) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
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