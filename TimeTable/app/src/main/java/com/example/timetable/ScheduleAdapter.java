package com.example.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<ScheduleRow> {
    public ScheduleAdapter(Context context, List<ScheduleRow> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_listitem, parent, false);
        }

        ScheduleRow item = getItem(position);

        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);

        title.setText(item.Name);
        description.setText(item.Description);

        Button btnEdit = convertView.findViewById((R.id.editBtn));
        Button btnDelete = convertView.findViewById((R.id.deleteBtn));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllManagers.DataBaseManager.deleteSchedule(item.ScheduleId);
            }
        });

        return convertView;
    }
}
