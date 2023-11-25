package com.example.finalproject.Classes;

import android.icu.util.Calendar;

public class UserValidations {

    public static Object[] validateFirstName(String firstName){
        if (firstName==null) return new Object[]{false, "null"};
        if (firstName.equals("")) return new Object[]{false, "empty"};
        int size = firstName.length();
        if(size>=1 && !(firstName.charAt(0) >= 'A' && firstName.charAt(0) <= 'Z')) return new Object[]{false, "name must start with an uppercase English letter"};
        if (size >= 2 && size <= 10){
            boolean inEnglish = true;
            char currentChar;
            for(int i = 0; i< size; i++){
                currentChar = firstName.charAt(i);
                if(!(currentChar >= 'a' && currentChar <= 'z') || !(currentChar >= 'A' && currentChar <= 'Z')) inEnglish = false;
            }
            return new Object[]{inEnglish, "in another language"};
        }
        return new Object[]{false, "shorter than 2 characters or longer than 10 characters"};
    }


    public static Object[] validateLastName(String firstName){
        if (firstName==null) return new Object[]{false, "null"};
        if (firstName.equals("")) return new Object[]{false, "empty"};
        int size = firstName.length();
        if(size>=1 && !(firstName.charAt(0) >= 'A' && firstName.charAt(0) <= 'Z')) return new Object[]{false, "name must start with an uppercase English letter"};
        if (size >= 2 && size <= 15){
            boolean inHebrew = true;
            char currentChar;
            for(int i = 0; i< size; i++){
                currentChar = firstName.charAt(i);
                if(!(currentChar >= 'א' && currentChar <= 'ת')) inHebrew = false;
            }
            return new Object[]{inHebrew, "not in Hebrew"};
        }
        return new Object[]{false, "shorter than 2 characters or longer than 10 characters"};
    }

    public static Object[] validateWeight(String[] weight){
        //weight is an array to emulate a pointer in C
        if (weight==null) return new Object[]{false, "weight cannot be null"};
        if (weight.length==0) return new Object[]{false, "weight must be initiated"};
        if (weight[0]==null) return new Object[]{false, "weight cannot be null"};
        if (weight[0].equals("")) return new Object[]{false, "weight cannot be empty"};
        if(weight[0].contains("-")) return new Object[]{false, "weight cannot be negative or contain -"};
        String[] weightStringArray = weight[0].split("\\.");
        if(weightStringArray.length > 2) return new Object[]{false, "weight must have 1 dot only"};
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

    public static Object[] validateWeight(Double weight){
        //weight is an array to emulate a pointer in C

        if (weight==null) return new Object[]{false, "weight cannot be null"};
        //if (weight==new Double(null)) return new Object[]{false, "weight cannot be null"};
        if(weight <= 0) return new Object[]{false, "negative or contains -"};
        if(weight < 20) return new Object[]{false, "smaller than 20kg"};
        if(weight > 300) return new Object[]{false, "larger than 300kg"};

        return new Object[]{true};
    }

    public static Object[] validateBirthDate(android.icu.util.Calendar cal){
        if(cal==null) return new Object[]{false, "null"};
        //create a new calendar to check if the age is under 16y
        final Calendar age = Calendar.getInstance();
        age.add(Calendar.YEAR, -16);
        if(cal.after(age)) return new Object[]{false, "under the age 16"};

        //create a new calendar to check if the age is over 150y
        final Calendar old = Calendar.getInstance();
        old.add(Calendar.YEAR, -150);
        if(cal.before(old)) return new Object[]{false, "over the age 150"};

        return new Object[]{true};
    }
    public static Object[] validateEmail(String email){
        if(email==null) return new Object[]{false, "email cannot be null"};
        if (email.equals("")) return new Object[]{false, "email cannot be empty"};
        long strLen = email.length();
        if(strLen > 30) return new Object[]{false, "email cannot be over 30 chars long"};
        String[] atSplit = email.split("@");
        if(atSplit.length != 2) return new Object[]{false, "email must have only 1 '@' symbols "};
        String[] dotSplit = atSplit[1].split("\\.");
        if(dotSplit.length < 2) return new Object[]{false, "email must have at least 1 '.' symbols "};
        boolean correct = true;
        char currentChar;
        for(int i = 0; i< strLen; i++){
            currentChar = email.charAt(i);
            if(!((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '@') || (currentChar == '.'))) correct = false;
        }
        return new Object[]{correct, "email can only contain English chars, '@'s or '.'s"};
    }

    public static Object[] validatePassword(String pass){
        if(pass==null) return new Object[]{false, "password cannot be null"};
        if (pass.equals("")) return new Object[]{false, "password cannot be empty"};
        long strLen = pass.length();
        if(strLen > 30) return new Object[]{false, "password cannot be over 30 chars long"};
        if(strLen < 6) return new Object[]{false, "password cannot be under 6 chars long"};

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
            return new Object[]{false, error};
        }

        return new Object[]{correct, "password can only contain English, numbers and special chars"};
    }
    public static Object[] validatePhoneNumber(String phoneNumber){

        return new Object[]{true};
    }
    public static int phoneNumberFromString(String phoneNumber){
        return -1;
    }
}
