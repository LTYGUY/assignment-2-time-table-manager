//Written by: Lorraine
package com.example.timetable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_date_listview);

        // Get ListView from XML layout
        ListView listView = findViewById(R.id.list_view);

        // Example data
        String[] data = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"};

        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        // Set adapter to ListView
        listView.setAdapter(adapter);
    }
}
