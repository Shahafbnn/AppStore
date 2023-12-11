package com.example.finalproject.Classes;

import static org.junit.Assert.assertEquals;

import android.icu.util.Calendar;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Date;

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
        assertEquals(true, UserValidations.validateWeight((double)50).isValid());
        assertEquals(true, UserValidations.validateWeight(30.2).isValid());

        //empties
        //assertEquals(false, UserValidations.validateWeight(new Double(null)).isValid());
        assertEquals(false, UserValidations.validateWeight(-10.0).isValid());
        assertEquals(false, UserValidations.validateWeight(500.0).isValid());
        assertEquals(false, UserValidations.validateWeight((double)Long.MAX_VALUE).isValid());
    }

    public void testTestValidateWeight() {
        assertEquals(true, UserValidations.validateWeight(new String[]{"30"}).isValid());
        assertEquals(true, UserValidations.validateWeight(new String[]{"30.5"}).isValid());
        assertEquals(true, UserValidations.validateWeight(new String[]{"30."}).isValid());

        //empties
        assertEquals(false, UserValidations.validateWeight(new String[1]).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[0]).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{null}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{""}).isValid());


        assertEquals(false, UserValidations.validateWeight(new String[]{"0"}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{"19"}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{"400"}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{"-50"}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{"-5-0"}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{".-5.-0"}).isValid());
        assertEquals(false, UserValidations.validateWeight(new String[]{".3"}).isValid());

        String[] str = new String[]{"."};
        assertEquals(false, UserValidations.validateWeight(str).isValid());
        String[] str2 = new String[]{"."};
        UserValidations.validateWeight(str2);
        //should fix any stray dots after func activated ^
        assertEquals("0.0", str[0]);






    }

    public void testValidateBirthDate() {
    }

    public void testValidateEmail() {
        assertEquals(true, UserValidations.validateEmail("a@b.c").isValid());
        assertEquals(true, UserValidations.validateEmail("example.com@co.ex.gov").isValid());

        //empties
        assertEquals(false, UserValidations.validateEmail(new String()).isValid());
        assertEquals(false, UserValidations.validateEmail("").isValid());
        assertEquals(false, UserValidations.validateEmail(null).isValid());


        assertEquals(false, UserValidations.validateEmail("examp@le@email.com").isValid());
        assertEquals(false, UserValidations.validateEmail("example@email.com!").isValid());
        assertEquals(false, UserValidations.validateEmail("example@email.ferrrrrrreeeeeeefeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").isValid());

    }

    @Test
    public void testValidatePassword() {
        assertEquals(true, UserValidations.validatePassword("Pass123!").isValid());
        assertEquals(false, UserValidations.validatePassword("Pass123").isValid());

        //empties
        assertEquals(false, UserValidations.validatePassword(new String()).isValid());
        assertEquals(false, UserValidations.validatePassword("").isValid());
        assertEquals(false, UserValidations.validatePassword(null).isValid());

        assertEquals(false, UserValidations.validatePassword("Pass123!ferrrrrrreeeeeeefeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").isValid());
        assertEquals(false, UserValidations.validatePassword("Pp.1").isValid());
        assertEquals(false, UserValidations.validatePassword("Password123!л").isValid());

    }

    @Test
    public void testGetAge() {
        User u = new User();
        u.setBirthDate(new Date(2005 - 1900, 1, 12));
        assertEquals(18.833333, u.getAge(), 0.1);

        u.setBirthDate(new Date(2000 - 1900, 12, 30));
        assertEquals(22.916666666666666666666666666667, u.getAge(), 0.1);
    }
}