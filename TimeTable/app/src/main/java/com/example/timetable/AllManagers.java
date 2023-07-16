//Written by: Ting Ying

package com.example.timetable;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.Callable;

public class AllManagers {
    public static AllManagers Instance;
    //no point making it non static, need to write one more '.Instance'.
    public static NavigationManager NavigationManager;
    public static DataBaseManager DataBaseManager;

    private Activity currentActivity;

    public AllManagers(Activity startingActivity){
        if (Instance != null)
            return;

        Instance = this;
        currentActivity = startingActivity;

        NavigationManager = new NavigationManager();
        NavigationManager.SetMainScreen(startingActivity);

        DataBaseManager = new DataBaseManager(startingActivity);
    }

    //Should be called in the beginnings of new Activity(s)
    //Some managers may want to do something, when an activity opens
    public void OpenedActivity(Activity latestActivity) {
        currentActivity = latestActivity;
        NavigationManager.OpenedActivity(latestActivity);
    }

    public void ClosedActivity(Activity nowCurrentActivity)
    {
        currentActivity = nowCurrentActivity;
    }

    public void MakeToast(String msg)
    {
        Toast.makeText(currentActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
