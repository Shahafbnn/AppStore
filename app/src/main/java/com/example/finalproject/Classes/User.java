package com.example.finalproject.Classes;

import android.util.Log;

import java.util.Calendar;
import java.util.LinkedList;

public class User {
    private static LinkedList<User> usersList = null;

    private String firstName;
    private String lastName;
    private android.icu.util.Calendar birthDate;
    private double weight;

    public User(String firstName, String lastName, android.icu.util.Calendar birthDate, double weight) {
        if(usersList==null) usersList = new LinkedList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.weight = weight;
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
                ", Birth date: " + calendarSpecialToString(birthDate) +" (" + getCurrentAgeDouble() + " years old)" +
                ", Weight: " + weight);
    }
    private String calendarSpecialToString(android.icu.util.Calendar calendar){
        return (calendar.get(android.icu.util.Calendar.DAY_OF_WEEK) + "/" + calendar.get(android.icu.util.Calendar.MONTH) + "/" + calendar.get(android.icu.util.Calendar.YEAR));
    }
    public android.icu.util.Calendar getCurrentAge() {
        Calendar today = Calendar.getInstance();
        int years = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        int months = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
        int days = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);

        if (days < 0) {
            months--;
            days += today.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if (months < 0) {
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
//        int years = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
//        int months = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
//        int days = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);
        age.add(android.icu.util.Calendar.YEAR, -birthDate.get(Calendar.YEAR));
        age.add(android.icu.util.Calendar.MONTH, -birthDate.get(Calendar.MONTH));
        age.add(android.icu.util.Calendar.DAY_OF_MONTH, -birthDate.get(Calendar.DAY_OF_MONTH));


//        if (days < 0) {
//            months--;
//            days += today.getActualMaximum(Calendar.DAY_OF_MONTH);
//        }
//
//        if (months < 0) {
//            years--;
//            months += 12;
//        }

        double ageDouble = age.get(android.icu.util.Calendar.YEAR) + age.get(android.icu.util.Calendar.MONTH)/12 + age.get(android.icu.util.Calendar.DAY_OF_YEAR)/365;
        return ageDouble;
    }

    public static Object[] validateFirstName(String firstName){
        if (firstName==null) return new Object[]{false, "null"};
        if (firstName.equals("")) return new Object[]{false, "empty"};
        int size = firstName.length();
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
        Log.v("weight", "weight before: " + weight[0]);

        if (weight[0]==null) return new Object[]{false, "null"};
        if (weight[0].equals("")) return new Object[]{false, "empty"};
        if(weight[0].contains("-")) return new Object[]{false, "negative or contains -"};
        String[] weightStringArray = weight[0].split("\\.");
        Log.v("weight", "weightStringArray: " + weightStringArray);

        //int dotAmount = weight[0].length() - weightStringArray.length;
        //if(dotAmount > 1) return new Object[]{false, "containing more than 1 dot"};
        if (weightStringArray[0].equals("")) weight[0] = "0" + weight[0];
        if (weightStringArray.length >= 2 && weightStringArray[1].equals("")) weight[0] = weight[0] + "0";
        double weightNum = Double.parseDouble(weightStringArray[0] + "." + weightStringArray[1]);
        if(weightNum < 20) return new Object[]{false, "smaller than 20kg"};
        if(weightNum > 300) return new Object[]{false, "larger than 300kg"};

        return new Object[]{true};
    }



}
