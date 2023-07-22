package com.example.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddEditNoteActivity extends AppCompatActivity {

    private NoteDatabaseHelper db;
    private EditText titleEditText;
    private EditText contentEditText;
    private SpeechRecognizer speechRecognizer;
    private static final int PICK_IMAGE_REQUEST = 1;


    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button speechToTextButton = findViewById(R.id.speechToTextButton);
        Button imageToTextButton = findViewById(R.id.imageToTextButton);

        db = new NoteDatabaseHelper(this);

        // Check for RECORD_AUDIO permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }

        // Check if we're editing an existing note
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getInt("NOTE_ID");
            Note note = db.getNote(noteId);
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                if (noteId == -1) {
                    // This is a new note, so set created_at and updated_at to the current time
                    String createdAt = updatedAt;
                    db.insertNote(new Note(-1, title, content, createdAt, updatedAt));
                } else {
                    // This is an existing note, so keep created_at the same and update updated_at
                    Note note = db.getNote(noteId);
                    note.setTitle(title);
                    note.setContent(content);
                    note.setUpdatedAt(updatedAt);
                    db.updateNote(note);
                }
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        speechToTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertSpeechToText();
            }
        });

        imageToTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertImageToText();
            }
        });
    }

    private void convertSpeechToText() {

        // Check if speech recognition is available on the device
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech Recognition is not available on this device!", Toast.LENGTH_LONG).show();
            // Disable the speech-to-text button or handle the situation ...
        } else {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

            if (speechRecognizer == null) {
                Toast.makeText(this, "Failed to initialize SpeechRecognizer!", Toast.LENGTH_LONG).show();
            } else {

                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speechRecognizer.startListening(speechIntent);            }
        }


    }

    private void convertImageToText() {
        // todo
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}