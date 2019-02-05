package com.mssinfotech.iampro.co.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
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
import com.mssinfotech.iampro.co.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import java.util.ArrayList;
public class AllFeedAdapter extends RecyclerView.Adapter<AllFeedAdapter.ViewHolder> {
    ArrayList<FeedModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    public AllFeedAdapter(Context context, ArrayList<FeedModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike;
         VideoView videoView;
         TextView fullname,udate,tv_comments,tv_totallike,detail_name,purchese_cost,selling_cost;
        RatingBar ratingBar;
         LinearLayout ll_showhide;
        FeedModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_favourite=v.findViewById(R.id.iv_favourite);
            image=v.findViewById(R.id.imageView);
             videoView=v.findViewById(R.id.videoView);
             ratingBar=v.findViewById(R.id.ratingBar);
            ivLike=v.findViewById(R.id.ivLike);
            fullname=v.findViewById(R.id.fullname);
            udate=v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_showhide=v.findViewById(R.id.ll_showhide);
            detail_name=v.findViewById(R.id.detail_name);

            purchese_cost=v.findViewById(R.id.purchese_cost);
            selling_cost=v.findViewById(R.id.selling_cost);

        }
        public void setData(FeedModel item) {
            this.item = item;
            uid=item.getUid();
              ratingBar.setRating(Float.parseFloat(String.valueOf(item.getAverage_rating())));
              fullname.setText(item.getFullname());
               udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComment()));
             tv_totallike.setText(String.valueOf(item.getLikes()));
            imageView_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ProfileActivity.class);
                     intent.putExtra("uid",uid);
                     mContext.startActivity(intent);
                    Toast.makeText(mContext,"uid: "+uid,Toast.LENGTH_LONG).show();
                }
            });
            Glide.with(mContext)
                    .load(item.getAvatar_path())
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(imageView_user);

            Log.d("image_setdata",""+item.getFimage_path());
            String type=item.getType();
            if(!type.equalsIgnoreCase("VIDEO")) {
                videoView.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(item.getFimage_path())
                        .apply(new RequestOptions()
                                .circleCrop().bitmapTransform(new CircleCrop())
                                .fitCenter())
                        .into(image);
            }
            else{
                videoView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                videoView.setVideoPath(item.getFimage_path());
                //videoView.start();
            }
  if (type.equalsIgnoreCase("VIDEO")){
      ll_showhide.setVisibility(View.GONE);
                imageView_icon.setImageResource(R.drawable.video_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.video_icon));

      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.video_icon));
            }
  else if (type.equalsIgnoreCase("IMAGE")){
      ll_showhide.setVisibility(View.GONE);
                imageView_icon.setImageResource(R.drawable.image_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.image_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.image_icon));
            }
            else if (type.equalsIgnoreCase("PRODUCT")){
                  ll_showhide.setVisibility(View.VISIBLE);
                imageView_icon.setImageResource(R.drawable.product_icon);
      detail_name.setText(item.getDetail_name());

      purchese_cost.setText("Rs: "+String.valueOf(item.getPurchese_cost()));
      selling_cost.setText("Rs: "+String.valueOf(item.getSelling_cost()));
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.product_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.product_icon));
            }
            else if (type.equalsIgnoreCase("PROVIDE")){
              ll_showhide.setVisibility(View.VISIBLE);
                imageView_icon.setImageResource(R.drawable.provide_icon);
               // Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
                 detail_name.setText(item.getDetail_name());
      purchese_cost.setText("");
      selling_cost.setText("Rs: "+String.valueOf(item.getSelling_cost()));
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.provide_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.provide_icon));
            }
            else if (type.equalsIgnoreCase("DEMAND")){
                  ll_showhide.setVisibility(View.VISIBLE);
                imageView_icon.setImageResource(R.drawable.demand_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      detail_name.setText(item.getDetail_name());
      purchese_cost.setText("");
      selling_cost.setText("Rs: "+String.valueOf(item.getSelling_cost()));
     // Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.demand_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.demand_icon));
            }
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public AllFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_item, parent, false);
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
        void onItemClick(FeedModel item);
    }
}

