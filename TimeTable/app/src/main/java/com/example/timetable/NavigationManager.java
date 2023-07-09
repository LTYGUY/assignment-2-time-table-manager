//Written by: Ting Ying
//To help make navigating of screen to screen much more easier.

package com.example.timetable;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;

public class NavigationManager {
    public static NavigationManager Instance = null;
    private Activity mainActivity = null;
    private Activity currentActivity = null;

    //represents how the activities are layered on top of one another
    private Stack<Activity> activitiesFromBeginning = new Stack<Activity>();

    //Just need to call once on the start of the app's life.
    //even if accidentally called more, it will do nothing to persist the first instantiated Instance.
    public NavigationManager()
    {
        if (Instance != null)
            return;

        Instance = this;
    }

    public void SetMainScreen(Activity main)
    {
        mainActivity = main;
        OpenedActivity(mainActivity);
    }

    public void OpenedActivity(Activity latestActivity)
    {
        currentActivity = latestActivity;
        activitiesFromBeginning.add(latestActivity);
    }

    public void GoToActivity(Class activityClass)
    {
        currentActivity.startActivity(new Intent(currentActivity, activityClass));
    }

    public void CloseCurrentActivity()
    {
        currentActivity.finish();
        activitiesFromBeginning.pop();

        currentActivity = activitiesFromBeginning.peek();
    }
}
