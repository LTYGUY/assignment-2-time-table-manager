//Written by: Ting Ying
package com.example.timetable;

public class ScheduleRow {
    public int ScheduleId;
    public String Name;
    public String Description;
    public String Date;
    public String Time;

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
                + ", " + Time;
    }
}
