//Written by: Lorraine
package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_listview);

        ListView listView = findViewById(R.id.list_view);
        // Retrieve the selected day from the intent
        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("SELECTED_DAY");

        DataBaseManager dbManager = new DataBaseManager(this);
        // Get the schedules for the selected day
        List<ScheduleRow> scheduleItems = dbManager.getScheduleForDate(selectedDate);

        ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleItems);

        listView.setAdapter(adapter);

    }
}

