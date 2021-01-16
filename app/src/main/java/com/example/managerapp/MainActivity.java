package com.example.managerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.managerapp.contactsapp.ContactsActivity;
import com.example.managerapp.noteapp.NotesActivity;
import com.example.managerapp.reminderapp.ReminderActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Useful Activity btw
    public void notesOnClick(View v) {
        startActivity(new Intent(getApplicationContext(), NotesActivity.class));
    }
    public void reminderOnClick(View v) {
        startActivity(new Intent(getApplicationContext(), ReminderActivity.class));
    }
    public void contactsOnClick(View v) {
        startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
    }

    // Ends activity
    @Override
    public void onBackPressed() {
        finish();
    }
}