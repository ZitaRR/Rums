package com.rums;

public class ChatRoom implements Identity {

    private String Id;

    public ChatRoom() {

    }

    public ChatRoom(String id) {
        Id = id;
    }

    @Override
    public String getId() {
        return Id;
    }

}
