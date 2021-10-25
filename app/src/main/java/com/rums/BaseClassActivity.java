package com.rums;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class BaseClassActivity extends AppCompatActivity {

    private Toolbar actionBar;
    protected Boolean shouldHaveBackArrowInActionBar = true;
    protected Class<?> previousActivityClass;
    protected int PREVIOUS_ACTIVITY_REQUEST_CODE = 149;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    protected void init() {
        setActionBar();
        getPreviousActivity();
    }

    protected void setActionBar() {
        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
        if(shouldHaveBackArrowInActionBar) {
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected void getPreviousActivity() {
        ComponentName previousActivity = getCallingActivity();
        if(previousActivity != null) {
            String className = previousActivity.getClassName();
            previousActivityClass = null;
            try {
                previousActivityClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getClass() == androidx.appcompat.view.menu.ActionMenuItem.class) {

            switch (item.getItemId()) {
                case android.R.id.home:
                    if (previousActivityClass != null) {
                        startSomeActivity(previousActivityClass);
                    }
//                NavUtils.navigateUpFromSameTask(this);
                    return true;
                default:
//                return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    protected void startSomeActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls).putExtra("fromActivity", "someThing");
        startActivityForResult(intent, PREVIOUS_ACTIVITY_REQUEST_CODE);
    }



}