package com.example.finalproject.Classes;

import static com.example.finalproject.Classes.UserValidations.ValidateTypes.*;

import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

public class UserValidations {

    public enum ValidateTypes{
        FIRST_NAME, LAST_NAME, WEIGHT, PHONE_NUMBER, PASSWORD, EMAIL, BIRTH_DATE
    }
    public static <T> ValidationData validate(T toValidate, ValidateTypes type){
        ValidateTypes[] types = {FIRST_NAME, LAST_NAME, WEIGHT, PHONE_NUMBER,PASSWORD, EMAIL, BIRTH_DATE};

        switch (type) {
            case FIRST_NAME:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateFirstName((String)toValidate);
            case LAST_NAME:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateLastName((String)toValidate);
            case WEIGHT:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateWeight((String) toValidate);
            case PHONE_NUMBER:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validatePhoneNumber((String)toValidate);
            case PASSWORD:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validatePassword((String)toValidate);
            case EMAIL:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateEmail((String)toValidate);
            case BIRTH_DATE:
                if(!(toValidate instanceof Date)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateBirthDate((Date) toValidate);
            default:
                throw new RuntimeException("UserValidations{validate{Wrong ValidateTypes}}");
        }

    }

    public static ValidationData validateFirstName(String firstName){
        if (firstName==null) return new ValidationData(false,  "first name cannot be null");
        if (firstName.equals("")) return new ValidationData(false,  "first name cannot be empty");
        int size = firstName.length();
        if(size>=1 && !(firstName.charAt(0) >= 'A' && firstName.charAt(0) <= 'Z')) return new ValidationData(false,  "first name must start with an uppercase English letter");
        if (size >= 2 && size <= 10){
            boolean inEnglish = true;
            char currentChar;
            for(int i = 0; i< size; i++){
                currentChar = firstName.charAt(i);
                if(!(currentChar >= 'a' && currentChar <= 'z') || !(currentChar >= 'A' && currentChar <= 'Z')) inEnglish = false;
            }
            return new ValidationData(inEnglish,  "first name cannot be in another language");
        }
        return new ValidationData(false,  "first name cannot be shorter than 2 characters or longer than 10 characters");
    }


    public static ValidationData validateLastName(String firstName){
        if (firstName==null) return new ValidationData(false,  "last name cannot be null");
        if (firstName.equals("")) return new ValidationData(false,  "last name cannot be empty");
        int size = firstName.length();
        if(size>=1 && !(firstName.charAt(0) >= 'A' && firstName.charAt(0) <= 'Z')) return new ValidationData(false,  "name must start with an uppercase English letter");
        if (size >= 2 && size <= 10){
            boolean inEnglish = true;
            char currentChar;
            for(int i = 0; i< size; i++){
                currentChar = firstName.charAt(i);
                if(!(currentChar >= 'a' && currentChar <= 'z') || !(currentChar >= 'A' && currentChar <= 'Z')) inEnglish = false;
            }
            return new ValidationData(inEnglish,  "last name cannot be in another language");
        }
        return new ValidationData(false,  "last name cannot be shorter than 2 characters or longer than 10 characters");
    }



    public static ValidationData validateWeight(String weight){
        if (weight==null) return new ValidationData(false,  "weight cannot be null");
        String[] str = new String[]{weight};
        return validateWeight(str);
    }
    public static ValidationData validateWeight(String[] weight){
        //weight is an array to emulate a pointer in C
        if (weight==null) return new ValidationData(false,  "weight cannot be null");
        if (weight.length==0) return new ValidationData(false,  "weight must be initiated");
        if (weight[0]==null) return new ValidationData(false,  "weight cannot be null");
        if (weight[0].equals("")) return new ValidationData(false,  "weight cannot be empty");
        if(weight[0].contains("-")) return new ValidationData(false,  "weight cannot be negative or contain -");
        String[] weightStringArray = weight[0].split("\\.");
        if(weightStringArray.length > 2) return new ValidationData(false,  "weight must have 1 dot only");
        if(weightStringArray.length <= 0) weight[0] = "0.0";

        double weightNum;


        if (weightStringArray.length > 0 && weightStringArray[0].equals("")) weight[0] = "0" + weight[0];
        if(weightStringArray.length == 2){
            if(weightStringArray[1].equals("")) weight[0] = weight[0] + "0";
            weightNum = Double.parseDouble(weightStringArray[0] + "." + weightStringArray[1]);
        }
        else weightNum = Double.parseDouble(weight[0]);
        return validateWeight(weightNum);
    }

    public static ValidationData validateWeight(Double weight){
        //weight is an array to emulate a pointer in C

        if (weight==null) return new ValidationData(false,  "weight cannot be null");
        //if (weight==new Double(null)) return new ValidationData(false,  "weight cannot be null");
        if(weight <= 0) return new ValidationData(false,  "weight cannot be negative or contains -");
        if(weight < 20) return new ValidationData(false,  "weight cannot be smaller than 20kg");
        if(weight > 300) return new ValidationData(false,  "weight cannot be larger than 300kg");

        return new ValidationData(true, null);
    }

    public static ValidationData validateBirthDate(Date date){
        if(date==null) return new ValidationData(false,  "birth date cannot be null");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal==null) return new ValidationData(false,  "birth date cannot be null");
        //create a new calendar to check if the age is under 16y
        final Calendar age = Calendar.getInstance();
        age.add(Calendar.YEAR, -16);
        if(cal.after(age)) return new ValidationData(false,  "birth date cannot be under the age 16");

        //create a new calendar to check if the age is over 150y
        final Calendar old = Calendar.getInstance();
        old.add(Calendar.YEAR, -150);
        if(cal.before(old)) return new ValidationData(false,  "birth date cannot be over the age 150");

        return new ValidationData(true, null);
    }
    public static ValidationData validateEmail(String email){
        if(email==null) return new ValidationData(false,  "email cannot be null");
        if (email.equals("")) return new ValidationData(false,  "email cannot be empty");
        long strLen = email.length();
        if(strLen > 30) return new ValidationData(false,  "email cannot be over 30 chars long");
        String[] atSplit = email.split("@");
        if(atSplit.length != 2) return new ValidationData(false,  "email must have only 1 '@' symbols ");
        String[] dotSplit = atSplit[1].split("\\.");
        if(dotSplit.length < 2) return new ValidationData(false,  "email must have at least 1 '.' symbols ");
        boolean correct = true;
        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = email.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '@') || (currentChar == '.'))) correct = false;
        }
        return new ValidationData(correct,  "email can only contain English chars, '@'s or '.'s");
    }

    public static ValidationData validatePassword(String pass){
        if(pass==null) return new ValidationData(false,  "password cannot be null");
        if (pass.equals("")) return new ValidationData(false,  "password cannot be empty");
        long strLen = pass.length();
        if(strLen > 30) return new ValidationData(false,  "password cannot be over 30 chars long");
        if(strLen < 6) return new ValidationData(false,  "password cannot be under 6 chars long");

        boolean correct = true;
        boolean hasLowercaseEnglish = false;
        boolean hasUppercaseEnglish = false;
        boolean hasSpecial = false;
        boolean hasNumber = false;

        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = pass.charAt(i);
            if((currentChar >= 'A' && currentChar <= 'Z')) hasUppercaseEnglish = true;
            if((currentChar >= 'a' && currentChar <= 'z')) hasLowercaseEnglish = true;

            if((currentChar >= '0' && currentChar <= '9')) hasNumber = true;
            //these are the specials in ASCII
            if((currentChar >= '!' && currentChar <= '/') || (currentChar >= ':' && currentChar <= '@') || (currentChar >= '[' && currentChar <= '`') || (currentChar >= '{' && currentChar <= '~')) hasSpecial = true;
            if(!(currentChar >= '!' && currentChar <= '~')) correct = false;
        }
        if(!hasLowercaseEnglish || !hasUppercaseEnglish || !hasSpecial || !hasNumber) {
            String error = "password must contain ";
            short correctsNum = 0;
            if(hasLowercaseEnglish) {
                error += "a lowercase English char ";
                correctsNum++;
            }
            if(hasUppercaseEnglish) {
                if(correctsNum > 0) error += ", ";
                error += "an uppercase English char ";
                correctsNum++;
            }
            if(hasSpecial) {
                if(correctsNum > 0) error += ", ";
                error += "a special char ";
                correctsNum++;
            }
            if(hasNumber) {
                if(correctsNum > 0) error += "and ";
                error += "a decimal digit (0-9 number) ";
                correctsNum++;
            }
            return new ValidationData(false,  error);
        }

        return new ValidationData(correct,  "password can only contain English, numbers and special chars");
    }
    public static ValidationData validatePhoneNumber(String phoneNumber){
        if(phoneNumber==null) return new ValidationData(false,  "password cannot be null");
        if (phoneNumber.equals("")) return new ValidationData(false,  "password cannot be empty");

        return new ValidationData(true, null);
    }
    public static int phoneNumberFromString(String phoneNumber){
        return -1;
    }
}
