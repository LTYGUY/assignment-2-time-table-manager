package com.example.timetable;

public class StringFormatHelper {
    public static String GetTime(int hour, int minute)
    {
        return String.format("%02d:%02d",hour,minute);
    }
}
