package com.rums;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>implements Filterable {

    private ArrayList<RumUser> mUserList;
    private Context context;
    private ArrayList<RumUser> mFiltered;

    public MyAdapter(Context context, ArrayList<RumUser> userList) {
        this.context = context;
        this.mUserList = userList;
        this.mFiltered = new ArrayList<>(userList);
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View userView = inflater.inflate(R.layout.item_user,parent,false);
        
        ViewHolder viewHolder = new ViewHolder(userView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //ChatRoom currentRoom= newChat.get(position);
        RumUser currentUser = mUserList.get(position);
        currentUser.getChecked();

        holder.userName.setText(currentUser.getUsername());
        holder.userEmail.setText(currentUser.getEmail());
        holder.inviteBox.setChecked(currentUser.getChecked());

            holder.inviteBox.setOnClickListener(new View.OnClickListener() {
                @Override

                // Här blir användare valda genom checkboxar
                public void onClick(View view) {
                    if (holder.inviteBox.isChecked()) {
                        currentUser.setChecked(holder.inviteBox.isChecked());
                        Toast.makeText(view.getContext(), (currentUser.getUsername()), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userEmail;
        public CheckBox inviteBox;
        public ImageView userPic;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.users_name);
            userEmail = itemView.findViewById(R.id.users_email);
            inviteBox = itemView.findViewById(R.id.check_Box);
            userPic = itemView.findViewById(R.id.user_pic);
        }
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RumUser> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length()==0) {
                filteredList.addAll(mFiltered);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();

                for (RumUser item : mFiltered) {
                    if (item.getUsername().toLowerCase().contains(filteredPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            mUserList.clear();
            mUserList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

}

