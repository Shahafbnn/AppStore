package com.example.finalproject.Classes;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionClass {

    public static boolean CheckPremission(Activity act){
        int resultCamera = ContextCompat.checkSelfPermission(act, CAMERA);
        int resultWriteStorage = ContextCompat.checkSelfPermission(act, WRITE_EXTERNAL_STORAGE);
        int resultReadStorage = ContextCompat.checkSelfPermission(act, READ_EXTERNAL_STORAGE);


        return resultCamera== PackageManager.PERMISSION_GRANTED && resultWriteStorage==PackageManager.PERMISSION_GRANTED && resultReadStorage==PackageManager.PERMISSION_GRANTED;}

    public static void RequestPerms(Activity act){
        ActivityCompat.requestPermissions(act, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }
}
