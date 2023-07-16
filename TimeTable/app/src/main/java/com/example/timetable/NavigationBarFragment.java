//Written by: Ting Ying,

package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class NavigationBarFragment extends Fragment {

    public NavigationBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        //calendar button in the main navigation bar
        ImageButton mainScreenButton = (ImageButton)v.findViewById(R.id.mainScreenButton);
        ImageButton addButton = (ImageButton)v.findViewById(R.id.addButton);
        ImageButton aboutButton = (ImageButton)v.findViewById(R.id.aboutScreenButton);


        mainScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllManagers.NavigationManager.GoToMainActivity();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewScheduleFragment frag = new AddNewScheduleFragment();
                frag.show(getParentFragmentManager(), "");
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllManagers.NavigationManager.GoToActivity(AboutActivity.class);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }
}