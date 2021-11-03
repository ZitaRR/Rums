package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class BaseClassActivity extends AppCompatActivity {

    private Toolbar actionBar;
    protected Boolean shouldHaveBackArrowInActionBar = true;
    protected Class<?> previousActivityClass;
    protected Class<?> specificActivityClassForBackArrow;
    protected int PREVIOUS_ACTIVITY_REQUEST_CODE = 149;
    protected Boolean omitOptionsMenu = false;
    private PersistantStorage storage;
    protected RumUser currentRumUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        init(); //Why does this need to be explicitly called, super.init(), in subclasses?
    }

    protected void init() {
        setActionBar();
        getPreviousActivity();
        storage = PersistantStorage.getInstance();
    }

    protected void setActionBar() {
        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
        if(shouldHaveBackArrowInActionBar) {
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected void getPreviousActivity() {
        ComponentName previousActivity = getCallingActivity();
        if(previousActivity != null) {
            String className = previousActivity.getClassName();
            previousActivityClass = null;
            try {
                previousActivityClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override      //Back arrow in ActionBar, etc.
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getClass() == androidx.appcompat.view.menu.ActionMenuItem.class) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    if (specificActivityClassForBackArrow == null) {
                        if (previousActivityClass != null) {
                            startSomeActivity(previousActivityClass);
                        }
                    } else {
                        startSomeActivity(specificActivityClassForBackArrow);
                    }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected RumUser getRumUserFromDatabase(String UID) {
        RumUser rumUser;
        try {
            rumUser = storage.getUsers().getById(UID); //getById() should return null if not successful
            setCurrentRumUser(rumUser);
        } catch (Exception e) {
            Log.d("Tag_1", "Exception: " + e.getMessage());
            rumUser = null;
        }
        return rumUser;
    }

    protected void moveUserToChatRoom(ChatRoom chatRoom) {
        RumUser currentUser = getCurrentRumUser();
        if((currentUser != null) || (chatRoom != null)) {
            currentUser.setCurrentChatRoomID(chatRoom.getId());
            currentUser.setCurrentChatRoom(chatRoom);
            startSomeActivity(ChatRoomActivity.class);
        } else {
            Log.d("Tag_1", "currentUser or chatRoom is null");
        }
    }

    protected void startSomeActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtra("fromActivity", "someThing");
        startActivityForResult(intent, PREVIOUS_ACTIVITY_REQUEST_CODE);
    }

    public RumUser getCurrentRumUser() {
        return currentRumUser;
    }
    public void setCurrentRumUser(RumUser currentRumUser) {
        this.currentRumUser = currentRumUser;
    }




}