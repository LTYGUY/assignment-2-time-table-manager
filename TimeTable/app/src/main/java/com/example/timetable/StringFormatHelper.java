//Written by: Ting Ying

package com.example.timetable;

import android.util.Log;
import android.widget.TextView;

//Don't be afraid to type in string formats that will be used throughout the project
//Meant to never have different formatting issues.
public class  StringFormatHelper {
    public static String GetTime(int hour, int minute)
    {
        return String.format("%02d:%02d",hour,minute);
    }

    public static String GetDate(String month, int day, String year)
    {
        return month + "/" + String.format("%02d", day)  + "/" + year;
    }

    public static String GetDate(String month, String day,String year)
    {
        if (day == "")
        {
            Log.i("StringFormatHelper", "The day you have provided is empty!");
            return "";
        }

        Integer parsedDay = Integer.parseInt(day);
        String formatted = GetDate( month, parsedDay, year);

        Log.i("StringFormatHelper", formatted);
        return formatted;
    }

    public static String GetDate(String month, TextView dayTV, String year)
    {
        return GetDate(month, dayTV.getText().toString(), year);
    }

    public static String DebugVS(String one, String two)
    {
        return one + " VS " + two;
    }

    public static String DebugVS(int one, int two)
    {
        return one + " VS " + two;
    }
}
