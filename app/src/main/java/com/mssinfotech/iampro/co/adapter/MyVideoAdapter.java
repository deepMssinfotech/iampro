package com.mssinfotech.iampro.co.adapter;
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
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    ArrayList<MyImageModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    String uid;
    public MyVideoAdapter(Context context, ArrayList<MyImageModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView  imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike;
        VideoView videoView;
        TextView tv_name,category,udate,tv_comments,tv_totallike,detail_name;
        RatingBar ratingBar;
        LinearLayout ll_showhide;
        MyImageModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            videoView=v.findViewById(R.id.videoView);
            tv_name=v.findViewById(R.id.tv_name);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_favourite=v.findViewById(R.id.iv_favourite);
            image=v.findViewById(R.id.imageView);
            videoView=v.findViewById(R.id.videoView);
            ratingBar=v.findViewById(R.id.ratingBar);
            ivLike=v.findViewById(R.id.ivLike);
            //udate=v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_showhide=v.findViewById(R.id.ll_showhide);
            category=v.findViewById(R.id.category);
            detail_name=v.findViewById(R.id.detail_name);
        }
        public void setData(MyImageModel item) {
            this.item = item;
            uid=item.getUid();
            //ratingBar.setRating(Float.parseFloat(String.valueOf(item.getRating())));
            category.setText(item.getCategory());
            tv_name.setText(item.getName());
            //udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));

            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ProfileActivity.class);
                    intent.putExtra("uid",uid);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext,"uid: "+uid,Toast.LENGTH_LONG).show();
                }
            });
            videoView.setVideoPath(item.getImage());
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public MyVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_row, parent, false);
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
        void onItemClick(MyImageModel item);
    }
}

