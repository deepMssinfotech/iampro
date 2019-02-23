package com.mssinfotech.iampro.co.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.User;
import com.mssinfotech.iampro.co.models.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Kartik Sharma
 * Created on: 8/28/2016 , 2:23 PM
 * Project: FirebaseChat
 */

public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder>  implements Filterable {
    private List<User> mUsers;
    private List<Users> users;
    private List<User> mUserss;

    public UserListingRecyclerAdapter() {
    }

    public UserListingRecyclerAdapter(List<User> users) {
        this.mUsers = users;
    }

    public void add(User user) {
        mUsers.add(user);
        notifyItemInserted(mUsers.size() - 1);
    }

    public UserListingRecyclerAdapter(List<Users> users, String msg) {
        this.users = users;
    }
    public void add(Users user) {
        users.add(user);
        notifyItemInserted(users.size() - 1);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_user_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        //Users users=users.get(position);
        String alphabet = user.email.substring(0, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date());

        holder.txtUsername.setText(user.email);
        holder.txtUserAlphabet.setText(alphabet);
        //holder.txtCount.setText(user.getNotifCount());
        holder.lsmessage.setText(user.getLastMessage());
        holder.txtTime.setText(dateString);
    }
    @Override
    public int getItemCount() {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mUserss= mUsers;
                } else {

                   List<User> filteredList = new ArrayList<>();
                    //AndroidVersion==chat
                    // mArrayList==mUsers
                    for (User androidVersion :mUsers) {

                        if (androidVersion.getUid().toLowerCase().contains(charString) || androidVersion.getEmail().toUpperCase().contains(charString) ) {

                            filteredList.add(androidVersion);
                        }
                    }
                    mUsers = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mUsers;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mUsers = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public User getUser(int position) {
        return mUsers.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserAlphabet, txtUsername,txtTime,txtCount,lsmessage;

        ViewHolder(View itemView) {
            super(itemView);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtUsername = (TextView) itemView.findViewById(R.id.text_view_user_name);
            txtTime=(TextView)itemView.findViewById(R.id.text_view_user_time);
            txtCount=(TextView)itemView.findViewById(R.id.count);
            lsmessage=(TextView)itemView.findViewById(R.id.text_view_user_urmessage);
        }
    }
}
