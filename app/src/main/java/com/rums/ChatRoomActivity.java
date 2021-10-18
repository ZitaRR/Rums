package com.rums;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 100;
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
        int duplicates = 1;
        for (int i = 0; i < duplicates; i++) {
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

    private void picAPicFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST);
        //Är ovanstående nyare är: ?
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

//        Uri imageUri;
//        ImageView imageView = findViewById(R.id.image_main_galary);

        if (resultCode == RESULT_OK && reqCode == 100){
//            imageUri = data.getData();
//            imageView.setImageURI(imageUri);
            Toast.makeText(this,"Pic from gallery OK", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Pic from galler NOT ok", Toast.LENGTH_SHORT).show();
        }
    }


    public void imageFromGalleryButtonMethod(View view) {
        picAPicFromGallery();
    }
}