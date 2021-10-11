package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class EditUserProfile extends AppCompatActivity {

    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
    }
}