package com.example.finalproject;

import android.content.Context;
import android.icu.util.Calendar;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.finalproject", appContext.getPackageName());
    }

    @Test
    public void testSetBirthDateFromString() {
        String date;
        date = "3/8/2020";
//        assertEquals(date, UserValidations.birthDateToString(UserValidations.getBirthDateFromString(date)));
//        date = "1/1/1800";
//        assertEquals(date, UserValidations.birthDateToString(UserValidations.getBirthDateFromString(date)));
//        date = "3/8/1";
//        assertEquals(date, UserValidations.birthDateToString(UserValidations.getBirthDateFromString(date)));
//        date = "-10/8/1";
//        assertEquals("21/7/1", UserValidations.birthDateToString(UserValidations.getBirthDateFromString(date)));
    }

    @Test
    public void testGetCurrentAge(){
        String date;
        date = "3/8/2020";
//        User u1 = new User(UserValidations.getBirthDateFromString("3/8/2020"));
//        double daysInYear = ((GregorianCalendar)u1.getBirthDate()).isLeapYear(u1.getBirthDate().get(Calendar.YEAR))?366:365;
//        //assertEquals((double)(2023 - 2020 + 3/12.0 + 9/daysInYear), u1.getCurrentAgeDouble(), 0.0001);
//        daysInYear = ((GregorianCalendar)u1.getBirthDate()).isLeapYear(u1.getBirthDate().get(Calendar.YEAR))?366:365;
//        u1 = new User(UserValidations.getBirthDateFromString("8/11/2006"));
//        assertEquals((double)(2023 - 2006 + 3/12.0 + 9/daysInYear), u1.getCurrentAgeDouble(), 0.0001);

        //alr gotta fix some stuffs.
    }

    @Test
    public void testBirthDateToString(){
        Calendar c1 = Calendar.getInstance();
        c1.clear();
        c1.set(2020, 9, 3);
//        assertEquals("3/9/2020", UserValidations.birthDateToString(c1));
//
//        c1.clear();
//        c1.set(1999, 1, 10);
//        assertEquals("10/1/1999", UserValidations.birthDateToString(c1));
    }
}