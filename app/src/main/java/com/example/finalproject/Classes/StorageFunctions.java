package com.example.finalproject.Classes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.finalproject.Classes.User.User;
import com.example.finalproject.GlideApp;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class StorageFunctions {

    public static void saveBitmapInFireStore(Bitmap bitmap, User user){
        String specialPath = "" + user.getUserId() + new SimpleDateFormat("ddMyy-HHmmss").format(new Date()) + ".jpg";
        String foldersPhotos = Constants.FIRESTORE_STORAGE_IMAGE_FOLDER;
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

    public static String humanReadableByte(Uri uri){
        if (uri.getScheme().equals("content")){
            File file = new File(uri.getPath());
            return humanReadableByte(file.length());
        }
        else return null;
    }
    public static String humanReadableByte(long bytes) {
        // If the byte count is between -1000 and 1000, return it directly.
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        // Create a CharacterIterator over the units.
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        // While the byte count is outside the range -999950 to 999950...
        while (bytes <= -999_950 || bytes >= 999_950) {
            // ...divide the byte count by 1000...
            bytes /= 1000;
            // ...and move to the next unit.
            ci.next();
        }
        // Return the byte count, divided by 1000 one last time to get the count in the correct unit,
        // formatted to one decimal place, followed by the unit and "B".
        double num = (bytes / 1000.0);
        return ((int)num) + "." + ((num*10)%10) + " " + ci.current();
    }


    public static UploadTask saveFileInFireStore(Uri uri, String fullFireStorePath){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference riversRef = storageRef.child(fullFireStorePath);
        UploadTask uploadTask = riversRef.putFile(uri);
        return uploadTask;
    }

    public static Uri getUriFromImageView(Context context, ImageView imageView, String imageName){
        // Get the bitmap from ImageView
        Drawable drawable = imageView.getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // Save bitmap to a file
        File cachePath = new File(context.getCacheDir(), "images");
        if (!cachePath.exists()) {
            cachePath.mkdirs();
        }
        String imageShortPath = imageName + ".png";
        try (FileOutputStream stream = new FileOutputStream(new File(cachePath, imageShortPath))) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (IOException e) {
            Log.e("debug", e.toString());
            return null;
        }

        // Get the URI of the file
        File newFile = new File(cachePath, imageShortPath);
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", newFile);
        return contentUri;
    }


    public static Bitmap getBitmapFromPhysicalPath(String filename, Context context){
        if(!PermissionClass.CheckPermission((Activity) context)){
            PermissionClass.RequestPerms((Activity) context);
            Log.v("Image", "RequestPerms has failed in saveBitmapInFolder");
        }
        if(PermissionClass.CheckPermission((Activity) context)) {
            if (filename == null)
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.emptypfp);
            String foldersPhotos = Constants.FIRESTORE_STORAGE_IMAGE_FOLDER;
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
    public static Uri getUriFromPhysicalPath(String path, Context context){
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
    public static byte[] getZippedFile(Context context, Uri uri, String name) {
        ByteArrayOutputStream zipData = new ByteArrayOutputStream();
        ZipOutputStream zipFile = new ZipOutputStream(zipData);

        try {
            // Add a file to the zip
            // create a new file that will be inside of the zip
            ZipEntry zipEntry = new ZipEntry(name);
            //put the file in the zip
            zipFile.putNextEntry(zipEntry);
            //turn the file into bytes
            InputStream fileData = context.getContentResolver().openInputStream(uri);

            //write the data from fileData to zipFile 1024 bytes at a time
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileData.read(buffer)) > 0) {
                zipFile.write(buffer, 0, len);
            }
            //close them
            zipFile.closeEntry();
            fileData.close();
            zipFile.close();

            // Return the zipped data
            return zipData.toByteArray();
        } catch (IOException e) {
            Log.e("debug", "error at getZippedFile: " + e.getMessage());
            return null;
        }
    }

    public static UploadTask uploadBytesToFireStore(byte[] bytes, String fullFireStorePath) {
        // Convert the ByteArrayOutputStream to ByteArrayInputStream
        ByteArrayInputStream data = new ByteArrayInputStream(bytes);

        // Get a reference to the storage service
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference
        StorageReference storageRef = storage.getReference();

        // Create a reference to 'your-zip-file.zip'
        StorageReference zipRef = storageRef.child(fullFireStorePath);

        // Upload the zipped data
        return zipRef.putStream(data);
    }
    public static UploadTask zipFileAndUploadToFireStore(Context context, Uri uri, String fullFireStorePath){
        String name = fullFireStorePath.substring(fullFireStorePath.lastIndexOf("/") + 1, fullFireStorePath.lastIndexOf("."));
        byte[] data = getZippedFile(context, uri, name);
        return uploadBytesToFireStore(data, fullFireStorePath);
    }
    public static File downloadApkFileFromFireStore(Activity act, String fireStorePath, OnCompleteListener<FileDownloadTask.TaskSnapshot> listener){
        StorageReference ref = FirebaseStorage.getInstance().getReference(fireStorePath);

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "apk");
        } catch (IOException e) {
            PermissionClass.RequestPerms(act);
            Toast.makeText(act, "Please accept permissions to download the app", Toast.LENGTH_LONG).show();
            return null;
        }

        FileDownloadTask a = ref.getFile(localFile);
        if(listener!=null) a.addOnCompleteListener(listener);
        return localFile;
    }

    public static void openApkFile(Context context, File file){
        Uri apkUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

        // Assume 'context' is your Context object and 'apkUri' is the Uri of your APK file
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e("debug", e.getMessage());
            Toast.makeText(context, "Unable to download app, please try again", Toast.LENGTH_LONG).show();
        }
    }

    public static void downloadAndOpenApkFileFromFireStore(Activity act, String fireStorePath, OnCompleteListener<FileDownloadTask.TaskSnapshot> listener){
        if(PermissionClass.CheckPermission(act)){
            File file = downloadApkFileFromFireStore(act, fireStorePath, listener);
            if (file != null) {
                openApkFile(act, file);
            }
        }else{
            PermissionClass.RequestPerms(act);
            Toast.makeText(act, "Please accept permissions to download the app", Toast.LENGTH_LONG).show();
        }
    }
}
