//Written by: Collin, Ting Ying, Lorraine

package com.example.timetable;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private ArrayList<String> daysOfMonth;

    private final OnItemListener onItemListener;
    private ArrayList scheduleDates;
    private LocalDate selectedDate;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, ArrayList scheduleDates, LocalDate selectedDate)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.scheduleDates = scheduleDates;
        this.selectedDate = selectedDate;
    }

    public void updateData(ArrayList<String> daysOfMonth, ArrayList scheduleDates, LocalDate selectedDate) {
        this.daysOfMonth = daysOfMonth;
        this.scheduleDates = scheduleDates;
        this.selectedDate = selectedDate;
        notifyDataSetChanged(); // Notify the RecyclerView to update
    }

    DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MM");
    DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yy");

    public String getSelectedMonth() {
        return selectedDate.format(monthFormat);
    }

    public String getSelectedYear() {
        return selectedDate.format(yearFormat);
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        //return new CalendarViewHolder(view, onItemListener);
        return new CalendarViewHolder(view, onItemListener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        if (dayText.isEmpty())
            return;

        String dateToCheck = getDateString(dayText);

        if (isScheduleOnDate(dateToCheck)) {
            holder.dayOfMonth.setTextColor(Color.RED);
        } else {
            holder.dayOfMonth.setTextColor(Color.BLACK);
        }

        TextView cellContent = holder.content;
        String dayNumber = dayText.toString();

        String dayCellTextContent = "";
        //append more details.
        String formattedDate = StringFormatHelper.GetDate(getSelectedMonth(),dayNumber,getSelectedYear());

        List<ScheduleRow> scheduleList = AllManagers.DataBaseManager.getScheduleForDate(formattedDate);

        if (scheduleList.size() > 0)
        {
            ScheduleRow row = scheduleList.get(0);

            dayCellTextContent += row.Time + " " + row.Name;
        }

        cellContent.setText(dayCellTextContent);

    }

    private String getDateString(String dayText) {
        String currentMonth = selectedDate.format(monthFormat);
        String currentYear = selectedDate.format(yearFormat);
        String formattedDayText = formatDayText(dayText);

        return currentMonth + "/" + formattedDayText + "/" + currentYear;
    }

    private String formatDayText(String dayText) {
        return dayText.length() == 1 ? "0" + dayText : dayText;
    }

    private boolean isScheduleOnDate(String date) {
        return scheduleDates.contains(date);
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}