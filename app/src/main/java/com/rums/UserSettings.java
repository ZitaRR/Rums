package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class UserSettings extends AppCompatActivity {

    private Toolbar actionBar;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        profilePicture = findViewById(R.id.imageView);

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}