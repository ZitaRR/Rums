package com.rums;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {

    ArrayList<String> names;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setupListViewAdapter();

    }


    private void setupListViewAdapter() {
        names = new ArrayList<>();
        names.add("Kalle");
        names.add("Bille");
        names.add("Mmmmmmmm. Mmmm. Ett längre meddelande som kanske wrappar. Är det så? Det får vi kolla. Ett långt, långt meddelande" +
                "som fortsätter vidare och vidare och vidare.");

        adapter = new ArrayAdapter<>(this, R.layout.my_list_textview, names);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }
}