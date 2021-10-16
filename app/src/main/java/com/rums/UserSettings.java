package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {

    private Toolbar actionBar;

    private EditText edit_username, edit_age, edit_description;
    private EditText old_password, new_password;
    private Switch switch_notifications;
    private Button button_save;
    private CircleImageView profile_picture;
    private ImageView profile_picture_add;

    private String username;
    private String user_description;
    private String old_pass, new_pass;

    private int user_age;

    private boolean notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        edit_username = (EditText) findViewById(R.id.input_username);
        edit_age = (EditText) findViewById(R.id.input_user_age);
        edit_description = (EditText) findViewById(R.id.input_description);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        button_save = (Button) findViewById(R.id.button_save);
        profile_picture = (CircleImageView) findViewById(R.id.profile_picture);
        profile_picture_add = (ImageView) findViewById(R.id.profile_picture_add);
        switch_notifications = (Switch) findViewById(R.id.switch_notifications);

        button_save.setOnClickListener(this);
        profile_picture.setOnClickListener(this);
        profile_picture_add.setOnClickListener(this);
        switch_notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switch_notifications.isChecked()) {
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
            case R.id.profile_picture_add:
                changeProfilePicture();
                break;
            case R.id.button_save:
                saveChanges();
                break;
            case R.id.button_save_password:
                // Intent + startnextactivity?
                break;
        }
    }

    private void changeProfilePicture() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

        Toast.makeText(UserSettings.this, "Change Profile Pic", Toast.LENGTH_SHORT).show();
    }

    private void changePassword() {



    }

    private void saveChanges() {

        boolean successfulSave = true;

        // Om användarnamn är mindre än 5 eller mer än 13 karaktärer, så får man felmeddelande.
        if(edit_username.getText().toString().length() < 5 || edit_username.getText().toString().length() > 13) {
           Toast.makeText(UserSettings.this, "Username must be between 5 and 13 characters.", Toast.LENGTH_SHORT).show();
           successfulSave = false;
        } else {
            setUsername(edit_username.getText().toString());
        }

        // Denna spelar ingen roll om den sätts till tom eller inte.
        setUser_description(edit_description.getText().toString());

        // Programmet kraschar om den försöker parsa ett tomt värde, så behöver se så det inte är tomt.
        // Behövs inget max-värde, satt max 3 siffror i EditText elementet, så 999 är max
        if (edit_age.getText().toString().length() > 1) {
           setUser_age(Integer.parseInt(edit_age.getText().toString()));
        }

        if (successfulSave) {
            Toast.makeText(UserSettings.this, "Save Successful", Toast.LENGTH_SHORT).show();
            // StartnextActivity
            changeDatabase();
        }

    }

    private void changeDatabase() {
        // När värden ändrats, skicka upp i sfären på nått sätt
    }

    // Getters & Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUser_description(String user_description) {
        this.user_description = user_description;
    }

    public void setUser_age(int user_age) {
        this.user_age = user_age;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

}