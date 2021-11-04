package com.rums;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

public class RumUser implements Identity {
    private String id;
    private String username;
    private String password;
    private String email;
    private String currentChatRoomID;
    private ChatRoom currentChatRoom; //Probably save to db, since it's a NoSQL db.

    public RumUser(){
    }

    public RumUser(String id){
        this.id = id;
    }

    public RumUser(String id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public void sendMessage(String messageText, String currentTime, String chatRoomID, PersistantStorage storage) {
        Message message = new Message(getId(), getUsername(), null, messageText, currentTime);

//        DatabaseReference chatRoomMessagesPath = FirebaseSingleton.getInstance().getChatRoomPath(currentChatRoomID + "/messages");
//
//        String newMessageKey = chatRoomMessagesPath.push().getKey();
////
//        chatRoomMessagesPath.child(newMessageKey).setValue(message);
//
//        storage.getRooms().getById(chatRoomID).getMessages().insert()
//                storage.getRums().insert(room);


    }

    public String getId(){
        return id;
    }

    public void setUsername(String value){
        username = value;
    }

    public String getUsername(){
        return username;
    }

    public void setPassword(String value){
        password = value;
    }

    public String getPassword(){
        return password;
    }

    public void setEmail(String value){
        email = value;
    }

    public String getEmail(){
        return email;
    }

    public String getCurrentChatRoomID() {
        return currentChatRoomID;
    }

    public void setCurrentChatRoomID(String currentChatRoomID) {
        this.currentChatRoomID = currentChatRoomID;
    }

    public ChatRoom getCurrentChatRoom() {
        return currentChatRoom;
    }

    public void setCurrentChatRoom(ChatRoom currentChatRoom) {
        this.currentChatRoom = currentChatRoom;
    }

}
