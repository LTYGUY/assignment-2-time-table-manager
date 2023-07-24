//Written by: Yu Feng

package com.example.timetable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AddEditNoteActivity extends AppCompatActivity {

    private NoteDatabaseHelper db;
    private EditText titleEditText;
    private EditText contentEditText;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;


    private int noteId = -1;

    private String datapath = Environment.getExternalStorageDirectory() + "/Documents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        AssetManager assetManager = getAssets();
        try {
            File dir = new File(datapath + "/tessdata/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            InputStream in = assetManager.open("eng.traineddata");
            OutputStream out = new FileOutputStream(datapath + "/tessdata/eng.traineddata");                    byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : "+e.toString());
        }

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

                if(title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddEditNoteActivity.this, "Please enter a title and content", Toast.LENGTH_SHORT).show();
                    return;
                }

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
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");

        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
    }

    private void convertImageToText() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                extractTextFromImage(bitmap);
            } catch (IOException e) {
                Log.e("AddEditNoteActivity", "Error while loading image: " + e.getMessage());
            }
        }else if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                contentEditText.append(result.get(0));
            }
        }
    }

    private void extractTextFromImage(Bitmap bitmap) {
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        String path = Environment.getExternalStorageDirectory() + "/tesseract/";
        Log.d("AddEditNoteActivity", "Tesseract data path1: " + path);
        String lang = "eng"; // Replace "eng" with the language code for the language you want to support
        tessBaseAPI.init(datapath, lang);

        tessBaseAPI.setImage(bitmap);

        String recognizedText = tessBaseAPI.getUTF8Text();

        tessBaseAPI.end();

        contentEditText.append(recognizedText);
    }
}