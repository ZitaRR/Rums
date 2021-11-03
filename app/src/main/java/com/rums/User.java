package com.rums;
import android.content.ClipData;

import java.util.ArrayList;

public class User {

    private String mName, mPhone;
    private boolean mChecked;

    public User(String name, String phone, boolean checked) {
        this.mName = name;
        this.mPhone = phone;
        this.mChecked = checked;
    }

    private static int lastContactId=0;

    public static ArrayList<User> createUsersList(int numUsers) {
        ArrayList<User> userList = new ArrayList<User>();

        for (int i =1; i<=numUsers; i++) {
           userList.add(new User("First Last "+ ++lastContactId, "070 xxx xx xx", i <= 0));
        }
        return userList;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
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
