package com.rums;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatRoom implements Identity {
    private String id;
    private String name;
    private ArrayList<String> usersByID;
    private Boolean isPrivate;
    private String adminByUserID;
    private ArrayList<Message> messages;

    public ChatRoom() {
    }

    public ChatRoom(String ID, String name, ArrayList<String> usersByID, Boolean isPrivate, String adminByUserID, ArrayList<Message> messages) {
        this.id = ID;
        this.name = name;
        this.isPrivate = isPrivate;
        this.adminByUserID = adminByUserID;
        if(usersByID != null) {
            this.usersByID = usersByID;
        } else {
            this.usersByID = new ArrayList<>();
        }

        if(messages != null) {
            this.messages = messages;
        } else {
//            Log.d("Tag__6", "NEWWWWWW ");
            this.messages = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "ID='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", usersByID=" + usersByID +
                ", isPrivate=" + isPrivate +
                ", adminByUserID='" + adminByUserID + '\'' +
                ", messages=" + messages +
                '}';
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getUsersByID() {
        return usersByID;
    }

    public void setUsersByID(ArrayList<String> usersByID) {
        this.usersByID = usersByID;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getAdminByUserID() {
        return adminByUserID;
    }

    public void setAdminByUserID(String adminByUserID) {
        this.adminByUserID = adminByUserID;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

}
