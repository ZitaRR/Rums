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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseClassActivity extends AppCompatActivity {

    private Toolbar actionBar;
    protected Boolean shouldHaveBackArrowInActionBar = true;
    protected Class<?> previousActivityClass;
    protected Class<?> specificActivityClassForBackArrow;
    protected int PREVIOUS_ACTIVITY_REQUEST_CODE = 149;
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

    protected boolean isLoggedIn() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            Log.d("Tag__1", "User IS logged in!");
            return true;
        } else {
            Log.d("Tag__1", "User is not logged in...");
            return false;
        }
    }

    protected FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected String getFirebaseUserUID() {
        return getFirebaseUser().getUid();
    }

    public static void repositoryIsInitted(Class<?> type) {
        Log.d("Tag__1", "repositoryIsInitted type: " + type.toString());

    }


    protected void testReadFromDatabase() {
        if(isLoggedIn()) {
            PersistantStorage storage = PersistantStorage.getInstance();
            RumUser rumUser;
            String firebaseUserUID = getFirebaseUserUID();
            try {
                rumUser = storage.getUsers().getById(firebaseUserUID);
                Log.d("Tag__1", "rumUser: " + rumUser);
            } catch (Exception e) {
                Log.d("Tag__1", "Exception: " + e + " - firebaseUserUID is " + firebaseUserUID);
            }
        }
    }

    protected RumUser setupNewRumUser() {
        return new RumUser(getFirebaseUserUID(), getFirebaseUser().getDisplayName(), getFirebaseUser().getEmail());
    }

    protected Boolean writeRumUserToDatabase(RumUser rumUser) {
        Boolean success = false;
        storage.getUsers().insert(rumUser);
        try {
            storage.getUsers().commit();
            success = true;
        } catch (Exception e) {
            Log.d("Tag__1", "writeRumUserToDatabase Exception: " + e.getMessage());
        }
        return success;
    }


    protected RumUser readRumUserFromDatabase(String UID) {
        RumUser rumUser;
        try {
            rumUser = storage.getUsers().getById(UID); //getById() should return null if not successful
//            rumUser = getRumUserByID(UID);
            setCurrentRumUser(rumUser);
        } catch (Exception e) {
            Log.d("Tag__1", "readRumUserFromDatabase Exception: " + e.getMessage());
            rumUser = setupNewRumUser();
            Log.d("Tag__1", "rumUser rumUser: " + rumUser.getId());
            writeRumUserToDatabase(rumUser);
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