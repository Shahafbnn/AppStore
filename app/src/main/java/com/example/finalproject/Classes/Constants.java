package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.User.Validations.ValidateAppTypes.*;
import static com.example.finalproject.Classes.User.Validations.ValidateUserTypes.*;

import com.example.finalproject.Classes.User.Validations;

public abstract class Constants {

    private static final Validations.ValidateUserTypes[] USER_TYPES = {FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL, CITY, ADDRESS};
    //etUploadAppName, etUploadAppDescription,actvUploadAppMainCategory, etUploadAppPrice, etUploadAppDiscountPercentage
    private static final Validations.ValidateAppTypes[] APP_TYPES = Validations.ValidateAppTypes.values();

    // user consts
    public static final String USER_ID_KEY = "userId";

    public static final String USER_FIRST_NAME_KEY = "userFirstName";
    public static final String USER_LAST_NAME_KEY = "userLastName";
    public static final String USER_WEIGHT_KEY = "userWeight";
    public static final String USER_BIRTH_DATE_KEY = "userBirthDate";
    public static final String USER_PHONE_NUMBER_KEY = "userPhoneNumber";
    public static final String USER_PASSWORD_KEY = "userPassword";
    public static final String USER_PASSWORD_CONFIRM_KEY = "userPasswordConfirm";
    public static final String USER_EMAIL_ADDRESS_KEY = "userEmailAddress";
    public static final String USER_HOME_ADDRESS_KEY = "userHomeAddress";
    public static final String USER_IMG_SOURCE_KEY = "userImageSource";
    public static final String USER_IS_ADMIN_KEY = "userIsAdmin";
    private static final String[] USER_KEYS = new String[]{USER_FIRST_NAME_KEY, USER_LAST_NAME_KEY, USER_WEIGHT_KEY, USER_BIRTH_DATE_KEY, USER_PHONE_NUMBER_KEY, USER_PASSWORD_KEY, USER_EMAIL_ADDRESS_KEY};


    public static final String USER_IMAGE_URI_ADDRESS_KEY = "userImageUriAddress";

    //city consts
    public static final String CITY_NAME_KEY = "cityName";
    public static final String CITY_ID_KEY = "cityId";


    public static final String[] ADMIN_PHONE_NUMBERS = new String[]{"0535622719"};

    public static Validations.ValidateUserTypes[] getUserTypes(){return USER_TYPES.clone();}
    public static Validations.ValidateAppTypes[] getAppTypes(){return APP_TYPES.clone();}

    public static String[] getUserKeys() { return USER_KEYS.clone();}
    //sp consts
    public static final String SHARED_PREFERENCES_KEY = "sharedPreferencesRegister";
    public static final String SHARED_PREFERENCES_INITIALIZED_KEY = "initialized";


    //firebase
    public static final String FIRESTORE_USER_CREATED_APPS_KEY = "createdApps";
    public static final String FIRESTORE_STORAGE_IMAGE_FOLDER = "Images";
    public static final String FIRESTORE_STORAGE_APK_FOLDER = "Apks";


    //intents
    public static final String REGISTER_ACTIVITY_RETURN_DATA_KEY = "registerActivitySuccess";
    public static final String INTENT_CURRENT_USER_KEY = "intentCurrentUser";
    public static final String INTENT_CURRENT_APP_KEY = "intentCurrentApp";
    public static final String INTENT_CATEGORIES_KEY = "intentCategories";



    public static final String[] APPLICATION_CATEGORIES = new String[]{"Games", "Fitness", "Business", "Food"};

}
