//Written by: Ting Ying, Lorraine

package com.example.timetable;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddNewScheduleFragment extends DialogFragment {
    Calendar calendar = Calendar.getInstance();
    EditText nameEditText;
    EditText descriptionEditText;
    EditText dateEditText;
    EditText timeEditText;

    //can use these to grab text directly from EditText
    private String getNameEditTextValue(){
        return nameEditText.getText().toString();
    }
    private String getDescriptionEditTextValue(){
        return descriptionEditText.getText().toString();
    }
    // can expect that these values are accurate,
    // as i will assign string value to this first, then onto setText() of their EditText
    String dateEditTextValue;
    String timeEditTextValue;

    public AddNewScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_new_schedule, container, false);

        setupEditTexts(v);
        setupDateButton(v);
        setupTimeButton(v);
        setupAddScheduleButton(v);

        return v;
    }

    private void setupEditTexts(View v) {
        nameEditText = v.findViewById(R.id.addNewNameEdit);
        descriptionEditText = v.findViewById(R.id.addNewDescEdit);
        dateEditText = v.findViewById(R.id.addNewDateEdit);
        timeEditText = v.findViewById(R.id.addNewTimeEditField);

        nameEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(nameEditText));
        descriptionEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(descriptionEditText));
        dateEditText.setOnFocusChangeListener(EditTextHelper.DateClearOnFirstTap(dateEditText));
        timeEditText.setOnFocusChangeListener(EditTextHelper.TimeClearOnFirstTap(timeEditText));
    }

    private void setupDateButton(View v) {
        ImageButton chooseDateBtn = (ImageButton) v.findViewById(R.id.addNewDateButton);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                String format = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

                dateEditTextValue = dateFormat.format(calendar.getTime());
                dateEditText.setText(dateEditTextValue);
            }
        };

        chooseDateBtn.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupTimeButton(View v) {
        ImageButton chooseTimeBtn = (ImageButton) v.findViewById(R.id.addNewTimeButton);

        chooseTimeBtn.setOnClickListener(view -> {
            calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getContext(), (timePickerView, selectedHour, selectedMinute) -> {
                timeEditTextValue = StringFormatHelper.GetTime(selectedHour, selectedMinute);
                timeEditText.setText(timeEditTextValue);
            }, hour, minute, true);
            timePicker.setTitle("Select Time");
            timePicker.show();
        });
    }

    private void setupAddScheduleButton(View v) {
        Button addScheduleBtn = (Button) v.findViewById(R.id.addNewScheduleButton);

        addScheduleBtn.setOnClickListener(view -> {
            AllManagers.DataBaseManager.insertSchedule(getNameEditTextValue(),
                    getDescriptionEditTextValue(),
                    dateEditTextValue,
                    timeEditTextValue);

            AllManagers.DataBaseManager.getAndLogAllSchedule();

            dismiss();

            AllManagers.Instance.MakeToast("Successfully added schedule!");

            if (onScheduleAddedListener != null) {
                onScheduleAddedListener.onScheduleAdded();
            }
        });
    }

    public interface OnScheduleAddedListener {
        void onScheduleAdded();
    }
    private OnScheduleAddedListener onScheduleAddedListener;

    public void setOnScheduleAddedListener(OnScheduleAddedListener onScheduleAddedListener) {
        this.onScheduleAddedListener = onScheduleAddedListener;
    }
}