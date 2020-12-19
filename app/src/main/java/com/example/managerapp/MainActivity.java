package com.example.managerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    // Ends activity
    @Override
    public void onBackPressed() {
        finish();
    }
}