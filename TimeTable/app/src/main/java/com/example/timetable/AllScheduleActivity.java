//Written by: Lorraine

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

        AllManagers.Instance.OpenedActivity(this, getSupportFragmentManager());

        ListView listView = findViewById(R.id.list_view);

        List<ScheduleRow> scheduleItems = AllManagers.DataBaseManager.getAllSchedule();

        ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleItems);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        AllManagers.Instance.ClosedActivity();
    }
}
