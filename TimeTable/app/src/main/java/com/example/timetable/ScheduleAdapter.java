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
            convertView = initializeView(parent);
        }

        ScheduleRow item = getItem(position);

        setTextViews(convertView, item);
        setButtonActions(convertView, item, position);

        return convertView;
    }

    private View initializeView(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(R.layout.schedule_listitem, parent, false);
    }

    private void setTextViews(View convertView, ScheduleRow item) {
        TextView time = convertView.findViewById(R.id.time);
        TextView date = convertView.findViewById(R.id.date);
        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        TextView location = convertView.findViewById(R.id.location);

        time.setText(item.Time);
        date.setText(item.Date);
        title.setText(item.Name);
        description.setText(item.Description);
        location.setText(item.Location);
    }

    private void setButtonActions(View convertView, ScheduleRow item, int position) {
        Button btnEdit = convertView.findViewById(R.id.editBtn);
        Button btnDelete = convertView.findViewById(R.id.deleteBtn);

        setEditButtonAction(btnEdit, item);
        setDeleteButtonAction(btnDelete, item, position);
    }

    private void setEditButtonAction(Button btnEdit, ScheduleRow item) {
        btnEdit.setOnClickListener(v -> {
            AddNewScheduleFragment frag = AllManagers.Instance.PopupAddNewScheduleFragment(AddNewScheduleFragment.Purpose.Update, item.ScheduleId);

            frag.setOnScheduleUpdatedListener(() -> {
                ScheduleRow updatedItem = AllManagers.DataBaseManager.getScheduleRowById(item.ScheduleId);

                item.CopyScheduleRow(updatedItem);

                ThisDataSetChanged();
            });
        });
    }

    private void setDeleteButtonAction(Button btnDelete, ScheduleRow item, int position) {
        btnDelete.setTag(position);
        btnDelete.setOnClickListener(v -> {
            int position1 = (int) v.getTag();
            ScheduleRow item1 = getItem(position1);
            AllManagers.DataBaseManager.deleteSchedule(item1.ScheduleId);
            removeItem(position1);
            notifyDataSetChanged();
        });
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
