package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class NewRoomActivity extends BaseClassActivity {

    private ArrayList<RumUser> userList;
    private RecyclerView userRow;
    private MyAdapter myAdapter;
    //private DatabaseReference database;
    private Toolbar actionBar;
    private CheckBox checkBox;

    private PersistantStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

        userRow = (RecyclerView) findViewById(R.id.user_Row);
        userRow.setLayoutManager(new LinearLayoutManager(this));

        userList = (ArrayList<RumUser>) storage.getUsers().getAll();
        myAdapter = new MyAdapter(this, userList);
        userRow.setAdapter(myAdapter);

        /*database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Search user"); // text-hint

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String hello = getResources().getString(R.string.say_hello);
        String more = getResources().getString(R.string.add_more);

        //checkBox.setChecked(false);
        int id = item.getItemId();
        if (id == R.id.add_done) {

            String itemChecked = "Users added to groupchat!";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final FrameLayout frameView = new FrameLayout(this);
            builder.setView(frameView)


                   .setPositiveButton("Say Hello!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
/*                            *
                            for(int i = 0; i <userRow.getChildCount(); i++) {
                            if (checkBox.isChecked()) {

                             itemChecked += checkBox;
                            setNewChat(userList);
                            }
                            }*/

                            showSettingsActivity();
                            finish();
                        }
                    })

                    .setNegativeButton(more, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                        }
                    });

            final AlertDialog alertDialog = builder.create();
            LayoutInflater inflater = alertDialog.getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.dialog_nameroom, frameView);
            alertDialog.show();

           Toast.makeText(this,itemChecked, Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void setNewChat(ArrayList<User> userList) {
    }

     private void showSettingsActivity () {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        startActivity(intent);
    }

}
