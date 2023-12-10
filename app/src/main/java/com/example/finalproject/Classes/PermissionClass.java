package com.example.finalproject.Classes;

import static android.Manifest.permission.CAMERA;
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

    public static boolean CheckPermission(Activity act){
        int resultCamera = ContextCompat.checkSelfPermission(act, CAMERA);
        int resultWriteStorage = ContextCompat.checkSelfPermission(act, WRITE_EXTERNAL_STORAGE);
        int resultReadStorage = ContextCompat.checkSelfPermission(act, READ_EXTERNAL_STORAGE);


        return resultCamera== PackageManager.PERMISSION_GRANTED && resultWriteStorage==PackageManager.PERMISSION_GRANTED && resultReadStorage==PackageManager.PERMISSION_GRANTED;}

    public static void RequestPerms(Activity act){
        ActivityCompat.requestPermissions(act, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
    }

    public static void saveBitmapInFolder(Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
        String foldersPhotos = "Photos";
        File myDir = new File(Environment.getExternalStorageDirectory(), "/" + foldersPhotos);

        if(!myDir.exists())
        {
            myDir.mkdirs();
        }

        File dest = new File(myDir, timeStamp);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress and write the bitmap to the output stream
            out.flush(); // Flush the stream
            out.close(); // Close the stream
        } catch (Exception e){
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
    }

    public static Bitmap getBitmapFromFolder(String filename){
        String foldersPhotos = "Photos";
        File myDir = new File(Environment.getExternalStorageDirectory(), "/" + foldersPhotos);
        File file = new File(myDir, filename);

        if(file.exists()){
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            return null;
        }
    }
}
