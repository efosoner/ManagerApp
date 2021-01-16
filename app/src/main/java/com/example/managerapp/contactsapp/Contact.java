package com.example.managerapp.contactsapp;

public class Contact {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String avatar;
    private boolean isMale;
    private boolean isFemale;

    public Contact() {
        this.name = "";
        this.surname = "";
        this.phoneNumber = "";
        this.email = "";
        this.isMale = true;
        this.isFemale = false;
        this.avatar = null;
    }

    public Contact(String name, String surname, String phoneNumber, String email, boolean isMale, boolean isFemale, String avatar) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isMale = isMale;
        this.isFemale = isFemale;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }

    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
