//Written by: Ting Ying

package com.example.timetable;

import android.app.Activity;
import android.widget.Toast;

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

    //ref:https://developer.android.com/guide/components/activities/activity-lifecycle
    //Should be called in the beginnings of new Activity(s)
    //Some managers may want to do something, when an activity opens
    public void OpenedActivity(Activity latestActivity) {
        currentActivity = latestActivity;
        NavigationManager.OpenedActivity(latestActivity);
    }

    //Should be called in the end of current activity
    //Some managers may want to do something, when an activity closes
    public void ClosedActivity()
    {
        currentActivity = NavigationManager.CurrentActivityClosed();
    }

    public void MakeToast(String msg)
    {
        Toast.makeText(currentActivity, msg, Toast.LENGTH_SHORT).show();
    }
}
