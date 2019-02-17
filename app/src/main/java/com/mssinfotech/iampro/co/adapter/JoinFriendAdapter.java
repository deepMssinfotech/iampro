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
import com.mssinfotech.iampro.co.data.JoinFriendItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinFriendAdapter extends RecyclerView.Adapter<JoinFriendAdapter.MyViewHolder> {
    private Context context;
    private List<JoinFriendItem> notifyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  fullname, category,city,total_image,total_video,total_user,total_product,total_provide,total_demand;
        public CircleImageView userimage;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            userimage = view.findViewById(R.id.userimage);
            fullname = view.findViewById(R.id.fullname);
            category = view.findViewById(R.id.category);
            city = view.findViewById(R.id.city);
            total_image = view.findViewById(R.id.total_image);
            total_video = view.findViewById(R.id.total_video);
            total_user = view.findViewById(R.id.total_user);
            total_product = view.findViewById(R.id.total_product);
            total_provide = view.findViewById(R.id.total_provide);
            total_demand = view.findViewById(R.id.total_demand);
        }
    }
    public JoinFriendAdapter(Context context, List<JoinFriendItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_friendrequest_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final JoinFriendItem item = notifyList.get(position);
        holder.category.setText(item.getCategory());
        //holder.city.setText(item.getCity());
        holder.fullname.setText(item.getFullname());
       // holder.total_demand.setText(String.valueOf(item.getTotal_product_demand()));
       // holder.total_image.setText(String.valueOf(item.getTotal_img()));
       // holder.total_product.setText(String.valueOf(item.getTotal_product()));
        //holder.total_provide.setText(String.valueOf(item.getTotal_product_provide()));
        //holder.total_video.setText(String.valueOf(item.getTotal_video()));
        //holder.total_user.setText(String.valueOf(item.getTotal_friend()));
        Glide.with(context).load(item.getAvatar()).apply(Config.options_avatar).into(holder.userimage);

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

    public void restoreItem(JoinFriendItem item, int position) {
        notifyList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}