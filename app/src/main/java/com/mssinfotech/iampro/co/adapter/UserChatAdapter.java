package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 23/02/19.
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.ChatList;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {
    ArrayList<ChatList> mValues;
    HashSet<String> heading_name;
    Context mContext;
    protected ItemListener mListener;
    //ChatList item;
    String uid,id;
    public UserChatAdapter(Context context, ArrayList<ChatList> values,ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
        this.heading_name=heading_name;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView tv_msg;
        ChatList item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            tv_msg=v.findViewById(R.id.tv_msg);
        }
        public void setData(ChatList item) {
            this.item = item;


        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public UserChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_userchat_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
         Vholder.tv_msg.setText(mValues.get(position).getMsg());
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(ChatList item);
    }
}

