//Written by: Ting Ying
package com.example.timetable;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ScheduleRow {

    //Sort to ascending order.
    public static ArrayList<ScheduleRow> GetSortedScheduleList(ArrayList<ScheduleRow> list) {
        //ref:https://stackoverflow.com/questions/8441664/how-do-i-copy-the-contents-of-one-arraylist-into-another
        ArrayList<ScheduleRow> copyList = (ArrayList<ScheduleRow>) list.clone();
        ArrayList<ScheduleRow> sortedList = new ArrayList<>();

        while(copyList.size() > 0)
        {
            ScheduleRow earliestDate = copyList.get(0);

            for (int i = 1; i < copyList.size(); i++)
            {
                ScheduleRow otherRow = copyList.get(i);
                if (otherRow.IsEarlier(earliestDate))
                {
                    earliestDate = otherRow;
                }
            }

            sortedList.add(earliestDate);
            copyList.remove(earliestDate);
        }

        return sortedList;
    }

    public boolean IsEarlier(ScheduleRow otherRow){
        long thisEpoch = 0;
        long otherEpoch = 0;

        //ref:https://stackoverflow.com/questions/6687433/convert-a-date-format-in-epoch
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try{
            java.util.Date thisDF = df.parse(Date+ " " +Time);
            thisEpoch = thisDF.getTime();
        }
        catch (ParseException e)
        {

        }

        try{
            java.util.Date otherDF = df.parse(otherRow.Date + " " + otherRow.Time);
            otherEpoch = otherDF.getTime();
        }
        catch (ParseException e)
        {

        }


        if (thisEpoch > otherEpoch)
        {
            return false;
        }


        return true;
    }



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

    public void CopyScheduleRow(ScheduleRow toCopy)
    {
        ScheduleId = toCopy.ScheduleId;
        Name = toCopy.Name;
        Description = toCopy.Description;
        Date = toCopy.Date;
        Time = toCopy.Time;
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
