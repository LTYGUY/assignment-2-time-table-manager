//Written by: Ting Ying
package com.example.timetable;

import android.database.Cursor;

public class ScheduleRow {
    public int ScheduleId;
    public String Name;
    public String Description;
    public String Date;
    public String Time;

    public ScheduleRow(Cursor c)
    {
        ScheduleId = c.getInt(0);
        Name = c.getString(1);
        Description = c.getString(2);
        Date = c.getString(3);
        Time = c.getString(4);
    }

    public ScheduleRow(int scheduleId, String name, String description, String date, String time)
    {
        ScheduleId = scheduleId;
        Name = name;
        Description = description;
        Date = date;
        Time = time;
    }

    //For debugging
    public String toString()
    {
        return "ScheduleId: " + ScheduleId
                + ", Name: " + Name
                + ", Description: " + Description
                + ", Date: " + Date
                + ", Time: " + Time;
    }
}
