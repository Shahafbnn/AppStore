package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.UserValidations.validate;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.finalproject.DatabaseClasses.MyDatabase;

import java.util.Map;

public class InitiateFunctions {

    public static boolean initUser(Object[] data){
        return initUser(data, null, false);
    }

    public static boolean initUser(Object[] data, EditText[] et){
        return initUser(data, et, true);
    }
    public static boolean initUser(Object[] data, EditText[] et, boolean usesEt){
        UserValidations.ValidateTypes[] types = Constants.getTypes();
        ValidationData v;
        boolean allValid = true;
        for(int i = 0; i < types.length; i++){
            v = validate(data[i], types[i]);
            if(!v.isValid()) allValid = false;
            if(usesEt){
                if(v.isValid()) et[i].setText(data[i].toString());
                else et[i].setError(v.getError());
            }
        }
        return allValid;
    }
    public static boolean initUser(EditText[] et){
        UserValidations.ValidateTypes[] types = Constants.getTypes();
        ValidationData v;
        boolean allValid = true;
        for(int i = 0; i < types.length; i++){
            v = validate(et[i].getText().toString(), types[i]);
            if(!v.isValid()) allValid = false;

            //if(v.isValid()) et[i].setText(et[i].toString());
            if(!v.isValid()) et[i].setError(v.getError());

        }
        return allValid;
    }

    public static Object initUserSharedPreferences(SharedPreferences sharedPreferences, Context context, MyDatabase myDatabase){
        sharedPreferences = context.getSharedPreferences("SharedPreferencesRegister", 0);
        if (sharedPreferences==null || sharedPreferences.contains("initialized")) {
            return "SP";
        }

        long id = sharedPreferences.getLong(Constants.USER_ID_KEY, -1);
        if(id<0){
            return "User";
        }
        return myDatabase.userDAO().getUserById(id);
    }

}
