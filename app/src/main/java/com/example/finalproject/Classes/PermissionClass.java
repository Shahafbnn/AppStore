package com.example.finalproject.Classes;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.REQUEST_INSTALL_PACKAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;

public class PermissionClass {

    /**
     * Checks if the necessary permissions have been granted.
     * @param act The activity where permissions are checked.
     * @return true if all permissions are granted, false otherwise.
     */
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public PermissionClass(AppCompatActivity act) {
        requestPermissionLauncher = act.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        // Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
                        Toast.makeText(act, "You've denied the permissions, you must manually accept them now ):", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public boolean CheckPermission(Activity act){
        int resultCamera = ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT < 34) {
            int resultWriteStorage = ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int resultReadStorage = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE);

            return resultCamera == PackageManager.PERMISSION_GRANTED &&
                    resultWriteStorage == PackageManager.PERMISSION_GRANTED &&
                    resultReadStorage == PackageManager.PERMISSION_GRANTED;
        } else {
            return resultCamera == PackageManager.PERMISSION_GRANTED;
        }
    }



    /**
     * Requests the necessary permissions.
     * @param act The activity where permissions are requested.
     */
    public void RequestPerms(Activity act){
        if (Build.VERSION.SDK_INT < 34) {
            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }


    public static ArrayList<String> getAllPerms(){
        java.lang.reflect.Field[] fields = getAllPermsFields();
        ArrayList<String> arr = new ArrayList<>();

        if(fields!=null){
            for (Field f : fields) {
                arr.add(f.toString());
            }
        }
        return arr;
    }
    public static Field[] getAllPermsFields(){
        try{
            return Manifest.permission.class.getFields();
        }catch(SecurityException e){
            return null;
        }
    }
    public static String[] getAllPermsStrings(){
        java.lang.reflect.Field[] fields = getAllPermsFields();
        String[] arr = null;
        String str;
        if(fields!=null){
            arr = new String[fields.length];
            for(int i = 0; i < fields.length; i++){
                str = fields[i].getName().toLowerCase().replace("_", " ");
                arr[i] = str.substring(0, 1).toUpperCase() + str.substring(1);
            }
        }
        return arr;
    }


}
