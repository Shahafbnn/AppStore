package com.example.finalproject.Classes;

import android.content.Context;
import android.icu.util.Calendar;
import android.widget.EditText;

import com.example.finalproject.DatabaseClasses.MyDatabase;

import java.util.Date;

public class UserValidations {

    public enum ValidateTypes{
        FIRST_NAME, LAST_NAME, WEIGHT, PHONE_NUMBER, PASSWORD, EMAIL, BIRTH_DATE, CITY, ADDRESS
    }
    public static <T> ValidationData validate(T toValidate, ValidateTypes type){
        return validate(toValidate, type, false, null, false, null);
    }
    public static <T> ValidationData validate(T toValidate, ValidateTypes type ,Context context){
        return validate(toValidate, type, true, context, false, null);
    }
    public static <T> ValidationData validate(T toValidate, ValidateTypes type ,Context context, String[] editValue){
        return validate(toValidate, type, true, context, true, editValue);
    }
    public static <T> ValidationData validate(T toValidate, ValidateTypes type, boolean usesDB ,Context context, boolean isValue, String[] editValue){
        //editValue[0] = phoneNumber, editValue[1] = email
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
                if(usesDB && isValue) return validatePhoneNumber((String)toValidate, context, editValue[0]);
                if(usesDB) return validatePhoneNumber((String)toValidate, true, context, false, null);
                return validatePhoneNumber((String)toValidate);
            case PASSWORD:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validatePassword((String)toValidate);
            case EMAIL:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                if(usesDB && isValue) return validateEmail((String)toValidate, context, editValue[1]);
                if(usesDB) return validateEmail((String)toValidate, true, context, false, null);
                else return validateEmail((String)toValidate);
            case BIRTH_DATE:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateBirthDate((String) toValidate);
            case CITY:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateCity((String) toValidate, context);
            case ADDRESS:
                if(!(toValidate instanceof String)) throw new RuntimeException("UserValidations{validate{toValidate is the wrong type}}");
                return validateAddress((String) toValidate);
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
                if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z'))) inEnglish = false;
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
                if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z'))) inEnglish = false;
            }
            return new ValidationData(inEnglish,  "last name cannot be in another language");
        }
        return new ValidationData(false,  "last name cannot be shorter than 2 characters or longer than 10 characters");
    }

    // used in the UsersListViewActivity
    public static ValidationData validateFullName(String fullName){
        if (fullName==null) return new ValidationData(false,  "full name cannot be null");
        if (fullName.equals("")) return new ValidationData(true, null);
        //it can be empty so we can search the database as non-admins in UsersListViewActivity
        int size = fullName.length();
        if(size>=1 && !(fullName.charAt(0) >= 'A' && fullName.charAt(0) <= 'Z')) return new ValidationData(false,  "full name must start with an uppercase English letter");
        if (!(size >= 2 && size <= 21)) return new ValidationData(false,  "full name cannot be shorter than 2 characters or longer than 21 characters");
        boolean inEnglish = true;
        char currentChar;
        for(int i = 0; i< size; i++){
            currentChar = fullName.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar == ' '))) inEnglish = false;
        }
        if(!inEnglish) return new ValidationData(false,  "full name cannot be in another language");
        String[] splitName = fullName.split(" ");
        if(splitName.length > 2 || splitName.length < 1) return new ValidationData(false,  "full name can only contain one space");
        if(splitName[0].equals("")) return new ValidationData(false,  "full name cannot start with a space");
        return new ValidationData(true,  null);
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

    public static ValidationData validateBirthDate(String date){
        if(date==null) return new ValidationData(false,  "date cannot be null");
        if(date.equals("")) return new ValidationData(false,  "date cannot be empty");

        Date d = User.getDateFromString(date);
        if(d==null) return new ValidationData(false,  "date must be NN/NN/NNNN");
        return validateBirthDate(d);
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
        return validateEmail(email, false, null);
    }
    public static ValidationData validateEmail(String email, Context context){
        return validateEmail(email, true, context);
    }

    public static ValidationData validateEmail(String email, boolean isCheckingDB, Context context){
        return validateEmail(email, isCheckingDB, context, false, null);
    }

    public static ValidationData validateEmail(String email, Context context, String editEmail){
        return validateEmail(email, true, context, true, editEmail);
    }
    public static ValidationData validateEmail(String email, boolean isCheckingDB, Context context, boolean isEditEmail, String editEmail){
        if(email==null) return new ValidationData(false,  "email cannot be null");
        if (email.equals("")) return new ValidationData(false,  "email cannot be empty");
        long strLen = email.length();
        if(strLen > 30) return new ValidationData(false,  "email cannot be over 30 chars long");
        String[] atSplit = email.split("@");
        if(atSplit.length != 2) return new ValidationData(false,  "email must contain one '@' symbol");
        String[] dotSplit = atSplit[1].split("\\.");
        if(dotSplit.length < 2) return new ValidationData(false,  "email must have at least 1 '.' symbols ");
        boolean correct = true;
        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = email.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '@') || (currentChar == '.') || (currentChar >= '0' && currentChar <= '9'))) correct = false;
        }
        if(isCheckingDB){
            MyDatabase myDatabase = MyDatabase.getInstance(context);
            if(myDatabase.userDAO().getUserByEmail(email) != null || (isEditEmail && (!editEmail.equals(email)))) return new ValidationData(false,  "email is already in use");
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
        return validatePhoneNumber(phoneNumber, false, null);
    }
    public static ValidationData validatePhoneNumber(String phoneNumber, Context context){
        return validatePhoneNumber(phoneNumber, true, context);
    }
    public static ValidationData validatePhoneNumber(String phoneNumber, boolean isCheckingDB, Context context){
        return validatePhoneNumber(phoneNumber, true, context, false, null);
    }
    public static ValidationData validatePhoneNumber(String phoneNumber, Context context, String editPhoneNumber){
        return validatePhoneNumber(phoneNumber, true, context, true, editPhoneNumber);
    }
    public static ValidationData validatePhoneNumber(String phoneNumber, boolean isCheckingDB, Context context, boolean isEditPhoneNumber, String editPhoneNumber){
        if(phoneNumber==null) return new ValidationData(false,  "phone number cannot be null");
        if (phoneNumber.equals("")) return new ValidationData(false,  "phone number cannot be empty");
        long len  = phoneNumber.length();
        if(len < 3 || len > 13) return new ValidationData(false, "phone number cannot be shorter than 3 chars or longer than 13 chars");
        if(isCheckingDB){
            MyDatabase myDatabase = MyDatabase.getInstance(context);
            if(!myDatabase.userDAO().getUsersByPhoneNumber(phoneNumber).isEmpty() || (isEditPhoneNumber && (!editPhoneNumber.equals(phoneNumber)))) return new ValidationData(false,  "phone number is already in use");
        }
        return new ValidationData(phoneNumber.matches("05(1[25][0-9]{2}|[02-46-8][0-9]{3}|055([23]{2}[0-9]|4[41]0|43[0-9]|5[105][0-9]|6[876][0-9]|7[2107][0-9]|8[987][0-9]|9[^0][0-9]))[0-9]{4}"), "phone number must have a valid prefix");
    }
    public static ValidationData validateCity(String city, Context context){
        if(city==null) return new ValidationData(false,  "city cannot be null");
        if (city.equals("")) return new ValidationData(false,  "city cannot be empty");
        long strLen = city.length();
        if(strLen > 30) return new ValidationData(false,  "city cannot be over 30 chars long");
        if(strLen < 2) return new ValidationData(false,  "city cannot be under 6 chars long");
        if(MyDatabase.getInstance(context).cityDAO().getCityByName(city.toUpperCase()) == null) return new ValidationData(false,  "city doesn't exist");
        return new ValidationData(true, null);
    }

    public static ValidationData validateAddress(String address){
        if(address==null) return new ValidationData(false,  "address cannot be null");
        if (address.equals("")) return new ValidationData(false,  "address cannot be empty");
        long strLen = address.length();
        if(strLen > 30) return new ValidationData(false,  "address cannot be over 30 chars long");
        if(strLen < 3) return new ValidationData(false,  "address cannot be under 3 chars long");
        return new ValidationData(true, null);
    }
}
