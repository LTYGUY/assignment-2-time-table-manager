//Written by: Ting Ying
package com.example.timetable;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScheduleRow {

    public static final String SIMPLE_DATE_TIME_FORMAT = "MM/dd/yy HH:mm";

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
        SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT);
        try{
            java.util.Date thisDF = df.parse(GetSimpleDateFormat());
            thisEpoch = thisDF.getTime();
        }
        catch (ParseException e)
        {

        }

        try{
            java.util.Date otherDF = df.parse(otherRow.GetSimpleDateFormat());
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

    public static ScheduleRow GetClosestIncomingSchedule(List<ScheduleRow> list)
    {
        if (list.size() < 1) {
            Log.i("ScheduleRow", "GetClosestIncomingSchedule(), your list contains 0 schedule rows!");
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(SIMPLE_DATE_TIME_FORMAT);
        String formattedNow = now.format(dateTimeFormatter);

        SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_TIME_FORMAT);

        long nowEpocTime = 0;
        try
        {
            java.util.Date nowD = df.parse(formattedNow);
            nowEpocTime = nowD.getTime();
        }catch(ParseException e){}

        //Setup with the first element.
        ScheduleRow closestIncomingSchedule = list.get(0);
        long smallestEpoch = Long.MAX_VALUE;
        try{
            java.util.Date d = df.parse(closestIncomingSchedule.GetSimpleDateFormat());
            long time = d.getTime();
            long timeDif = time - nowEpocTime;

            //If its positive value, means the schedule still in the future, and eligible to be smallestEpoch.
            if (timeDif >= 0)
                smallestEpoch = d.getTime();
        }
        catch (ParseException e) {}

        //Check with the remaining elements, if we can get the closest incoming schedule
        for (int i = 1; i < list.size(); i++)
        {
            ScheduleRow row = list.get(i);
            try{
                java.util.Date d = df.parse(row.GetSimpleDateFormat());
                long timeDif = d.getTime() - nowEpocTime;

                //If its negative, means the schedule is already over, skip it.
                if (timeDif < 0)
                    continue;

                //If found new smallest time difference, use this schedule.
                if (timeDif < smallestEpoch) {
                    smallestEpoch = d.getTime();
                    closestIncomingSchedule = row;
                }
            }
            catch (ParseException e) {}
        }

        return closestIncomingSchedule;
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

    public String GetSimpleDateFormat()
    {
        return Date+ " " +Time;
    }
}
