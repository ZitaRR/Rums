package com.rums;

import com.google.firebase.database.FirebaseDatabase;

public class PersistantStorage {
    private static PersistantStorage instance;
    private FirebaseDatabase context;
    private Repository<RumUser> users;

    public Repository<ChatRoom> getRums() {
        return rums;
    }

    private Repository<ChatRoom> rums;

    private PersistantStorage() {
        if (instance != null) {
            throw new ExceptionInInitializerError(String.format("There's already an instance of %s", getClass().getName()));
        }

        instance = this;
        context = FirebaseDatabase.getInstance();

        users = new Repository<>(RumUser.class);
        rums = new Repository<>(ChatRoom.class);
    }

    public Repository<RumUser> getUsers(){
        return users;
    }

    public static PersistantStorage getInstance(){
        return instance != null ? instance : new PersistantStorage();
    }
}
