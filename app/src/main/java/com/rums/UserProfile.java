package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private Toolbar actionBar;

    private EditText editUsername, editAge, editPhone, editDescription;
    private Switch switchNotification;
    private Button buttonSave;
    private CircleImageView profilePicture;

    private String username;
    private String userDescription;
    private int userAge;
    private int userPhone;
    private boolean userNotific;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        editUsername = (EditText) findViewById(R.id.input_username);
        editAge = (EditText) findViewById(R.id.input_user_age);
        editPhone = (EditText) findViewById(R.id.input_user_phone);
        editDescription = (EditText) findViewById(R.id.input_description);
        buttonSave = (Button) findViewById(R.id.button_save);
        profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        switchNotification = (Switch) findViewById(R.id.switch_notifications);

        buttonSave.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchNotification.isChecked()) {
                    setNotifications(true);
                } else {
                    setNotifications(false);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_picture:
                changeProfilePicture();
                break;
            case R.id.button_save:
                saveChanges();
                break;
        }
    }


    // Inte klar, tror behöver Firebase Storage
    private void changeProfilePicture() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

        Toast.makeText(UserProfile.this, "Change Profile Pic", Toast.LENGTH_SHORT).show();
    }

    private void saveChanges() {

        boolean successfulSave = true;

        // Om användarnamn är mindre än 5 eller mer än 13 karaktärer, så får man felmeddelande.
        if(editUsername.getText().toString().length() < 5 || editUsername.getText().toString().length() > 13) {
           Toast.makeText(UserProfile.this, "Username must be between 5 and 13 characters.", Toast.LENGTH_SHORT).show();
           successfulSave = false;
        } else {
            setUsername(editUsername.getText().toString());
        }

        if (!editDescription.getText().toString().isEmpty()) {
            setUserDescription(editDescription.getText().toString());
        }

        // Programmet kraschar om den försöker parsa ett tomt värde, så behöver se så det inte är tomt.
        // Behövs inget max-värde, satt max 3 siffror i EditText elementet, så 999 är max
        if (editAge.getText().toString().length() > 1) {
           setUserAge(Integer.parseInt(editAge.getText().toString()));
        }

        if (editPhone.getText().toString().length() > 1) {
            setUserPhone(Integer.parseInt(editPhone.getText().toString()));
        }

        if (successfulSave) {
            Toast.makeText(UserProfile.this, "Save Successful", Toast.LENGTH_SHORT).show();
            changeDatabase();
            // StartnextActivity
        }

    }

    private void changeDatabase() {
        // När värden ändrats, skicka upp i sfären på nått sätt
    }





    // Getters & Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserAge(int user_age) {
        this.userAge = user_age;
    }

    public void setUserPhone(int user_phone) {
        this.userPhone = user_phone;
    }

    public void setUserDescription(String user_description) {
        this.userDescription = user_description;
    }

    public void setNotifications(boolean notifications) {
        this.userNotific = notifications;
    }

}