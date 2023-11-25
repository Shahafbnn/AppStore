package com.example.finalproject.Classes;

import android.content.SharedPreferences;
import android.icu.util.GregorianCalendar;

import android.icu.util.Calendar;

import java.util.LinkedList;

public class User {
    private static LinkedList<User> usersList = null;
    private String firstName;
    private String lastName;
    private android.icu.util.Calendar birthDate;
    private double weight;
    private String email;
    private LocationAddress location;
    private String password;
    private String passwordConfirm;
    private String phoneNumber;

    public static final String FIRST_NAME_KEY = "firstName";
    public static final String LAST_NAME_KEY = "lastName";
    public static final String WEIGHT_KEY = "weight";
    public static final String BIRTH_DATE_KEY = "birthDate";
    public static final String PHONE_NUMBER_KEY = "phoneNumber";

    public static final String PASSWORD_KEY = "password";

    public static final String PASSWORD_CONFIRM_KEY = "passwordConfirm";

    public static final String EMAIL_ADDRESS_KEY = "emailAddress";
    public static final String HOME_ADDRESS_KEY = "homeAddress";
    public static final String IMAGE_URI_ADDRESS_KEY = "imageUriAddress";







    public User(String firstName, String lastName, Calendar birthDate, double weight, String email, LocationAddress location, String password, String phoneNumber) {
        if(usersList==null) usersList = new LinkedList<>();
        if((boolean)validateFirstName(firstName)[0]) this.firstName = firstName;
        else this.firstName = "Guest";
        if((boolean)validateLastName(firstName)[0]) this.lastName = lastName;
        else this.lastName = "Guest";
        if((boolean)validateBirthDate(birthDate)[0]) this.birthDate = birthDate;
        else {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.YEAR, -16);
            this.birthDate = c;
        }
        if((boolean)validateWeight(weight)[0]) this.weight = weight;
        else this.weight = 100;

        Object[] emailValidated = validateEmail(email);
        if((boolean)emailValidated[0]) this.email = email;
        else throw new RuntimeException(emailValidated[1].toString());

        this.location = location;

        if((boolean)validatePassword(email)[0]) this.password = password;
        else this.password = "Password123!";

        Object[] phoneNumberValidated = validatePhoneNumber(phoneNumber);
        if((boolean) phoneNumberValidated[0]) this.phoneNumber = phoneNumber;
        else throw new RuntimeException(phoneNumberValidated[1].toString());
    }
    public User(Calendar birthDate){
        this.birthDate = birthDate;
    }
    //FOR TESTS ONLY, DELETE LATER!

    public User(SharedPreferences sp){
        if(usersList==null) usersList = new LinkedList<>();


        String firstName = sp.getString(FIRST_NAME_KEY, "default");
        String lastName = sp.getString(LAST_NAME_KEY, "default");
        android.icu.util.Calendar birthDate = User.getBirthDateFromString(sp.getString(BIRTH_DATE_KEY, "default"));
        double weight = sp.getFloat(WEIGHT_KEY, 100);

        if((boolean)User.validateFirstName(firstName)[0]) this.firstName = firstName;
        if((boolean)User.validateLastName(lastName)[0]) this.lastName = lastName;
        if((boolean)User.validateWeight(weight)[0]) this.weight = weight;
        if((boolean)User.validateBirthDate(birthDate)[0]) this.birthDate = birthDate;


    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public android.icu.util.Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(android.icu.util.Calendar birthDate) {
        this.birthDate = birthDate;
    }

    public static android.icu.util.Calendar getBirthDateFromString(String birthDate){
        //i am not checking for cases where birthDate is null, empty, lacking slashes... because it will already show an exception
        String[] time = birthDate.split("/");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(time[2]));
        calendar.set(Calendar.MONTH, Integer.parseInt(time[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time[0]));

        return calendar;
    }


    public static String birthDateToString(Calendar cal){
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)) + "/" + cal.get(Calendar.YEAR);
    }
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public static LinkedList<User> getUsersList() {
        return usersList;
    }

    public static void setUsersList(LinkedList<User> usersList) {
        User.usersList = usersList;
    }

    @Override
    public String toString(){
        return ("Full name: " + firstName + " " + lastName +
                ", Birth date: " + birthDateToString(birthDate) +" (" + getCurrentAgeDouble() + " years old)" +
                ", Weight: " + weight);
    }

    public android.icu.util.Calendar getCurrentAge() {
        final Calendar today = Calendar.getInstance();
        int years = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int months = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
        int days = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);

        while (days < 0) {
            months--;
            days += today.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        while (months < 0) {
            years--;
            months += 12;
        }

        android.icu.util.Calendar age = android.icu.util.Calendar.getInstance();
        age.set(Calendar.YEAR, years);
        age.set(Calendar.MONTH, months);
        age.set(Calendar.DAY_OF_MONTH, days);

        return age;
    }

    public double getCurrentAgeDouble() {
        Calendar age = Calendar.getInstance();
        age.add(android.icu.util.Calendar.YEAR, -birthDate.get(Calendar.YEAR));
        age.add(android.icu.util.Calendar.MONTH, -birthDate.get(Calendar.MONTH));
        age.add(android.icu.util.Calendar.DAY_OF_MONTH, -birthDate.get(Calendar.DAY_OF_MONTH));

        int years = age.get(Calendar.YEAR);
        int months = age.get(Calendar.MONTH) + 1;
        int days = age.get(Calendar.DAY_OF_MONTH);

        // Check if the current month is the birth month and the current day is less than the birth day
        if (months == 0 && days < 0) {
            years--;
        }

        double ageDouble = years + months/12.0 + (double)days/ (((GregorianCalendar)age).isLeapYear(age.get(Calendar.YEAR))?366:365);
        return ageDouble;
    }

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
