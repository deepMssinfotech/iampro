package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.ViewHolder> {
    ArrayList<MyImageModel> mValues;
    HashSet<String> heading_name;
    Context mContext;
    protected ItemListener mListener;
    String uid,id;
    public MyImageAdapter(Context context, ArrayList<MyImageModel> values,HashSet<String> heading_name, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
        this.heading_name=heading_name;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike;
        VideoView videoView;
        TextView tv_name,category,udate,tv_comments,tv_totallike,detail_name;
        RatingBar ratingBar;
        LinearLayout ll_showhide;
        MyImageModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView=v.findViewById(R.id.imageView);
            tv_name=v.findViewById(R.id.tv_name);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_favourite=v.findViewById(R.id.iv_favourite);
            image=v.findViewById(R.id.imageView);
            videoView=v.findViewById(R.id.videoView);
            ratingBar=v.findViewById(R.id.ratingBar);
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
            id=item.getId();
             //if(!(item.getRating()!="NAN") || !(item.getRating().equalsIgnoreCase("NAN")))
               //ratingBar.setRating(Float.parseFloat(String.valueOf(item.getRating())));
            category.setText(item.getCategory());
            tv_name.setText(item.getName());
            //udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetail fragment = new ImageDetail();
                    Bundle args = new Bundle();
                    args.putString("id", id);
                    args.putString("type", "image");
                    args.putString("uid", uid);
                    function.loadFragment(mContext,fragment,args);
                }
            });

            Glide.with(mContext)
                    .load(Config.ALBUM_URL+item.getImage())
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(imageView);
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public MyImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_row, parent, false);
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

