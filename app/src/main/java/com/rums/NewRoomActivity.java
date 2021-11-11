package com.rums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.List;

public class NewRoomActivity extends BaseClassActivity {

    private ArrayList<RumUser> userList;
    private RecyclerView userRow;
    private MyAdapter myAdapter;
    private Toolbar actionBar;
    private PersistantStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        init();
        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
    }

    @Override
    protected void init () {
        super.init();

        storage = PersistantStorage.getInstance();
        userRow = findViewById(R.id.user_Row);

        if(getIsRepositoryReady()) {

            if (userRow != null) {
                getUsersRumUsers();
                setUpRecyclerView();
            }

        }
    }

    private void getUsersRumUsers(){
        userList = (ArrayList<RumUser>) getStorage().getUsers().getAll();
    }

    private void setUpRecyclerView() {
        myAdapter = new MyAdapter(this, userList);
        userRow.setAdapter(myAdapter);
        userRow.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void repositoryIsInitialized(Class<?> type) {
        super.repositoryIsInitialized(type);

        getUsersRumUsers();
        setUpRecyclerView();
/*
        if(getCurrentRumUser() != null) {
            if(type == ChatRoom.class) {
                makeChatRoom("nameRoomTxt", null, false, getCurrentRumUser().getId(), null);
            }
        }
        BaseClassActivity.getCurrentInstance().moveUserToChatRoom(getChatRoomAtIndex(2));
        finish();*/
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
                myAdapter.getFilter().filter(newText);
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    public void NameRoom () {

        EditText nameRoom = findViewById(R.id.name_Room);
        String nameRoomTxt = nameRoom.getText().toString();
        Toast.makeText(NewRoomActivity.this, nameRoomTxt, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ResourceType")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        //checkBox.setChecked(false);
        int id = item.getItemId();
       // EditText nameRoom = findViewById(R.id.name_Room);

        // Här trycker man när man är valt sina användare/boxar
        if (id == R.id.add_done) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            FrameLayout frameView = new FrameLayout(this);


            builder.setView(frameView)

                    // Om nöjd med val, namn på charrummet sätts.
                   .setPositiveButton(R.string.say_hello, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ChatRoom rm;


                            if(getCurrentRumUser() != null) {
                                final EditText userInput = frameView.findViewById(R.id.name_Room);
                                String nameRoomTxt = userInput.getText().toString();
                                myAdapter.selectedUsers.add(getCurrentRumUser().getId());

                                   rm = makeChatRoom(nameRoomTxt, myAdapter.selectedUsers, false, getCurrentRumUser().getId(), null);
                                    BaseClassActivity.getCurrentInstance().moveUserToChatRoom(rm);
                                Toast.makeText(NewRoomActivity.this, nameRoomTxt, Toast.LENGTH_SHORT).show();

                               // }
                            }
                            finish();
                        }
                    })

                    .setNegativeButton(R.string.add_more, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });


            final AlertDialog alertDialog = builder.create();
            LayoutInflater inflater = alertDialog.getLayoutInflater();

            View dialog = inflater.inflate(R.layout.dialog_nameroom, frameView);

            alertDialog.show();
            alertDialog.getWindow().setLayout(800, 400);
            alertDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));

        }
        return super.onOptionsItemSelected(item);
    }

     private void showSettingsActivity () {

         Toast.makeText(this,"Users added to groupchat!", Toast.LENGTH_SHORT).show();

         Intent intent = new Intent(this, ChatRoomActivity.class);
         startActivity(intent);
    }

}
