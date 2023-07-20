//Written by: Collin, Ting Ying, Lorraine

package com.example.timetable;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //String selectedDate = selectedMonth + "/" + dayOfMonth.getText().toString() + "/" + selectedYear;
        //Log.d("CalendarViewHolder", "Date selected: " + selectedDate);
        Intent intent = new Intent(view.getContext(), ScheduleActivity.class);
        //intent.putExtra("SELECTED_DAY", selectedDate);
        view.getContext().startActivity(intent);
    }
}