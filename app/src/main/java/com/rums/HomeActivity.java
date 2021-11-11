package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import java.util.ArrayList;
import android.widget.Toolbar;
import java.util.List;
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
    protected void onResume() {
        super.onResume();

        if(getIsRepositoryReady()) {
            Log.d("Tag__1", "Hellooo");
            getUsersChatRooms();
            setUpRecyclerView();
        }


    }

    public void getUsersInChatRoom() {



    }

    @Override
    protected void init() {
        super.init();
        storage = PersistantStorage.getInstance();
        homeRecycler = findViewById(R.id.recycler_home);
//      testActivity = findViewById(R.id.button_test_activity);
        buttonNewRoom = findViewById(R.id.imgbtn_newroom);
        setShouldHaveBackArrowInActionBar(false);
        //ActionBar actionBar = getSupportActionBar();
        // actionBar.setIcon(R.drawable.ic_rums_ikon_lilac);
        //actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_actionbar));

        if(getIsRepositoryReady()) {
            Log.d("Tag__1", "Hellooo");
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
      //chatRooms = (ArrayList<ChatRoom>) storage.getRooms().getRange(ChatRoom -> ChatRoom.getUsersByID().contains(getCurrentRumUser().getId()));
      chatRooms = (ArrayList<ChatRoom>) storage.getRooms().getAll();
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
        setUpRecyclerView();

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
                finish();

                return true;
            case R.id.menu_profile:
                startSomeActivity(UserProfile.class);
                return true;
        }
        return false;
    }

}