package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettings extends AppCompatActivity implements View.OnClickListener {

    private Toolbar actionBar;
    private EditText edit_username;
    private EditText edit_age;
    private EditText edit_description;
    private EditText old_password;
    private EditText new_password;
    private Switch switch_notifications;
    private Button button_apply;
    private Button button_continue;
    private CircleImageView profile_picture;
    private ImageView profile_picture_add;

    private String username;
    private int user_age;
    private String user_description;
    private String old_pass;
    private String new_pass;
    private boolean notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        edit_username = (EditText) findViewById(R.id.input_username);
        edit_username.setOnClickListener(this);
        edit_age = (EditText) findViewById(R.id.input_user_age);
        edit_age.setOnClickListener(this);
        edit_description = (EditText) findViewById(R.id.input_description);
        edit_description.setOnClickListener(this);
        old_password = (EditText) findViewById(R.id.old_password);
        old_password.setOnClickListener(this);
        new_password = (EditText) findViewById(R.id.new_password);
        new_password.setOnClickListener(this);

        switch_notifications = (Switch) findViewById(R.id.switch_notifications);
        switch_notifications.setOnClickListener(this);

        button_apply = (Button) findViewById(R.id.button_apply);
        button_apply.setOnClickListener(this);
        button_continue = (Button) findViewById(R.id.button_continue);
        button_continue.setOnClickListener(this);
        profile_picture = (CircleImageView) findViewById(R.id.profile_picture);
        profile_picture.setOnClickListener(this);
        profile_picture_add = (ImageView) findViewById(R.id.profile_picture_add);
        profile_picture_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.input_username:
                changeUsername();
                break;
            case R.id.input_user_age:
                changeUserAge();
                break;
            case R.id.input_description:
                changeUserDescription();
                break;
            case R.id.button_apply:
                saveChanges();
                break;
            case R.id.button_continue:
                // Intent + startnextactivity?
                Toast.makeText(UserSettings.this, "Change Activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile_picture:
            case R.id.profile_picture_add:
                changeProfilePicture();
                break;
            case R.id.old_password:
                confirmOldPassword();
                break;
            case R.id.new_password:
                setNewPassword();
                break;
            case R.id.switch_notifications:
                changeNotifications();
                break;
        }

    }

    private void changeNotifications() {

        Toast.makeText(UserSettings.this, "Test: switch", Toast.LENGTH_SHORT).show();
    }

    private void changeUsername() {

        username = edit_username.getText().toString();
        Toast.makeText(UserSettings.this, "Test: " + username, Toast.LENGTH_SHORT).show();
    }

    private void changeUserAge() {

        user_age = Integer.parseInt(edit_age.getText().toString());
        String showAgeInToast = user_age + "";

        Toast.makeText(UserSettings.this, "Test: " + showAgeInToast, Toast.LENGTH_SHORT).show();
    }

    private void changeUserDescription() {

        user_description = edit_description.getText().toString();

        Toast.makeText(UserSettings.this, "Test: " + user_description, Toast.LENGTH_SHORT).show();
    }

    private void changeProfilePicture() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);

        Toast.makeText(UserSettings.this, "Change Profile Pic", Toast.LENGTH_SHORT).show();
    }

    private void confirmOldPassword() {

        Toast.makeText(UserSettings.this, "Old Password", Toast.LENGTH_SHORT).show();
    }

    private void setNewPassword() {

        Toast.makeText(UserSettings.this, "New password", Toast.LENGTH_SHORT).show();
    }

    private void saveChanges() {

        // Ändra i databaser

        Toast.makeText(UserSettings.this, "Changes Saved", Toast.LENGTH_SHORT).show();
    }
}