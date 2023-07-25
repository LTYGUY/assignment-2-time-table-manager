//Written by: Yu Feng

package com.example.timetable;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private ListView listView;
    private NoteDatabaseHelper databaseHelper;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        listView = findViewById(R.id.listView);
        Button addNewNoteButton = findViewById(R.id.addNewNoteButton);

        databaseHelper = new NoteDatabaseHelper(this);
        notes = databaseHelper.getAllNotes();

        ArrayAdapter<Note> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        // Handling note click to open note in AddEditNoteActivity for editing
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotesActivity.this, AddEditNoteActivity.class);
                intent.putExtra("NOTE_ID", notes.get(position).getId());
                startActivity(intent);
            }
        });

        // Handling the delete record
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(NotesActivity.this)
                        .setTitle("Delete Note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Note noteToDelete = notes.get(position);
                                databaseHelper.deleteNote(noteToDelete);
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

        // Handling "Add New Note" button click to open AddEditNoteActivity for adding a new note
        addNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesActivity.this, AddEditNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refreshing the notes list after returning from AddEditNoteActivity
        notes.clear();
        notes.addAll(databaseHelper.getAllNotes());

        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
}