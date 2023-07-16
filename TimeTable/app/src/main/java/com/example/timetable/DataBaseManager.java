//Written By: Ting Ying
package com.example.timetable;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DataBaseManager {
    private SQLiteDatabase db;
    public static final String TABLE_ROW_ID = "_id";
    private static final String DB_NAME = "time_table_db";
    private static final int DB_VERSION = 1;

    //name for the table that will contain all schedules and their information
    private static final String SCHEDULE_TABLE = "schedule_table";
    private static final String SCHEDULE_ROW_NAME = "name";
    private static final String SCHEDULE_ROW_DESCRIPTION = "description";
    private static final String SCHEDULE_ROW_DATE = "date";
    private static final String SCHEDULE_ROW_TIME = "time";

    public DataBaseManager(Context context) {
        // Create an instance of our internal CustomSQLiteOpenHelper class
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        // Get a writable database
        db = helper.getWritableDatabase();
    }

    //insert schedule into schedule_table
    public void insertSchedule(String name,
                               String description,
                               String date,
                               String time)
    {
        //Easier to read
        String query = String.format("INSERT INTO " + SCHEDULE_TABLE +
                " (name, description, date, time) VALUES ('%s', '%s', '%s', '%s');", name, description, date, time);

        Log.i("insertSchedule() = ", query);
        db.execSQL(query);
    }

    public ScheduleRow getScheduleRowById(int scheduleID)
    {
        String query = QueryHelper.SelectWhereInt(SCHEDULE_TABLE, TABLE_ROW_ID, scheduleID);

        Log.i("getScheduleRowById()=", query);
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst())
        {
            return new ScheduleRow(c);
        }

        return null;
    }


    //Overload method. update schedule row by given id
    public void updateScheduleRowById(int scheduleId,
                                      String name,
                                      String description,
                                      String date,
                                      String time)
    {
        updateScheduleRowById(new ScheduleRow(scheduleId,name,description,date,time));
    }

    //Update schedule row by given ScheduleRow, by its ScheduleId
    public void updateScheduleRowById(ScheduleRow row)
    {
        //ref:https://stackoverflow.com/questions/9798473/sqlite-in-android-how-to-update-a-specific-row
        ContentValues cv = new ContentValues();
        cv.put(SCHEDULE_ROW_NAME, row.Name);
        cv.put(SCHEDULE_ROW_DESCRIPTION, row.Description);
        cv.put(SCHEDULE_ROW_DATE, row.Date);
        cv.put(SCHEDULE_ROW_TIME, row.Time);

        Log.i("updateScheduleRowById()=", row.toString());

        db.update(SCHEDULE_TABLE, cv,TABLE_ROW_ID+"="+row.ScheduleId,null);
    }


    public boolean deleteSchedule(int scheduleId)
    {
        Log.i("deleteSchedule() = ", TABLE_ROW_ID + "=" + scheduleId);
        //ref:https://stackoverflow.com/questions/7510219/deleting-row-in-sqlite-in-android
        boolean deleteSuccessful = db.delete(SCHEDULE_TABLE, TABLE_ROW_ID + "=" + scheduleId, null) > 0;
        return deleteSuccessful;
    }

    //returns ArrayList<ScheduleRow> and log all schedules
    public ArrayList<ScheduleRow> getAndLogAllSchedule(){
        ArrayList<ScheduleRow> list = AllManagers.DataBaseManager.getAllSchedule();

        //the tag can be anything you want
        for(int i = 0; i < list.size(); i++)
            Log.i("DEBUG", list.get(i).toString());

        return list;
    }

    //Get an entire list of all schedules
    public ArrayList<ScheduleRow> getAllSchedule(){
        Cursor cursor = selectAllSchedule();
        ArrayList<ScheduleRow> list = new ArrayList<>();
        while(cursor.moveToNext())
        {
            list.add(new ScheduleRow(cursor));
        }
        return list;
    }

    // select all schedules
    private Cursor selectAllSchedule(){
        Cursor c = db.rawQuery("SELECT * from " + SCHEDULE_TABLE, null);
        return c;
    }

    //check if schedule table exists
    public boolean scheduleTableExists()
    {
        return tableExists(SCHEDULE_TABLE);
    }

    //drop schedule_table table
    public void dropScheduleTable()
    {
        dropTable(SCHEDULE_TABLE);
    }

    //drops specified table
    private void dropTable(String tableName)
    {
        if (!tableExists(tableName))
            return;

        String query = "DROP TABLE " + tableName + ";";
        Log.i("dropTable() = ", query);
        db.execSQL(query);
    }

    //Checks if the specified name if the table exists in the database
    private boolean tableExists(String tableName)
    {
        //ref:https://gist.github.com/ruslan-hut/389134bc0bd3cbbbc783c41f430b00ff
        int count = 0;

        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+ tableName +"';";
        Log.i("tableExists() = ", query);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            count = cursor.getInt(0);
        }
        cursor.close();

        return count > 0;
    }

    // This class is created when our DataManager is initialized
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) { super(context, DB_NAME, null, DB_VERSION);
        }
        // This method only runs the first time the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {

            // Create schedule_table
            String newScheduleTable = QueryHelper.CreateTable(SCHEDULE_TABLE,
                    QueryHelper.PrimaryIncrementKeyPhrase_Comma(TABLE_ROW_ID)
                    + QueryHelper.TextNotNull_Comma(SCHEDULE_ROW_NAME)
                    + QueryHelper.TextNotNullWithDefault_Comma(SCHEDULE_ROW_DESCRIPTION, "")
                    + QueryHelper.TextNotNull_Comma(SCHEDULE_ROW_DATE)
                    + QueryHelper.TextNotNull(SCHEDULE_ROW_TIME));

            db.execSQL(newScheduleTable); }

        // This method only runs when we increment DB_VERSION
        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
        }
    }
}
