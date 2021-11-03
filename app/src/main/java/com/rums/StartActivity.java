package com.rums;

import android.os.Bundle;

public class StartActivity extends BaseClassActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        omitOptionsMenu = true;
        init();
    }

    @Override
    protected void init() {
        super.init();
        if(isLoggedIn()) {
            getU
            startSomeActivity(HomeActivity.class);
        } else {
            startSomeActivity(LoginActivity.class);
        }
    }


}