//Written by: Ting Ying, Yu Feng

package com.example.timetable;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        try {
            AllManagers.Instance.OpenedActivity(this, getSupportFragmentManager());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GoBack(View view){
        try {
            AllManagers.NavigationManager.CloseCurrentActivity();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //https://developer.android.com/guide/components/activities/activity-lifecycle
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try {
            AllManagers.Instance.ClosedActivity();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}