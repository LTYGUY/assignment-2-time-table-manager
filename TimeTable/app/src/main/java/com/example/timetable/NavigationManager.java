//Written by: Ting Ying
//To help make navigating of screen to screen much more easier.

package com.example.timetable;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;

public class NavigationManager {
    private Activity mainActivity = null;
    private Activity currentActivity = null;

    //represents how the activities are layered on top of one another
    private Stack<Activity> activitiesFromBeginning = new Stack<Activity>();

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

    public void GoToMainActivity()
    {
        GoToActivity(mainActivity.getClass());
    }

    public void GoToActivity(Class activityClass)
    {
        //if trying to go same activity, will do nothing
        if (activityClass == currentActivity.getClass())
            return;

        currentActivity.startActivity(new Intent(currentActivity, activityClass));
    }

    //Basically to close current activity, and return to previous activity.
    //Treat it like pressing the back button in web browser
    public void CloseCurrentActivity()
    {
        currentActivity.finish();
        activitiesFromBeginning.pop();

        currentActivity = activitiesFromBeginning.peek();
    }
}
