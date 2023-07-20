package com.example.timetable;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AllScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_listview);

        ListView listView = findViewById(R.id.list_view);

        DataBaseManager dbManager = new DataBaseManager(this);
        List<ScheduleRow> scheduleItems = dbManager.getAllSchedule();

        ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleItems);

        listView.setAdapter(adapter);
    }
}
