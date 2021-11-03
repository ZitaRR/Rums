package com.rums;

import com.google.firebase.database.FirebaseDatabase;

public class PersistantStorage {
    private static PersistantStorage instance;
    private Repository<RumUser> users;
    private Repository<ChatRoom> rooms;

    private PersistantStorage() {
        if (instance != null) {
            throw new ExceptionInInitializerError(String.format("There's already an instance of %s", getClass().getName()));
        }

        instance = this;

        users = new Repository<>(RumUser.class, new BaseClassActivity());
        rooms = new Repository<>(ChatRoom.class, new BaseClassActivity());
    }

    public Repository<RumUser> getUsers(){
        return users;
    }

    public Repository<ChatRoom> getRooms() {
        return rooms;
    }

    public static PersistantStorage getInstance(){
        return instance != null ? instance : new PersistantStorage();
    }
}
