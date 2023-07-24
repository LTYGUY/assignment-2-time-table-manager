package com.example.timetable;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;

public class NotesActivityTest {

    public ActivityTestRule<NotesActivity> activityTestRule =
            new ActivityTestRule<>(NotesActivity.class);

    @Before
    public void setUp() {
        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void notesList_Test(){
        onView(withId(R.id.addNewNoteButton));
    }

    @Test
    public void deleteNote_Test(){
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .perform(longClick());

        onView(withText("Delete Note"))
                .perform(click());
    }
}
