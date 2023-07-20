//Written by: Ting Ying

package com.example.timetable;

//Don't be afraid to type in string formats that will be used throughout the project
//Meant to never have different formatting issues.
public class  StringFormatHelper {
    public static String GetTime(int hour, int minute)
    {
        return String.format("%02d:%02d",hour,minute);
    }
}
