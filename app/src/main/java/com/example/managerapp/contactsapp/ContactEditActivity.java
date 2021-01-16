package com.example.managerapp.contactsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactEditActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    int contactID;
    EditText name;
    EditText surname;
    EditText phoneNumber;
    EditText email;
    RadioButton isMale;
    RadioButton isFemale;
    ImageView avatar;
    String avatarString;
    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        FloatingActionButton confirmButton = findViewById(R.id.confirmButton);
        FloatingActionButton callButton = findViewById(R.id.callButton);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iContacts", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("contacts", null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        contacts = gson.fromJson(json, type);
        if (contacts == null) {
            contacts = new ArrayList<Contact>();
        }

        name = findViewById(R.id.contactName);
        surname = findViewById(R.id.contactSurname);
        phoneNumber = findViewById(R.id.contactNum);
        email = findViewById(R.id.contactMail);
        isMale = findViewById(R.id.maleButton);
        isFemale = findViewById(R.id.femaleButton);
        avatar = findViewById(R.id.avatarView);
        Intent intent = getIntent();
        contactID = intent.getIntExtra("contactID", -1);

        if (contactID != -1) {
            name.setText(contacts.get(contactID).getName());
            surname.setText(contacts.get(contactID).getSurname());
            phoneNumber.setText(contacts.get(contactID).getPhoneNumber());
            email.setText(contacts.get(contactID).getEmail());
            isMale.setChecked(contacts.get(contactID).isMale());
            isFemale.setChecked(contacts.get(contactID).isFemale());
            if (contacts.get(contactID).getAvatar() != null) {
                avatarString = contacts.get(contactID).getAvatar();
                byte[] decodedString = Base64.decode(avatarString, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                avatar.setImageBitmap(decodedByte);
            }
            else {
                if (contacts.get(contactID).isMale()) {
                    avatar.setImageResource(R.drawable.male);
                }
                else avatar.setImageResource(R.drawable.female);
            }

        }
        else {
            contacts.add(new Contact());
            contactID = contacts.size() - 1;
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avatarString != null) {
                    contacts.set(contactID, new Contact(name.getText().toString(), surname.getText().toString(),
                                                        phoneNumber.getText().toString(), email.getText().toString(),
                                                        isMale.isChecked(), isFemale.isChecked(), avatarString));
                }
                else {
                    contacts.set(contactID, new Contact(name.getText().toString(), surname.getText().toString(),
                                                        phoneNumber.getText().toString(), email.getText().toString(),
                                                        isMale.isChecked(), isFemale.isChecked(), null));
                }

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("iContacts", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(contacts);
                prefsEditor.putString("contacts", json);
                prefsEditor.apply();

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));

                TextView text = layout.findViewById(R.id.text);
                text.setText("Succesfully created contact!");
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                // Ends activity
                finish();
                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber.getText().toString(), null)));
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        pickImageFromGallery();
                    }
                }
                else {
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            avatar.setImageURI(data.getData());
            BitmapDrawable bitmapDrawable = (BitmapDrawable) avatar.getDrawable();
            Bitmap image = bitmapDrawable.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            avatarString = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                toast("Permission denied!");
            }
        }
    }

    @Override
    public void onBackPressed () {
        finish();
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