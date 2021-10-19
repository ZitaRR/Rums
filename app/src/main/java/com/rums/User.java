package com.rums;

import java.util.ArrayList;


public class User {
    private String mName;
    private boolean mChecked;

    public User(String name, boolean checked) {
        this.mName = name;
        this.mChecked = checked;
    }

    private static int lastContactId=0;

    public static ArrayList<User> createUsersList(int numUsers) {

        ArrayList<User> users = new ArrayList<User>();

        for (int i =1; i<=numUsers; i++) {
            users.add(new User("@string/person_name" + ++lastContactId, i <= 0));
        }
        return users;
    }
}
