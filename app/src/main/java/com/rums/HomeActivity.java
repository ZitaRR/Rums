package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private Toolbar actionBar;
    private Button testActivity;
    private RecyclerView homeRecycler;
    private ImageButton buttonNewRoom;

    // Test för att få till recycler bara
    ArrayList<String> chatRoomNames = new ArrayList<>();
    ArrayList<String> chatUserNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        homeRecycler = findViewById(R.id.recycler_home);

        testActivity = findViewById(R.id.button_test_activity);

        buttonNewRoom = findViewById(R.id.imgbtn_newroom);

        buttonNewRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NewRoomActivity.class);
                startActivity(i);

            }
        });

        testActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NewRoomActivity.class);
                startActivity(i);
            }
        });

        chatRoomNames.add("ChattOne");
        chatRoomNames.add("ChattTwooo");
        chatUserNames.add("Kalle");
        chatUserNames.add("Johan");
        HomeAdapter homeAdapter = new HomeAdapter(this, chatRoomNames, chatUserNames);
        homeRecycler.setAdapter(homeAdapter);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));



    }


    // Meny i toolbar funktionalitet (Sign-out & Edit-profile)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       // Kanske ändra till IF?
        switch (item.getItemId()){
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.menu_profile:
                startActivity(new Intent(this, UserProfile.class));
                finish();
                return true;
        }
        return false;
    }

}