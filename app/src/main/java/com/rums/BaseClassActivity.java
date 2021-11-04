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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Log.d("Tag__4", "repositoryIsInitialized in baseclass - type: " + type.toString());
        setIsRepositoryReady(true);
        readRumUserFromDatabase(getFirebaseUserUID());
        //Test:
//        if(getCurrentRumUser() != null) {
//            if(type == ChatRoom.class) {
//                makeChatRoom("A roooom name", null, false, getCurrentRumUser().getId(), null);
//            }
//        }
        //Subscription. Returnerar dock inget initialt.
//        storage.getUsers().subscribe((userList) -> {
//            List<RumUser> users = (List<RumUser>) userList;
//            Log.d("Tag__4", "repositoryIsInitialized in baseclass - users: " + users);
//            for (RumUser user: users) {
//                Log.d("Tag__4", "useruseruser: " + user);
//            }
//        });
//        ArrayList<ChatRoom> rooms = (ArrayList<ChatRoom>)getStorage().getRooms().getAll();
//        for (ChatRoom room: rooms) {
//            Log.d("Tag__4", "roomroom: " + room);
//        }
        moveUserToChatRoom(getChatRoomAtIndex(0));
    }

    protected void moveUserToChatRoom(ChatRoom chatRoom) {
        Log.d("Tag__4", "moveUserToChatRoom " + chatRoom);
        if(chatRoom != null) {
            RumUser currentUser = getCurrentRumUser();
            if (currentUser != null) {
                currentUser.setCurrentChatRoomID(chatRoom.getId());
                currentUser.setCurrentChatRoom(chatRoom);
                startSomeActivity(ChatRoomActivity.class);
//            } else {
//                Log.d("Tag__4", "currentUser or chatRoom is null");
            }
        }
    }

    protected ChatRoom getChatRoomAtIndex(int index) {
        ArrayList<ChatRoom> rooms = (ArrayList<ChatRoom>)getStorage().getRooms().getAll();
        int length = rooms.size();
        Log.d("Tag__4", "length " + length);

        if(index < rooms.size() - 1) {
            return rooms.get(index);
        } else {
            Log.d("Tag__4", "index is past last element of chat room list");
        }
        return null;
    }

    private ChatRoom makeChatRoom(String roomName, ArrayList<String> usersByID, Boolean isPrivate, String adminByUserID, HashMap<String, Message> messages) {
        String ID = getStorage().getRooms().getUniqueKey();
        ChatRoom room = new ChatRoom(ID, roomName, usersByID, isPrivate, adminByUserID, messages);
        writeChatRoomToDatabase(room);
        return room;
    }

    protected void writeChatRoomToDatabase(ChatRoom chatRoom) {
        Repository<ChatRoom> rooms = getStorage().getRooms();
        rooms.insert(chatRoom);
        try {
            rooms.commit();
        } catch (Exception e) {
            Log.d("Tag__1", "Exception: " + e);
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


    protected void startSomeActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtra("fromActivity", "someThing");
        startActivityForResult(intent, PREVIOUS_ACTIVITY_REQUEST_CODE);
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