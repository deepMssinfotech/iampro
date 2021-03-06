package com.mssinfotech.iampro.co.adapter;
/**
 * Created by mssinfotech on 15/01/19.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.AddProductActivity;
import com.mssinfotech.iampro.co.user.AddProvideActivity;
import com.mssinfotech.iampro.co.user.AddVideoActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
public class SectionVideoAdapter extends RecyclerView.Adapter<SectionVideoAdapter.SingleItemRowHolder> {
    private ArrayList<MyImageModel> itemsList;
    private Context mContext;
    //private String uid,id;
    //ArrayList<MyImageModel> mValues;
    HashSet<String> heading_name;
    HashMap<String,String> item_name;
    protected MyImageAdapter.ItemListener mListener;
     public   SectionVideoAdapter(Context context, ArrayList<MyImageModel> itemsList,HashMap<String,String> item_name) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.item_name=item_name;
     }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_row, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }
    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int i) {
        MyImageModel singleItem = itemsList.get(i);
        Log.d("single_item",""+singleItem);
        //orgg
         //String uid,id;
        //final String uid=singleItem .getUid();

        //if (PrefManager.isLogin(mContext))
        final String uid= PrefManager.getLoginDetail(mContext,"id");

        final String id=singleItem .getId();
        final String uidd=singleItem.getUid();


        int my_uid=Integer.parseInt(uidd);
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
        if (PrefManager.isLogin(mContext)){
            holder.likeButton.setEnabled(true);
        }
        else {
            holder.likeButton.setEnabled(false);
        }
        holder.category.setText(singleItem.getCategory());
        holder.tv_name.setText(singleItem.getName());
        if(itemsList.get(i).getMore()!=null && itemsList.get(i).getMore().equalsIgnoreCase("loadmore")){
            //holder.btnMore.setVisibility(View.GONE);
            holder.buttonViewOption.setVisibility(View.GONE);
            holder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.AVATAR_URL+singleItem.getAvatar())
                    .apply(Config.options_video)
                    .into(holder.user_image);
            holder.category.setText(itemsList.get(i).getFullname());
            holder.ratingBar.setRating(Float.parseFloat(itemsList.get(i).getRating()));
            //holder.image.

           Glide.with(mContext)
                    .load(Config.V_URL+itemsList.get(i).getImage())
                    .apply(Config.options_video)
                    .into(holder.videoView);
           //Toast.makeText(mContext,"vimage",Toast.LENGTH_LONG).show();
            Log.d("vimagee_path",Config.V_URL+itemsList.get(i).getV_image());
        }
        else{
            Glide.with(mContext)
                    .load(Config.V_URL+itemsList.get(i).getImage())
                    .apply(Config.options_video)
                    .into(holder.videoView);
            holder.videoView.setVisibility(View.VISIBLE);
             Log.d("v_url_main",""+Config.V_URL+itemsList.get(i).getImage());
        }
       // holder.udate.setText(singleItem.getUdate());
        holder.tv_comments.setText(String.valueOf(singleItem.getComments()));
        holder.tv_totallike.setText(String.valueOf(singleItem.getTotallike()));
        holder.likeButton.setUnlikeDrawableRes(R.drawable.like);
        holder.likeButton.setLikeDrawableRes(R.drawable.like_un);
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike =Integer.parseInt(holder.tv_totallike.getText().toString())+1;
                holder.tv_totallike.setTextColor(Color.RED);
                holder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype=video";
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(holder.tv_totallike.getText().toString())-1;
                holder.tv_totallike.setTextColor(Color.BLACK);
                holder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+id+"&ptype=video";
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
        });


        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                Toast.makeText(mContext,""+ratingBar.getRating(),Toast.LENGTH_LONG).show();
                sendrating(ratingBar.getRating(),Integer.parseInt(uidd),Integer.parseInt(id));
            }
        });

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageDetail.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "video");
                intent.putExtra("uid", uidd);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext)
                .load(Config.V_URL+singleItem.getV_image())
                .apply(Config.options_video)
                .into(holder.videoView);
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog myDialog = new Dialog(mContext);
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.iv_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent=new Intent(mContext, AddProductActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("id",String.valueOf(itemsList.get(i).getId()));
                                mContext.startActivity(intent);
                                break;
                            case R.id.menu_delete:
                                myDialog.setContentView(R.layout.confirm_popup);
                                TextView yes = myDialog.findViewById(R.id.yes);
                                TextView no = myDialog.findViewById(R.id.no);
                                TextView heading = myDialog.findViewById(R.id.heading);
                                TextView detail = myDialog.findViewById(R.id.detail);
                                heading.setText("Delete");
                                detail.setText("Are you sure you want to remove");
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteVideo(itemsList.get(i).getId());
                                        //Toast.makeText(mContext,"deleted",Toast.LENGTH_LONG).show();

                                        itemsList.remove(i);
                                        notifyDataSetChanged();
                                        myDialog.dismiss();
                                    }
                                });
                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myDialog.dismiss();
                                    }
                                });
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog.show();
                                //handle menu1 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    /*
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

                        deleteVideo(itemsList.get(i).getId());
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
                Intent intent=new Intent(mContext, AddVideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",itemsList.get(i).getId());
                mContext.startActivity(intent);
            }
        });
        */
         holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("type","video");
                intent.putExtra("id",String.valueOf(itemsList.get(i).getId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
         holder.user_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 /*ProfileActivity fragment = new ProfileActivity();
                 Bundle args = new Bundle();
                 args.putString("uid", String.valueOf(uidd));
                 function.loadFragment(mContext,fragment,args);*/

                 Intent intent1=new Intent(mContext,ProfileActivity.class);
                 intent1.putExtra("uid",String.valueOf(uidd));
                 mContext.startActivity(intent1);

             }
         });
        if(PrefManager.isLogin(mContext)){
            if(!PrefManager.getLoginDetail(mContext,"id").equalsIgnoreCase(itemsList.get(i).getUid().toString()))
                holder.buttonViewOption.setVisibility(View.GONE);
            else
                holder.buttonViewOption.setVisibility(View.VISIBLE);
        }
        else{
            holder.buttonViewOption.setVisibility(View.GONE);
        }
        if(itemsList.get(i).getMore()==null) {
            Glide.with(mContext)
                    .load(Config.V_URL + itemsList.get(i).getImage())
                    .apply(Config.options_video)
                    .into(holder.videoView);
            holder.videoView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle,totallike,comments,daysago,user_name;
        protected ImageView itemImage;
        public de.hdodenhof.circleimageview.CircleImageView btnMore,user_image;
        protected LinearLayout likelayout;
        //this.btnMore= view.findViewById(R.id.btnMore);
        //orginal
        ImageView  imageView_user,imageView_icon,iv_comments,image,iv_favourite,videoView,imageView,buttonViewOption;
        //VideoView videoView;
        TextView tv_name,category,udate,tv_comments,tv_totallike,detail_name;
        RatingBar ratingBar;
        LinearLayout ll_showhide,ll_comment;
        MyImageModel item;
        LikeButton likeButton;
        public SingleItemRowHolder(View view) {
            super(view);
            //orgg
           /* imageView=view.findViewById(R.id.imageView);
            tv_name=view.findViewById(R.id.tv_name);
            imageView_user=view.findViewById(R.id.imageView_user);
            imageView_icon=view.findViewById(R.id.imageView_icon);
            iv_comments=view.findViewById(R.id.iv_comments);
            iv_favourite=view.findViewById(R.id.iv_favourite);
            image=view.findViewById(R.id.imageView);
            videoView=view.findViewById(R.id.videoView);
            ratingBar=view.findViewById(R.id.ratingBar);
            ivLike=view.findViewById(R.id.ivLike);
            //udate=v.findViewById(R.id.udate);
            tv_comments=view.findViewById(R.id.tv_comments);
            tv_totallike=view.findViewById(R.id.tv_totallike);
            ll_showhide=view.findViewById(R.id.ll_showhide);
            category=view.findViewById(R.id.category);
            detail_name=view.findViewById(R.id.detail_name); */

            videoView=view.findViewById(R.id.videoView);
            buttonViewOption = view.findViewById(R.id.textViewOptions);
            tv_name=view.findViewById(R.id.tv_name);
            imageView_user=view.findViewById(R.id.imageView_user);
            imageView_icon=view.findViewById(R.id.imageView_icon);
            ll_comment=view.findViewById(R.id.ll_comment);
            iv_comments=view.findViewById(R.id.iv_comments);
            iv_favourite=view.findViewById(R.id.iv_favourite);

            image=view.findViewById(R.id.imageView);
           // videoView=view.findViewById(R.id.videoView);
            user_image=view.findViewById(R.id.user_image);
            ratingBar=view.findViewById(R.id.ratingBar);
            likeButton =view.findViewById(R.id.likeButton);
             udate=view.findViewById(R.id.udate);
            tv_comments=view.findViewById(R.id.tv_comments);
            tv_totallike=view.findViewById(R.id.tv_totallike);
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
        String urlv= Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=video&total_rate="+rating;
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
    public void deleteVideo(String pid){
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
}
