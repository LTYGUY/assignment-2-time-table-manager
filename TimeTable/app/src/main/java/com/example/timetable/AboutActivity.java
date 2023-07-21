//Written by: Ting Ying

package com.example.timetable;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        AllManagers.Instance.OpenedActivity(this);
    }

    public void GoBack(View view){
        AllManagers.NavigationManager.CloseCurrentActivity();
    }

    //https://developer.android.com/guide/components/activities/activity-lifecycle
    @Override
    protected void onStop()
    {
        super.onStop();
        AllManagers.Instance.ClosedActivity();
    }
}