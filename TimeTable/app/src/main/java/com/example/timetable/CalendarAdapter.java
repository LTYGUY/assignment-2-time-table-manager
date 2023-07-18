//Written by: Collin, Ting Ying

package com.example.timetable;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private final ArrayList events;
    private LocalDate selectedDate;
    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, ArrayList events, LocalDate selectedDate)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.events = events;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }
    DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MM");
    DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("yy");

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);
        if (!dayText.isEmpty()) {
            String dateToCheck = getDateString(dayText);

            if (isEventOnDate(dateToCheck)) {
                holder.dayOfMonth.setTextColor(Color.RED);
            } else {
                holder.dayOfMonth.setTextColor(Color.BLACK);
            }
        }
    }

    private String getDateString(String dayText) {
        String currentMonth = selectedDate.format(monthFormat); // "MM" format
        String currentYear = selectedDate.format(yearFormat); // "yyyy" format
        String formattedDayText = formatDayText(dayText);

        return currentMonth + "/" + formattedDayText + "/" + currentYear;
    }

    private String formatDayText(String dayText) {
        return dayText.length() == 1 ? "0" + dayText : dayText;
    }

    private boolean isEventOnDate(String date) {
        return events.contains(date);
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