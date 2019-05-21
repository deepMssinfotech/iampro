package com.mssinfotech.iampro.co.adapter;
/**
 * Created by mssinfotech on 16/01/19.
 */
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CommentActivity;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.ArrayList;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    public int id = 0;
    int uid,added_by;
    public ImageAdapter(Context context, ArrayList<DataModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,tv_tlike,tv_comments,tv_daysago,tv_sprice,tv_pprice,uname;
        RatingBar ratingBar;
        public ImageView imageView,iv_comments;
        public LikeButton likeButton;
        de.hdodenhof.circleimageview.CircleImageView userImage;
        DataModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            iv_comments = v.findViewById(R.id.iv_comments);
            likeButton = v.findViewById(R.id.likeButton);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            tv_tlike=v.findViewById(R.id.tv_totallike);
            //tv_comments
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_daysago=v.findViewById(R.id.tv_daysago);
            tv_sprice=v.findViewById(R.id.tv_sprice);
            tv_pprice=v.findViewById(R.id.tv_sprice);
            ratingBar=v.findViewById(R.id.ratingBar);
            uname=v.findViewById(R.id.uname);
            userImage=v.findViewById(R.id.user_image);

            tv_comments.setOnClickListener(CommnetOnClickListener);
            iv_comments.setOnClickListener(CommnetOnClickListener);

        }
        private View.OnClickListener CommnetOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("id",String.valueOf(item.getId()));
                intent.putExtra("type","image");
                intent.putExtra("uid",PrefManager.getLoginDetail(mContext,"id"));
                mContext.startActivity(intent);
            }
        };
        public void setData(DataModel item) {
            this.item = item;
            textView.setText(item.getName());
            String url=item.getImage();
            String userImages=item.getUserImage();
            tv_tlike.setText(String.valueOf(item.getTotallike()));
            tv_comments.setText(String.valueOf(item.getComments()));
            ratingBar.setRating(item.getRating());
            if(String.valueOf(item.getsCost())!=null || !String.valueOf(item.getsCost()).equalsIgnoreCase(null)) {
                tv_sprice.setVisibility(View.VISIBLE);
                tv_sprice.setText(String.valueOf(String.valueOf(item.getsCost())));
            }
            if(String.valueOf(item.getpCost())!=null || !String.valueOf(item.getpCost()).equalsIgnoreCase(null) || item.getpCost()!=0){
                tv_pprice.setVisibility(View.VISIBLE);
                tv_pprice.setText(String.valueOf(item.getpCost()));
            }
            //tv_daysago.setVisibility(View.VISIBLE);
            tv_daysago.setText(item.getDaysago());
            uname.setText(item.getFullname());
            added_by=item.getUid();
            uid=item.getUid();
            if (PrefManager.isLogin(mContext)) {
                uid = Integer.parseInt(PrefManager.getLoginDetail(mContext, "id"));
                id = item.getId();
            }
            Glide.with(mContext)
                    .load(userImages)
                    .apply(Config.options_avatar)
                    .into(userImage);


            Glide.with(mContext)
                    .load(url)
                    .apply(Config.options_image)
                    .into(imageView);
            //imageView.setImageResource(item.image);

            // relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
            //userImage

            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity fragment = new ProfileActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                }
            });
            likeButton.setUnlikeDrawableRes(R.drawable.like);
            likeButton.setLikeDrawableRes(R.drawable.like_un);
            if(PrefManager.getLoginDetail(mContext,"id")==null){
                likeButton.setEnabled(false);
            }
            if((item.getIsliked())==1){
                likeButton.setLiked(true);
                tv_tlike.setTextColor(Color.RED);
            }else{
                likeButton.setLiked(false);
                tv_tlike.setTextColor(Color.BLACK);
            }
            if (PrefManager.isLogin(mContext)){
                likeButton.setEnabled(true);
            }
            else {
                 likeButton.setEnabled(false);
            }
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    int newlike = (int) Integer.parseInt(tv_tlike.getText().toString())+1;
                    tv_tlike.setTextColor(Color.RED);
                    tv_tlike.setText(String.valueOf(newlike));
                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype=image";
                    Log.e(Config.TAG,url);
                    function.executeUrl(mContext,"get",url,null);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int newlike = (int) Integer.parseInt(tv_tlike.getText().toString())-1;
                    tv_tlike.setTextColor(Color.BLACK);
                    tv_tlike.setText(String.valueOf(newlike));
                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype=image";
                    Log.e(Config.TAG,url);
                    function.executeUrl(mContext,"get",url,null);
                }
            });
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, final int position) {
        Vholder.setData(mValues.get(position));
        Vholder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetail fragment = new ImageDetail();
                Bundle args = new Bundle();
                args.putString("id", String.valueOf(mValues.get(position).getId()));
                args.putString("type", "image");
                args.putString("uid", String.valueOf(mValues.get(position).getUid()));
                function.loadFragment(mContext,fragment,args);
            }
        });
        if (PrefManager.isLogin(mContext)){
                Vholder.likeButton.setEnabled(true);
        }
        else {
               Vholder.likeButton.setEnabled(false);
        }
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}