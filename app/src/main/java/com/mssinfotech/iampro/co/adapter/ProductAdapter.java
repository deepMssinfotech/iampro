package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 21/01/19.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    public int uid,id,added_by;

    public ProductAdapter(Context context, ArrayList<DataModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,tv_tlike,tv_comments,tv_daysago,tv_sprice,tv_pprice,uname;
        RatingBar ratingBar;
        public ImageView imageView,iv_comments;
        de.hdodenhof.circleimageview.CircleImageView userImage;
        public RelativeLayout relativeLayout;
        LikeButton likeButton;
        DataModel item;
        ImageView ivLike,iv_buy;
        LinearLayout ll_comments;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            likeButton = v.findViewById(R.id.likeButton);
            tv_tlike=v.findViewById(R.id.tv_totallike);
            //tv_comments
            tv_comments=v.findViewById(R.id.tv_comments);
            iv_buy=v.findViewById(R.id.iv_buy);
            iv_comments=v.findViewById(R.id.iv_comments);
            ll_comments=v.findViewById(R.id.ll_comments);
            tv_daysago=v.findViewById(R.id.tv_daysago);
            tv_sprice=v.findViewById(R.id.tv_sprice);
            tv_pprice=v.findViewById(R.id.tv_pprice);

            ratingBar=v.findViewById(R.id.ratingBar);

            uname=v.findViewById(R.id.uname);
            userImage=v.findViewById(R.id.user_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Product clicked"+item.getName()+"\n"+item.getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext, ProductDetail.class);
                    intent.putExtra("pid",String.valueOf(item.getPid()));
                    intent.putExtra("uid",String.valueOf(item.getUid()));
                    mContext.startActivity(intent);
                }
            });
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    rateMe(item.getPid(),String.valueOf(item.getUid()),rating);
                }
            });
           //tv_comments.setOnClickListener(CommnetOnClickListener);
            //iv_comments.setOnClickListener(CommnetOnClickListener);

        }
        /* private View.OnClickListener CommnetOnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("id",String.valueOf(item.getId()));
                intent.putExtra("type","product");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }; */

        public void rateMe(String id,String uid,float rating){
             String url=Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=product&total_rate="+rating;
             RequestQueue requestQueue = Volley.newRequestQueue(mContext);
             // Initialize a new JsonObjectRequest instance
             JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                     Request.Method.GET,
                     url,
                     null,
                     new Response.Listener<JSONObject>() {
                         @Override
                         public void onResponse(JSONObject response) {
                             // Do something with response
                             //mTextView.setText(response.toString());

                             // Process the JSON
                             try{
                                 String status=response.optString("status");
                                 String msg=response.optString("msg");
                                 String update_status=response.optString("update_status");
                                 String totalrating=response.optString("totalrating");
                                 if (status=="success" || status.equalsIgnoreCase("success")){
                                       ratingBar.setRating(Float.parseFloat(totalrating));
                                 }
                                 else {

                                 }
                                 //Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                             }
                             catch (Exception e){
                                 e.printStackTrace();
                                 Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                             }
                         }
                     },
                     new Response.ErrorListener(){
                         @Override
                         public void onErrorResponse(VolleyError error){
                             // Do something when error occurred
                             Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                         }
                     }
             );
             // Add JsonObjectRequest to the RequestQueue
             requestQueue.add(jsonObjectRequest);
         }
        public void setData(DataModel item) {
            this.item = item;
            textView.setText(item.getName());
            //textView.setBackgroundColor(Color.BLUE);
            String url=item.getImage();
            String userImages=item.getUserImage();
            //Toast.makeText(mContext,"image:"+url,Toast.LENGTH_LONG).show();
            //Log.d("url_adapter",url);
            //Toast.makeText(mContext, ""+url, Toast.LENGTH_SHORT).show();
            tv_tlike.setText(String.valueOf(item.getTotallike()));
            tv_comments.setText(String.valueOf(item.getComments()));
            ratingBar.setRating(item.getRating());

            id = item.getId();
            uid=item.getUid();
            if (PrefManager.isLogin(mContext))
            uid = Integer.parseInt(PrefManager.getLoginDetail(mContext,"id"));
            added_by = item.getUid();

          /*  if(String.valueOf(item.getsCost())!=null || !String.valueOf(item.getsCost()).equalsIgnoreCase(null)) {
                tv_sprice.setVisibility(View.VISIBLE);
                tv_sprice.setText(String.valueOf(String.valueOf(item.getsCost())));
            }
            if(String.valueOf(item.getpCost())!=null || !String.valueOf(item.getpCost()).equalsIgnoreCase(null) || item.getpCost()!=0){
                tv_pprice.setVisibility(View.VISIBLE);
                tv_pprice.setText(String.valueOf(item.getpCost()));
            } */

            tv_daysago.setVisibility(View.VISIBLE);
            tv_daysago.setText(item.getDaysago());
            uname.setText(item.getFullname());

            //tv_daysago.setText();
            Glide.with(mContext)
                    .load(userImages)
                    .apply(Config.options_avatar)
                    .into(userImage);

         /*   RequestOptions options=new RequestOptions();
            options.centerCrop().placeholder(mContext.getResources().getDrawable(R.drawable.user_placeholder));
            Glide.with(mContext)
                    .load(userImages)
                    .apply(options)
                    .into(userImage); */

            Glide.with(mContext)
                    .load(url)
                    .apply(Config.options_product)
                    .into(imageView);
            //imageView.setImageResource(item.image);

            // relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
            //userImage
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*ProfileActivity fragment = new ProfileActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(added_by));
                    function.loadFragment(mContext,fragment,args); */

                    Intent intent1=new Intent(mContext,ProfileActivity.class);
                    intent1.putExtra("uid",String.valueOf(added_by));
                    mContext.startActivity(intent1);

                }
            });
            likeButton.setUnlikeDrawableRes(R.drawable.like);
            likeButton.setLikeDrawableRes(R.drawable.like_un);
            if (PrefManager.getLoginDetail(mContext, "id") == null) {
                likeButton.setEnabled(false);
            }
            if ((item.getIsliked()) == 1) {
                likeButton.setLiked(true);
                tv_tlike.setTextColor(Color.RED);
            } else {
                likeButton.setLiked(false);
                tv_tlike.setTextColor(Color.BLACK);
            }
            if (PrefManager.isLogin(mContext)){
                likeButton.setEnabled(true);
                //iv_buy.setEnabled(true);
            }
            else {
                likeButton.setEnabled(false);
                //iv_buy.setEnabled(false);
            }
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    int newlike = (int) Integer.parseInt(tv_tlike.getText().toString()) + 1;
                    tv_tlike.setTextColor(Color.RED);
                    tv_tlike.setText(String.valueOf(newlike));
                    String url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(id) + "&uid=" + uid + "&ptype=product";
                    Log.e(Config.TAG, url);
                    function.executeUrl(mContext, "get", url, null);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int newlike = (int) Integer.parseInt(tv_tlike.getText().toString()) - 1;
                    tv_tlike.setTextColor(Color.BLACK);
                    tv_tlike.setText(String.valueOf(newlike));
                    String url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(id) + "&uid=" + uid + "&ptype=product";
                    Log.e(Config.TAG, url);
                    function.executeUrl(mContext, "get", url, null);
                }
            });
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
                //ivLike.onItemClick(item);
            }
        }
    }
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_view_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, final int position) {
        Vholder.setData(mValues.get(position));
        if (PrefManager.isLogin(mContext)) {
            Vholder.likeButton.setEnabled(true);
            Vholder.ratingBar.setFocusable(true);
            Vholder.ratingBar.setIsIndicator(false);
            //holder.ratingBar.setClickable(true);
        }
        else {
            Vholder.likeButton.setEnabled(false);
            Vholder.ratingBar.setFocusable(false);
            Vholder.ratingBar.setIsIndicator(true);
            //holder.ratingBar.setClickable(false);
        }
         Vholder.ll_comments.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(mContext, CommentActivity.class);
                 intent.putExtra("id",String.valueOf(mValues.get(position).getPid()));
                 intent.putExtra("type","product");
                 intent.putExtra("uid",mValues.get(position).getUid());
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
         });
        Vholder.tv_pprice.setVisibility(View.VISIBLE);
         Vholder.tv_sprice.setVisibility(View.VISIBLE);
          Vholder.tv_pprice.setText(String.valueOf("Rs: "+mValues.get(position).getpCost()));
        Vholder.tv_sprice.setText(String.valueOf("Rs: "+mValues.get(position).getsCost()));
        Vholder.iv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CartActivity c=
                if (!PrefManager.isLogin(mContext)){
                     Toast.makeText(mContext,""+"First Login and Try again...",Toast.LENGTH_LONG).show();
                     return;
                }else {
                    function.addtocart(mContext, mValues.get(position).getPid(), "1", String.valueOf(mValues.get(position).getsCost()));
                    CartActivity fragment = new CartActivity();
                    function.loadFragment(mContext, fragment, null);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(DataModel item);
    }
}
