package com.rums;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>implements Filterable {

    private ArrayList<User> mUserList;
    private Context context;
    private ArrayList<User> mUsersFull;

    public MyAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.mUserList = userList;
        this.mUsersFull = new ArrayList<>(userList);
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

        User currentUser = mUserList.get(position);

        holder.userName.setText(currentUser.getName());
        holder.userPhone.setText(currentUser.getPhone());

        TextView tv = holder.userName;
        tv.setText(currentUser.getName());
       // Checkbox invite = holder.inviteBox;
        holder.inviteBox.setChecked(mUserList.get(position).getChecked());
        /*holder.inviteBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserList.get(position).getChecked()) {
                    mUserList.get(position).setChecked(false);
                } else {
                    mUserList.get(position).setChecked(true);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhone;
        public CheckBox inviteBox;
        public ImageView userPic;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView)  itemView.findViewById(R.id.users_name);
            userPhone = (TextView) itemView.findViewById(R.id.users_phone);
            inviteBox = (CheckBox) itemView.findViewById(R.id.check_Box);
            userPic = (ImageView) itemView.findViewById(R.id.user_pic);
        }
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length()==0) {
                filteredList.addAll(mUsersFull);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();

                for (User item : mUsersFull) {
                    if (item.getName().toLowerCase().contains(filteredPattern)) {
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

