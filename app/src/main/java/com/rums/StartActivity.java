package com.rums;

import android.os.Bundle;

public class StartActivity extends BaseClassActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        omitOptionsMenu = true;
    }

//    @Override
//    protected void init() {
//        super.init();
//        if(getFirebaseSingleton().isLoggedIn()) {
////            RumUser.getInstance(this); //Fetches from db or creates new
////            intent = new Intent(this, LoggedInActivity.class);
////            setupRumUser(getFirebaseSingleton().getUserUID());
//            startSomeActivity(LoggedInActivity.class);
//        } else {
//            startSomeActivity(LogInOrSignUpActivity.class);
//        }
//    }


}