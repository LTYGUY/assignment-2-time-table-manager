//Written by: Ting Ying

package com.example.timetable;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

//Help make setting up EditTexts easier
public class EditTextHelper {

    //ref:https://stackoverflow.com/questions/52149015/android-edit-text-onclicklistener-doesnt-work-the-first-time-when-clicked
    public static View.OnFocusChangeListener ClearOnFirstTap(EditText editText){
        return new View.OnFocusChangeListener() {
            Boolean firstClick = false;

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                    return;

                if (firstClick)
                    return;

                editText.setText("");

                firstClick = true;
            }
        };
    }
}
