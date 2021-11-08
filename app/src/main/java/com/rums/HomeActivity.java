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
import java.util.HashMap;

public class HomeActivity extends BaseClassActivity {
    private Toolbar actionBar;
    private Button testActivity;
    private RecyclerView homeRecycler;
    private ImageButton buttonNewRoom;

    private PersistantStorage storage;
    ArrayList<ChatRoom> chatRooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setOnClickListeners();
        setUpRecyclerView();

    }

    @Override
    protected void init() {
        super.init();
        storage = PersistantStorage.getInstance();
        homeRecycler = findViewById(R.id.recycler_home);
        testActivity = findViewById(R.id.button_test_activity);
        buttonNewRoom = findViewById(R.id.imgbtn_newroom);

    }

    private void setOnClickListeners() {

        // När man trycker på gröna knappen nere till höger, skapa nytt rum
        buttonNewRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NewRoomActivity.class);
                startActivity(i);

            }
        });

        // När man trycker på Button test activity (ska tas bort)
        testActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NewRoomActivity.class);
                startActivity(i);
            }
        });
    }

    private void setUpRecyclerView() {

        // Lagrar chatRooms med info från databas (finns inget just nu)
        chatRooms = (ArrayList<ChatRoom>) storage.getRooms().getAll();

        // Skapar tillfälligt ChatRoom-objekt för att se så recycler fungerar
        ArrayList<String> striiings = new ArrayList<>();
        striiings.add("Hej");
//        HashMap<String, Message> hashMap = new HashMap<>();
        ChatRoom room = new ChatRoom("ID", "ChatRoom", striiings, true, "adminid", null);
        chatRooms.add(room);

        // Recyclerview
        HomeAdapter homeAdapter = new HomeAdapter(this, chatRooms);
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