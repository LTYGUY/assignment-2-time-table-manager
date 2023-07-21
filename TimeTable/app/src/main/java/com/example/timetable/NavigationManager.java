//Written by: Ting Ying
//To help make navigating of screen to screen much more easier.

package com.example.timetable;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;
import java.util.concurrent.Callable;

public class NavigationManager {
    private Activity mainActivity = null;
    public Activity GetRunningRootActivity(){return mainActivity;}

    private Activity currentActivity = null;
    public Activity GetTopRunningActivity(){ return currentActivity; }

    //represents how the activities are layered on top of one another
    private Stack<Activity> activitiesFromBeginning = new Stack<Activity>();

    private Callable<Void> allManagersOnClosedActivity = null;

    public void SetMainScreen(Activity main)
    {
        //Don't allow SetMainScreen to happen twice, meant for the first actual main screen.
        if (mainActivity != null)
            return;

        mainActivity = main;
        OpenedActivity(mainActivity);
    }

    //To be called in onCreate of activities.
    // Keep track of what activities has opened.
    public void OpenedActivity(Activity latestActivity)
    {
        currentActivity = latestActivity;
        activitiesFromBeginning.add(latestActivity);
    }

    //NOT MainActivity.java
    //Go to the main activity that was set by SetMainScreen()
    public void GoToMainActivity()
    {
        GoToActivity(mainActivity.getClass());
    }

    //Just go to activity without caring about modifying intent.
    public void GoToActivity(Class activity)
    {
        GoToActivity(activity, new IntentDelegate() {
            @Override
            public void intentFunc(Intent intent) {

            }
        });
    }

    //Go to specified activity class,
    //with a prevention of starting same as current activity.
    //Learnt from Lorraine's work done.
    //Also allows modification to intent
    public void GoToActivity(Class activityClass, IntentDelegate intentDelegate)
    {
        //if trying to go same activity, will do nothing
        if (activityClass == currentActivity.getClass())
            return;

        Intent canBeModifiedIntent = new Intent(currentActivity, activityClass);
        intentDelegate.intentFunc(canBeModifiedIntent);

        currentActivity.startActivity(canBeModifiedIntent);
    }

    //Basically to close current activity, and return to previous activity.
    //Treat it like pressing the back button in web browser
    public void CloseCurrentActivity()
    {
        currentActivity.finish();
    }

    public Activity CurrentActivityClosed()
    {
        activitiesFromBeginning.pop();

        currentActivity = activitiesFromBeginning.peek();

        //Let AllManagers update its currentActivity
        return currentActivity;
    }

    public interface IntentDelegate{
        void intentFunc(Intent intent);
    }
}
