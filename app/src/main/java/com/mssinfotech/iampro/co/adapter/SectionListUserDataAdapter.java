package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 11/03/19.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.ArrayList;

public class SectionListUserDataAdapter extends RecyclerView.Adapter<SectionListUserDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemsList;
    private Context mContext;
    public String uid="0";
    public String utype;
    ImageView ivLike;
    private static final int LAYOUT_ONE= 0;
    private static final int LAYOUT_TWO= 1;
    int user_block;
    String FrindStatus;
    public SectionListUserDataAdapter(Context context,ArrayList<SingleItemModel> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_items, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }
   /* @Override
    public int getItemViewType(int position)
    {
        if(position==2)       // Even position
            return LAYOUT_ONE;
    } */

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {
        SingleItemModel singleItem = itemsList.get(i);
        /*holder.tvTitle.setText(singleItem.getName());//+"-"+singleItem.getIsliked());
        holder.totallike.setText(String.valueOf(singleItem.getTotallike()));
        holder.comments.setText(String.valueOf(singleItem.getComments()));

        holder.user_name.setText(singleItem.getFullname());
        uid= PrefManager.getLoginDetail(mContext,"id");
        final int id=singleItem.getId();
        final int added_by=singleItem.getUid();
        //user_image
        Glide.with(mContext)
                .load(singleItem.getAvatar())
                .apply(new RequestOptions()
                        .circleCrop().bitmapTransform(new CircleCrop())
                        .fitCenter())
                .into(holder.user_image);


        String url=singleItem.getImage();
        //Log.d("url_adapter",url);
        //Toast.makeText(mContext, ""+url, Toast.LENGTH_SHORT).show();
        Glide.with(mContext)
                .load(url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter())
                .into(holder.itemImage);
        utype=singleItem.getType();
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(utype.equals("image") || utype.equals("video")) {
                    ImageDetail fragment = new ImageDetail();
                    Bundle args = new Bundle();
                    args.putString("id", String.valueOf(id));
                    args.putString("type", utype);
                    args.putString("uid", uid);
                    function.loadFragment(mContext,fragment,args);

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
                    Intent intent=new Intent(mContext, DemandDetailActivity.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid",String.valueOf(id));
                    intent.putExtra("uid",String.valueOf(uid));
                    mContext.startActivity(intent);
                }
                //Toast.makeText(mContext, utype+" clicked", Toast.LENGTH_SHORT).show();
            }
        });
        if(PrefManager.isLogin(mContext)) {
            int my_uid = Integer.parseInt(uid);
            if (my_uid == 0) {
                holder.likeButton.setEnabled(false);
            }
            if ((singleItem.getIsliked()) == 1) {
                holder.likeButton.setLiked(true);
                holder.totallike.setTextColor(Color.RED);
            } else {
                holder.likeButton.setLiked(false);
                holder.totallike.setTextColor(Color.BLACK);
            }
        }
        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle bundle = new Bundle();
                bundle.putString("uid", String.valueOf(added_by));
                function.loadFragment(mContext,fragment,bundle);

            }
        });
        holder.likelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Total Like "+String.valueOf(uid), Toast.LENGTH_SHORT).show();
            }
        });
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(holder.totallike.getText().toString())+1;
                holder.totallike.setTextColor(Color.RED);
                holder.totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+utype;
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(holder.totallike.getText().toString())-1;
                holder.totallike.setTextColor(Color.BLACK);
                holder.totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+utype;
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
        });
        holder.iv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("id",String.valueOf(id));
                intent.putExtra("type",utype);
                mContext.startActivity(intent);
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("id",String.valueOf(id));
                intent.putExtra("type",utype);
                mContext.startActivity(intent);
            }
        }); */


        //holder.setData(mValues.get(position));

        holder.imageView_message.setBackground(AppCompatResources.getDrawable(mContext,R.drawable.user_slide_info_message));
        holder.imageView_frequest.setBackground(AppCompatResources.getDrawable(mContext,R.drawable.user_slide_nfo));
        holder.imageView_viewProfile.setBackground(AppCompatResources.getDrawable(mContext,R.drawable.user_slide_info_view_profile));
        holder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext,R.drawable.unblockicone));



    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
       /* protected TextView tvTitle,totallike,comments,daysago,user_name;
        protected ImageView itemImage,iv_comments;
        protected de.hdodenhof.circleimageview.CircleImageView btnMore,user_image;
        protected LinearLayout likelayout;
        protected LikeButton likeButton; */

        public TextView textView,tv_category,tv_images,tv_videos,tv_users,tv_products,tv_provides,tv_demands,tv_viewProfile,tv_fRequest,tv_sendMessage,tv_blockUser;
        de.hdodenhof.circleimageview.CircleImageView imageView,imageView_frequest,imageView_message,imageView_block,imageView_viewProfile;


        public SingleItemRowHolder(View view) {
            super(view);
           /* this.iv_comments = view.findViewById(R.id.iv_comments);
            this.likeButton = view.findViewById(R.id.likeButton);
            this.likelayout = view.findViewById(R.id.likelayout);
            this.tvTitle =view.findViewById(R.id.tvTitle);
            this.itemImage =  view.findViewById(R.id.itemImage);
            this.totallike=view.findViewById(R.id.tv_totallike);
            this.comments=view.findViewById(R.id.tv_comments);
            this.daysago=view.findViewById(R.id.tv_daysago);
            this.user_image=view.findViewById(R.id.user_image);
            this.user_name=view.findViewById(R.id.tv_user_name); */

            textView =view.findViewById(R.id.textView);
            imageView =view.findViewById(R.id.imageView);
            tv_category=view.findViewById(R.id.tvcategory);
            tv_images=view.findViewById(R.id.tv_images);
            tv_videos=view.findViewById(R.id.tv_videos);
            tv_users=view.findViewById(R.id.tv_users);
            tv_products=view.findViewById(R.id.tv_products);
            tv_provides=view.findViewById(R.id.tv_provides);
            tv_demands=view.findViewById(R.id.tv_demands);

            imageView_frequest=view.findViewById(R.id.imageView_frequest);
            imageView_message=view.findViewById(R.id.imageView_message);
            imageView_block=view.findViewById(R.id.imageView_block);
            imageView_viewProfile=view.findViewById(R.id.imageView_viewProfile);


            tv_viewProfile=view.findViewById(R.id.tv_viewProfile);
            tv_fRequest=view.findViewById(R.id.tv_fRequest);
            tv_sendMessage=view.findViewById(R.id.tv_sendMessage);
            tv_blockUser=view.findViewById(R.id.tv_blockUser);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //        Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}