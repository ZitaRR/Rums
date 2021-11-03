package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private Toolbar actionBar;

    private EditText editUsername, editAge, editPhone, editDescription;
    private Switch switchNotification;
    private Button buttonSave;
    private ImageView profilePicture;

    private String username, userDescription;
    private int userAge, userPhone;
    private boolean userNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        actionBar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        editUsername = (EditText) findViewById(R.id.input_username);
        editAge = (EditText) findViewById(R.id.input_user_age);
        editPhone = (EditText) findViewById(R.id.input_user_phone);
        editDescription = (EditText) findViewById(R.id.input_description);
        buttonSave = (Button) findViewById(R.id.button_save);
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
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

        String inputUsername = editUsername.getText().toString();
        String inputDescription = editDescription.getText().toString();
        String inputAge = editAge.getText().toString();
        String inputPhone = editPhone.getText().toString();

        boolean successfulSave = true;

        // Användarnamn. Vilkor: Mellan 5 och 13 karaktärer.
        if(inputUsername.length() < 5 || inputUsername.length() > 13) {
           Toast.makeText(UserProfile.this, "Username must be between 5 and 13 characters.", Toast.LENGTH_SHORT).show();
           successfulSave = false;
        } else {
            setUsername(inputUsername);
        }

        if (!inputDescription.isEmpty()) {
            setUserDescription(inputDescription);
        }

        if (!inputAge.isEmpty()) {
           setUserAge((Integer.parseInt(inputAge)));
        }

        if (!inputPhone.isEmpty()) {
            setUserPhone(Integer.parseInt(inputPhone));
        }

        if (successfulSave) {
            Toast.makeText(UserProfile.this, "Save Successful", Toast.LENGTH_SHORT).show();
            changeDatabase();
            Intent i = new Intent(UserProfile.this, HomeActivity.class);
            startActivity(i);
        }

    }

    private void changeDatabase() {
        // När värden ändrats, skicka till User.Class eller databas eller nått? Dunno?
    }

    // Getters & Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserAge(int user_age) {
        this.userAge = user_age;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserPhone(int user_phone) {
        this.userPhone = user_phone;
    }

    public int getUserPhone() {
        return userPhone;
    }

    public void setUserDescription(String user_description) {
        this.userDescription = user_description;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setNotifications(boolean notifications) {
        this.userNotification = notifications;
    }

    public boolean isUserNotification() {
        return userNotification;
    }


    // För att kunna använda bakåtknappen. Inte det mest effektiva sättet att göra det förmodligen men enda som jag fick att fungera.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i = new Intent(UserProfile.this, HomeActivity.class);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}