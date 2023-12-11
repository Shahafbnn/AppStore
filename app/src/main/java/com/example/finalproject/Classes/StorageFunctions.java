package com.example.finalproject.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class StorageFunctions {
    //warning: the function does not check for perms.
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

    //warning: the function does not check for perms.

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
