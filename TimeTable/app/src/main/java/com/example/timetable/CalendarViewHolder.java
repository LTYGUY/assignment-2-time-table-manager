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
    public void onClick(View view)
    {
        //onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
        Intent intent = new Intent(view.getContext(), ListViewActivity.class);
        //String day = (String) dayOfMonth.getText();
        //intent.putExtra("dayOfMonth", day);
        view.getContext().startActivity(intent);
    }
}