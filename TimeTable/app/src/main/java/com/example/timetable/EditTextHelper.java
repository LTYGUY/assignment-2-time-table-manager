//Written by: Ting Ying

package com.example.timetable;

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

    public static final String CHOOSE_A_DATE = "Choose a date";
    public static final String CHOOSE_A_TIME = "Choose a time";

    public static View.OnFocusChangeListener DateClearOnFirstTap(EditText editText){
        return ClearOnFirstTapIfNotDefault(editText, CHOOSE_A_DATE);
    }

    public static View.OnFocusChangeListener TimeClearOnFirstTap(EditText editText){
        return ClearOnFirstTapIfNotDefault(editText, CHOOSE_A_TIME);
    }

    public static View.OnFocusChangeListener ClearOnFirstTapIfNotDefault(EditText editText, String defaultText){
        editText.setText(defaultText);

        return new View.OnFocusChangeListener() {
            Boolean firstClick = false;

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                    return;


                if (firstClick)
                    return;

                //Only if first click AND the editText's text is same as CHOOSE_A_DATE, then clear the editText
                //ref:https://www.javatpoint.com/string-comparison-in-java
                //ref:https://stackoverflow.com/questions/4396376/how-to-get-edittext-value-and-display-it-on-screen-through-textview
                if (editText.getText().toString().equals(defaultText)) {
                    editText.setText("");
                }

                firstClick = true;
            }
        };
    }
}
