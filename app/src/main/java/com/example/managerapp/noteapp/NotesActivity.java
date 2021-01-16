package com.example.managerapp.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class NotesActivity extends AppCompatActivity {

    static ArrayList<Note> notes = new ArrayList<Note>();
    static ArrayAdapter<Note> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        SearchView searchView = findViewById(R.id.searchView);

        // Initializations
        ListView listView = findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iNotes", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("notes", null);
        Type type = new TypeToken<ArrayList<Note>>() {
        }.getType();
        notes = gson.fromJson(json, type);
        if (notes == null) {
            notes = new ArrayList<Note>();
        }

        // Button
        FloatingActionButton addNoteButton = findViewById(R.id.addNoteButton);
        // Launches activity
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), NoteEditActivity.class));
            }
        });

        if(notes.isEmpty()){
            toast("You have no notes! Create one!");
        }

        arrayAdapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        // Method responsible for opening Edit Activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), NoteEditActivity.class);
                intent.putExtra("noteID", position);
                startActivity(intent);
            }
        });

        // Method responsible for dialog for/and deleting notes
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(NotesActivity.this) // I couldn't use getApplicationContext() here as we want the activity to be the context, not the application
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) // to remove the selected note once "Yes" is pressed
                            {
                                // Removes note from list
                                notes.remove(position);
                                NotesActivity.arrayAdapter.notifyDataSetChanged();

                                // Updates set
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iNotes", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(notes);
                                prefsEditor.putString("notes", json);
                                prefsEditor.apply();

                                // Beautiful toast
                                toast("Successfully deleted note!");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true; // If this is false listener requires another shortclick after long click
            }
        });


        // Method responsible for search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Static search -- searches after click
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if (notes.contains(query)) {
//                    arrayAdapter.getFilter().filter(query);
//                    arrayAdapter.notifyDataSetChanged();
//                }
//                else {
//                    toast("Match not found!");
//                }
                  return false;
            }

            // Dynamic search -- searches while typing
            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        arrayAdapter.notifyDataSetChanged();
    }

    // Ends activity
    @Override
    public void onBackPressed () {
        finish();
        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    // Code's shortening
    public void toast(String string) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(string);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}