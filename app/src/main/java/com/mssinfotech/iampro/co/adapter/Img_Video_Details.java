package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 30/01/19.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.orientation.LandscapeOrientation;
import bg.devlabs.fullscreenvideoview.orientation.PortraitOrientation;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Img_Video_Details extends RecyclerView.Adapter<Img_Video_Details.ViewHolder> {
    ArrayList<ImageDetailModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    public Img_Video_Details(Context context, ArrayList<ImageDetailModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView fullname,udate,tv_comments,tv_totallike,name,category;
        ImageView imageView_user,imageView_icon,iv_comments,image;
         FullscreenVideoView fullscreenVideoView;
         VideoView videoView;
        RatingBar ratingBar;
        ImageDetailModel item;
        LikeButton likeButton;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
             fullname = v.findViewById(R.id.fullname);
             udate =v.findViewById(R.id.udate);
             tv_comments=v.findViewById(R.id.tv_comments);
               fullscreenVideoView =v.findViewById(R.id.fullscreenVideoView);
            //tv_comments
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            name=v.findViewById(R.id.name);
            category=v.findViewById(R.id.category);
            ratingBar=v.findViewById(R.id.ratingBar);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            likeButton=v.findViewById(R.id.likeButton);
            image=v.findViewById(R.id.image);
            imageView_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity fragment = new ProfileActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                }
            });
           videoView=v.findViewById(R.id.video);
        }
        public void setData(final ImageDetailModel item) {
            this.item = item;
            //TextView fullname,udate,tv_comments,tv_totallike,name,category;
            //ImageView imageView_user,imageView_icon,iv_comments,image;
            //RatingBar ratingBar;

             fullname.setText(item.getFullname());
            udate.setText(String.valueOf(item.getUdatee()));
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));
             name.setText(item.getName());
              category.setText(String.valueOf(item.getCategory()));
            likeButton.setUnlikeDrawableRes(R.drawable.like);
            likeButton.setLikeDrawableRes(R.drawable.like_un);

            String avatar=item.getAvatar();
            String images=item.getImage();

            ratingBar.setRating(item.getRating());

             uid=item.getUid();

            Glide.with(mContext)
                    .load(avatar)
                    .apply(Config.options_avatar)
                    .into(imageView_user);
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public Img_Video_Details.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.img_video_detail, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
        //sendrating(float rating,int uid,int id)
        final int uid=mValues.get(position).getUid();
         final int id=mValues.get(position).getId();
        final String type=mValues.get(position).getType();
        Vholder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(Vholder.tv_totallike.getText().toString())+1;
                Vholder.tv_totallike.setTextColor(Color.RED);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+type;
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike =Integer.parseInt(Vholder.tv_totallike.getText().toString())-1;
                Vholder.tv_totallike.setTextColor(Color.BLACK);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+type;
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
        });

        if(mValues.get(position).getType().equalsIgnoreCase("image")) {
            Vholder.videoView.setVisibility(View.GONE);
            Vholder.image.setVisibility(View.VISIBLE);
             Vholder.fullscreenVideoView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(mValues.get(position).getImage())
                    .apply(Config.options_image)
                    .into(Vholder.image);

            Glide.with(mContext)
                    .load(R.drawable.image_icon)
                    .apply(Config.options_avatar)
                    .into(Vholder.imageView_icon);
            final int added_by_id =mValues.get(position).getUid();
             Vholder.imageView_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyImageActivity fragment = new MyImageActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(added_by_id));
                    function.loadFragment(mContext,fragment,args);
                }
            });

            //imageView_icon.setImageResource(R.drawable.image_icon);
        }
        else if(mValues.get(position).getType().equalsIgnoreCase("video")) {
            Vholder.image.setVisibility(View.GONE);
            //videoView.setVisibility(View.VISIBLE);

            //videoView.setVideoPath(item.getImage());
            //videoView.start();//
            final int added_by_id = mValues.get(position).getUid();
            Vholder.imageView_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyVideoActivity fragment = new MyVideoActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(added_by_id));
                    function.loadFragment(mContext,fragment,args);
                }
            });
             Glide.with(mContext)
                    .load(R.drawable.video_icon)
                    .apply(Config.options_video)
                    .into(Vholder.imageView_icon);

             Vholder.fullscreenVideoView.setVisibility(View.VISIBLE);
            String ImageHol = Config.URL_ROOT+"uploads/video/"+mValues.get(position).getImage();
                                /*videoView.setVideoPath(ImageHol);
                                Log.d(Config.TAG, ImageHol);
                                mediaController = new MediaController(CommentActivity.this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                videoView.requestFocus();
                                videoView.start();*/
             Vholder.fullscreenVideoView.videoUrl(ImageHol)
                    .enableAutoStart()
                    .addSeekBackwardButton()
                    .addSeekForwardButton()
                    .portraitOrientation(PortraitOrientation.DEFAULT)
                    .landscapeOrientation(LandscapeOrientation.DEFAULT);

            //imageView_icon.setImageResource(R.drawable.video_icon);
              Toast.makeText(mContext,"video",Toast.LENGTH_LONG).show();
        }
         Vholder.imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(uid));
                function.loadFragment(mContext,fragment,args);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(ImageDetailModel item);
    }
}
