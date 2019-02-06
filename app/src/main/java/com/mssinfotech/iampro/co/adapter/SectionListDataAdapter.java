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
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    private int uid,id;
    ImageView ivLike;
    public SectionListDataAdapter(Context context, ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);

        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        SingleItemModel singleItem = itemsList.get(i);
        holder.tvTitle.setText(singleItem.getName());
        holder.totallike.setText(String.valueOf(singleItem.getTotallike()));
        holder.comments.setText(String.valueOf(singleItem.getComments()));
        /*
        if(singleItem.getDaysago()!="") {
              holder.daysago.setVisibility(View.VISIBLE);
              holder.daysago.setText(singleItem.getDaysago());
        }*/
        holder.user_name.setText(singleItem.getFullname());
        uid=singleItem.getUid();
        id=singleItem.getId();
        //user_image
        Glide.with(mContext)
                .load(singleItem.getAvatar())
                .apply(new RequestOptions()
                        .circleCrop().bitmapTransform(new CircleCrop())
                        .fitCenter())
                .into(holder.user_image);

       /* Picasso.with(mContext)
                .load(singleItem.getUrl())
                .resize(70,70)
                .into(holder.itemImage); */

        /* Glide.with(mContext)
                .load(singleItem.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(holder.itemImage); */

        String url=singleItem.getImage();
        //Log.d("url_adapter",url);
        //Toast.makeText(mContext, ""+url, Toast.LENGTH_SHORT).show();
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter())
                .into(holder.itemImage);
        final String utype=singleItem.getType();
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utype.equals("image") || utype.equals("video")) {
                    Intent intent = new Intent(mContext, ImageDetail.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("id", id);
                    intent.putExtra("type", utype);
                    mContext.startActivity(intent);
                }else if(utype.equals("product")){
                    Intent intent=new Intent(mContext, ProductDetail.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid",String.valueOf(id));
                    intent.putExtra("uid",String.valueOf(uid));
                    mContext.startActivity(intent);
                }else if(utype.equals("provide")){
                    Intent intent=new Intent(mContext, ProvideDetailActivity.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid",String.valueOf(id));
                    intent.putExtra("uid",String.valueOf(uid));
                    mContext.startActivity(intent);
                }else if(utype.equals("demand")){
                    Intent intent=new Intent(mContext, DemandDetail.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid",String.valueOf(id));
                    intent.putExtra("uid",String.valueOf(uid));
                    mContext.startActivity(intent);
                }
                //Toast.makeText(mContext, utype+" clicked", Toast.LENGTH_SHORT).show();
            }
        });
         holder.user_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(mContext, "Uid:"+String.valueOf(uid), Toast.LENGTH_SHORT).show();
                 Intent intent=new Intent(mContext, ProfileActivity.class);
                  intent.putExtra("uid",String.valueOf(uid));
                  mContext.startActivity(intent);
             }
         });
         holder.likelayout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(mContext, "Total Like "+String.valueOf(uid), Toast.LENGTH_SHORT).show();
             }
         });
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


        public SingleItemRowHolder(View view) {
            super(view);
            this.likelayout = view.findViewById(R.id.likelayout);
            this.tvTitle =view.findViewById(R.id.tvTitle);
            this.itemImage =  view.findViewById(R.id.itemImage);
            this.totallike=view.findViewById(R.id.tv_totallike);
            this.comments=view.findViewById(R.id.tv_comments);
            this.daysago=view.findViewById(R.id.tv_daysago);
            this.user_image=view.findViewById(R.id.user_image);
            this.user_name=view.findViewById(R.id.tv_user_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            //        Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
