//Written by: Ting Ying

package com.example.timetable;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.Stack;

public class AllManagers {
    public static AllManagers Instance;
    //no point making it non static, need to write one more '.Instance'.
    public static NavigationManager NavigationManager;
    public static DataBaseManager DataBaseManager;

    private Activity currentActivity;

    public AllManagers(Activity startingActivity, FragmentManager fm){
        if (Instance != null)
            return;

        Instance = this;
        currentActivity = startingActivity;
        fragmentManagersStack.add(fm);

        NavigationManager = new NavigationManager();
        NavigationManager.SetMainScreen(startingActivity);

        DataBaseManager = new DataBaseManager(startingActivity);
    }

    private CalendarActivity calendarActivity = null;
    public void TakeCalendarActivity(CalendarActivity activity)
    {
        calendarActivity = activity;
    }
    public void UpdateCalendarActivityUI()
    {
        if (calendarActivity == null)
        {
            Log.i("UpdateCalendarActivityUI", "is null! give up setMonthView()");
            return;
        }

        calendarActivity.setMonthView();
    }

    //Method overload. Since java don't allow default values for parameters
    public AddNewScheduleFragment PopupAddNewScheduleFragment(AddNewScheduleFragment.Purpose purpose){
        return PopupAddNewScheduleFragment(purpose, -1);
    }

    public AddNewScheduleFragment PopupAddNewScheduleFragment(AddNewScheduleFragment.Purpose purpose, int scheduleId){
        AddNewScheduleFragment frag = AddNewScheduleFragment.newInstance(purpose, scheduleId);

        frag.show(fragmentManagersStack.peek(), "");

        return frag;
    }

    private Stack<FragmentManager> fragmentManagersStack = new Stack<>();

    //ref:https://developer.android.com/guide/components/activities/activity-lifecycle
    //Should be called in the beginnings of new Activity(s)
    //Some managers may want to do something, when an activity opens
    public void OpenedActivity(Activity latestActivity, FragmentManager fm) {
        currentActivity = latestActivity;
        NavigationManager.OpenedActivity(latestActivity);
        fragmentManagersStack.add(fm);
    }

    //Should be called in the end of current activity
    //Some managers may want to do something, when an activity closes
    public void ClosedActivity()
    {
        currentActivity = NavigationManager.CurrentActivityClosed();
        fragmentManagersStack.pop();
    }

    public void MakeToast(String msg)
    {
        Toast.makeText(currentActivity, msg, Toast.LENGTH_SHORT).show();
    }

}
