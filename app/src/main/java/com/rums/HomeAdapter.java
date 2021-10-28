package com.rums;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter <HomeAdapter.HomeViewHolder> {
    ArrayList<String> chatRoomNames;
    ArrayList<String> chatUserNames;
    Context context;


    public HomeAdapter (Context ct, ArrayList<String> roomNames, ArrayList<String> userNames) {

        context = ct;
        chatRoomNames = roomNames;
        chatUserNames = userNames;


    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_chat_rows, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
    holder.myText1.setText(chatRoomNames.get(position));
    holder.myText2.setText(chatUserNames.get(position));
    holder.myImg.setImageResource(R.drawable.ic_age);

    // När man klickar på en rad..
    holder.homeChatRows.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, ChatRoomActivity.class);
            context.startActivity(i);
        }
    });
    }

    @Override
    public int getItemCount() {
        return chatRoomNames.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView myText1, myText2;
        ImageView myImg;
        ConstraintLayout homeChatRows;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.textview_chatlist_room);
            myText2 = itemView.findViewById(R.id.textview_chatlist_username);
            myImg = itemView.findViewById(R.id.imageview_chatlist_profilepic);
            homeChatRows = itemView.findViewById(R.id.home_chat_rows);
        }
    }

}
