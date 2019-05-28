package com.mssinfotech.iampro.co.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.data.CategoryItem;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    ArrayList<CategoryItem> mValues;
    Context mContext;
    protected ItemListener mListener;

    public CategoryListAdapter(Context context,ArrayList<CategoryItem> values,ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public de.hdodenhof.circleimageview.CircleImageView ivGallery;
        public CategoryItem item;

        public int uid;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ivGallery = view.findViewById(R.id.ivGallery);
            ivGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toasty.success(mContext,"check click");
                }
            });

        }
        public void setData(CategoryItem item) {
            this.item = item;

            Glide.with(mContext)
                    .load(item.getImage())
                    .apply(Config.options_avatar)
                    .into(ivGallery);

        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cat_list, parent, false);
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
        void onItemClick(CategoryItem item);
    }
}