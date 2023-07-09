//Written by: Ting Ying,

package com.example.timetable;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class AddNewScheduleFragment extends DialogFragment {

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

        ImageButton chooseDateBtn = (ImageButton)v.findViewById(R.id.addNewDateButton);
        ImageButton chooseTimeBtn = (ImageButton)v.findViewById(R.id.addNewTimeButton);
        Button addScheduleBtn = (Button)v.findViewById(R.id.addNewScheduleButton);

        chooseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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