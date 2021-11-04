package com.rums;

public class RumUser implements Identity {
    private String id;
    private String username;
    private String password;
    private String email;

    private boolean isChecked;

    private String currentChatRoomID;
    private ChatRoom currentChatRoom; //Probably save to db, since it's a NoSQL db.

    public RumUser(){}

    public RumUser(String id){
        this.id = id;
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


    public boolean getChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
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
