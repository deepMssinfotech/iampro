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
import com.mssinfotech.iampro.co.data.NotificationItem;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private List<NotificationItem> notifyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, user_name, detail;
        public ImageView user_image,product_image;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            //product_name = view.findViewById(R.id.product_name);
            //user_name = view.findViewById(R.id.user_count);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            product_image = view.findViewById(R.id.product_image);
            user_image = view.findViewById(R.id.user_image);
            detail = view.findViewById(R.id.detail);
        }
    }


    public NotificationAdapter(Context context, List<NotificationItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NotificationItem item = notifyList.get(position);
        holder.detail.setText(item.getDetail());
        //holder.price.setText("₹" + item.getPrice());
        Glide.with(context).load(item.getPoduct_image()).into(holder.product_image);
        Glide.with(context).load(item.getUser_image()).into(holder.user_image);
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public void removeItem(int position) {
        notifyList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationItem item, int position) {
        notifyList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}