//Written by: Ting Ying, Lorraine, Yu Feng

package com.example.timetable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

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
        ImageButton notesButton = v.findViewById(R.id.notesScreenButton);
        ImageButton aboutButton = v.findViewById(R.id.aboutScreenButton);

        setupMainScreenButton(allScheduleButton);
        setupAddButton(addButton);
        setupNotesButton(notesButton);
        setupAboutButton(aboutButton);

        return v;
    }

    private void setupMainScreenButton(ImageButton allScheduleButton) {
        allScheduleButton.setOnClickListener(view -> {
            AllManagers.NavigationManager.GoToActivity(ScheduleActivity.class,
                    (intentToModify)->
            {
                intentToModify.putExtra(ScheduleActivity.PURPOSE,
                        ScheduleActivity.Purpose.DisplayAll.ordinal());
            });
        });
    }

    private void setupAddButton(ImageButton addButton) {
        addButton.setOnClickListener(view -> {
            AllManagers.Instance.PopupAddNewScheduleFragment(AddNewScheduleFragment.Purpose.Add);
        });
    }
    private void setupAboutButton(ImageButton aboutButton) {
        aboutButton.setOnClickListener(view -> AllManagers.NavigationManager.GoToActivity(AboutActivity.class));
    }

    private void setupNotesButton(ImageButton notesButton) {
        notesButton.setOnClickListener(view -> AllManagers.NavigationManager.GoToActivity(NotesActivity.class));
    }
}