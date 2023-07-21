//Written by: Lorraine, Collin, Ting Ying

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

        TextView time = convertView.findViewById(R.id.time);
        TextView date = convertView.findViewById(R.id.date);
        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);

        time.setText(item.Time);
        date.setText(item.Date);
        title.setText(item.Name);
        description.setText(item.Description);

        Button btnEdit = convertView.findViewById((R.id.editBtn));
        Button btnDelete = convertView.findViewById((R.id.deleteBtn));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewScheduleFragment frag = AllManagers.Instance.PopupAddNewScheduleFragment(AddNewScheduleFragment.Purpose.Update, item.ScheduleId);

                frag.setOnScheduleUpdatedListener(() -> {
                    ScheduleRow updatedItem = AllManagers.DataBaseManager.getScheduleRowById(item.ScheduleId);

                    item.CopyScheduleRow(updatedItem);

                    ThisDataSetChanged();
                });
            }
        });

        btnDelete.setTag(position);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ScheduleRow item = getItem(position);
                AllManagers.DataBaseManager.deleteSchedule(item.ScheduleId);
                removeItem(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void ThisDataSetChanged()
    {
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < getCount()) {
            remove(getItem(position));
        }
    }
}
