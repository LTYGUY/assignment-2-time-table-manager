package com.example.timetable;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

public class AboutActivityTest {

    public ActivityTestRule<AboutActivity> activityTestRule =
            new ActivityTestRule<>(AboutActivity.class);

    @Before
    public void setUp() {
        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void seeAboutPage_Test(){
        onView(withId(R.id.aboutContent2))
                .check(matches(withText(containsString("Timely Table is a timetable"))));
    }

    @Test
    public void goBackHome_Test(){
        onView(withId(R.id.aboutBackButton)).perform(click());
    }
}
