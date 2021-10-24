package com.rums;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PersistantStorage {
    private static PersistantStorage instance;
    private FirebaseDatabase context;
    private DatabaseReference reference;
    private Repository<RumUser> users;

    private PersistantStorage() {
        if (instance != null) {
            throw new ExceptionInInitializerError(String.format("There's already an instance of %s", getClass().getName()));
        }

        instance = this;
        context = FirebaseDatabase.getInstance();
    }

    public Repository<RumUser> getUsers(){
        if(users == null){
            users = new Repository<>(RumUser.class);
        }

        return users;
    }

    public static PersistantStorage getInstance(){
        return instance != null ? instance : new PersistantStorage();
    }
}
