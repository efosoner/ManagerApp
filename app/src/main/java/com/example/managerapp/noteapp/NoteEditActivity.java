package com.example.managerapp.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class NoteEditActivity extends AppCompatActivity {

    int noteID;
    EditText noteText;
    EditText titleText;
    ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        // Initializations
        FloatingActionButton confirmButton = findViewById(R.id.confirmButton);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iNotes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        notes = gson.fromJson(json, type);
        if (notes == null) {
            notes = new ArrayList<Note>();
        }

        noteText = findViewById(R.id.notesText);
        titleText = findViewById(R.id.titleText);
        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteID", -1);

        if (noteID != -1) { // If note exists
            noteText.setText(notes.get(noteID).getNoteText());      // NotesActivity.notes.get(noteID).getNoteText());
            titleText.setText(notes.get(noteID).getNoteTitle());    // NotesActivity.notes.get(noteID).getNoteTitle());
        }
        else { // Apparently note doesn't exist
            notes.add(new Note("", ""));          // NotesActivity.notes.add(new Note("", "")); // initially, the note is empty
            noteID = notes.size() - 1;                              // noteID = NotesActivity.notes.size() - 1;
        }

        // Confirmation of note's existence
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Adds note to list updates hashSet
                notes.set(noteID, new Note(titleText.getText().toString(), noteText.getText().toString()));     // NotesActivity.notes.set(noteID, new Note(titleText.getText().toString(), noteText.getText().toString()));

                // Updates Set
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iNotes", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(notes);
                prefsEditor.putString("notes", json);
                prefsEditor.apply();

                // Toast can't be separated -- it somehow crashes app then
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

                // Beautiful toast
                TextView text = layout.findViewById(R.id.text);
                text.setText("Succesfully created note!");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                // Ends activity
                finish();
                startActivity(new Intent(getApplicationContext(), NotesActivity.class));
            }
        });
    }

    // Ends activity
    @Override
    public void onBackPressed () {
        finish();
        //startActivity(new Intent(getApplicationContext(), NotesActivity.class));
    }
}