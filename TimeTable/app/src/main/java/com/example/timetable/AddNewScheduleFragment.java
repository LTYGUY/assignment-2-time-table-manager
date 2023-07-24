//Written by: Ting Ying, Lorraine

package com.example.timetable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AddNewScheduleFragment extends DialogFragment {

    //Wrap set of data for easier passing into parameters of functions
    private class EasySetupData{
        public View View;
        public Purpose Purpose;
        public ScheduleRow ScheduleRow;

        public EasySetupData(View v, Purpose p, ScheduleRow s)
        {
            View = v;
            Purpose = p;
            ScheduleRow = s;
        }
    }

    public static final String PURPOSE = "purpose";
    public static final String SCHEDULE_ID = "scheduleId";
    public enum Purpose{
        Add,
        Update
    }

    private ActivityResultLauncher<Intent> mapLauncher;
    Calendar calendar = Calendar.getInstance();
    TextView titleText;
    EditText nameEditText;
    EditText descriptionEditText;
    TextView dateText;
    TextView timeText;
    TextView locationText;
    double latitude;
    double longitude;

    private String getNameEditTextValue(){
        return nameEditText.getText().toString();
    }
    private String getDescriptionEditTextValue(){
        return descriptionEditText.getText().toString();
    }

    String dateEditTextValue;
    String timeEditTextValue;
    String locationEditTextValue;

    //ref:https://stackoverflow.com/questions/20405070/how-to-use-dialog-fragment-showdialog-deprecated-android
    public static AddNewScheduleFragment newInstance(Purpose purpose, int scheduleId)
    {
        AddNewScheduleFragment frag = new AddNewScheduleFragment();
        frag.setOnScheduleAddedListener(() -> {
            AllManagers.Instance.UpdateCalendarActivityUI();
        });

        Bundle args = new Bundle();

        Integer i = purpose.ordinal();
        //ref:https://stackoverflow.com/questions/5878952/cast-int-to-enum-in-java
        args.putInt(PURPOSE, i);
        args.putInt(SCHEDULE_ID, scheduleId);
        frag.setArguments(args);
        return frag;
    }

    public AddNewScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_schedule, container, false);

        //get the intended purpose of this fragment instance
        Purpose purpose =  Purpose.values()[getArguments().getInt(PURPOSE, Purpose.Add.ordinal())];
        Integer scheduleId = getArguments().getInt(SCHEDULE_ID, -1);

        ScheduleRow scheduleRow = null;

        //if its default value, means no need to find schedule id.
        //Of course PURPOSE can also aid in indicating,
        // but using schedule id as the decider is better, because you can add more PURPOSE.
        if (scheduleId != -1)
            scheduleRow = AllManagers.DataBaseManager.getScheduleRowById(scheduleId);

        //Make passing values much easier
        EasySetupData easySetupData = new EasySetupData(v, purpose, scheduleRow);

        //locationText = v.findViewById(R.id.addNewLocationTextView);
        setupMapLauncher();
        setupEditTexts(easySetupData);
        setupDateButton(easySetupData);
        setupTimeButton(easySetupData);
        setupAddScheduleButton(easySetupData);
        setupLocationButton(easySetupData);

        return v;
    }

    private void setupMapLauncher() {
        mapLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult);
    }

    private void handleActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                latitude = data.getDoubleExtra("latitude", 0);
                longitude = data.getDoubleExtra("longitude", 0);
                updateLocationText(latitude, longitude);
                locationEditTextValue = locationText.getText().toString();
                // Add this line to set locationEditTextValue to null if it's empty
                if (locationEditTextValue.isEmpty()) {
                    locationEditTextValue = null;
                }
            }
        }
    }

    private void updateLocationText(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !((List<?>) addresses).isEmpty()) {
                Address address = addresses.get(0);
                String locationName = address.getAddressLine(0);
                locationText.setText(locationName);
            } else {
                // clear the locationText when no address is found
                locationText.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Unable to get location name. Please check your network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupEditTexts(EasySetupData esd) {
        assignViewReferences(esd.View);

        switch (esd.Purpose) {
            case Add:
                setupForAdd();
                break;
            case Update:
                setupForUpdate(esd.ScheduleRow);
                break;
        }

        dateText.setText(dateEditTextValue);
        timeText.setText(timeEditTextValue);
        locationText.setText(locationEditTextValue);
    }

    private void assignViewReferences(View v) {
        titleText = v.findViewById(R.id.addNewScheduleTitle);
        nameEditText = v.findViewById(R.id.addNewNameEdit);
        descriptionEditText = v.findViewById(R.id.addNewDescEdit);
        dateText = v.findViewById(R.id.addNewDateTextView);
        timeText = v.findViewById(R.id.addNewTimeTextView);
        locationText = v.findViewById(R.id.addNewLocationTextView);
    }

    private void setupForAdd() {
        titleText.setText("Add Schedule");
        nameEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(nameEditText));
        descriptionEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(descriptionEditText));

        dateEditTextValue = "";
        timeEditTextValue = "";
        locationEditTextValue = "";
    }

    private void setupForUpdate(ScheduleRow row) {
        titleText.setText("Update Schedule");

        nameEditText.setText(row.Name);
        descriptionEditText.setText(row.Description);

        //date and time EditTextValue is null on every new of this fragment,
        // need to assign it if user wants to update schedule
        dateEditTextValue = row.Date;
        timeEditTextValue = row.Time;
        //locationEditTextValue = row.Location; //may not have to have this line since ald update in handleActivityResult
        updateLocationText(latitude, longitude); //may or may not be this method, will have to test
    }

    private void setupAddScheduleButton(EasySetupData esd) {
        Button addScheduleBtn = esd.View.findViewById(R.id.addNewScheduleButton);

        switch (esd.Purpose) {
            case Add:
                setupForAdd(addScheduleBtn);
                break;
            case Update:
                setupForUpdate(addScheduleBtn, esd.ScheduleRow);
                break;
        }
    }

    private void setupForAdd(Button addScheduleBtn) {
        addScheduleBtn.setText("Add Schedule");
        addScheduleBtn.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }
            performAddSchedule();
        });
    }

    private void setupForUpdate(Button addScheduleBtn, ScheduleRow row) {
        addScheduleBtn.setText("Update Schedule");
        addScheduleBtn.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }

            performUpdateSchedule(row.ScheduleId);
        });
    }

    private void performAddSchedule() {
        AllManagers.DataBaseManager.insertSchedule(getNameEditTextValue(),
                getDescriptionEditTextValue(),
                dateEditTextValue,
                timeEditTextValue,
                locationEditTextValue);

        AllManagers.DataBaseManager.getAndLogAllSchedule();
        dismiss();
        AllManagers.Instance.MakeToast("Successfully added schedule!");

        if (onScheduleAddedListener != null) {
            onScheduleAddedListener.onScheduleAdded();
        }
    }

    private void performUpdateSchedule(int scheduleId) {
        ScheduleRow updatedRow = new ScheduleRow(
                scheduleId,
                getNameEditTextValue(),
                getDescriptionEditTextValue(),
                dateEditTextValue,
                timeEditTextValue,
                locationEditTextValue);

        AllManagers.DataBaseManager.updateScheduleRowById(updatedRow);
        AllManagers.DataBaseManager.getAndLogAllSchedule();
        dismiss();
        AllManagers.Instance.MakeToast("Successfully updated schedule!");

        if (onScheduleUpdatedListener != null) {
            onScheduleUpdatedListener.onScheduleUpdated();
        }
    }

    private void setupDateButton(EasySetupData esd) {
        View v = esd.View;
        ImageButton chooseDateBtn = v.findViewById(R.id.addNewDateButton);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            String format = "MM/dd/yy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

            dateEditTextValue = dateFormat.format(calendar.getTime());
            dateText.setText(dateEditTextValue);
        };

        chooseDateBtn.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupTimeButton(EasySetupData esd) {
        View v = esd.View;

        ImageButton chooseTimeBtn = v.findViewById(R.id.addNewTimeButton);

        chooseTimeBtn.setOnClickListener(view -> {
            calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getContext(), (timePickerView, selectedHour, selectedMinute) -> {
                timeEditTextValue = StringFormatHelper.GetTime(selectedHour, selectedMinute);
                timeText.setText(timeEditTextValue);
            }, hour, minute, true);
            timePicker.setTitle("Select Time");
            timePicker.show();
        });
    }

    private void setupLocationButton(EasySetupData esd) {
        View v = esd.View;
        ImageButton locationButton = v.findViewById(R.id.addNewLocationButton);
        locationButton.setOnClickListener(v1 -> {
            Intent mapIntent = new Intent(getContext(), MapsActivity.class);
            mapLauncher.launch(mapIntent);
        });
    }

    private boolean validateFields()
    {
        if (nameEditText.getText().toString().equals(""))
        {
            AllManagers.Instance.MakeToast("Please give a name.");
            return false;
        }

        if (dateText.getText().toString().equals(""))
        {
            AllManagers.Instance.MakeToast("Please pick a date");
            return false;
        }

        if (timeText.getText().toString().equals(""))
        {
            AllManagers.Instance.MakeToast("Please pick a time");
            return false;
        }
        return true;
    }

    private OnScheduleAddedListener onScheduleAddedListener;

    public interface OnScheduleAddedListener {
        void onScheduleAdded();
    }

    public void setOnScheduleAddedListener(OnScheduleAddedListener onScheduleAddedListener) {
        this.onScheduleAddedListener = onScheduleAddedListener;
    }

    private OnScheduleUpdatedListener onScheduleUpdatedListener;

    public interface OnScheduleUpdatedListener{
        void onScheduleUpdated();
    }

    public void setOnScheduleUpdatedListener(OnScheduleUpdatedListener listener){
        this.onScheduleUpdatedListener = listener;
    }

}