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
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
/*
public class MyProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<String> mItemList;
    public MyProductAdapter(List<String> itemList) {

        mItemList = itemList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

           // tvItem = itemView.findViewById(R.id.tvItem);
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }
    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String item = mItemList.get(position);
        viewHolder.tvItem.setText(item);

    }
} */

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder> {
    ArrayList<MyProductModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    public MyProductAdapter(Context context, ArrayList<MyProductModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike;
        VideoView videoView;
        TextView tv_name,uname,udate,tv_comments,tv_totallike,detail_name,tv_purchaseprice,tv_sellingprice;
        RatingBar ratingBar;
        LinearLayout ll_showhide;
        MyProductModel item;
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
            ivLike=v.findViewById(R.id.ivLike);
            //udate=v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_showhide=v.findViewById(R.id.ll_showhide);
            uname=v.findViewById(R.id.uname);
            detail_name=v.findViewById(R.id.detail_name);

            tv_purchaseprice=v.findViewById(R.id.tv_purchaseprice);

            tv_sellingprice=v.findViewById(R.id.tv_sellingprice);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ProfileActivity.class);
                    intent.putExtra("uid",uid);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext,"uid: "+uid,Toast.LENGTH_LONG).show();
                }
            });

        }
        public void setData(MyProductModel item) {
            this.item = item;
            uid=item.getUid();
            ratingBar.setRating(Float.parseFloat(String.valueOf(item.getRating())));
            uname.setText(item.getFullname());
            tv_name.setText(item.getName());
            //udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));
            tv_purchaseprice.setText(String.valueOf(item.getpCost()));
            tv_sellingprice.setText(String.valueOf(item.getsCost()));

            Glide.with(mContext)
                    .load(item.getImage())
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
    public MyProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_row, parent, false);
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
        void onItemClick(MyProductModel item);
    }
}

