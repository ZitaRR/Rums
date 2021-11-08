package com.rums;

import androidx.annotation.NonNull;
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

public class HomeActivity extends BaseClassActivity {
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
        setUpClickers();
    }

    @Override
    protected void init() {
        super.init();
        storage = PersistantStorage.getInstance();
        homeRecycler = findViewById(R.id.recycler_home);
//        testActivity = findViewById(R.id.button_test_activity);
        buttonNewRoom = findViewById(R.id.imgbtn_newroom);

        if(getIsRepositoryReady()) {
            getUsersChatRooms();
            setUpRecyclerView();
        }

    }

    private void setUpClickers() {

        // När man trycker på gröna knappen nere till höger, skapa nytt rum
        buttonNewRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSomeActivity(NewRoomActivity.class);

            }
        });

        /*// När man trycker på Button test activity (ska tas bort)
        testActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSomeActivity(ChatRoomActivity.class);
            }
        });*/
    }

    private void getUsersChatRooms(){
        chatRooms = (ArrayList<ChatRoom>) storage.getRooms().getRange(ChatRoom -> ChatRoom.getUsersByID().contains(getCurrentRumUser().getId()));
    }

    private void setUpRecyclerView() {

        HomeAdapter homeAdapter = new HomeAdapter(this, chatRooms);
        homeRecycler.setAdapter(homeAdapter);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void repositoryIsInitialized(Class<?> type) {
        super.repositoryIsInitialized(type);
        getUsersChatRooms();

    }

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
                startSomeActivity(LoginActivity.class);
                return true;
            case R.id.menu_profile:
                startSomeActivity(UserProfile.class);
                return true;
        }
        return false;
    }

}