package com.rums;
import android.content.ClipData;

import java.util.ArrayList;

public class User {

    private boolean mChecked;

    public User(boolean checked) {
        this.mChecked = checked;
    }

    private static int lastContactId=0;

    public static ArrayList<User> createUsersList(int numUsers) {
        ArrayList<User> userList = new ArrayList<User>();

        for (int i =1; i<=numUsers; i++) {
           userList.add(new User(i <= 0));
        }
        return userList;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public boolean getChecked() {
        return mChecked;
    }
    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
