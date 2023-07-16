//Written by: Ting Ying

package com.example.timetable;

import android.app.Activity;
import android.content.Context;

public class AllManagers {
    public static AllManagers Instance;
    //no point making it non static, need to write one more '.Instance'.
    public static NavigationManager NavigationManager;
    public static DataBaseManager DataBaseManager;

    public AllManagers(Activity startingActivity){
        if (Instance != null)
            return;

        Instance = this;
        NavigationManager = new NavigationManager();
        NavigationManager.SetMainScreen(startingActivity);

        DataBaseManager = new DataBaseManager(startingActivity);
    }

    //Should be called in the beginnings of new Activity(s)
    //Some managers may want to do something, when an activity opens
    public void OpenedActivity(Activity latestActivity) {
        NavigationManager.OpenedActivity(latestActivity);
    }
}
