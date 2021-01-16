package com.example.managerapp.noteapp;

public class Note {
    private String noteText;
    private String noteTitle;

    public Note(String noteTitle, String noteText) {
        this.noteText = noteText;
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    @Override
    public String toString(){
        return noteTitle;
    }
}
