//Written by: Ting Ying

package com.example.timetable;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DataBaseManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //just need to run this once for initialising
        //Load up all the managers this application will use
        new AllManagers(this, getSupportFragmentManager());
        dbManager = new DataBaseManager(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
    }
}