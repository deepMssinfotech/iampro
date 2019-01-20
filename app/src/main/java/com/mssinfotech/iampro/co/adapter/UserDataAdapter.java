package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 18/01/19.
 */

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.UserModel;

import java.util.ArrayList;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {
    ArrayList<UserModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    public UserDataAdapter(Context context, ArrayList<UserModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,tv_category;
        //public ImageView imageView;
        de.hdodenhof.circleimageview.CircleImageView imageView;
        public RelativeLayout relativeLayout;
        UserModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView =v.findViewById(R.id.imageView);

            tv_category=(TextView)v.findViewById(R.id.tvcategory);

            //relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
        }
        public void setData(UserModel item) {
            this.item = item;
            textView.setText(item.getName());
             tv_category.setText(item.getCategory());
            //textView.setBackgroundColor(Color.BLUE);
            String url=item.getImage();
            //Toast.makeText(mContext,"image:"+url,Toast.LENGTH_LONG).show();
            //Log.d("url_adapter",url);
            //Toast.makeText(mContext, ""+url, Toast.LENGTH_SHORT).show();
            Glide.with(mContext)
                    .load(url)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .fitCenter())
                    .into(imageView);
            //imageView.setImageResource(item.image);

            // relativeLayout.setBackgroundColor(Color.parseColor("#000000"));

        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

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
        void onItemClick(UserModel item);
    }
}