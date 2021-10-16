package com.rums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        //Kristian: Här kan vi starta "vår egen" Activity,
        // så vi kan testa den.
        // Byt bara ChatRoomActivity till namnet på din Activity.
        startSomeActivity(ChatRoomActivity.class);
    }

    private void startSomeActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

}