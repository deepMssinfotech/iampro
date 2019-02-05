package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 21/01/19.
 */

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
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

import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProvideAdapter extends RecyclerView.Adapter<ProvideAdapter.ViewHolder> {
    ArrayList<DataModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    public ProvideAdapter(Context context, ArrayList<DataModel> values, ItemListener itemListener) {
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
        DataModel item;
        ImageView ivLike;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            ivLike=v.findViewById(R.id.ivLike);
            tv_tlike=v.findViewById(R.id.tv_totallike);
            //tv_comments
            tv_comments=v.findViewById(R.id.tv_comments);
            iv_comments=v.findViewById(R.id.iv_comments);
            tv_daysago=v.findViewById(R.id.tv_daysago);
            tv_sprice=v.findViewById(R.id.tv_sprice);
            tv_pprice=v.findViewById(R.id.tv_sprice);

            ratingBar=v.findViewById(R.id.ratingBar);

            uname=v.findViewById(R.id.uname);
            userImage=v.findViewById(R.id.user_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Provide clicked"+item.getName()+"\n"+item.getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext,ProvideDetailActivity.class);
                    intent.putExtra("pid",String.valueOf(item.getPid()));
                    intent.putExtra("uid",String.valueOf(item.getUid()));
                    mContext.startActivity(intent);
                }
            });
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeProvide(item.getPid(),String.valueOf(item.getUid()));
                }
            });
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    rateMe(item.getPid(),String.valueOf(item.getUid()),rating);
                }
            });
            tv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, CommentActivity.class);
                    intent.putExtra("id",String.valueOf(item.getUid()));
                    mContext.startActivity(intent);
                }
            });
            iv_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, CommentActivity.class);
                    intent.putExtra("id",String.valueOf(item.getUid()));
                    mContext.startActivity(intent);
                }
            });
        }
        public void likeProvide(String id,String uid){
            String url="https://www.iampro.co/api/app_service.php?type=like_me&id="+id+"&uid="+uid+"&ptype=provide";
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
                                Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                                 String update_status=response.optString("update_status");
                                   String totallike=response.optString("totallike");
                                if ((update_status.equalsIgnoreCase("1") || update_status=="1") && (status=="success" || status.equalsIgnoreCase("success"))){
                                    tv_tlike.setTextColor(Color.RED);
                                    //ivLike.setBackgroundColor(Color.RED);
                                    tv_tlike.setText(totallike);
                                }
                                else {
                                    tv_tlike.setTextColor(Color.BLACK);
                                    tv_tlike.setText(totallike);
                                }
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
        public void rateMe(String id,String uid,float rating){
            String url="https://www.iampro.co/api/app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=provide&total_rate="+rating;

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
            if(String.valueOf(item.getsCost())!=null || !String.valueOf(item.getsCost()).equalsIgnoreCase(null)) {
                tv_sprice.setVisibility(View.VISIBLE);
                tv_sprice.setText(String.valueOf(String.valueOf(item.getsCost())));
            }
            if(String.valueOf(item.getpCost())!=null || !String.valueOf(item.getpCost()).equalsIgnoreCase(null) || item.getpCost()!=0){
                tv_pprice.setVisibility(View.VISIBLE);
                tv_pprice.setText(String.valueOf(item.getpCost()));
            }

            tv_daysago.setVisibility(View.VISIBLE);
            tv_daysago.setText(item.getDaysago());
            uname.setText(item.getFullname());

            uid=item.getUid();

            //tv_daysago.setText();
            Glide.with(mContext)
                    .load(userImages)
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(userImage);

         /*   RequestOptions options=new RequestOptions();
            options.centerCrop().placeholder(mContext.getResources().getDrawable(R.drawable.user_placeholder));
            Glide.with(mContext)
                    .load(userImages)
                    .apply(options)
                    .into(userImage); */

            Glide.with(mContext)
                    .load(url)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .fitCenter())
                    .into(imageView);
            //imageView.setImageResource(item.image);

            // relativeLayout.setBackgroundColor(Color.parseColor("#000000"));
            //userImage
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"uid:"+uid,Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("uid",String.valueOf(uid));
                    mContext.startActivity(intent);
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
    public ProvideAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.provide_view_item, parent, false);
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
        void onItemClick(DataModel item);
    }
}