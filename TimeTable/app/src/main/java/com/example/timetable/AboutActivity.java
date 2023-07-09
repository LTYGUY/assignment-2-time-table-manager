package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        NavigationManager.Instance.OpenedActivity(this);
    }

    public void GoBack(View view){
        NavigationManager.Instance.CloseCurrentActivity();
    }
}