package com.example.finalproject.Classes;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class PermissionClass {

    /**
     * Checks if the necessary permissions have been granted.
     * @param act The activity where permissions are checked.
     * @return true if all permissions are granted, false otherwise.
     */
    public static boolean CheckPermission(Activity act){
        int resultCamera = ContextCompat.checkSelfPermission(act, CAMERA);
        int resultWriteStorage = ContextCompat.checkSelfPermission(act, WRITE_EXTERNAL_STORAGE);
        int resultReadStorage = ContextCompat.checkSelfPermission(act, READ_EXTERNAL_STORAGE);



        return resultCamera== PackageManager.PERMISSION_GRANTED && resultWriteStorage==PackageManager.PERMISSION_GRANTED && resultReadStorage==PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests the necessary permissions.
     * @param act The activity where permissions are requested.
     */
    public static void RequestPerms(Activity act){
        ActivityCompat.requestPermissions(act, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }


}
