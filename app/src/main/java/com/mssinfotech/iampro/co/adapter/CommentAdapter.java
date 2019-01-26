package com.mssinfotech.iampro.co.adapter;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;
import android.widget.RadioButton;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<Review> mValues;
    Context mContext;
    protected ItemListener mListener;

    public CommentAdapter(Context context, ArrayList<Review> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public Review item;

        public TextView tv_user_name,tv_time_ago,tv_comment_user;
        public de.hdodenhof.circleimageview.CircleImageView user_reviewimage;
        public int uid;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            user_reviewimage=view.findViewById(R.id.user_reviewimage);
            tv_user_name=view.findViewById(R.id.tv_user_name);
            tv_time_ago=view.findViewById(R.id.tv_time_ago);
            tv_comment_user=view.findViewById(R.id.tv_comment_user);

            user_reviewimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"uid:"+uid,Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("uid",String.valueOf(uid));
                    mContext.startActivity(intent);
                }
            });

        }
        public void setData(Review item) {
            this.item = item;
             uid=Integer.parseInt(item.getAdded_by());
            tv_user_name.setText(item.getFname());
             tv_time_ago.setText(item.getRdate());
              tv_comment_user.setText(item.getComments());
            Glide.with(mContext)
                    .load(item.getUser_image())
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(user_reviewimage);

        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_items, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(Review item);
    }
}