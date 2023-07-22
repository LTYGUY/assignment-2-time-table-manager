//Written by: Ting Ying, Lorraine

package com.example.timetable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Locale;



//going to use it for editing existing schedules as well
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
    private static final int REQUEST_CODE_MAP = 1;
    Calendar calendar = Calendar.getInstance();

    TextView titleText;
    EditText nameEditText;
    EditText descriptionEditText;
    TextView dateText;
    TextView timeText;

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

        //regardless of purpose, this will be setup.
        mapLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result from the MapActivity here
                        Intent data = result.getData();
                        if (data != null) {
                            // Get the selected location data from the MapActivity
                            double latitude = data.getDoubleExtra("latitude", 0);
                            double longitude = data.getDoubleExtra("longitude", 0);

                            // Do something with the selected location (latitude and longitude)
                            // For example, update the EditText with the selected location
                            String selectedLocation = latitude + ", " + longitude;
                        }
                    }
                });

        setupEditTexts(easySetupData);
        setupDateButton(easySetupData);
        setupTimeButton(easySetupData);
        setupAddScheduleButton(easySetupData);
        setupMapButton(easySetupData);

        return v;
    }

    private void setupEditTexts(EasySetupData esd) {
        View v = esd.View;


        titleText = v.findViewById(R.id.addNewScheduleTitle);
        nameEditText = v.findViewById(R.id.addNewNameEdit);
        descriptionEditText = v.findViewById(R.id.addNewDescEdit);
        dateText = v.findViewById(R.id.addNewDateTextView);
        timeText = v.findViewById(R.id.addNewTimeTextView);

        switch (esd.Purpose)
        {
            case Add:
                titleText.setText("Add Schedule");
                nameEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(nameEditText));
                descriptionEditText.setOnFocusChangeListener(EditTextHelper.ClearOnFirstTap(descriptionEditText));

                dateEditTextValue = "";
                timeEditTextValue = "";
                break;

            case Update:
                titleText.setText("Update Schedule");
                ScheduleRow row = esd.ScheduleRow;

                nameEditText.setText(row.Name);
                descriptionEditText.setText(row.Description);

                //date and time EditTextValue is null on every new of this fragment,
                // need to assign it if user wants to update schedule
                dateEditTextValue = esd.ScheduleRow.Date;
                timeEditTextValue = esd.ScheduleRow.Time;

                break;
        }

        dateText.setText(dateEditTextValue);
        timeText.setText(timeEditTextValue);
    }

    private void setupDateButton(EasySetupData esd) {
        View v = esd.View;

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
                dateText.setText(dateEditTextValue);
            }
        };

        chooseDateBtn.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void setupTimeButton(EasySetupData esd) {
        View v = esd.View;

        ImageButton chooseTimeBtn = (ImageButton) v.findViewById(R.id.addNewTimeButton);

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
    private void setupMapButton(EasySetupData esd) {
        View v = esd.View;

        Button mapButton = v.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the MapActivity to select a location
                Intent mapIntent = new Intent(getContext(), MapsActivity.class);
                mapLauncher.launch(mapIntent);
            }
        });
    }


    private void setupAddScheduleButton(EasySetupData esd) {
        View v = esd.View;

        Button addScheduleBtn = (Button) v.findViewById(R.id.addNewScheduleButton);

        View.OnClickListener codeToRunOnClick = null;

        switch (esd.Purpose)
        {
            case Add:
                addScheduleBtn.setText("Add a schedule");
                codeToRunOnClick = view -> {
                    if (!allowAddBtnToBePressed())
                        return;

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
                };
                break;

            case Update:

                addScheduleBtn.setText("Update schedule");
                codeToRunOnClick = view -> {
                    if (!allowAddBtnToBePressed())
                        return;

                    ScheduleRow updatedRow = new ScheduleRow(
                            esd.ScheduleRow.ScheduleId,
                            getNameEditTextValue(),
                            getDescriptionEditTextValue(),
                            dateEditTextValue,
                            timeEditTextValue);

                    AllManagers.DataBaseManager.updateScheduleRowById(updatedRow);

                    AllManagers.DataBaseManager.getAndLogAllSchedule();

                    dismiss();

                    AllManagers.Instance.MakeToast("Successfully updated schedule!");

                    if (onScheduleUpdatedListener != null) {
                        onScheduleUpdatedListener.onScheduleUpdated();
                    }
                };
                break;
        }

        addScheduleBtn.setOnClickListener(codeToRunOnClick);
    }

    private boolean allowAddBtnToBePressed()
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

    public interface OnScheduleUpdatedListener{
        void onScheduleUpdated();
    }

    private OnScheduleUpdatedListener onScheduleUpdatedListener;
    public void setOnScheduleUpdatedListener(OnScheduleUpdatedListener listener){
        this.onScheduleUpdatedListener = listener;
    }

    public interface OnScheduleAddedListener {
        void onScheduleAdded();
    }

    private OnScheduleAddedListener onScheduleAddedListener;

    public void setOnScheduleAddedListener(OnScheduleAddedListener onScheduleAddedListener) {
        this.onScheduleAddedListener = onScheduleAddedListener;
    }
}