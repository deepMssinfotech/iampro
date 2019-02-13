package com.mssinfotech.iampro.co.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
public class AllFeedAdapter extends RecyclerView.Adapter<AllFeedAdapter.ViewHolder> {
    ArrayList<FeedModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public AllFeedAdapter(Context context, ArrayList<FeedModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike,iv_buy;
         VideoView videoView;
         TextView fullname,udate,tv_comments,tv_totallike,detail_name,purchese_cost,selling_cost;
        RatingBar ratingBar;
         LinearLayout ll_showhide;
        FeedModel item;

         int id;
        //int uid;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_buy=v.findViewById(R.id.iv_buy);
            iv_favourite=v.findViewById(R.id.iv_favourite);
            image=v.findViewById(R.id.imageView);
             videoView=v.findViewById(R.id.videoView);
             ratingBar=v.findViewById(R.id.ratingBar);
            ivLike=v.findViewById(R.id.ivLike);
            fullname=v.findViewById(R.id.fullname);
            udate=v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_showhide=v.findViewById(R.id.ll_showhide);
            detail_name=v.findViewById(R.id.detail_name);

            purchese_cost=v.findViewById(R.id.purchese_cost);
            selling_cost=v.findViewById(R.id.selling_cost);
        }
        public void setData(final FeedModel item) {
            this.item = item;
            //final String uid= PrefManager.getLoginDetail(mContext,"id");
            final String uid= String.valueOf(item.getUid());
            final String type=item.getType();
            final String id=String.valueOf(item.getId());
            //final String id=String.valueOf(item.getShareId());
              ratingBar.setRating(Float.parseFloat(String.valueOf(item.getAverage_rating())));
              fullname.setText(item.getFullname());
               udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComment()));
             tv_totallike.setText(String.valueOf(item.getLikes()));
            imageView_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,ProfileActivity.class);
                    intent.putExtra("uid",uid);
                    mContext.startActivity(intent);
                    Toast.makeText(mContext,"uid: "+uid,Toast.LENGTH_LONG).show();
                }
            });
            tv_totallike.setText(String.valueOf(item.getLikes()));
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equalsIgnoreCase("IMAGE")){
                        Intent intent=new Intent(mContext,ImageDetail.class);
                        intent.putExtra("uid",Integer.parseInt(uid));
                        intent.putExtra("id",Integer.parseInt(id));
                        intent.putExtra("type","image");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(mContext,"uid: "+uid+"-"+id,Toast.LENGTH_LONG).show();
                    }
                    else if (type.equalsIgnoreCase("VIDEO")){
                        Intent intent=new Intent(mContext,ImageDetail.class);
                        intent.putExtra("uid",Integer.parseInt(uid));
                        intent.putExtra("id",id);
                        intent.putExtra("type","video");
                        mContext.startActivity(intent);
                    }
                    else if (type.equalsIgnoreCase("DEMAND")){
                        Intent intent=new Intent(mContext, DemandDetail.class);
                        //intent.putExtra("id",String.valueOf(item.getPid()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("pid",String.valueOf(id));
                        intent.putExtra("uid",String.valueOf(uid));
                        mContext.startActivity(intent);
                    }
                    else if (type.equalsIgnoreCase("PROVIDE")){
                        Intent intent=new Intent(mContext,ProvideDetailActivity.class);
                        intent.putExtra("pid",String.valueOf(id));
                        intent.putExtra("uid",String.valueOf(uid));
                        mContext.startActivity(intent);
                    }
                    else if (type.equalsIgnoreCase("PRODUCT")){
                        Intent intent=new Intent(mContext, ProductDetail.class);
                        intent.putExtra("pid",String.valueOf(id));
                        intent.putExtra("uid",String.valueOf(uid));
                        mContext.startActivity(intent);
                    }

                   // Intent intent=new Intent(mContext,ProfileActivity.class);
                    //intent.putExtra("uid",uid);
                   // mContext.startActivity(intent);
                   // Toast.makeText(mContext,"uid: "+uid,Toast.LENGTH_LONG).show();
                }
            });

            Glide.with(mContext)
                    .load(item.getAvatar_path())
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(imageView_user);

            Log.d("image_setdata",""+item.getFimage_path());
            //String type=item.getType();
            if(!type.equalsIgnoreCase("VIDEO")) {
                videoView.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(item.getFimage_path())
                        .apply(Config.options_avatar)
                        .into(image);
            }
            else{
                videoView.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                videoView.setVideoPath(item.getFimage_path());
                //videoView.start();
                Uri video = Uri.parse(item.getFimage_path());
                videoView.setVideoURI(video);

                MediaController mc = new MediaController(mContext);
                videoView.setMediaController(mc);
                videoView.start();
            }
  if (type.equalsIgnoreCase("VIDEO")){
      ll_showhide.setVisibility(View.GONE);
                //imageView_icon.setImageResource(R.drawable.video_icon);
      Glide.with(mContext)
              .load(R.drawable.video_icon)
              .apply(Config.options_product)
              .into(imageView_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.video_icon));

      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.video_icon));
            }
  else if (type.equalsIgnoreCase("IMAGE")){
      ll_showhide.setVisibility(View.GONE);
                //imageView_icon.setImageResource(R.drawable.image_icon);
      Glide.with(mContext)
              .load(R.drawable.image_icon)
              .apply(Config.options_avatar)
              .into(imageView_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.image_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.image_icon));
            }
            else if (type.equalsIgnoreCase("PRODUCT")){
                  ll_showhide.setVisibility(View.VISIBLE);
                //imageView_icon.setImageResource(R.drawable.product_icon);
      Glide.with(mContext)
              .load(R.drawable.product_icon)
              .apply(Config.options_product)
              .into(imageView_icon);
      detail_name.setText(item.getDetail_name());

      purchese_cost.setText("Rs: "+String.valueOf(item.getPurchese_cost()));
      selling_cost.setText("Rs: "+String.valueOf(item.getSelling_cost()));
      iv_buy.setVisibility(View.VISIBLE);
       iv_buy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(mContext,"buy clicked",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(mContext, CartActivity.class);
                intent.putExtra("uid",item.getUid());
                intent.putExtra("pid",item.getId());
                intent.putExtra("id",item.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  mContext.startActivity(intent);
           }
       });
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      //Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.product_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.product_icon));
            }
            else if (type.equalsIgnoreCase("PROVIDE")){
              ll_showhide.setVisibility(View.VISIBLE);
                //imageView_icon.setImageResource(R.drawable.provide_icon);
      Glide.with(mContext)
              .load(R.drawable.provide_icon)
              .apply(Config.options_provide)
              .into(imageView_icon);
               // Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
                 detail_name.setText(item.getDetail_name());
      purchese_cost.setText("");
      selling_cost.setText("Rs: "+String.valueOf(item.getSelling_cost()));
      iv_favourite.setVisibility(View.VISIBLE);
            }
            else if (type.equalsIgnoreCase("DEMAND")){
                  ll_showhide.setVisibility(View.VISIBLE);
                //imageView_icon.setImageResource(R.drawable.demand_icon);
      Glide.with(mContext)
              .load(R.drawable.demand_icon)
              .apply(Config.options_demand)
              .into(imageView_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
      detail_name.setText(item.getDetail_name());
      purchese_cost.setText("");
      selling_cost.setText("Rs: "+String.valueOf(item.getSelling_cost()));
     // Resources resources = mContext.getResources();
      //imageView_icon.setImageDrawable(resources.getDrawable(R.drawable.demand_icon));
      //image.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.demand_icon));
      iv_favourite.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }


    }
    @Override
    public AllFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));
        final String type=mValues.get(position).getType();
        final int uid=mValues.get(position).getUid();
        final int id=mValues.get(position).getId();
        Vholder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                Toast.makeText(mContext,""+ratingBar.getRating(),Toast.LENGTH_LONG).show();
                float ratingb=ratingBar.getRating();
                sendrating(ratingb,uid,id);
            }
        });


    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(FeedModel item);
    }
    public void sendrating(float rating,int uid,int id){
        String urlv="https://www.iampro.co/api/app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=feed&total_rate="+rating;

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

