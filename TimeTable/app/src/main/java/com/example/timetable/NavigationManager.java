//Written by: Ting Ying
//To help make navigating of screen to screen much more easier.

package com.example.timetable;

import android.app.Activity;
import android.content.Intent;

public class NavigationManager {
    public static NavigationManager Instance = null;
    private Activity mainActivity = null;
    private Activity currentActivity = null;

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
    }

    public void SetCurrentActivity(Activity current)
    {
        currentActivity = current;
    }

    public void GoToActivity(Class activityClass)
    {
        currentActivity.startActivity(new Intent(currentActivity, activityClass));
    }
}
