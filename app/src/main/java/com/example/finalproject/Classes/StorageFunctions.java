package com.example.finalproject.Classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.finalproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class StorageFunctions {

    public static MyPair<Boolean, String> saveBitmapInPath(Bitmap bitmap){
        String timeStamp = new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
        String foldersPhotos = "Photos";
        File myDir = new File(Environment.getExternalStorageDirectory(), "/Pictures/" + foldersPhotos);

        if (!myDir.exists()) {
            if(!myDir.mkdir()) Log.e("Runtime Exception", "" + "myDir.mkdirs() has failed");
        }

        File dest = new File(myDir, timeStamp);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // Compress and write the bitmap to the output stream
            out.flush(); // Flush the stream
            out.close(); // Close the stream
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
        return new MyPair<>(true, dest.getPath());
    }


    public static Bitmap getBitmapFromPath(String filename, Context context){
        if(!PermissionClass.CheckPermission((Activity) context)){
            PermissionClass.RequestPerms((Activity) context);
            Log.v("Image", "RequestPerms has failed in saveBitmapInFolder");
        }
        if(PermissionClass.CheckPermission((Activity) context)) {
            if (filename == null)
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.emptypfp);
            String foldersPhotos = "Photos";
            File myDir = new File(Environment.getExternalStorageDirectory(), "/" + foldersPhotos);
            File file = new File(myDir, filename);

            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            } else {
                return null;
            }
        }
        return null;
    }
    public static Uri getUriFromPath(String path, Context context){
        if(path==null) return Uri.parse("android.resource://" + context.getPackageName() + "/" + R.drawable.emptypfp);
        File f = new File(path);
        if(f.exists()) return Uri.parse(path);
        else {
            Log.e("", "file does not exist in getUriFromPath");
            return null;
        }
    }
}
