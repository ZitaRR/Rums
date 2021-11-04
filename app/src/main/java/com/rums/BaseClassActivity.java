package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class BaseClassActivity extends AppCompatActivity {

    private Toolbar actionBar;
    protected Boolean shouldHaveBackArrowInActionBar = true;
    protected Class<?> previousActivityClass;
    protected Class<?> specificActivityClassForBackArrow;
    protected Boolean omitOptionsMenu = false;
    protected int PREVIOUS_ACTIVITY_REQUEST_CODE = 149;
    private PersistantStorage storage;
    protected static RumUser currentRumUser;
    private static BaseClassActivity activityForRepositoryCallback;
    private static Boolean isRepositoryReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init(); //Why does this need to be explicitly called, super.init(), in subclasses?
    }

    protected void init() {
//        Log.d("Tag__100", "init i Base class");
        setActionBar();
        getPreviousActivity();
        activityForRepositoryCallback = this;
        storage = PersistantStorage.getInstance();
    }

    protected void setActionBar() {
        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
        if(getShouldHaveBackArrowInActionBar()) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!omitOptionsMenu) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        }
        return true;
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
            Log.d("Tag__1", "User IS logged in");
            return true;
        } else {
            Log.d("Tag__1", "User is not logged in");
            return false;
        }
    }

    protected FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected String getFirebaseUserUID() {
        FirebaseUser firebaseUser = getFirebaseUser();
        if(firebaseUser != null) {
            return firebaseUser.getUid();
        }
        else {
            return null;
        }
    }

    public void repositoryIsInitialized(Class<?> type) {
        Log.d("Tag__6", "repositoryIsInitialized in baseclass - type: " + type.toString());
        setIsRepositoryReady(true);
        readRumUserFromDatabase(getFirebaseUserUID());

        //Test:
        //Create a ChatRoom and save to database
//        if(getCurrentRumUser() != null) {
//            if(type == ChatRoom.class) {
//                makeChatRoom("Hm hm", null, false, getCurrentRumUser().getId(), null);
//            }
//        }

        //Enter a ChatRoom:
        moveUserToChatRoom(getChatRoomAtIndex(1));
    }

    protected void moveUserToChatRoom(ChatRoom chatRoom) {
        Log.d("Tag__6", "moveUserToChatRoom " + chatRoom);
        if(chatRoom != null) {
            RumUser currentUser = getCurrentRumUser();
            if (currentUser != null) {
                currentUser.setCurrentChatRoomID(chatRoom.getId());
                currentUser.setCurrentChatRoom(chatRoom);
                startSomeActivity(ChatRoomActivity.class);
            } else {
                Log.d("Tag__6", "currentUser is null");
            }
        }
    }

    protected ChatRoom getChatRoomAtIndex(int index) {
        ArrayList<ChatRoom> rooms = (ArrayList<ChatRoom>)getStorage().getRooms().getAll();
        int length = rooms.size();
        Log.d("Tag__6", "length " + length + " index " + index);

        if(index <= length - 1) {
            Log.d("Tag__4", "ROOM ID " + rooms.get(index).getId());

            return rooms.get(index);
        } else {
            Log.d("Tag__6", "index is past last element of chat room list");
        }
        return null;
    }

    protected ChatRoom makeChatRoom(String roomName, ArrayList<String> usersByID, Boolean isPrivate, String adminByUserID, ArrayList<Message> messages) {
        String ID = getStorage().getRooms().getUniqueKey();
        ChatRoom room = new ChatRoom(ID, roomName, usersByID, isPrivate, adminByUserID, messages);
        Log.d("Tag__6", "makeChatRoom room.getMessages(): " + room.getMessages());
        writeChatRoomToDatabase(room);
        return room;
    }

    protected void writeChatRoomToDatabase(ChatRoom chatRoom) {
        Repository<ChatRoom> rooms = getStorage().getRooms();
        rooms.insert(chatRoom);
        try {
            rooms.commit();
        } catch (Exception e) {
            Log.d("Tag__6", "Exception: " + e);
        }
    }

    protected RumUser setupNewRumUser() {
        FirebaseUser firebaseUser = getFirebaseUser();
        if(firebaseUser != null) {
            return new RumUser(getFirebaseUserUID(), getFirebaseUser().getDisplayName(), getFirebaseUser().getEmail());
        } else {
            return null;
        }
    }

    protected Boolean writeRumUserToDatabase(RumUser rumUser) {
        Boolean success = false;
        getStorage().getUsers().insert(rumUser);
        try {
            getStorage().getUsers().commit();
            success = true;
        } catch (Exception e) {
            Log.d("Tag__1", "writeRumUserToDatabase Exception: " + e.getMessage());
        }
        return success;
    }


    protected RumUser readRumUserFromDatabase(String UID) {
        RumUser rumUser;
        try {
            rumUser = getStorage().getUsers().getById(UID); //getById() should return null if not successful
//            rumUser = getRumUserByID(UID);
            setCurrentRumUser(rumUser);
//            Log.d("Tag__1", "readRumUserFromDatabase getCurrentRumUser(): " + getCurrentRumUser());
        } catch (Exception e) {
            Log.d("Tag__1", "readRumUserFromDatabase Exception: " + e.getMessage());
            rumUser = setupNewRumUser();
        }
        return rumUser;
    }

    //To avoid crash. Null check is still needed:
//    Repository<RumUser> users = getRumUsersRepository();
//        if(users != null) {
//        //Do stuff...
//    }
    protected Repository<RumUser> getRumUsersRepository() {
        try {
            return getStorage().getUsers();
        } catch (Exception e) {
            return null;
        }
    }

    protected Repository<ChatRoom> getChatRoomsRepository() {
        try {
            return getStorage().getRooms();
        } catch (Exception e) {
            return null;
        }

    }


    protected void startSomeActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtra("fromActivity", "someThing");
        startActivityForResult(intent, PREVIOUS_ACTIVITY_REQUEST_CODE);
    }

    //Timestamp as string, in the current timezone.
    //This should really be a timezone-agnostic object (Date or Calendar),
    // and the display string should take into account the timestamp of the device.
    protected String currentTime(String pattern) {
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(timeZone);
        String formatPattern;
        if(pattern != null) {
            formatPattern = pattern;
        } else {
            formatPattern = "d MMMM HH:mm";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(calendar.getTime());
    }

    protected PersistantStorage getStorage() {
        if(storage == null) {
            storage = PersistantStorage.getInstance();
        }
        return storage;
    }

    public RumUser getCurrentRumUser() {
        return currentRumUser;
    }
    public void setCurrentRumUser(RumUser currentRumUser) {
        this.currentRumUser = currentRumUser;
    }

    public Boolean getShouldHaveBackArrowInActionBar() {
        return shouldHaveBackArrowInActionBar;
    }

    public void setShouldHaveBackArrowInActionBar(Boolean shouldHaveBackArrowInActionBar) {
        this.shouldHaveBackArrowInActionBar = shouldHaveBackArrowInActionBar;
    }

    public static BaseClassActivity getActivityForRepositoryCallback() {
        return activityForRepositoryCallback;
    }

    public static void setIsRepositoryReady(Boolean isRepositoryReady) {
        BaseClassActivity.isRepositoryReady = isRepositoryReady;
    }

    public static Boolean getIsRepositoryReady() {
        return isRepositoryReady;
    }






}