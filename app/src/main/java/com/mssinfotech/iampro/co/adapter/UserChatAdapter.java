package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 23/02/19.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
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
    String type;
    public UserChatAdapter(Context context, ArrayList<ChatList> values,ItemListener itemListener,String type) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
         this.type=type;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView tv_msg;
        de.hdodenhof.circleimageview.CircleImageView user_image;
        ChatList item;
        LinearLayout cll;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            tv_msg=v.findViewById(R.id.tv_msg);
            user_image=v.findViewById(R.id.user_image);
             cll=v.findViewById(R.id.cll);
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
        //Toast.makeText(mContext,""+type,Toast.LENGTH_LONG).show();
        View view=null;
        if(type.equalsIgnoreCase("sent")) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_userchat_row, parent, false);

        }
        else if (type.equalsIgnoreCase("received")){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_userchat_row_other, parent, false);
        }
        else{
             //view = LayoutInflater.from(mContext).inflate(R.layout.item_userchat_row_other, parent, false);
        }
        //view = LayoutInflater.from(mContext).inflate(R.layout.item_userchat_row_other, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
        Log.d("chatbind_data",""+mValues.get(position));
        type=mValues.get(position).getMessageType();
        if(mValues.get(position).getMessageType().equalsIgnoreCase("received")) {
            Vholder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mValues.get(position).getAvatar())
                    .apply(Config.options_avatar)
                    .into(Vholder.user_image);
            Log.d("avatar_user",mValues.get(position).getAvatar());
            Vholder.tv_msg.setText(mValues.get(position).getMsg());
            //Vholder.tv_msg.setBackground(R.drawable.rounded_corner1);
            Vholder.tv_msg.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner1));
            Vholder.tv_msg.setTextColor(Color.BLACK);
            Vholder.tv_msg.setGravity(Gravity.LEFT | Gravity.START);

        }
        else if(mValues.get(position).getMessageType().equalsIgnoreCase("sent")){
            Vholder.tv_msg.setText(mValues.get(position).getMsg());
            Vholder.tv_msg.setTextColor(Color.BLACK);
            Vholder.tv_msg.setGravity(Gravity.END);
            Vholder.user_image.setVisibility(View.GONE);
            //Vholder.tv_msg.setBackground(R.drawable.rounded_corner);
             Vholder.tv_msg.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner));
            Vholder.cll.setGravity(Gravity.END);
        }
        else{
           /* Vholder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mValues.get(position).getAvatar())
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(Vholder.user_image);
            Vholder.tv_msg.setText(mValues.get(position).getMsg());
            Vholder.tv_msg.setTextColor(Color.BLACK);
            Vholder.tv_msg.setGravity(Gravity.LEFT | Gravity.START);
             Vholder.cll.setGravity(Gravity.LEFT); */
        }
         //Toast.makeText(mContext,mValues.get(position).getMsg(),Toast.LENGTH_LONG).show();
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(ChatList item);
    }
}

