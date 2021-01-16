package com.example.managerapp.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.managerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderEditActivity extends AppCompatActivity {

    int reminderID;
    int id;
    EditText reminderTitle;
    EditText reminderText;
    EditText reminderDateText;
    EditText reminderTimeText;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    String pattern = "dd/MM/yyyy HH:mm";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    Calendar calendar = Calendar.getInstance();
    ArrayList<Reminder> reminders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_edit);

        FloatingActionButton confirmButton = findViewById(R.id.confirmReminderButton);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iReminder", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("reminders", null);
        Type type = new TypeToken<ArrayList<Reminder>>() {
        }.getType();
        reminders = gson.fromJson(json, type);
        if (reminders == null) {
            reminders = new ArrayList<Reminder>();
        }

        reminderText = findViewById(R.id.reminderText);
        reminderTitle = findViewById(R.id.reminderTitleText);
        reminderDateText = findViewById(R.id.reminderDate);
        reminderDateText.setInputType(InputType.TYPE_NULL);
        reminderTimeText = findViewById(R.id.reminderTime);
        reminderTimeText.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();
        reminderID = intent.getIntExtra("reminderID", -1);

        if (reminderID != -1) {
            reminderText.setText(reminders.get(reminderID).getReminderText());
            reminderTitle.setText(reminders.get(reminderID).getReminderTitle());
            reminderDateText.setText(reminders.get(reminderID).getReminderDate());
            reminderTimeText.setText(reminders.get(reminderID).getReminderTime());
        }
        else {
            Reminder reminder = new Reminder("", "", "", "");
            if (reminders.isEmpty()) {
                id = 0;
                reminder.setId(0);
            }
            else {
                id = reminders.get(reminders.size() - 1).getId() + 1;
                reminder.setId(id);
            }
            reminders.add(reminder);
            reminderID = reminders.size() - 1;
        }

        reminderDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(ReminderEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                reminderDateText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        reminderTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog( ReminderEditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                reminderTimeText.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                timePicker.show();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reminders.set(reminderID, new Reminder(reminderTitle.getText().toString(),
                        reminderText.getText().toString(), reminderDateText.getText().toString(), reminderTimeText.getText().toString()));

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iReminder", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(reminders);
                prefsEditor.putString("reminders", json);
                prefsEditor.apply();

                //===========================================================================================================================

                Intent broadcastIntent = new Intent(getApplicationContext(), ReminderBroadcast.class);

                broadcastIntent.putExtra("reminderTitle", reminderTitle.getText().toString());
                broadcastIntent.putExtra("reminderText", reminderText.getText().toString());
                broadcastIntent.putExtra("id", String.valueOf(id));

                String temp = reminderDateText.getText().toString() + " " + reminderTimeText.getText().toString();
                try {
                    calendar.setTime(simpleDateFormat.parse(temp));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                //===========================================================================================================================

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = layout.findViewById(R.id.text);
                text.setText("Succesfully created reminder!");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                finish();
                startActivity(new Intent(getApplicationContext(), ReminderActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed () {
        finish();
    }
}