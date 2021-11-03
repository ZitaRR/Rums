package com.rums;

import java.util.ArrayList;
import java.util.HashMap;


public class ChatRoom implements Identity {
    private String id;
    private String name;
    private ArrayList<String> usersByID;
    private Boolean isPrivate;
    private String adminByUserID;
    private HashMap<String, Message> messages;

    public ChatRoom() {
    }

    public ChatRoom(String ID, String name, ArrayList<String> usersByID, Boolean isPrivate, String adminByUserID, HashMap<String, Message> messages) {
        this.id = ID;
        this.name = name;
        this.usersByID = usersByID;
        this.isPrivate = isPrivate;
        this.adminByUserID = adminByUserID;

        if(messages != null) {
            this.messages = messages;
        } else {
            this.messages = new HashMap<>();
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

    public HashMap<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, Message> messages) {
        this.messages = messages;
    }
}
