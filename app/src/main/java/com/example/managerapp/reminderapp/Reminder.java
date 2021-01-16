package com.example.managerapp.reminderapp;

public class Reminder {
    private String reminderTitle;
    private String reminderText;
    private String reminderDate;
    private String reminderTime;
    private int id;

    public Reminder(String reminderTitle, String reminderText, String reminderDate, String reminderTime) {
        this.reminderTitle = reminderTitle;
        this.reminderText = reminderText;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return reminderTitle;
    }
}
