package com.example.timetable;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

public class AddNewScheduleFragmentTest {

    public ActivityTestRule<CalendarActivity> activityTestRule =
            new ActivityTestRule<>(CalendarActivity.class);

    @Before
    public void setUp() {

        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void addNewSchedule_Test(){
        onView(withId(R.id.addButton)).perform(click());

        // Fill in the title
        onView(withId(R.id.addNewNameEdit))
                .perform(replaceText(""))
                .perform(typeText("New Schedule"), closeSoftKeyboard() );

        // Fill in the content
        onView(withId(R.id.addNewDescEdit))
                .perform(replaceText(""))
                .perform(typeText("New Schedule Content"), closeSoftKeyboard() );

        // Set the date
        onView(withId(R.id.addNewDateTextView))
                .perform(replaceText(""))
                .perform(typeText("2020-12-12"), closeSoftKeyboard() );

        // Set the time
        onView(withId(R.id.addNewTimeTextView))
                .perform(replaceText(""))
                .perform(typeText("12:12"), closeSoftKeyboard() );

        // Click to save
        onView(withId(R.id.addNewScheduleButton)).perform(click());
    }

    @Test
    public void editSchedule_Test(){
    }
}
