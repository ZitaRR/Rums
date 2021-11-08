package com.rums;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter <HomeAdapter.HomeViewHolder> {
    ArrayList<ChatRoom> chatRooms;
    Context context;


    public HomeAdapter (Context ct, ArrayList<ChatRoom> cr) {

        context = ct;
        chatRooms = cr;

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

    ChatRoom currentRoom = chatRooms.get(position);

    holder.myText1.setText(currentRoom.getName());
    holder.myText2.setText(currentRoom.getId());
    holder.myCircleView.setImageResource(R.drawable.rums_ikon);

    // När man klickar på en rad..
    holder.homeChatRows.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BaseClassActivity.getCurrentInstance().moveUserToChatRoom(currentRoom);

        }
    });
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView myText1, myText2;
        CircleImageView myCircleView;
        ConstraintLayout homeChatRows;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.textview_chatlist_room);
            myText2 = itemView.findViewById(R.id.textview_chatlist_username);
            homeChatRows = itemView.findViewById(R.id.home_chat_rows);
            myCircleView = itemView.findViewById(R.id.circle_imageview_chatlist_profilepic);
        }
    }

}
