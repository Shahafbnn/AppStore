package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.UserValidations.validate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

import java.io.File;

public class InitiateFunctions {
    private Context context;

    public InitiateFunctions(Context context) {
        this.context = context;
    }


    public static boolean initUser(Object[] data){
        return initUser(data, false, null, false, null, false, null);
    }

    public static boolean initUser(Object[] data, EditText[] et){
        return initUser(data, true, et, true, null, false, null);
    }
    public static boolean initUser(Object[] data, EditText[] et ,Context context){
        return initUser(data, true, et, true, context, false, null);
    }
    public static boolean initUser(Object[] data, EditText[] et ,Context context, String[] editValue){
        return initUser(data, true, et, true, context, true, editValue);
    }
    public static boolean initUser(Object[] data, boolean usesEt, EditText[] et, boolean usesDB ,Context context, boolean usesEditValue, String[] editValue){
        UserValidations.ValidateTypes[] types = Constants.getTypes();
        ValidationData v;
        boolean allValid = true;
        for(int i = 0; i < types.length; i++){
            if(usesDB && usesEditValue) v = validate(data[i], types[i], context, editValue);
            else if(usesDB) v = validate(data[i], types[i], context);
            else v = validate(data[i], types[i]);
            if(!v.isValid()) allValid = false;
            if(usesEt){
                if(v.isValid()) et[i].setText(data[i].toString());
                else et[i].setError(v.getError());
            }
        }
        return allValid;
    }

//    public static boolean initUser(EditText[] et){
//        Object[] data = new Object[et.length];
//        for(int i = 0; i < et.length; i++) data[i] = et[i].getText().toString();
//        return initUser(data, et, true, false, null, null);
//    }
//
//    public static boolean initUser(EditText[] et ,Context context, String[] editValue){
//        Object[] data = new Object[et.length];
//        for(int i = 0; i < et.length; i++) data[i] = et[i].getText().toString();
//        return initUser(data, et, true, true, context, editValue);
//    }

//    public static boolean initUser(EditText[] et){
//        UserValidations.ValidateTypes[] types = Constants.getTypes();
//        ValidationData v;
//        boolean allValid = true;
//        for(int i = 0; i < types.length; i++){
//            v = validate(et[i].getText().toString(), types[i]);
//            if(!v.isValid()) {
//                allValid = false;
//                et[i].setError(v.getError());
//            }
//        }
//        return allValid;
//    }

    public static MyPair<ValidationData, User> initUserSharedPreferences(SharedPreferences sharedPreferences, MyDatabase myDatabase){
        if (sharedPreferences==null || !sharedPreferences.contains(Constants.SHARED_PREFERENCES_INITIALIZED_KEY)) {
            return new MyPair<>(new ValidationData(false, "Shared preferences is not initialized"), null);
        }

        long id = sharedPreferences.getLong(Constants.USER_ID_KEY, -1);
        User u = myDatabase.userDAO().getUserById(id);
        if(id<0 || u == null){
            return new MyPair<>(new ValidationData(false, "User ID is invalid or user does not exist"), null);
        }
        return new MyPair<>(new ValidationData(true, null), u);
    }

    public void initViewsFromUser(User user, boolean isValid, MyDatabase myDatabase, TextView tvWelcome, ImageView ivProfilePic){
        initViewsFromUser(user, isValid, context, myDatabase, tvWelcome, ivProfilePic);
    }
    public static void initViewsFromUser(User user, boolean isValid, Context context, MyDatabase myDatabase, TextView tvWelcome, ImageView ivProfilePic){
        if(isValid){
            String fullName = user.getFullNameAdmin();
            boolean isAdmin = User.isAdmin(user.getPhoneNumber());
            //checking if the isAdmin in the db is correct, if not it updates the user.
            if(isAdmin != user.isAdmin()) {
                user.setAdmin(isAdmin);
                myDatabase.userDAO().update(user);
            }

            tvWelcome.setText("Welcome " + fullName + "!");
            ivProfilePic.setImageURI(user.getImgUri(context));

            Toast.makeText(context, "Log In successful", Toast.LENGTH_LONG).show();

        }
        else {
            tvWelcome.setText("Welcome Guest!");
            ivProfilePic.setImageResource(R.drawable.emptypfp);
        }
    }
    //invalidates the user
    public static void invalidateViews(TextView tvWelcome, ImageView ivProfilePic){
        tvWelcome.setText("Welcome Guest!");
        ivProfilePic.setImageResource(R.drawable.emptypfp);
//        initViewsFromUser(null, false, null, null, tvWelcome, ivProfilePic);
    }


}
