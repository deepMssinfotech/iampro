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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.AddImageActivity;
import com.mssinfotech.iampro.co.user.AddProductActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import es.dmoral.toasty.Toasty;

public class SectionImageVideoAdapter extends RecyclerView.Adapter<SectionImageVideoAdapter.SingleItemRowHolder>  {
    private ArrayList<MyImageModel> itemsList;
    private Context mContext;
    //private String uid,id;
    //ArrayList<MyImageModel> mValues;
    HashSet<String> heading_name;
    protected MyImageAdapter.ItemListener mListener;
     TreeMap<String,String> item_type;
     String type=null;
    String svalue="error";
    public SectionImageVideoAdapter(Context context, ArrayList<MyImageModel> itemsList,TreeMap<String,String> item_type)  {
        this.itemsList=itemsList;
        this.mContext=context;
        this.item_type=item_type;
    }
     public SectionImageVideoAdapter(Context context, ArrayList<MyImageModel> itemsList,TreeMap<String,String> item_type,String type)  {
        this.itemsList=itemsList;
        this.mContext=context;
        this.item_type=item_type;
        this.type=type;
     }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //Log.e("asdasdas",this.type);
         //if(item_type.get("loadmore").equalsIgnoreCase("loadmore")){
           if(type.equalsIgnoreCase("product")){
               View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_row, null);
               SingleItemRowHolder mh = new SingleItemRowHolder(v);
               return mh;
           }
           else  if(type.equalsIgnoreCase("provide")){
               View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_provide_row, null);
               SingleItemRowHolder mh = new SingleItemRowHolder(v);
               return mh;
           }
           else  if(type.equalsIgnoreCase("demand")){
               View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_demand_row, null);
               SingleItemRowHolder mh = new SingleItemRowHolder(v);
               return mh;
           }
            else{
               View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_row, null);
               SingleItemRowHolder mh = new SingleItemRowHolder(v);
               return mh;
           }
       /* View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_row, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh; */
    }
    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final int i=position;
        MyImageModel singleItem = itemsList.get(i);
        Log.d("onBind_resp",""+itemsList.size());
        //orgg
        //final String uid=singleItem.getUid();
        final String uid=PrefManager.getLoginDetail(mContext,"id");
        final String id=singleItem.getId();
        final String pid=singleItem.getId();
        final String uidv=itemsList.get(i).getUid();

        holder. category.setText(itemsList.get(i).getName());
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
        if(PrefManager.isLogin(mContext)) {
            holder.likeButton.setEnabled(true);
        }
        else {
            holder.likeButton.setEnabled(false);
            //holder.ratingBar.setEnabled(false);
        }
       /* if(PrefManager.isLogin(mContext)){
            holder.likeButton.setEnabled(true);
            //holder.favButton.setEnabled(true);
        }
        else {
            holder.likeButton.setEnabled(false);
            //holder.favButton.setEnabled(false);
        } */
        //if(itemsList.get(i).getMore().equalsIgnoreCase("loadmore")){
            //holder.user_image.setVisibility(View.VISIBLE);
         Glide.with(mContext)
                 .load(Config.ALBUM_URL+singleItem.getAvatar())
                 .apply(Config.options_avatar)
                 .into(holder.user_image);
         holder.category.setText(itemsList.get(i).getFullname());
             //holder.ratingBar.setRating(Float.parseFloat(itemsList.get(i).getRating()));
         //}
       //if(singleItem.getRating()!="NAN" || singleItem.getRating().length()>0 || !(singleItem.getRating().equalsIgnoreCase("NAN")) || singleItem.getRating()!="" || !singleItem.getRating().equalsIgnoreCase("")
        // || !singleItem.getRating().isEmpty())
        try {
            holder.ratingBar.setRating(Float.parseFloat(String.valueOf(singleItem.getRating())));
        }
        catch(Exception e){
            holder.ratingBar.setRating(0);
        }
        holder.tv_name.setText(singleItem.getName());
        holder.category.setText(singleItem.getFullname());
        holder.udate.setText(singleItem.getUdate());
        holder.tv_comments.setText(String.valueOf(singleItem .getComments()));

        holder.tv_totallike.setText(String.valueOf(singleItem .getTotallike()));
        //holder.likeButton.setUnlikeDrawableRes(R.drawable.like);
        holder.likeButton.setUnlikeDrawable(mContext.getResources().getDrawable(R.drawable.like));
        //holder.likeButton.setLikeDrawableRes(R.drawable.like_un);
        holder.likeButton.setLikeDrawable(mContext.getResources().getDrawable(R.drawable.like_un));
        holder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(type.equalsIgnoreCase("image")) {
                 Intent intent = new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type", type);
                 intent.putExtra("id", id);

                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
             else if(type.equalsIgnoreCase("video")) {
                 Intent intent = new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type","video");
                 intent.putExtra("id", id);

                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
             else if(type.equalsIgnoreCase("product")) {
                 Intent intent = new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type","product");
                 intent.putExtra("id", id);

                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
             else if(type.equalsIgnoreCase("provide")) {
                 Intent intent = new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type","provide");
                 intent.putExtra("id", id);

                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
             else if(type.equalsIgnoreCase("demand")) {
                 Intent intent = new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type", "demand");
                 intent.putExtra("id", id);

                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
             else{
                 Intent intent = new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type", "image");
                 intent.putExtra("id", id);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
             }
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
                if (type.equalsIgnoreCase("product")){
                    Intent intent=new Intent(mContext, ProductDetail.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pid",String.valueOf(itemsList.get(i).getId()));
                    intent.putExtra("uid",String.valueOf(itemsList.get(i).getUid()));
                    intent.putExtra("type", "product");
                    mContext.startActivity(intent);
                }
                else if (type.equalsIgnoreCase("provide")){
                    Intent intent=new Intent(mContext, ProvideDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pid",String.valueOf(itemsList.get(i).getId()));
                    intent.putExtra("uid",String.valueOf(itemsList.get(i).getUid()));
                    intent.putExtra("type", "provide");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
                else if (type.equalsIgnoreCase("demand")){
                    Intent intent=new Intent(mContext, DemandDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pid",String.valueOf(itemsList.get(i).getId()));
                    intent.putExtra("uid",String.valueOf(itemsList.get(i).getUid()));
                    intent.putExtra("type", "demand");
                    mContext.startActivity(intent);
                }
                else if(type.equalsIgnoreCase("image")){
                    Intent intent = new Intent(mContext, ImageDetail.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "image");
                    intent.putExtra("uid", String.valueOf(itemsList.get(i).getUid()));
                    mContext.startActivity(intent);
                }
                else if(type.equalsIgnoreCase("video")){
                    Intent intent = new Intent(mContext, ImageDetail.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "video");
                    intent.putExtra("uid", String.valueOf(itemsList.get(i).getUid()));
                    mContext.startActivity(intent);
                }

            }
        });

         if(type.equalsIgnoreCase("product")){
            holder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.OTHER_IMAGE_URL+singleItem.getImage())
                    .apply(Config.options_product)
                    .into(holder.imageView);

            Glide.with(mContext)
                    .load(Config.AVATAR_URL+singleItem.getAvatar())
                    .apply(Config.options_product)
                    .into(holder.user_image);

            holder.category.setVisibility(View.VISIBLE);
            holder.tv_sellingprice.setVisibility(View.VISIBLE);
             holder.tv_purchaseprice.setVisibility(View.VISIBLE);
             holder.category.setText(itemsList.get(i).getFullname());
            holder.tv_sellingprice.setText("Rs:"+itemsList.get(i).getScost());
            holder.tv_purchaseprice.setText("Rs:"+itemsList.get(i).getPcost());
        }
         else if(type.equalsIgnoreCase("provide")){
             holder.user_image.setVisibility(View.VISIBLE);
             Glide.with(mContext)
                    .load(Config.OTHER_IMAGE_URL+singleItem.getImage())
                    .apply(Config.options_provide)
                    .into(holder.imageView);

             Glide.with(mContext)
                    .load(Config.AVATAR_URL+singleItem.getAvatar())
                    .apply(Config.options_product)
                    .into(holder.user_image);

            holder.category.setText(itemsList.get(i).getFullname());
            holder.tv_sellingprice.setText("Rs:"+"\t"+itemsList.get(i).getScost());
        }
         else if(type.equalsIgnoreCase("demand")){
             holder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.OTHER_IMAGE_URL+singleItem.getImage())
                    .apply(Config.options_demand)
                    .into(holder.imageView);

            Glide.with(mContext)
                    .load(Config.AVATAR_URL+singleItem.getAvatar())
                    .apply(Config.options_product)
                    .into(holder.user_image);

             holder.category.setText(itemsList.get(i).getFullname());
             holder.tv_sellingprice.setText("Rs:"+"\t"+itemsList.get(i).getScost());

        }
         else if(type.equalsIgnoreCase("video")){
            holder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.V_URL+singleItem.getImage())
                    .apply(Config.options_video)
                    .into(holder.imageView);
            Glide.with(mContext)
                    .load(Config.AVATAR_URL+singleItem.getAvatar())
                    .apply(Config.options_avatar)
                    .into(holder.user_image);

        }
        else{
            holder.user_image.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.ALBUM_URL+singleItem.getImage())
                    .apply(Config.options_image)
                    .into(holder.imageView);
            Glide.with(mContext)
                    .load(Config.AVATAR_URL+singleItem.getAvatar())
                    .apply(Config.options_avatar)
                    .into(holder.user_image);
        }
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
                                intent.putExtra("id",String.valueOf(itemsList.get(position).getId()));
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
                                        deleteImage(itemsList.get(position).getId());
                                        //Toast.makeText(mContext,"deleted"+valc,Toast.LENGTH_LONG).show();
                                        //if (valc.equalsIgnoreCase("success")){
                                        itemsList.remove(position);
                                        notifyDataSetChanged();myDialog.dismiss();
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
        /*holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Delete it!");
                alertDialog.setMessage("Are you sure...");
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dialog.cancel();
                        String valc=deleteImage(itemsList.get(position).getId());
                         Toast.makeText(mContext,"deleted"+valc,Toast.LENGTH_LONG).show();
                        //if (valc.equalsIgnoreCase("success")){
                        itemsList.remove(position);
                        notifyDataSetChanged(); }
                    //}
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
        });*/
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
               //Toast.makeText(mContext,""+ratingBar.getRating(),Toast.LENGTH_LONG).show();
                sendrating(ratingBar.getRating(),Integer.parseInt(uidv),Integer.parseInt(id));
            }
        });

        holder.user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*ProfileActivity fragment = new ProfileActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(String.valueOf(itemsList.get(i).getUid())));
                    function.loadFragment(mContext,fragment,args); */

                    Intent intent1=new Intent(mContext,ProfileActivity.class);
                    intent1.putExtra("uid",String.valueOf(itemsList.get(i).getUid()));
                    mContext.startActivity(intent1);
                }
            });
      //  }

        if(PrefManager.isLogin(mContext)){
            if(!PrefManager.getLoginDetail(mContext,"id").equalsIgnoreCase(itemsList.get(i).getUid()))
                holder.buttonViewOption.setVisibility(View.GONE);
            else
                holder.buttonViewOption.setVisibility(View.VISIBLE);
        }
        else{
            holder.buttonViewOption.setVisibility(View.VISIBLE);
        }
        if (itemsList.get(i).getUid().equalsIgnoreCase(PrefManager.getLoginDetail(mContext,"id")) && type.equalsIgnoreCase("Product")){
            holder.buttonViewOption.setVisibility(View.VISIBLE);
        }
        else{
            if (type.equalsIgnoreCase("Product")) {
                holder.iv_buy.setVisibility(View.VISIBLE);
                holder.buttonViewOption.setVisibility(View.GONE);
            }
        }
        if (type.equalsIgnoreCase("Product")) {
            holder.iv_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     if (!PrefManager.isLogin(mContext)){
                          Toast.makeText(mContext,""+"First Login and try again...",Toast.LENGTH_LONG).show();
                            return;
                     }
                     else {
                         function.addtocart(mContext, itemsList.get(i).getId(), "1", String.valueOf(itemsList.get(i).getScost()));
                         CartActivity fragment = new CartActivity();
                         function.loadFragment(mContext, fragment, null);
                     }
                }
            });
        }
        if (itemsList.get(i).getUid().equalsIgnoreCase(PrefManager.getLoginDetail(mContext,"id")) && (type.equalsIgnoreCase("Provide") || type.equalsIgnoreCase("Demand"))) {
            holder.buttonViewOption.setVisibility(View.VISIBLE);
        }
        else{
            if (type.equalsIgnoreCase("Provide") || type.equalsIgnoreCase("Demand")) {
                holder.favButton.setVisibility(View.VISIBLE);
                holder.buttonViewOption.setVisibility(View.INVISIBLE);
                if (PrefManager.isLogin(mContext)){
                    holder.favButton.setEnabled(true);
                    holder.likeButton.setEnabled(true);
                }
                else{
                    holder.favButton.setEnabled(false);
                    holder.likeButton.setEnabled(false);
                }
            }
        }
          if (type.equalsIgnoreCase("Provide")){
              if (itemsList.get(position).getIs_featured().equalsIgnoreCase("1")) {
                  holder.favButton.setLiked(true);
              } else {
                  holder.favButton.setLiked(false);
              }
              if (PrefManager.isLogin(mContext)){
                    holder.likeButton.setEnabled(true);
                      holder.favButton.setEnabled(true);
              }
              else {
                       holder.likeButton.setEnabled(false);
                         holder.favButton.setEnabled(false);
              }
              if (PrefManager.isLogin(mContext)){
                  holder.favButton.setEnabled(true);
              }
              else{
                  holder.favButton.setEnabled(false);
              }
              holder.favButton.setOnLikeListener(new OnLikeListener() {
                  @Override
                  public void liked(LikeButton likeButton) {
                      //Toast.makeText(mContext,"Liked",Toast.LENGTH_LONG).show();
                      String url = Config.API_URL + "app_service.php?type=provide_favo&pid=" + String.valueOf(itemsList.get(position).getId()) + "&uid=" +itemsList.get(position).getUid() + "&product_type=" + type;
                      Log.e(Config.TAG, url);
                      function.executeUrl(mContext, "get", url, null);
                  }

                  @Override
                  public void unLiked(LikeButton likeButton) {
                      //Toast.makeText(mContext,"UnLiked",Toast.LENGTH_LONG).show();
                      String url = Config.API_URL + "app_service.php?type=provide_favo&pid=" + String.valueOf(itemsList.get(position).getId()) + "&uid=" +itemsList.get(position).getUid()+ "&product_type=" + type;
                      Log.e(Config.TAG, url);
                      function.executeUrl(mContext, "get", url, null);
                  }
              });
          }
          if (type.equalsIgnoreCase("Demand")){
              if (PrefManager.isLogin(mContext)){
                  holder.favButton.setEnabled(true);
                  holder.likeButton.setEnabled(true);
              }
              else{
                  holder.favButton.setEnabled(false);
                  holder.likeButton.setEnabled(false);
              }
              if (itemsList.get(position).getIs_featured().equalsIgnoreCase("1")) {
                  holder.favButton.setLiked(true);
              } else {
                  holder.favButton.setLiked(false);
              }
              holder.favButton.setOnLikeListener(new OnLikeListener() {
                  @Override
                  public void liked(LikeButton likeButton) {
                      //Toast.makeText(mContext,"Liked",Toast.LENGTH_LONG).show();

                      String url = Config.API_URL + "app_service.php?type=provide_favo&pid=" + String.valueOf(itemsList.get(position).getId()) + "&uid=" +itemsList.get(position).getUid() + "&product_type=" + type;
                      Log.e(Config.TAG, url);
                      function.executeUrl(mContext, "get", url, null);
                  }
                  @Override
                  public void unLiked(LikeButton likeButton) {
                      //Toast.makeText(mContext,"UnLiked",Toast.LENGTH_LONG).show();
                      String url = Config.API_URL + "app_service.php?type=provide_favo&pid=" + String.valueOf(itemsList.get(position).getId()) + "&uid=" +itemsList.get(position).getUid()+ "&product_type=" + type;
                      Log.e(Config.TAG, url);
                      function.executeUrl(mContext, "get", url, null);
                  }
              });
          }
        if(itemsList.get(i).getMore()!=null && itemsList.get(i).getMore().equalsIgnoreCase("loadmore")){
            //holder.btnMore.setVisibility(View.GONE);
            holder.buttonViewOption.setVisibility(View.GONE);
            if (type.equalsIgnoreCase("provide") || type.equalsIgnoreCase("demand")){
                if (PrefManager.isLogin(mContext)){
                    holder.favButton.setEnabled(true);
                    holder.likeButton.setEnabled(true);
                    holder.ratingBar.setFocusable(true);
                }
                else{
                    holder.favButton.setEnabled(false);
                    holder.likeButton.setEnabled(false);
                    holder.ratingBar.setFocusable(false);
                }
            }
            else if (type.equalsIgnoreCase("product")){
                if (PrefManager.isLogin(mContext)){
                    holder.favButton.setEnabled(true);
                    holder.likeButton.setEnabled(true);
                    holder.ratingBar.setFocusable(true);
                    holder.ratingBar.setIsIndicator(false);
                    holder.iv_buy.setEnabled(true);
                }
                else{
                    holder.favButton.setEnabled(false);
                    holder.likeButton.setEnabled(false);
                    holder.ratingBar.setFocusable(false);
                    holder.ratingBar.setIsIndicator(true);
                    holder.iv_buy.setEnabled(false);
                }
            }
            else {
                if(PrefManager.isLogin(mContext)) {
                    holder.likeButton.setEnabled(true);
                    holder.ratingBar.setFocusable(true);
                    holder.ratingBar.setIsIndicator(false);
                    //holder.ratingBar.setClickable(true);
                    holder.buttonViewOption.setVisibility(View.VISIBLE);
                }
                else{
                    holder.likeButton.setEnabled(false);
                    holder.ratingBar.setFocusable(false);
                    holder.ratingBar.setIsIndicator(true);
                    holder.buttonViewOption.setVisibility(View.GONE);
                }
            }
        }
        if (PrefManager.isLogin(mContext)) {
            holder.likeButton.setEnabled(true);
            holder.ratingBar.setFocusable(true);
            holder.ratingBar.setIsIndicator(false);
            //holder.ratingBar.setClickable(true);
            holder.buttonViewOption.setVisibility(View.VISIBLE);
        }
        else {
            holder.likeButton.setEnabled(false);
            holder.ratingBar.setFocusable(false);
            holder.ratingBar.setIsIndicator(true);
            holder.buttonViewOption.setVisibility(View.GONE);
        }
        }
    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView tvTitle,totallike,comments,daysago,user_name;
        protected ImageView itemImage,iv_buy;
        protected de.hdodenhof.circleimageview.CircleImageView btnMore,user_image;
        protected LinearLayout likelayout;
        //this.btnMore= view.findViewById(R.id.btnMore);

        //orginal
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,buttonViewOption;
        VideoView videoView;
        TextView tv_name,category,udate,tv_comments,tv_totallike,detail_name,tv_purchaseprice,tv_sellingprice;
        RatingBar ratingBar;
        LinearLayout ll_showhide,ll_comment;
        MyImageModel item;
        LikeButton likeButton,favButton;
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

            buttonViewOption = view.findViewById(R.id.textViewOptions);

            tv_name=view.findViewById(R.id.tv_name);
            imageView_user=view.findViewById(R.id.imageView_user);
            favButton=view.findViewById(R.id.favButton);
            user_image= view.findViewById(R.id.imageView_user);

            ll_comment=view.findViewById(R.id.ll_comment);
            likeButton =view.findViewById(R.id.likeButton);
            iv_comments=view.findViewById(R.id.iv_comments);
            iv_favourite=view.findViewById(R.id.iv_favourite);

            user_image=view.findViewById(R.id.user_image);
            image=view.findViewById(R.id.imageView);
            videoView=view.findViewById(R.id.videoView);
            ratingBar=view.findViewById(R.id.ratingBar);
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

            if (type.equalsIgnoreCase("Product")){
                iv_buy=view.findViewById(R.id.iv_buy);
            }
            if (type.equalsIgnoreCase("Provide") || type.equalsIgnoreCase("Demand")){
                favButton=view.findViewById(R.id.favButton);
            }
            imageView_icon=view.findViewById(R.id.imageView_icon);
            if(type.equalsIgnoreCase("product")){
                tv_purchaseprice=view.findViewById(R.id.tv_purchaseprice);
                tv_sellingprice=view.findViewById(R.id.tv_sellingprice);
                //user_image=view.findViewById(R.id.user_image);
            }
            else if(type.equalsIgnoreCase("provide")){
                tv_sellingprice=view.findViewById(R.id.tv_sellingprice);
                //user_image=view.findViewById(R.id.user_image);
            }
            else if(type.equalsIgnoreCase("demand")){
                tv_sellingprice=view.findViewById(R.id.tv_sellingprice);
                //user_image=view.findViewById(R.id.user_image);
            }

        }
    }
    public void sendrating(float rating,int uid,int id){
        String urlv= Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=image&total_rate="+rating;

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
                                //Toast.makeText(mContext,""+msgv,Toast.LENGTH_LONG).show();
                            }
                            else{
                                //Toast.makeText(mContext,""+msgv,Toast.LENGTH_LONG).show();
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
    public String deleteImage(String pid){
        String url=Config.AJAX_URL+ "profile.php?type=deleteAlbemimage&id="+Integer.parseInt(pid);
        RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("delete_reponses",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status=jsonObject.optString("status");
                    String msg=jsonObject.getString("msg");
                    svalue=status;
                    if(status.equalsIgnoreCase("success")){
                        Toast.makeText(mContext,"Deleted successfully"+" "+msg,Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException ex){
                    Toast.makeText(mContext,""+ex.getMessage(),Toast.LENGTH_LONG).show();
                     Log.d("delete_catch",""+ex.getMessage());
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
        return svalue;
    }
}
