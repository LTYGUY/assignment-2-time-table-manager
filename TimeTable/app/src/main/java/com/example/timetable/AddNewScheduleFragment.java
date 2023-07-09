//Written by: Ting Ying,

package com.example.timetable;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.app.DatePickerDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddNewScheduleFragment extends DialogFragment {
    final Calendar calendar = Calendar.getInstance();
    EditText calendarEditText;

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



        calendarEditText = v.findViewById(R.id.addNewDateEdit);
        ImageButton chooseDateBtn = (ImageButton)v.findViewById(R.id.addNewDateButton);
        ImageButton chooseTimeBtn = (ImageButton)v.findViewById(R.id.addNewTimeButton);
        Button addScheduleBtn = (Button)v.findViewById(R.id.addNewScheduleButton);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day){
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                String format = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
                calendarEditText.setText(dateFormat.format(calendar.getTime()));
            }
        };

        //ref:https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        chooseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        date,
                        calendar.get(java.util.Calendar.YEAR),
                        calendar.get(java.util.Calendar.MONTH),
                        calendar.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });

        chooseTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}