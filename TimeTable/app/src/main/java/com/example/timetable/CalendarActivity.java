//Written by: Collin, Ting Ying, Lorraine

package com.example.timetable;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private CalendarAdapter calendarAdapter;
    private DataBaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        dbManager = new DataBaseManager(this);
        //just need to run this once for initialising
        //Load up all the managers this application will use
        new AllManagers(this, getSupportFragmentManager());
        AllManagers.Instance.TakeCalendarActivity(this);

        //Uncomment this if you encounter database issue. It will drop and create schedule table
//        AllManagers.DataBaseManager.dropScheduleTable();
//        AllManagers.DataBaseManager.createScheduleTable();

        initWidgets();
        selectedDate = LocalDate.now();
        calendarAdapter = new CalendarAdapter(new ArrayList<>(), this, new ArrayList<>(), selectedDate); // Create the adapter here

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7); // Set the layout manager here
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        setMonthView();

    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        ArrayList scheduleDates = getScheduleDates();

        //Update the adapter's data instead of creating a new adapter every time month view is set.
        calendarAdapter.updateData(daysInMonth, scheduleDates, selectedDate);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                //actual days in the month.
                //the day.
                String dayNumber = String.valueOf(i - dayOfWeek);
                daysInMonthArray.add(dayNumber);
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<String> getScheduleDates() {
        return dbManager.getScheduleDates();
    }
}