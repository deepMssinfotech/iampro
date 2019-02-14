package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 30/01/19.
 */

import android.content.Intent;
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Img_Video_Details extends RecyclerView.Adapter<Img_Video_Details.ViewHolder> {
    ArrayList<ImageDetailModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    public Img_Video_Details(Context context, ArrayList<ImageDetailModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView fullname,udate,tv_comments,tv_totallike,name,category;
        ImageView imageView_user,imageView_icon,iv_comments,image;
         VideoView videoView;
        RatingBar ratingBar;
        ImageDetailModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            fullname = v.findViewById(R.id.fullname);
            udate =v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            //tv_comments
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            name=v.findViewById(R.id.name);
            category=v.findViewById(R.id.category);

            ratingBar=v.findViewById(R.id.ratingBar);

            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            image=v.findViewById(R.id.image);

            imageView_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(mContext, "Image clicked", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext,ProfileActivity.class);
                    intent.putExtra("uid",uid);
                    mContext.startActivity(intent);
                }
            });
           videoView=v.findViewById(R.id.video);
        }
        public void setData(ImageDetailModel item) {
            this.item = item;

            //TextView fullname,udate,tv_comments,tv_totallike,name,category;
            //ImageView imageView_user,imageView_icon,iv_comments,image;
            //RatingBar ratingBar;

             fullname.setText(item.getFullname());
            udate.setText(String.valueOf(item.getUdatee()));
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));
             name.setText(item.getName());
              category.setText(String.valueOf(item.getCategory()));

            String avatar=item.getAvatar();
            String images=item.getImage();


            ratingBar.setRating(item.getRating());

             uid=item.getUid();

            Glide.with(mContext)
                    .load(avatar)
                    .apply(Config.options_avatar)
                    .into(imageView_user);

           if(item.getType().equalsIgnoreCase("image")) {
               videoView.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
               Glide.with(mContext)
                       .load(images)
                       .apply(new RequestOptions()
                               .centerCrop()
                               .fitCenter())
                       .into(image);

               Glide.with(mContext)
                       .load(R.drawable.image_icon)
                       .apply(Config.options_avatar)
                       .into(imageView_icon);


               //imageView_icon.setImageResource(R.drawable.image_icon);
           }
           else if(item.getType().equalsIgnoreCase("video")) {
               image.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

               videoView.setVideoPath(item.getImage());
               //videoView.start();

               Glide.with(mContext)
                       .load(R.drawable.video_icon)
                       .apply(Config.options_video)
                       .into(imageView_icon);

               //imageView_icon.setImageResource(R.drawable.video_icon);
           }
               imageView_user.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(mContext, "uid:" + uid, Toast.LENGTH_LONG).show();

                       Intent intent = new Intent(mContext, ProfileActivity.class);
                       intent.putExtra("uid", String.valueOf(uid));
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    public Img_Video_Details.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.img_video_detail, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder,int position) {
        Vholder.setData(mValues.get(position));
        //sendrating(float rating,int uid,int id)
        final int uidv=mValues.get(position).getUid();
        //final int idv=mValues.get(position).
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(ImageDetailModel item);
    }


    public void sendrating(float rating,int uid,int id){
        String urlv="https://www.iampro.co/api/app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=image&total_rate="+rating;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlv,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Prod_detaili_profile",""+response);
                        try{
                            String status=response.optString("status");
                            String msgv=response.optString("msg");
                            if(status.equalsIgnoreCase("success")) {
                                Toast.makeText(mContext,""+msgv,Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(mContext,""+msgv,Toast.LENGTH_LONG).show();
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
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }
}
