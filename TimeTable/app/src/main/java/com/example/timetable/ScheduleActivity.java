//Written by: Lorraine
package com.example.timetable;

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

        // Get the selected day from the intent extras
        String selectedDay = getIntent().getStringExtra("SELECTED_DAY");
        Log.d("ListViewActivity", "Selected day: " + selectedDay);

        DataBaseManager dbManager = new DataBaseManager(this);
        // Filter the schedules based on the selected day
        List<ScheduleRow> scheduleItems = dbManager.getScheduleByDate(selectedDay);
        Log.d("ListViewActivity", "Number of schedule items: " + scheduleItems.size());

        ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleItems);
        listView.setAdapter(adapter);
    }
}

