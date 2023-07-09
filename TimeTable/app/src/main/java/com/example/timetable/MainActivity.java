package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //just need to happen once, but can be called multiple times without breaking.
        new NavigationManager();

        NavigationManager.Instance.SetMainScreen(this);
        NavigationManager.Instance.SetCurrentActivity(this);
    }
}