package com.example.finalproject.Classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.finalproject.Classes.User.User;
import com.example.finalproject.GlideApp;
import com.example.finalproject.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

public class StorageFunctions {

    public static void saveBitmapInPath(Bitmap bitmap, User user){
        String specialPath = "" + user.getUserId() + new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
        String foldersPhotos = "Photos";
        String fullPath = foldersPhotos + specialPath;

        user.setUserImagePath(fullPath);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child(fullPath);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
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

    public static void setImage(Context context, ImageView imageView, String path){
        GlideApp.with(context)
                .load(FirebaseStorage.getInstance().getReference().child(path))
                .into(imageView);
    }
}
