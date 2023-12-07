package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.UserValidations.validate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.finalproject.DatabaseClasses.MyDatabase;

public class InitiateFunctions {

    public static boolean initUser(Object[] data){
        return initUser(data, null, false, false, null, null);
    }

    public static boolean initUser(Object[] data, EditText[] et){
        return initUser(data, et, true, false, null, null);
    }
    public static boolean initUser(Object[] data, EditText[] et ,Context context, String[] editValue){
        return initUser(data, et, true, true, context, editValue);
    }
    public static boolean initUser(Object[] data, EditText[] et, boolean usesEt, boolean usesDB ,Context context, String[] editValue){
        UserValidations.ValidateTypes[] types = Constants.getTypes();
        ValidationData v;
        boolean allValid = true;
        for(int i = 0; i < types.length; i++){
            if(usesDB) v = validate(data[i], types[i], true, context, editValue);
            else v = validate(data[i], types[i]);
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

    public static User initUserSharedPreferences(SharedPreferences sharedPreferences, MyDatabase myDatabase, Boolean[] vals){
        //vals[0] = isSPValid
        //vals[1] = isUserInDB;
        if (sharedPreferences==null || !sharedPreferences.contains(Constants.SHARED_PREFERENCES_INITIALIZED_KEY)) {
            vals[0] = false;
            vals[1] = false;
            vals[2] = false;
            return null;
            //throw new RuntimeException("InitiateFunctions{initUserSharedPreferences{sharedPreferences is " + sharedPreferences.toString() +"}}");
        }

        long id = sharedPreferences.getLong(Constants.USER_ID_KEY, -1);
        if(id<0){
            //throw new RuntimeException("InitiateFunctions{initUserSharedPreferences{id is " + id +"}}");
            vals[0] = true;
            vals[1] = false;
            return null;
        }
        return myDatabase.userDAO().getUserById(id);
    }

}
