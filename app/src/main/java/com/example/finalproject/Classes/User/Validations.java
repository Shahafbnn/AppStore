package com.example.finalproject.Classes.User;

import android.icu.util.Calendar;
import android.util.Log;

import com.example.finalproject.Classes.ValidationData;

import java.util.Date;
import java.util.Objects;

public class Validations {

    public enum ValidateUserTypes {
        FIRST_NAME, LAST_NAME, WEIGHT, PHONE_NUMBER, PASSWORD, EMAIL, BIRTH_DATE, CITY, ADDRESS
    }
    public enum ValidateAppTypes {
        NAME, DESCRIPTION, MAIN_CATEGORY, PRICE, DISCOUNT_PERCENTAGE
    }
    public static <T> ValidationData validateUser(T toValidate, ValidateUserTypes type){
        //editValue[0] = phoneNumber, editValue[1] = email
        switch (type) {
            case FIRST_NAME:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate FIRST_NAME is the wrong type: "+toValidate+"}}");
                return validateFirstName((String)toValidate);
            case LAST_NAME:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate LAST_NAME is the wrong type: "+toValidate+"}}");
                return validateLastName((String)toValidate);
            case WEIGHT:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate WEIGHT is the wrong type: "+toValidate+"}}");
                return validateWeight((String) toValidate);
            case PHONE_NUMBER:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate PHONE_NUMBER is the wrong type: "+toValidate+"}}");
                return validatePhoneNumber((String)toValidate);
            case PASSWORD:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate PASSWORD is the wrong type: "+toValidate+"}}");
                return validatePassword((String)toValidate);
            case EMAIL:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate EMAIL is the wrong type: "+toValidate+"}}");
                else return validateEmail((String)toValidate);
            case BIRTH_DATE:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate BIRTH_DATE is the wrong type: "+toValidate+"}}");
                return validateBirthDate((String) toValidate);
            case CITY:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate CITY is the wrong type: "+toValidate+"}}");
                return validateCity((String) toValidate);
            case ADDRESS:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate ADDRESS is the wrong type: "+toValidate+"}}");
                return validateAddress((String) toValidate);
            default:
                Log.e("Runtime Exception", "" + "UserValidations{validate{default Wrong ValidateTypes}}");
                return new ValidationData(false, "UserValidations{validate{default Wrong ValidateTypes}}");
        }

    }

    public static <T> ValidationData validateApp(T toValidate, ValidateAppTypes type){
        //editValue[0] = phoneNumber, editValue[1] = email
        switch (type) {
            case NAME:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate NAME is the wrong type: "+toValidate+"}}");
                return validateAppName((String)toValidate);
            case DESCRIPTION:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate DESCRIPTION is the wrong type: "+toValidate+"}}");
                return validateAppDescription((String)toValidate);
            case MAIN_CATEGORY:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate MAIN_CATEGORY is the wrong type: "+toValidate+"}}");
                return validateCategorySearch((String) toValidate);
            case PRICE:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate PRICE is the wrong type: "+toValidate+"}}");
                return validateAppPrice((String)toValidate);
            case DISCOUNT_PERCENTAGE:
                if(!(toValidate instanceof String)) Log.e("Runtime Exception", "" + "UserValidations{validate{toValidate DISCOUNT_PERCENTAGE is the wrong type: "+toValidate+"}}");
                return validateAppDiscountPercentage((String)toValidate);
            default:
                Log.e("Runtime Exception", "" + "AppValidations{validate{default Wrong ValidateTypes}}");
                return new ValidationData(false, "AppValidations{validate{default Wrong ValidateTypes}}");
        }

    }

    public static ValidationData validateAppName(String name){
        if(name==null) return new ValidationData(false,  "name cannot be null");
        if (name.equals("")) return new ValidationData(false,  "name cannot be empty");
        long strLen = name.length();
        if(strLen > 30) return new ValidationData(false,  "name cannot be over 30 chars long");
        if(strLen < 2) return new ValidationData(false,  "name cannot be under 2 chars long");
        if(name.charAt(0) == ' ') return new ValidationData(false,  "name cannot begin with a space");
        char[] chars = name.toCharArray();
        for (char letter:chars) {
            if(!( (letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z') || (letter >= '0' && letter <= '9') || (letter == ' ') )) return new ValidationData(false,  "name must only contain English chars, numbers or a space");
        }
        return new ValidationData(true, null);
    }

    public static ValidationData validateAppDescription(String description){
        if(description==null) return new ValidationData(false,  "description cannot be null");
        if (description.equals("")) return new ValidationData(false,  "description cannot be empty");
        long strLen = description.length();
        if(strLen > 300) return new ValidationData(false,  "description cannot be over 300 chars long");
        if(strLen < 20) return new ValidationData(false,  "description cannot be under 20 chars long");
        if(description.charAt(0) == ' ') return new ValidationData(false,  "description cannot begin with a space");
        char[] chars = description.toCharArray();
        for (char letter:chars) {
            if(!( (letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z') || (letter >= '0' && letter <= '9') || (letter >= '!' && letter <= '/') || (letter == ' ') )) return new ValidationData(false,  "description must only contain English chars, numbers a space or special characters");
        }
        return new ValidationData(true, null);
    }

    public static ValidationData validateCategorySearch(String categoryName){
        if(categoryName==null) return new ValidationData(false,  "category name cannot be null");
        if (categoryName.equals("")) return new ValidationData(false,  "category name cannot be empty");
        long strLen = categoryName.length();
        if(strLen > 30) return new ValidationData(false,  "category name cannot be over 30 chars long");
        if(strLen < 3) return new ValidationData(false,  "category name cannot be under 3 chars long");
        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = categoryName.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z'))) return new ValidationData(false, "category name can only contain English letters");
        }
        return new ValidationData(true, null);
    }

    public static ValidationData validateAppPrice(String price){
        Double num = stringToDecimalNumber(price);
        if(num == null) return new ValidationData(false, "price must be a number");
        if(num < 0) return new ValidationData(false, "price must be a positive number");
        if(Math.round(num * 100.0) / 100.0 != num) return new ValidationData(false, "price must have at most two digits after the decimal point");
        return new ValidationData(true, null);
    }
    public static ValidationData validateAppDiscountPercentage(String discountPercentage){
        Double num = stringToDecimalNumber(discountPercentage);
        if(num == null) return new ValidationData(false, "discount percentage must be a number");
        if(Math.round(num * 100.0) / 100.0 != num) return new ValidationData(false, "discount percentage must have at most two digits after the decimal point");
        return new ValidationData(true, null);
    }

//    public static Double fixNumber(String number){
////        //weight is an array to emulate a pointer in C
////        if (number==null) return 0.0;
////        if (number.equals("")) return 0.0;
////        if(number.equals(".")) return 0.0;
////        if(number.equals("-.")) return 0.0;
////        if(number.contains("-") && number.charAt(0) != '-') return null; //"5-1"
////        String[] numberDotStringArray = number.split("\\.");
////        if(numberDotStringArray.length > 2) return null; //"5.1.2"
////        if(numberDotStringArray.length == 0) return 0.0;
////        if(numberDotStringArray.length==1)
////
////        double numberDecimal;
////        if(numberDotStringArray.length == 1 && number)
////        return validateWeight(numberDecimal);
//        return null;
//    }
    public static boolean validateDecimalNumber(String number){
        return stringToDecimalNumber(number) != null;
    }
    private static Double stringToDecimalNumber(String number){
        try{
            return Double.parseDouble(number);
        }
        catch(NumberFormatException e){
            return null;
        }
    }


    public static ValidationData validateName(String name, String nameKind, boolean isQuery){
        if (!isQuery && name==null) return new ValidationData(false,  nameKind + " name cannot be null");
        if (!isQuery && name.equals("")) return new ValidationData(false,  nameKind + " name cannot be empty");
        int size = name.length();
        if(!isQuery && size>=1 && !((name.charAt(0) >= 'A' && name.charAt(0) <= 'Z') || (name.charAt(0) >= 'a' && name.charAt(0) <= 'z'))) return new ValidationData(false,  "name must start with an English letter");
        boolean hasEnglish = false;
        if (!isQuery && !(size >= 2 && size <= 30)) return new ValidationData(false,  nameKind + " name cannot be shorter than 2 characters or longer than 30 characters");

        long specialCount = 0;
        char currentChar;
        boolean lastHasSpecial;
        for(int i = 0; i< size; i++){
            currentChar = name.charAt(i);
            if((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') ) hasEnglish = true;
            if(nameKind.equals("first") && !((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z'))) return new ValidationData(false,   "first name must only be in English");
            lastHasSpecial = i != 0 && ((name.charAt(i - 1) == ' ') || (name.charAt(i - 1) == '-'));
            if(name.charAt(i) == ' ' || name.charAt(i) == '-') specialCount++;
            if(!nameKind.equals("first") && specialCount > 1) return new ValidationData(false,  nameKind + " name cannot have multiple special chars");
            if(!nameKind.equals("first") && lastHasSpecial){
                if (currentChar == ' ' || currentChar == '-') return new ValidationData(false,  nameKind + " name cannot have multiple special chars in a row");
            }
            if(!hasEnglish) return new ValidationData(false,  nameKind + " name must only contain English, space or a hyphen");
        }
        if(!isQuery && (name.charAt(size-1) == ' ' || name.charAt(size-1) == '-')) return new ValidationData(false,  nameKind + " name cannot end with a special char");

        return new ValidationData(true, null);
    }


    public static ValidationData validateFirstName(String firstName){
        return validateName(firstName, "first", false);
    }
    public static ValidationData validateLastName(String lastName){
        return validateName(lastName, "last", false);
    }

    // used in the UsersListViewActivity
    public static ValidationData validateFullName(String fullName){
        return validateName(fullName, "full", true);
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
        if(weightStringArray.length == 0) weight[0] = "0.0";

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
        if(weight < 0) return new ValidationData(false,  "weight cannot be negative or contain -");
        if(weight < 20) return new ValidationData(false,  "weight cannot be smaller than 20kg");
        if(weight > 300) return new ValidationData(false,  "weight cannot be larger than 300kg");

        return new ValidationData(true, null);
    }

    public static ValidationData validateBirthDate(String date){
        if(date==null) return new ValidationData(false,  "date cannot be null");
        if(date.equals("")) return new ValidationData(false,  "date cannot be empty");

        Date d = Objects.requireNonNull(User.getDateFromString(date));
        //if(d==null) return new ValidationData(false,  "date must be NN/NN/NNNN");
        return validateBirthDate(d);
    }
    public static ValidationData validateBirthDate(Date date){
        if(date==null) return new ValidationData(false,  "birth date cannot be null");
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
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
        //editEmail is the connected user, isEditEmail represents if it should be used or not.
        if(email==null) return new ValidationData(false,  "email cannot be null");
        if (email.equals("")) return new ValidationData(false,  "email cannot be empty");
        long strLen = email.length();
        if(strLen > 30) return new ValidationData(false,  "email cannot be over 30 chars long");
        String[] atSplitEmail = email.split("@");
        if(atSplitEmail.length != 2) return new ValidationData(false,  "email must contain one '@' symbol");
        if(atSplitEmail[0] == null || atSplitEmail[0].equals("")) return new ValidationData(false,  "email must contain something before the '@' symbol");
        if(atSplitEmail[1] == null || atSplitEmail[1].equals("")) return new ValidationData(false,  "email must contain something before the '@' symbol");

        String[] dotSplitAll = email.split("\\.");
        if(dotSplitAll.length < 2) return new ValidationData(false,  "email must have at least 1 '.' symbols ");

        for (String s : dotSplitAll) {
            if (s == null || s.equals("") || s.equals("@") || s.equals("."))
                return new ValidationData(false, "email must contain an english letter before and after a '.' symbol");
        }

        String[] dotSplitAfter = atSplitEmail[1].split("\\.");
        if(dotSplitAfter.length < 2) return new ValidationData(false,  "email must have at least 1 '.' symbol after the '@' symbol ");
        boolean correct = true;
        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = email.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '@') || (currentChar == '.') || (currentChar >= '0' && currentChar <= '9'))) correct = false;
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
            if(!hasLowercaseEnglish) {
                error += "a lowercase English char ";
                correctsNum++;
            }
            if(!hasUppercaseEnglish) {
                if(correctsNum > 0) error += ", ";
                error += "an uppercase English char ";
                correctsNum++;
            }
            if(!hasSpecial) {
                if(correctsNum > 0) error += ", ";
                error += "a special char ";
                correctsNum++;
            }
            if(!hasNumber) {
                if(correctsNum > 0) error += "and ";
                error += "a decimal digit (0-9 number) ";
                correctsNum++;
            }
            return new ValidationData(false,  error);
        }

        return new ValidationData(correct,  "password can only contain English, numbers and special chars");
    }

    public static ValidationData validatePhoneNumber(String phoneNumber){
        if(phoneNumber==null) return new ValidationData(false,  "phone number cannot be null");
        if (phoneNumber.equals("")) return new ValidationData(false,  "phone number cannot be empty");
        long len  = phoneNumber.length();
        if(len != 10) return new ValidationData(false, "phone number must be 10 chars long");
        return new ValidationData(phoneNumber.matches("05(1[25][0-9]{2}|[02-46-8][0-9]{3}|055([23]{2}[0-9]|4[41]0|43[0-9]|5[105][0-9]|6[876][0-9]|7[2107][0-9]|8[987][0-9]|9[^0][0-9]))[0-9]{4}"), "phone number must have a valid prefix");
    }
    public static ValidationData validateCity(String city){
        if(city==null) return new ValidationData(false,  "city cannot be null");
        if (city.equals("")) return new ValidationData(false,  "city cannot be empty");
        long strLen = city.length();
        if(strLen > 30) return new ValidationData(false,  "city cannot be over 30 chars long");
        if(strLen < 2) return new ValidationData(false,  "city cannot be under 2 chars long");
        return new ValidationData(true, null);
    }

    public static ValidationData validateAddress(String address){
        if(address==null) return new ValidationData(false,  "address cannot be null");
        if (address.equals("")) return new ValidationData(false,  "address cannot be empty");
        long strLen = address.length();
        if(strLen > 30) return new ValidationData(false,  "address cannot be over 30 chars long");
        if(strLen < 3) return new ValidationData(false,  "address cannot be under 3 chars long");
        String[] addresses = address.split(" ");
        if(addresses.length != 2) return new ValidationData(false,  "address must have one space");
        if(addresses[0]==null || addresses[0].equals("")) return new ValidationData(false,  "address must contain english characters before the space");
        if(addresses[1]==null || addresses[1].equals("")) return new ValidationData(false,  "address must contain numbers after the space");

        //check if it conforms to LETTERS NUMBERS
        for (char cur:addresses[0].toCharArray()) {
            if(!((cur >= 'A' && cur <= 'Z') || (cur >= 'a' && cur <= 'z'))) return new ValidationData(false,  "address must be an only in English before the space");
        }
        for (char cur:addresses[1].toCharArray()) {
            if(!(cur >= '0' && cur <= '9')) return new ValidationData(false,  "address must be only a number after the space");
        }
        boolean inEnglish = true;
        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = address.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= '0' && currentChar <= '9') || (currentChar == ' '))) inEnglish = false;
        }
        if(!inEnglish) return new ValidationData(false,  "address can only contain English chars, numbers and a space.");


        return new ValidationData(true, null);
    }

}
