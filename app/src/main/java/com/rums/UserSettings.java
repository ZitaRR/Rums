package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

public class UserSettings extends AppCompatActivity {

    private Toolbar actionBar;
    private EditText edit_username;
    private EditText edit_age;
    private EditText edit_description;
    private Switch switch_notifications;
    private Button button_apply;
    private Button button_continue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
    }
}