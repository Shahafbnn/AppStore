package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.UserValidations.validate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
            if (usesEt) {
                if (v.isValid()) et[i].setText(data[i].toString());
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

    public interface FirestoreCallback {
        void onCallback(User user);
    }

    public static void initUserSharedPreferences(SharedPreferences sharedPreferences, FirebaseFirestore db, Context context, Activity activity){
        if (sharedPreferences==null || !sharedPreferences.contains(Constants.SHARED_PREFERENCES_INITIALIZED_KEY)) {
            Toast.makeText(context, "No user logged in", Toast.LENGTH_LONG).show();
            return;
        }

        String id = sharedPreferences.getString(Constants.USER_ID_KEY, "-1");
        db.collection("users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                    } else {
                        Toast.makeText(context, "user doesn't exist", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("debug", "initUserSharedPreferences get failed with ", task.getException());
                }
            }
        });
    }


    public void initViewsFromUser(User user, boolean isValid, FirebaseFirestore db, TextView tvWelcome, ImageView ivProfilePic){
        initViewsFromUser(user, isValid, context, db, tvWelcome, ivProfilePic);
    }
    public static void initViewsFromUser(User user, boolean isValid, Context context, FirebaseFirestore db, TextView tvWelcome, ImageView ivProfilePic){
        if(isValid){
            String fullName = user.getFullNameAdmin();
            boolean isAdmin = User.isAdmin(user.getPhoneNumber());
            //checking if the isAdmin in the db is correct, if not it updates the user.
            if(isAdmin != user.isAdmin()) {
                user.setAdmin(isAdmin);
                // needed to get the document with the ID so we won't create a new user with a new ID each update.
                // I don't know yet if i should use update or set here. and if I use update does it only update the changed variables?
                db.collection("users").document(user.getId()).set(user);
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
