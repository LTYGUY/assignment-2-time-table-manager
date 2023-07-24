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

public class AddEditNotesActivityTest {

    public ActivityTestRule<AddEditNoteActivity> activityTestRule =
            new ActivityTestRule<>(AddEditNoteActivity.class);

    @Before
    public void setUp() {
        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void addNote_Test(){
        // Fill in the title
        onView(withId(R.id.titleEditText))
                .perform(replaceText(""))
                .perform(typeText("New Meeting Note"), closeSoftKeyboard() );

        // Fill in the content
        onView(withId(R.id.contentEditText))
                .perform(replaceText(""))
                .perform(typeText("New Meeting Note Content"), closeSoftKeyboard() );

        // Click to save
        onView(withId(R.id.saveButton)).perform(click());
    }
}
