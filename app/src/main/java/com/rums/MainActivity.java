package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private Toolbar actionBar;
    private RecyclerView rvUsers;
    UsersAdapter adapter;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        rvUsers = (RecyclerView) findViewById(R.id.rv_Users);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        rvUsers.setLayoutManager(linearLayoutManager);


      /*  users = User.createUsersList(20);
        adapter = new UsersAdapter(users);
        rvUsers.setAdapter(adapter); */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Search user");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // adapter.getFilter().filter(newText);
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //för att kunna välja items i listan

        //checkBox.setChecked(true);
        int id = item.getItemId();
        if (id == R.id.add_done) {

            String itemSelected = "Invites send!";

            showSettingsActivity(); //anropar metod för att skickas till ny activity

        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsActivity() { // metod för att skickas till Secondactivity
        Intent intent = new Intent(this, Rooms.class);
        startActivity(intent);
    }

}