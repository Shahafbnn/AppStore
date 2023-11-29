package com.example.finalproject.DatabaseClasses;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter
{
    @TypeConverter
    public static long DateToLong(Date date)
    {
        return date.getTime();
    }

    @TypeConverter
    public static Date LongToDate(long value)
    {
        return new Date(value);
    }
}