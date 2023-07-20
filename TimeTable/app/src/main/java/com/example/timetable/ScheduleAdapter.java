package com.example.timetable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

        btnDelete.setTag(position);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ScheduleRow item = getItem(position);
                AllManagers.DataBaseManager.deleteSchedule(item.ScheduleId);
                AllManagers.DataBaseManager.deleteSchedule(item.ScheduleId);

                // Now, update the UI by removing the item from the data source
                // and notifying the adapter about the change.
                removeItem(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    public void removeItem(int position) {
        if (position >= 0 && position < getCount()) {
            remove(getItem(position));
        }
    }
}
