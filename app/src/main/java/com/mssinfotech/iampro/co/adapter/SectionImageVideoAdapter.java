package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 15/01/19.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
public class SectionImageVideoAdapter extends RecyclerView.Adapter<SectionImageVideoAdapter.SingleItemRowHolder> {
    private ArrayList<MyImageModel> itemsList;
    private Context mContext;
    //private String uid,id;
    ImageView ivLike;
    ArrayList<MyImageModel> mValues;
    HashSet<String> heading_name;
    protected MyImageAdapter.ItemListener mListener;
    public SectionImageVideoAdapter(Context context, ArrayList<MyImageModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_row, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        MyImageModel singleItem = itemsList.get(i);
        //orgg
        final String uid=singleItem.getUid();
        final String id=singleItem .getId();
        //if(!(item.getRating()!="NAN") || !(item.getRating().equalsIgnoreCase("NAN")))
        //ratingBar.setRating(Float.parseFloat(String.valueOf(item.getRating())));
        holder. category.setText(singleItem .getCategory());
        holder.tv_name.setText(singleItem .getName());
        //udate.setText(item.getUdate());
        holder.tv_comments.setText(String.valueOf(singleItem .getComments()));
        holder.tv_totallike.setText(String.valueOf(singleItem .getTotallike()));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ImageDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid",uid);
                intent.putExtra("id",id);
                intent.putExtra("type","image");
                mContext.startActivity(intent);
                Log.d("uid_idvideo",""+uid+" "+id);
            }
        });

        Glide.with(mContext)
                .load(Config.ALBUM_URL+singleItem.getImage())
                .apply(new RequestOptions()
                        .circleCrop().bitmapTransform(new CircleCrop())
                        .fitCenter())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle,totallike,comments,daysago,user_name;
        protected ImageView itemImage;
        protected de.hdodenhof.circleimageview.CircleImageView btnMore,user_image;
        protected LinearLayout likelayout;
        //this.btnMore= view.findViewById(R.id.btnMore);

        //orginal
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike;
        VideoView videoView;
        TextView tv_name,category,udate,tv_comments,tv_totallike,detail_name;
        RatingBar ratingBar;
        LinearLayout ll_showhide;
        MyImageModel item;

        public SingleItemRowHolder(View view) {
            super(view);
          /*  this.likelayout = view.findViewById(R.id.likelayout);
            this.tvTitle =view.findViewById(R.id.tvTitle);
            this.itemImage =  view.findViewById(R.id.itemImage);
            this.totallike=view.findViewById(R.id.tv_totallike);
            this.comments=view.findViewById(R.id.tv_comments);
            this.daysago=view.findViewById(R.id.tv_daysago);
            this.user_image=view.findViewById(R.id.user_image);
            this.user_name=view.findViewById(R.id.tv_user_name); */


            //orgg
            imageView=view.findViewById(R.id.imageView);
            tv_name=view.findViewById(R.id.tv_name);
            imageView_user=view.findViewById(R.id.imageView_user);
            imageView_icon=view.findViewById(R.id.imageView_icon);
            iv_comments=view.findViewById(R.id.iv_comments);
            iv_favourite=view.findViewById(R.id.iv_favourite);
            image=view.findViewById(R.id.imageView);
            videoView=view.findViewById(R.id.videoView);
            ratingBar=view.findViewById(R.id.ratingBar);
            ivLike=view.findViewById(R.id.ivLike);
            //udate=v.findViewById(R.id.udate);
            tv_comments=view.findViewById(R.id.tv_comments);
            tv_totallike=view.findViewById(R.id.tv_totallike);
            ll_showhide=view.findViewById(R.id.ll_showhide);
            category=view.findViewById(R.id.category);
            detail_name=view.findViewById(R.id.detail_name);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //        Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
