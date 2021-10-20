package com.rums;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 100;
    ArrayList<String> names;
    ArrayAdapter<String> adapter;
    ListView listView;
    private Toolbar actionBar;
    private MenuItem mSpinnerItem1 = null;

    private ArrayList<String> testParticipants = new ArrayList() {{add("Pylle"); add("Lylle");}};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setupListViewAdapter();

        actionBar = findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chat_room_menu, menu);

        //Spinner:

            MenuInflater mi=getMenuInflater();
            mi.inflate(R.menu.chat_room_menu, menu);
            mSpinnerItem1 = menu.findItem( R.id.spinner);
            View view1 = mSpinnerItem1.getActionView();
            if (view1 instanceof Spinner)
            {
                final Spinner spinner = (Spinner) view1;
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.chat_bubble_list_item, testParticipants);

                spinner.setAdapter(arrayAdapter);


//                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                               int arg2, long arg3) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });

            }



        return super.onCreateOptionsMenu(menu);
    }


    private void setupListViewAdapter() {
        names = new ArrayList<>();
        int duplicates = 1;
        for(int i = 0; i<duplicates; i++) {
            names.add("Kalle");
            names.add("Bille");
            names.add("Mmmmmmmm. Mmmm. Ett längre meddelande som kanske wrappar. Är det så? Det får vi kolla. Ett långt, långt meddelande" +
                    "som fortsätter vidare och vidare och vidare.");
        }

        adapter = new ArrayAdapter<>(this, R.layout.chat_bubble_list_item, names);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        //Scroll to bottom:
        listView.post(() -> listView.setSelection(listView.getCount() - 1));
    }

    public void imageFromGalleryButtonMethod(View view) {
        picAPicFromGallery();
    }

    private void picAPicFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //Deprecated:
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        Uri imageUri;
//        ImageView imageView = findViewById(R.id.profile_image_view);
        if (resultCode == RESULT_OK && reqCode == 100){
            imageUri = data.getData();
//            imageView.setImageURI(imageUri);
            Toast.makeText(this,"Pic Uri: " + imageUri, Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Pic Uri: " + imageUri);
        } else {
            Toast.makeText(this,"Pic from gallery NOT ok", Toast.LENGTH_SHORT).show();
        }
    }
}