//Written by: Collin, Ting Ying, Lorraine

package com.example.timetable;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    private final CalendarAdapter calendarAdapter; // reference to the adapter

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, CalendarAdapter calendarAdapter) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        this.calendarAdapter = calendarAdapter; // store the Calenadapter reference
        itemView.setOnClickListener(this);
    }

    private String getSelectedDate() {
        String selectedMonth = calendarAdapter.getSelectedMonth();
        String selectedYear = calendarAdapter.getSelectedYear();
        if (dayOfMonth.getText().toString().equals ("")){
            return"";
        }

        String selectedDay = String.format("%02d", Integer.parseInt(dayOfMonth.getText().toString()));
        return selectedMonth + "/" + selectedDay + "/" + selectedYear;
    }

    private void startScheduleActivity(View view, String selectedDate) {
        AllManagers.NavigationManager.GoToActivity(ScheduleActivity.class,
                (intentToModify)->
                {
                    intentToModify.putExtra(ScheduleActivity.PURPOSE,
                            ScheduleActivity.Purpose.SpecifiedDate.ordinal());
                    intentToModify.putExtra(ScheduleActivity.SELECTED_DAY, selectedDate);
                });
    }

    @Override
    public void onClick(View view) {
        String selectedDate = getSelectedDate();
        if (selectedDate.equals("")){
            return;
        }
        startScheduleActivity(view, selectedDate);
    }
}

