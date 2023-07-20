//Written by: Ting Ying, Lorraine

package com.example.timetable;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        ImageButton allScheduleButton = v.findViewById(R.id.allScheduleButton);
        ImageButton addButton = v.findViewById(R.id.addButton);
        ImageButton aboutButton = v.findViewById(R.id.aboutScreenButton);

        setupMainScreenButton(allScheduleButton);
        setupAddButton(addButton);
        setupAboutButton(aboutButton);

        return v;
    }

    /*private void setupMainScreenButton(ImageButton allScheduleButton) {
        allScheduleButton.setOnClickListener(view -> AllManagers.NavigationManager.GoToMainActivity());
    }*/
    private void setupMainScreenButton(ImageButton allScheduleButton) {
        allScheduleButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AllScheduleActivity.class);
            startActivity(intent);
        });
    }

    private void setupAddButton(ImageButton addButton) {
        addButton.setOnClickListener(view -> {
            AddNewScheduleFragment frag = new AddNewScheduleFragment();
            frag.setOnScheduleAddedListener(() -> {
                // Get the parent activity and cast it to CalendarActivity
                CalendarActivity calendarActivity = (CalendarActivity) getActivity();

                // Call setMonthView if the cast was successful
                if (calendarActivity != null) {
                    calendarActivity.setMonthView();
                }
            });
            frag.show(getParentFragmentManager(), "");
        });
    }
    private void setupAboutButton(ImageButton aboutButton) {
        aboutButton.setOnClickListener(view -> AllManagers.NavigationManager.GoToActivity(AboutActivity.class));
    }
}