package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.UserValidations.ValidateTypes.BIRTH_DATE;
import static com.example.finalproject.Classes.UserValidations.ValidateTypes.EMAIL;
import static com.example.finalproject.Classes.UserValidations.ValidateTypes.FIRST_NAME;
import static com.example.finalproject.Classes.UserValidations.ValidateTypes.LAST_NAME;
import static com.example.finalproject.Classes.UserValidations.ValidateTypes.PASSWORD;
import static com.example.finalproject.Classes.UserValidations.ValidateTypes.PHONE_NUMBER;
import static com.example.finalproject.Classes.UserValidations.ValidateTypes.WEIGHT;

public abstract class Constants {

    private static final UserValidations.ValidateTypes[] TYPES = {FIRST_NAME, LAST_NAME, WEIGHT, BIRTH_DATE, PHONE_NUMBER, PASSWORD, EMAIL};
    public static final String USER_ID_KEY = "id";

    public static final String USER_FIRST_NAME_KEY = "firstName";
    public static final String USER_LAST_NAME_KEY = "lastName";
    public static final String USER_WEIGHT_KEY = "weight";
    public static final String USER_BIRTH_DATE_KEY = "birthDate";
    public static final String USER_PHONE_NUMBER_KEY = "phoneNumber";
    public static final String USER_PASSWORD_KEY = "password";
    public static final String USER_PASSWORD_CONFIRM_KEY = "passwordConfirm";
    public static final String USER_EMAIL_ADDRESS_KEY = "emailAddress";
    public static final String USER_HOME_ADDRESS_KEY = "homeAddress";
    public static final String USER_IMG_SOURCE_KEY = "imageSource";
    public static final String USER_IS_ADMIN_KEY = "isAdmin";
    private static final String[] USER_KEYS = new String[]{USER_FIRST_NAME_KEY, USER_LAST_NAME_KEY, USER_WEIGHT_KEY, USER_BIRTH_DATE_KEY, USER_PHONE_NUMBER_KEY, USER_PASSWORD_KEY, USER_EMAIL_ADDRESS_KEY};

    public static final String CITY_ID_KEY = "homeCityId";

    public static final String USER_IMAGE_URI_ADDRESS_KEY = "imageUriAddress";

    public static final String CITY_NAME_KEY = "cityName";

    public static final String[] ADMIN_PHONE_NUMBERS = new String[]{"0535622719"};

    public static UserValidations.ValidateTypes[] getTypes(){return TYPES.clone();}

    public static String[] getUserKeys() { return USER_KEYS.clone();
    }
}
