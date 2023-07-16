//Written by: Ting Ying

package com.example.timetable;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddNewScheduleFragment extends DialogFragment {
    Calendar calendar = Calendar.getInstance();

    EditText nameEditText;
    EditText descriptionEditText;

    EditText dateEditText;
    EditText timeEditText;

    //can use these to grab text directly from EditText
    private String getNameEditTextValue(){
        return nameEditText.getText().toString();
    }
    private String getDescriptionEditTextValue(){
        return descriptionEditText.getText().toString();
    }
    //can expect that these values are accurate,
    // as i will assign string value to this first, then onto setText() of their EditText
    String dateEditTextValue;
    String timeEditTextValue;

    public AddNewScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_new_schedule, container, false);


        nameEditText = v.findViewById(R.id.addNewNameEdit);
        descriptionEditText = v.findViewById(R.id.addNewDescEdit);
        dateEditText = v.findViewById(R.id.addNewDateEdit);
        timeEditText = v.findViewById(R.id.addNewTimeEditField);
        ImageButton chooseDateBtn = (ImageButton)v.findViewById(R.id.addNewDateButton);
        ImageButton chooseTimeBtn = (ImageButton)v.findViewById(R.id.addNewTimeButton);
        Button addScheduleBtn = (Button)v.findViewById(R.id.addNewScheduleButton);

        nameEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(nameEditText));
        descriptionEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(descriptionEditText));
        dateEditText.setOnFocusChangeListener(EditTextHelper.DateClearOnFirstTap(dateEditText));
        timeEditText.setOnFocusChangeListener(EditTextHelper.TimeClearOnFirstTap(timeEditText));

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day){
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                String format = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

                dateEditTextValue = dateFormat.format(calendar.getTime());
                dateEditText.setText(dateEditTextValue);
            }
        };

        //ref:https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        chooseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();

                new DatePickerDialog(getContext(),
                        date,
                        calendar.get(java.util.Calendar.YEAR),
                        calendar.get(java.util.Calendar.MONTH),
                        calendar.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });

        //ref:https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
        chooseTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();

                int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = calendar.get(java.util.Calendar.MINUTE);

                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        //ref:https://javarevisited.blogspot.com/2013/02/add-leading-zeros-to-integers-Java-String-left-padding-example-program.html#:~:text=The%20format()%20method%20of,is%20used%20to%20print%20integers.
                        //ref:https://www.javatpoint.com/java-string-format

                        timeEditTextValue = StringFormatHelper.GetTime(selectedHour, selectedMinute);
                        timeEditText.setText(timeEditTextValue);
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });

        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AllManagers.DataBaseManager.insertSchedule(getNameEditTextValue(),
//                        getDescriptionEditTextValue(),
//                        dateEditTextValue,
//                        timeEditTextValue);

                dismiss();

                AllManagers.Instance.MakeToast("Successfully added schedule!");
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}