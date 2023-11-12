package com.example.finalproject.Classes;

import static org.junit.Assert.assertEquals;

import android.icu.util.Calendar;

import junit.framework.TestCase;

import org.junit.Test;

public class UserTest extends TestCase {

    public void testGetFirstName() {
    }

    public void testSetFirstName() {
    }

    public void testGetLastName() {
    }

    public void testSetLastName() {
    }

    public void testGetBirthDate() {
    }

    public void testSetBirthDate() {
    }

    public void testSetBirthDateFromString() {
    }

    public void testGetWeight() {
    }

    public void testSetWeight() {
    }

    public void testGetUsersList() {
    }

    public void testSetUsersList() {
    }

    public void testTestToString() {
    }

    public void testGetCurrentAge() {
    }

    public void testGetCurrentAgeDouble() {
    }

    public void testValidateFirstName() {
    }

    public void testValidateLastName() {
    }

    public void testValidateWeight() {
        assertEquals(true, (boolean)User.validateWeight((double)50)[0]);
        assertEquals(true, (boolean)User.validateWeight(30.2)[0]);

        //empties
        //assertEquals(false, (boolean)User.validateWeight(new Double(null))[0]);
        assertEquals(false, (boolean)User.validateWeight(-10.0)[0]);
        assertEquals(false, (boolean)User.validateWeight(500.0)[0]);
        assertEquals(false, (boolean)User.validateWeight((double)Long.MAX_VALUE)[0]);
    }

    public void testTestValidateWeight() {
        assertEquals(true, (boolean)User.validateWeight(new String[]{"30"})[0]);
        assertEquals(true, (boolean)User.validateWeight(new String[]{"30.5"})[0]);
        assertEquals(true, (boolean)User.validateWeight(new String[]{"30."})[0]);

        //empties
        assertEquals(false, (boolean)User.validateWeight(new String[1])[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[0])[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{null})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{""})[0]);


        assertEquals(false, (boolean)User.validateWeight(new String[]{"0"})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{"19"})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{"400"})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{"-50"})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{"-5-0"})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{".-5.-0"})[0]);
        assertEquals(false, (boolean)User.validateWeight(new String[]{".3"})[0]);

        String[] str = new String[]{"."};
        assertEquals(false, (boolean)User.validateWeight(str)[0]);
        String[] str2 = new String[]{"."};
        User.validateWeight(str2);
        //should fix any stray dots after func activated ^
        assertEquals("0.0", str[0]);






    }

    public void testValidateBirthDate() {
    }

    public void testValidateEmail() {
        assertEquals(true, (boolean)User.validateEmail("a@b.c")[0]);
        assertEquals(true, (boolean)User.validateEmail("example.com@co.ex.gov")[0]);

        //empties
        assertEquals(false, (boolean)User.validateEmail(new String())[0]);
        assertEquals(false, (boolean)User.validateEmail("")[0]);
        assertEquals(false, (boolean)User.validateEmail(null)[0]);


        assertEquals(false, (boolean)User.validateEmail("examp@le@email.com")[0]);
        assertEquals(false, (boolean)User.validateEmail("example@email.com!")[0]);
        assertEquals(false, (boolean)User.validateEmail("example@email.ferrrrrrreeeeeeefeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")[0]);

    }

    @Test
    public void testValidatePassword() {
        assertEquals(true, (boolean)User.validatePassword("Pass123!")[0]);
        assertEquals(false, (boolean)User.validatePassword("Pass123")[0]);

        //empties
        assertEquals(false, (boolean)User.validatePassword(new String())[0]);
        assertEquals(false, (boolean)User.validatePassword("")[0]);
        assertEquals(false, (boolean)User.validatePassword(null)[0]);

        assertEquals(false, (boolean)User.validatePassword("Pass123!ferrrrrrreeeeeeefeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")[0]);
        assertEquals(false, (boolean)User.validatePassword("Pp.1")[0]);
        assertEquals(false, (boolean)User.validatePassword("Password123!л")[0]);

    }
}