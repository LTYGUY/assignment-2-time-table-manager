//Written by: Collin, Ting Ying, Lorraine

package com.example.timetable;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView dayOfMonth;
    public final TextView content;
    private final CalendarAdapter.OnItemListener onItemListener;
    private final CalendarAdapter calendarAdapter; // reference to the adapter

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, CalendarAdapter calendarAdapter) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        content = itemView.findViewById(R.id.cellDayContent);
        this.onItemListener = onItemListener;
        this.calendarAdapter = calendarAdapter; // store the Calendar adapter reference
        itemView.setOnClickListener(this);
    }

    private String getSelectedDate() {
        String selectedMonth = calendarAdapter.getSelectedMonth();
        String selectedYear = calendarAdapter.getSelectedYear();
        if (dayOfMonth.getText().toString().equals ("")){
            return"";
        }

        String selectedDay = StringFormatHelper.GetDate(selectedMonth, dayOfMonth, selectedYear);
        return selectedDay;
    }

    private void startScheduleActivity(String selectedDate) {
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

        //Prevent from entering days with no schedules
        List<ScheduleRow> dataList = AllManagers.DataBaseManager.getScheduleForDate(selectedDate);
        if (dataList.size() < 1)
            return;

        startScheduleActivity(selectedDate);
    }
}

