package com.rums;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ChatRoomActivity extends BaseClassActivity {

    private static final int GALLERY_REQUEST = 100;
    ArrayList<String> names;
    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<Message> messages;
    Menu actionBarMenu;
    MenuItem nameMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        super.init();
        setupListViewAdapter();
        if(getIsRepositoryReady()) {
            Log.d("Tag__4", "getIsRepositoryReady: " + getIsRepositoryReady());
            setupFromDatabase();
            setupSubscriptionForMessages();
        }

    }

    //Subscription. Returnerar dock inget initialt.
    protected void setupSubscriptionForMessages() {
        RumUser user = getCurrentRumUser();
        ChatRoom room = getCurrentChatRoom();
        if(user != null && room != null) {
            String currentChatRoomID = room.getId();
            try {
                subscibeForMessages(currentChatRoomID);
            } catch (Exception e) {
                Log.d("Tag__70", "Exception, subscibeForMessages: " + e);
            }
        }
    }

    protected void subscibeForMessages(String currentChatRoomID) {
        getStorage().getRooms().subscribe((roomList) -> {
            List<ChatRoom> rooms = (List<ChatRoom>) roomList;
            ChatRoom theCurrentChatRoom = getStorage().getRooms().getById(currentChatRoomID);
            adapter.clear();
            for (Message message: theCurrentChatRoom.getMessages()) {
//                Log.d("Tag__6", "roomroomroom subscription: " + message);

                //Fyll
                adapter.add(message.getMessageText());
            }
        });
    }

    private ChatRoom getChatRoomFromDatabase(String roomID) {
        return getStorage().getRooms().getById(roomID);
    }




    protected void setupFromDatabase() {
        Log.d("Tag__6", "setupFromDatabase getCurrentRumUser " + getCurrentRumUser() + " getCurrentChatRoom " + getCurrentChatRoom());
        fillMessagesList();
        setRoomName(actionBarMenu, "____ONE____");
    }

    private void setupListViewAdapter() {
        names = new ArrayList<>();
//        int duplicates = 1;
//        for(int i = 0; i<duplicates; i++) {
//            names.add("Kalle");
//            names.add("Bille");
//            names.add("Mmmmmmmm. Mmmm. Ett längre meddelande som kanske wrappar. Är det så? Det får vi kolla. Ett långt, långt meddelande" +
//                    "som fortsätter vidare och vidare och vidare.");
//        }

        adapter = new ArrayAdapter<>(this, R.layout.chat_bubble_list_item, names);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //Scroll to bottom:
//        listView.post(() -> listView.setSelection(listView.getCount() - 1));
    }

    private void updateMessagesList(ChatRoom  chatRoom) {
        adapter.clear();
        for (Message message: chatRoom.getMessages()) {
//                Log.d("Tag__6", "roomroomroom subscription: " + message);
            //Fyll
            adapter.add(message.getMessageText());
        }
    }


    private void fillMessagesList() {
//        if(getRepositoryReady()) {
////            Log.d("Tag__1", "it's ready: ");
//        } else {
////            Log.d("Tag__1", "it's NOT ready: ");
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(!omitOptionsMenu) {
            getMenuInflater().inflate(R.menu.menu_chat_room, menu);
            actionBarMenu = menu;
            setRoomName(menu, "____TWO____");
        }
        return true;
    }

    public void sendMessageButtonMethod(View view) {
        EditText editText = findViewById(R.id.message_EditText);
        String messageText = editText.getText().toString();
        if(messageText.length() > 0) {
            Log.d("Tag__6", "getCurrentChatRoom() " + getCurrentChatRoom());
            sendMessage(messageText);
//            RumUser rumUser = getCurrentRumUser();
//            rumUser.sendMessage(messageText, currentTime(null), getCurrentChatRoom().getId(), getStorage());
            editText.setText(""); //But what if message couldn't be delivered?
        }
    }

    public void sendMessage(String messageText) {
        //For now:
        String messageTextWithUsername = getCurrentRumUser().getUsername() + ": " + messageText;

//        Log.d("Tag__6", "getCurrentRumUser().getUsername() " + getCurrentRumUser().getUsername());
        String messageID = getStorage().getRooms().getUniqueKey();
        RumUser user = getCurrentRumUser();
        ChatRoom chatRoom = getCurrentChatRoom();
        String timeStamp = currentTime(null);
        Message message = new Message(user.getId(), user.getUsername(), null, messageTextWithUsername, messageID, timeStamp);

        addMessageToChatRoom(message);
        getStorage().getRooms().update(chatRoom);
        getStorage().getRooms().commit();
    }

    private void addMessageToChatRoom(Message message) {
        ChatRoom room = getCurrentChatRoom();
        if(room != null) {
            if(room.getMessages() == null) {
                room.setMessages(new ArrayList<>());
            }
            room.getMessages().add(message);
        }
    }

    private ChatRoom getCurrentChatRoom() {
        RumUser user = getCurrentRumUser();
        Log.d("Tag__3", "user " + user);

        if(user != null) {
            ChatRoom room = user.getCurrentChatRoom();;
            if(room != null) {
                return room;
            }
        }
        return null;
    }

    private void setRoomName(Menu menu, String callNumber) {
        ChatRoom room = getCurrentChatRoom();
        Log.d("Tag__4", "setRoomName getCurrentChatRoom " + room + " callNumber " + callNumber);
        if (room != null) {
            if (menu != null) {
                nameMenuItem = menu.findItem(R.id.chat_room_name_menu_item);
                Log.d("Tag_2", "nameMenuItem " + nameMenuItem);
                if (nameMenuItem != null) {
                    String name = room.getName();
                    if (name != null) {
                        nameMenuItem.setTitle(name);
                    }
                }
            }
        }
    }

    @Override
    public void repositoryIsInitialized(Class<?> type) {
        super.repositoryIsInitialized(type);
        RumUser user = getCurrentRumUser();
        Log.d("Tag__6", "repositoryIsInitialized in ChatRoomActivity user: " + user);
        setupFromDatabase();
    }


    public void imageFromGalleryButtonMethod(View view) {
        pickAPicFromGallery();
    }

    private void pickAPicFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //Deprecated:
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        Uri imageUri;
//        ImageView imageView = findViewById(R.id.profile_image_view);
        if (resultCode == RESULT_OK && reqCode == 100){
            imageUri = data.getData();
//            imageView.setImageURI(imageUri);
            Toast.makeText(this,"Pic Uri: " + imageUri, Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Pic Uri: " + imageUri);
        } else {
            Toast.makeText(this,"Pic from gallery NOT ok", Toast.LENGTH_SHORT).show();
        }
    }
}