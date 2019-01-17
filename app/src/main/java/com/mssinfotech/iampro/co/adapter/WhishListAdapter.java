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
import com.mssinfotech.iampro.co.data.WhishListItem;

import java.util.List;

public class WhishListAdapter  extends RecyclerView.Adapter<WhishListAdapter.MyViewHolder> {
    private Context context;
    private List<WhishListItem> whishList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public WhishListAdapter(Context context, List<WhishListItem> whishList) {
        this.context = context;
        this.whishList = whishList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_whishlist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final WhishListItem item = whishList.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("₹" + item.getPrice());

        Glide.with(context)
                .load(item.getImage())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return whishList.size();
    }

    public void removeItem(int position) {
        whishList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(WhishListItem item, int position) {
        whishList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}