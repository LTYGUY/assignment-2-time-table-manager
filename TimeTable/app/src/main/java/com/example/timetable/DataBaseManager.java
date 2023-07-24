//Written By: Ting Ying, Lorraine

package com.example.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    private SQLiteDatabase db;
    public static final String TABLE_ROW_ID = "_id";
    private static final String DB_NAME = "time_table_db";
    private static final int DB_VERSION = 1;
    private static final String SCHEDULE_TABLE = "schedule_table";
    private static final String SCHEDULE_ROW_NAME = "name";
    private static final String SCHEDULE_ROW_DESCRIPTION = "description";
    public static final String SCHEDULE_ROW_DATE = "date";
    private static final String SCHEDULE_ROW_TIME = "time";
    private static final String SCHEDULE_ROW_LOCATION = "location";

    public DataBaseManager(Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        // Get a writable database
        db = helper.getWritableDatabase();
    }

    //insert schedule into schedule_table
    public void insertSchedule(String name,
                               String description,
                               String date,
                               String time,
                               String location)
    {
        String query = String.format("INSERT INTO " + SCHEDULE_TABLE +
                        " (name, description, date, time, location) VALUES ('%s', '%s', '%s', '%s', '%s');",
                name, description, date, time, location);

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
                                      String time,
                                      String location)
    {
        updateScheduleRowById(new ScheduleRow(scheduleId,name,description,date,time,location));
    }

    //Update schedule row by given ScheduleRow, by its ScheduleId
    public void updateScheduleRowById(ScheduleRow row)
    {
        ContentValues cv = new ContentValues();
        cv.put(SCHEDULE_ROW_NAME, row.Name);
        cv.put(SCHEDULE_ROW_DESCRIPTION, row.Description);
        cv.put(SCHEDULE_ROW_DATE, row.Date);
        cv.put(SCHEDULE_ROW_TIME, row.Time);
        cv.put(SCHEDULE_ROW_LOCATION, row.Location);

        Log.i("updateScheduleRowById()=", row.toString());

        db.update(SCHEDULE_TABLE, cv,TABLE_ROW_ID+"="+row.ScheduleId,null);
    }

    public boolean deleteAllSchedules()
    {
        return db.delete(SCHEDULE_TABLE, null, null) > 0;
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

        //added sorting
        list = ScheduleRow.GetSortedScheduleList(list);

        return list;
    }

    // select all schedules
    Cursor selectAllSchedule(){
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

    private String getCreateScheduleTableQuery() {
        return QueryHelper.CreateTable(SCHEDULE_TABLE,
                QueryHelper.PrimaryIncrementKeyPhrase_Comma(TABLE_ROW_ID)
                        + QueryHelper.TextNotNull_Comma(SCHEDULE_ROW_NAME)
                        + QueryHelper.TextNotNullWithDefault_Comma(SCHEDULE_ROW_DESCRIPTION, "")
                        + QueryHelper.TextNotNull_Comma(SCHEDULE_ROW_DATE)
                        + QueryHelper.TextNotNull_Comma(SCHEDULE_ROW_TIME)
                        + QueryHelper.TextNotNull(SCHEDULE_ROW_LOCATION));  // changed to the version without comma
    }

    //create schedule table. Can also change it to public, just to call it once to create the table into the database
    // also useful if you dropped the table.
    public void createScheduleTable()
    {
        //if table already exists, don't do anything
        if (tableExists(SCHEDULE_TABLE))
            return;

        String newScheduleTable = getCreateScheduleTableQuery();

        db.execSQL(newScheduleTable);
    }

    //Checks if the specified name if the table exists in the database
    private boolean tableExists(String tableName)
    {
        //ref:https://gist.github.com/ruslan-hut/389134bc0bd3cbbbc783c41f430b00ff

        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+ tableName +"';";
        Log.i("tableExists() = ", query);
        Cursor cursor = db.rawQuery(query,null);
        boolean tableExist = false;
        if(cursor.moveToFirst())
        {
            tableExist = true;
        }
        cursor.close();

        return tableExist;
    }

    // This class is created when our DataManager is initialized
    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) { super(context, DB_NAME, null, DB_VERSION);
        }
        // This method only runs the first time the database is created
        @Override
        public void onCreate(SQLiteDatabase db) {

            String newScheduleTable = getCreateScheduleTableQuery();

            db.execSQL(newScheduleTable);
        }

        // This method only runs when we increment DB_VERSION
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Warning: This will delete all data
            db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);
            onCreate(db);
        }
    }

    public ArrayList<String> getScheduleDates() {
        ArrayList<String> scheduleDates = new ArrayList<>();
        Cursor cursor = selectAllSchedule();

        // The column index of the date in the table
        int dateColumnIndex = cursor.getColumnIndex(SCHEDULE_ROW_DATE);
        while (cursor.moveToNext()) {
            String date = cursor.getString(dateColumnIndex);
            scheduleDates.add(date);
        }
        cursor.close();

        return scheduleDates;
    }

    public List<ScheduleRow> getScheduleForDate(String date){
        Cursor cursor = db.rawQuery("SELECT * FROM " + SCHEDULE_TABLE + " WHERE Date = ?", new String[]{date});
        ArrayList<ScheduleRow> list = new ArrayList<>();
        while(cursor.moveToNext())
        {
            list.add(new ScheduleRow(cursor));
        }
        cursor.close();

        //Added ascending sort.
        list = ScheduleRow.GetSortedScheduleList(list);

        // Log the date and the list
        //Log.d("DatabaseManager", "Date: " + date + ", Schedules: " + list);

        return list;
    }

    public void close() {
        db.close();
    }

}
