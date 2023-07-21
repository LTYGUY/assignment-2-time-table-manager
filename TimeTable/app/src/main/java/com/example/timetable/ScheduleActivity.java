//Written by: Lorraine, Collin

package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    public final static String PURPOSE = "purpose";
    public final static String SELECTED_DAY = "SELECTED_DAY";
    public enum Purpose{
        DisplayAll,
        SpecifiedDate,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_listview);
        AllManagers.Instance.OpenedActivity(this, getSupportFragmentManager());

        Intent intent =  getIntent();
        //ref:https://www.geeksforgeeks.org/how-to-send-data-from-one-activity-to-second-activity-in-android/
        Integer purposeInt = intent.getIntExtra(PURPOSE, 0);
        Purpose purpose = Purpose.values()[purposeInt];

        ListView listView = findViewById(R.id.list_view);

        List<ScheduleRow> scheduleItems = new ArrayList<>();
        switch (purpose)
        {
            case DisplayAll:
                scheduleItems = AllManagers.DataBaseManager.getAllSchedule();
                break;

            case SpecifiedDate:
                // Retrieve the selected day from the intent
                String selectedDate = intent.getStringExtra(SELECTED_DAY);

                // Get the schedules for the selected day
                scheduleItems = AllManagers.DataBaseManager.getScheduleForDate(selectedDate);
                break;
        }

        ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleItems);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        AllManagers.Instance.ClosedActivity();
    }
}

