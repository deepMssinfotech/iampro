package com.mssinfotech.iampro.co.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mssinfotech.iampro.co.model.Review;

import java.util.List;

/**
 * Created by mssinfotech on 25/01/19.
 */
/*
public class CountryAdapter extends
        RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    private List<Review> countryList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView countryText;
        public TextView popText;
        TextView tv_user_name,tv_time_ago,tv_comment_user;
        de.hdodenhof.circleimageview.CircleImageView user_reviewimage;
        public MyViewHolder(View view) {
            super(view);
            user_reviewimage=view.findViewById(R.id.user_reviewimage);
            tv_user_name=view.findViewById(R.id.tv_user_name);
            tv_time_ago=view.findViewById(R.id.tv_time_ago);
            tv_comment_user=view.findViewById(R.id.tv_comment_user);

            countryText = (TextView) view.findViewById(R.id.countryName);
            popText = (TextView) view.findViewById(R.id.pop);
        }
    }
    public CountryAdapter(Context context,List<Review> countryList) {
        this.countryList = countryList;
         this.context=context;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Review c = countryList.get(position);
          //holder.user_reviewimage

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_items,parent, false);
        return new MyViewHolder(v);
    }
}
*/