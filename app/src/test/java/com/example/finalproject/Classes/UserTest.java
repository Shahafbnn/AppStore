package com.example.finalproject.Classes;

import static org.junit.Assert.assertEquals;

import com.example.finalproject.Classes.User.User;
import com.example.finalproject.Classes.User.Validations;

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
        assertEquals(true, Validations.validateWeight((double)50).isValid());
        assertEquals(true, Validations.validateWeight(30.2).isValid());

        //empties
        //assertEquals(false, UserValidations.validateWeight(new Double(null)).isValid());
        assertEquals(false, Validations.validateWeight(-10.0).isValid());
        assertEquals(false, Validations.validateWeight(500.0).isValid());
        assertEquals(false, Validations.validateWeight((double)Long.MAX_VALUE).isValid());
    }

    public void testTestValidateWeight() {
        assertEquals(true, Validations.validateWeight(new String[]{"30"}).isValid());
        assertEquals(true, Validations.validateWeight(new String[]{"30.5"}).isValid());
        assertEquals(true, Validations.validateWeight(new String[]{"30."}).isValid());

        //empties
        assertEquals(false, Validations.validateWeight(new String[1]).isValid());
        assertEquals(false, Validations.validateWeight(new String[0]).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{null}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{""}).isValid());


        assertEquals(false, Validations.validateWeight(new String[]{"0"}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{"19"}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{"400"}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{"-50"}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{"-5-0"}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{".-5.-0"}).isValid());
        assertEquals(false, Validations.validateWeight(new String[]{".3"}).isValid());

        String[] str = new String[]{"."};
        assertEquals(false, Validations.validateWeight(str).isValid());
        String[] str2 = new String[]{"."};
        Validations.validateWeight(str2);
        //should fix any stray dots after func activated ^
        assertEquals("0.0", str[0]);






    }

    public void testValidateBirthDate() {
    }

    public void testValidateEmail() {
        assertEquals(true, Validations.validateEmail("a@b.c").isValid());
        assertEquals(true, Validations.validateEmail("example.com@co.ex.gov").isValid());

        //empties
        assertEquals(false, Validations.validateEmail(new String()).isValid());
        assertEquals(false, Validations.validateEmail("").isValid());
        assertEquals(false, Validations.validateEmail(null).isValid());


        assertEquals(false, Validations.validateEmail("examp@le@email.com").isValid());
        assertEquals(false, Validations.validateEmail("example@email.com!").isValid());
        assertEquals(false, Validations.validateEmail("example@email.ferrrrrrreeeeeeefeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").isValid());

    }

    @Test
    public void testValidatePassword() {
        assertEquals(true, Validations.validatePassword("Pass123!").isValid());
        assertEquals(false, Validations.validatePassword("Pass123").isValid());

        //empties
        assertEquals(false, Validations.validatePassword(new String()).isValid());
        assertEquals(false, Validations.validatePassword("").isValid());
        assertEquals(false, Validations.validatePassword(null).isValid());

        assertEquals(false, Validations.validatePassword("Pass123!ferrrrrrreeeeeeefeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").isValid());
        assertEquals(false, Validations.validatePassword("Pp.1").isValid());
        assertEquals(false, Validations.validatePassword("Password123!л").isValid());

    }

    @Test
    public void testGetAge() {
        User u = new User();
        u.setUserBirthDate(new Date(2005 - 1900, 1, 12));
        assertEquals(18.833333, u.getAge(), 0.1);

        u.setUserBirthDate(new Date(2000 - 1900, 12, 30));
        assertEquals(22.916666666666666666666666666667, u.getAge(), 0.1);
    }
}