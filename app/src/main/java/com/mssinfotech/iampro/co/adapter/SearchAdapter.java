package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.SearchItem;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.GlideApp;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private Context context;
    private List<SearchItem> searchList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,product_price, user_name, detail,fullname,like,comment;
        public ImageView product_image,video_imageView,playIcon;
        public CircleImageView imageView_icon,imageView_user;
        public LinearLayout video_imageView_ll,pplayout;
        public MyViewHolder(View view) {
            super(view);
            like = view.findViewById(R.id.like);
            comment = view.findViewById(R.id.comment);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            product_image = view.findViewById(R.id.product_image);
            detail = view.findViewById(R.id.detail);
            imageView_user = view.findViewById(R.id.imageView_user);
            imageView_icon = view.findViewById(R.id.imageView_icon);
            fullname = view.findViewById(R.id.fullname);
            video_imageView_ll = view.findViewById(R.id.video_imageView_ll);
            video_imageView = view.findViewById(R.id.video_imageView);
            playIcon=view.findViewById(R.id.playIcon);
            pplayout =view.findViewById(R.id.pplayout);
        }
    }


    public SearchAdapter(Context context, List<SearchItem> searchList) {
        this.context = context;
        this.searchList = searchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchItem item = searchList.get(position);
        final String user_id=item.getUser_Id();
        final String id= String.valueOf(item.getId());
        final String p_type=item.getType();
        String avatar = Config.AVATAR_URL+item.getUser_avatar();
        holder.video_imageView_ll.setVisibility(View.GONE);
        holder.pplayout.setVisibility(View.GONE);
        holder.playIcon.setVisibility(View.GONE);
        holder.fullname.setText(item.getUser_fullname());
        Glide.with(context)
                .load(avatar)
                .apply(Config.options_avatar)
                .into(holder.imageView_user);
        if (p_type.equalsIgnoreCase("image")) {
            holder.video_imageView_ll.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Config.ALBUM_URL+item.getImage())
                    .apply(Config.options_video)
                    .into(holder.video_imageView);
            Glide.with(context)
                    .load(R.drawable.image_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }else if (p_type.equalsIgnoreCase("video")) {
            holder.playIcon.setVisibility(View.VISIBLE);
            holder.video_imageView_ll.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Config.V_URL+item.getImage())
                    .apply(Config.options_video)
                    .into(holder.video_imageView);
            Glide.with(context)
                    .load(R.drawable.video_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        else if (p_type.equalsIgnoreCase("product")) {
            holder.product_name.setText(item.getName());
            holder.product_price.setText("Rs. "+item.getSelling_cost());
            holder.like.setText(item.getTotallike()+" Like ");
            holder.comment.setText(item.getComments()+" Comment");
            holder.pplayout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Config.PRODUCT_URL+item.getImage())
                    .apply(Config.options_demand)
                    .into(holder.product_image);
            Glide.with(context)
                    .load(R.drawable.product_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        else if (p_type.equalsIgnoreCase("provide")) {
            holder.product_name.setText(item.getName());
            holder.product_price.setText("Rs. "+item.getSelling_cost());
            holder.like.setText(item.getTotallike()+" Like ");
            holder.comment.setText(item.getComments()+" Comment");
            holder.pplayout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Config.PROVIDE_URL+item.getImage())
                    .apply(Config.options_demand)
                    .into(holder.product_image);
            Glide.with(context)
                    .load(R.drawable.provide_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        else if (p_type.equalsIgnoreCase("demand")) {
            holder.product_name.setText(item.getName());
            holder.product_price.setText("Rs. "+item.getSelling_cost());
            holder.like.setText(item.getTotallike()+" Like ");
            holder.comment.setText(item.getComments()+" Comment");
            holder.pplayout.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Config.DEMAND_URL+item.getImage())
                    .apply(Config.options_demand)
                    .into(holder.product_image);
            Glide.with(context)
                    .load(R.drawable.demand_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        holder.imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(user_id));
                function.loadFragment(context,fragment,args);
            }
        });
        holder.imageView_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,""+notifyList.get(position).getP_type()+"\t"+n_type,Toast.LENGTH_LONG).show();
                if (p_type.equalsIgnoreCase("IMAGE")) {
                    MyImageActivity fragment = new MyImageActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    function.loadFragment(context,fragment,args);
                } else if (p_type.equalsIgnoreCase("VIDEO")) {
                    MyVideoActivity fragment = new MyVideoActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    function.loadFragment(context,fragment,args);
                } else if (p_type.equalsIgnoreCase("PRODUCT")) {
                    MyProductActivity fragment = new MyProductActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    function.loadFragment(context,fragment,args);
                } else if (p_type.equalsIgnoreCase("PROVIDE")) {
                    MyProvideActivity fragment = new MyProvideActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    function.loadFragment(context,fragment,args);
                } else if (p_type.equalsIgnoreCase("DEMAND")) {
                    MyDemandActivity fragment = new MyDemandActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    function.loadFragment(context,fragment,args);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

}