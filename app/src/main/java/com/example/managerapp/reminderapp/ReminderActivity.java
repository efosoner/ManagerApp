package com.example.managerapp.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {

    static ArrayList<Reminder> reminders = new ArrayList<Reminder>();
    static ArrayAdapter<Reminder> arrayAdapter;
    public static final String CHANNEL_1_ID = "ReminderChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        SearchView searchView = findViewById(R.id.searchView);

        createNotificationChannels();

        ListView listView = findViewById(R.id.listView);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iReminder", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reminders", null);
        Type type = new TypeToken<ArrayList<Reminder>>() {
        }.getType();
        reminders = gson.fromJson(json, type);
        if (reminders == null) {
            reminders = new ArrayList<Reminder>();
        }

        FloatingActionButton addReminderButton = findViewById(R.id.addReminderButton);

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), ReminderEditActivity.class));
            }
        });

        if (reminders.isEmpty()) {
            toast("You have no reminders! Create one!");
        }

        arrayAdapter = new ArrayAdapter<Reminder>(this, android.R.layout.simple_expandable_list_item_1, reminders);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ReminderEditActivity.class);
                intent.putExtra("reminderID", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(ReminderActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete?")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) // to remove the selected note once "Yes" is pressed
                            {

                                Intent broadcastIntent = new Intent(getApplicationContext(), ReminderBroadcast.class);

                                broadcastIntent.putExtra("reminderTitle", reminders.get(position).getReminderTitle());
                                broadcastIntent.putExtra("reminderText", reminders.get(position).getReminderText());
                                broadcastIntent.putExtra("id", String.valueOf(reminders.get(position).getId()));
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), reminders.get(position).getId(), broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);

                                reminders.remove(position);
                                ReminderActivity.arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iReminder", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(reminders);
                                prefsEditor.putString("reminders", json);
                                prefsEditor.apply();

                                toast("Successfully deleted reminder!");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
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

    @Override
    public void onBackPressed () {
        finish();
    }

    private void createNotificationChannels() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_1_ID, "Reminder Channel",
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

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