package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 15/01/19.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.user.AddImageActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class SectionProductAdapter extends RecyclerView.Adapter<SectionProductAdapter.SingleItemRowHolder>  {
    private ArrayList<MyImageModel> itemsList;
    private Context mContext;
    //private String uid,id;
    //ArrayList<MyImageModel> mValues;
    HashSet<String> heading_name;
    protected MyImageAdapter.ItemListener mListener;
    TreeMap<String,String> item_type;
    String status="";
    public SectionProductAdapter(Context context, ArrayList<MyImageModel> itemsList,TreeMap<String,String> item_type)  {
        this.itemsList=itemsList;
        this.mContext=context;
        this.item_type=item_type;
    }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //if(item_type.get("loadmore").equalsIgnoreCase("loadmore")){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_row, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;

        /* }else {
             View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_row, null);
             SingleItemRowHolder mh = new SingleItemRowHolder(v);
             return mh;
          } */
    }
    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {
        MyImageModel singleItem = itemsList.get(i);
        //orgg
        //final String uid=singleItem.getUid();
        final String uid=PrefManager.getLoginDetail(mContext,"id");
        final String id=singleItem.getId();
        final String pid=singleItem.getId();
        final String uidv=itemsList.get(i).getUid();
        holder. category.setText(singleItem .getCategory());
        int my_uid=Integer.parseInt(uidv);
        if(my_uid==0){
            holder.likeButton.setEnabled(false);
        }
        if(Integer.parseInt(itemsList.get(i).getLike_unlike())==1){
            holder.likeButton.setLiked(true);
            holder.tv_totallike.setTextColor(Color.RED);
        }else{
            holder.likeButton.setLiked(false);
            holder.tv_totallike.setTextColor(Color.BLACK);
        }

        if(itemsList.get(i).getMore().equalsIgnoreCase("loadmore")){
            holder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.ALBUM_URL+singleItem.getAvatar())
                    .apply(Config.options_avatar)
                    .into(holder.user_image);
            holder.category.setText(itemsList.get(i).getFullname());
            holder.ratingBar.setRating(Float.parseFloat(itemsList.get(i).getRating()));
        }
        if(singleItem.getRating()!="NAN" || singleItem.getRating().length()>0 || !(singleItem.getRating().equalsIgnoreCase("NAN")) || singleItem.getRating()!="" || !singleItem.getRating().equalsIgnoreCase("") || !singleItem.getRating().isEmpty())
            holder.ratingBar.setRating(Float.parseFloat(String.valueOf(singleItem.getRating())));

        holder.tv_name.setText(singleItem.getName());
        holder.udate.setText(singleItem.getUdate());
        holder.tv_comments.setText(String.valueOf(singleItem .getComments()));
        if(itemsList.get(i).getMore()!=null && itemsList.get(i).getMore().equalsIgnoreCase("loadmore")){
            //holder.btnMore.setVisibility(View.GONE);
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_edit.setVisibility(View.GONE);
        }
        holder.tv_totallike.setText(String.valueOf(singleItem .getTotallike()));
        holder.likeButton.setUnlikeDrawableRes(R.drawable.like);
        holder.likeButton.setLikeDrawableRes(R.drawable.like_un);
        holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("type","image");
                intent.putExtra("id",id);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike =Integer.parseInt(holder.tv_totallike.getText().toString())+1;
                holder.tv_totallike.setTextColor(Color.RED);
                holder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype=image";
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(holder.tv_totallike.getText().toString())-1;
                holder.tv_totallike.setTextColor(Color.BLACK);
                holder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+id+"&ptype=image";
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ProductDetail.class);
                intent.putExtra("id",Integer.parseInt(id));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        Glide.with(mContext)
                .load(Config.ALBUM_URL+singleItem.getImage())
                .apply(Config.options_product)
                .into(holder.imageView);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Delete it!");
                alertDialog.setMessage("Are you sure...");
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.cancel();

                        deleteImage(itemsList.get(i).getId());
                        //Toast.makeText(mContext,"deleted",Toast.LENGTH_LONG).show();

                        itemsList.remove(i);
                        notifyDataSetChanged();

                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, AddImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",itemsList.get(i).getId());
                mContext.startActivity(intent);
            }
        });
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                Toast.makeText(mContext,""+ratingBar.getRating(),Toast.LENGTH_LONG).show();
                sendrating(ratingBar.getRating(),Integer.parseInt(uidv),Integer.parseInt(id));
            }
        });
         if (itemsList.get(i).getUid().equalsIgnoreCase(PrefManager.getLoginDetail(mContext,"id"))){
             holder.iv_delete.setVisibility(View.VISIBLE);
             holder.iv_edit.setVisibility(View.VISIBLE);
         }
         else{
             holder.iv_buy.setVisibility(View.VISIBLE);
             holder.iv_delete.setVisibility(View.GONE);
             holder.iv_edit.setVisibility(View.GONE);
         }
        holder.iv_buy.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v)  {
                String statuss=addToCart(mContext,itemsList.get(i).getId(), "1",String.valueOf(itemsList.get(i).getScost()));
                 Toast.makeText(mContext,""+statuss,Toast.LENGTH_LONG).show();
                if (statuss.equalsIgnoreCase("success")) {

                    CartActivity fragment = new CartActivity();
                    function.loadFragment(mContext, fragment, null);
                }
                 //function.addtocart(mContext,itemsList.get(i).getId(), "1",String.valueOf(itemsList.get(i).getScost()));
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

        //orginal
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,iv_delete,iv_edit,iv_buy;
        VideoView videoView;
        TextView tv_name,category,udate,tv_comments,tv_totallike,detail_name;
        RatingBar ratingBar;
        LinearLayout ll_showhide,ll_comment;
        MyImageModel item;
        LikeButton likeButton;
        public SingleItemRowHolder(View view) {
            super(view);
          /*  this.likelayout = view.findViewById(R.id.likelayout);
            this.tvTitle =view.findViewById(R.id.tvTitle);
            this.itemImage =  view.findViewById(R.id.itemImage);
            this.totallike=view.findViewById(R.id.tv_totallike);
            this.comments=view.findViewById(R.id.tv_comments);
            this.daysago=view.findViewById(R.id.tv_daysago);
            this.user_image=view.findViewById(R.id.user_image);
            this.user_name=view.findViewById(R.id.tv_user_name); */

            //orgg
            imageView=view.findViewById(R.id.imageView);
            tv_name=view.findViewById(R.id.tv_name);
            imageView_user=view.findViewById(R.id.imageView_user);
            imageView_icon=view.findViewById(R.id.imageView_icon);
            ll_comment=view.findViewById(R.id.ll_comment);
            iv_buy= view.findViewById(R.id.iv_buy);
            likeButton =view.findViewById(R.id.likeButton);
            iv_comments=view.findViewById(R.id.iv_comments);
            iv_favourite=view.findViewById(R.id.iv_favourite);


            image=view.findViewById(R.id.imageView);
            videoView=view.findViewById(R.id.videoView);
            ratingBar=view.findViewById(R.id.ratingBar);
            udate=view.findViewById(R.id.udate);
            tv_comments=view.findViewById(R.id.tv_comments);
            tv_totallike=view.findViewById(R.id.tv_totallike);
            user_image=view.findViewById(R.id.user_image);
            ll_showhide=view.findViewById(R.id.ll_showhide);
            category=view.findViewById(R.id.category);
            detail_name=view.findViewById(R.id.detail_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //        Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void sendrating(float rating,int uid,int id){
        String urlv=Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=image&total_rate="+rating;

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
    public void deleteImage(String pid){
        String url=Config.AJAX_URL+ "profile.php?type=deleteAlbemimage&id="+Integer.parseInt(pid);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status=jsonObject.optString("status");
                    String msg=jsonObject.getString("msg");
                    if(status.equalsIgnoreCase("success")){
                        Toast.makeText(mContext,"Deleted successfully"+" "+msg,Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException ex){
                    Toast.makeText(mContext,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

     private String addToCart(Context context,String pid,String qty,String price){
         String url =Config.API_URL+ "cart.php?type=addtocart&p_type=product&pid="+pid+"&qty="+qty+"&price="+price+"&uid="+ PrefManager.getLoginDetail(context,"id") +"&ip_address="+ Config.IP_ADDRESS;

         RequestQueue queue = Volley.newRequestQueue(context);
         StringRequest sr = new StringRequest(Request.Method.GET,"http://api.someservice.com/post/comment", new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject jsonObject = new JSONObject(response);
                      String statusv=jsonObject.optString("status");
                       String msg=jsonObject.optString("msg");
                     status=statusv;
                        Toast.makeText(mContext,""+msg,Toast.LENGTH_LONG).show();
                 }
                  catch(JSONException je){
                       Toast.makeText(mContext,""+je.getMessage(),Toast.LENGTH_LONG).show();
                  }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {

             }
         }){
             @Override
             protected Map<String,String> getParams(){
                 Map<String,String> params = new HashMap<String, String>();
                  params.put("type","addtocart");
                 params.put("p_type","product");

                 return params;
             }

             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 Map<String,String> params = new HashMap<String, String>();
                 params.put("Content-Type","application/x-www-form-urlencoded");
                 return params;
             }
         };
         queue.add(sr);
      return status;
    }
}
