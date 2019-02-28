package com.mssinfotech.iampro.co.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.user.AddProductActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder> {
    ArrayList<MyProductModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
     //static String myid,pid;
     String myid;
    public MyProductAdapter(Context context, ArrayList<MyProductModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,iv_delete,iv_edit;
        VideoView videoView;
        TextView tv_name,uname,udate,tv_comments,tv_totallike,detail_name,tv_purchaseprice,tv_sellingprice;
         LinearLayout ll_comment;
        LikeButton likeButton;
        RatingBar ratingBar;
        LinearLayout ll_showhide;
        MyProductModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView=v.findViewById(R.id.imageView);
            tv_name=v.findViewById(R.id.tv_name);

            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            ll_comment=v.findViewById(R.id.ll_comment);
            likeButton =v.findViewById(R.id.likeButton);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_favourite=v.findViewById(R.id.iv_favourite);
            iv_edit = v.findViewById(R.id.iv_edit);
            image=v.findViewById(R.id.imageView);
            iv_delete=v.findViewById(R.id.iv_delete);
            videoView=v.findViewById(R.id.videoView);
            ratingBar=v.findViewById(R.id.ratingBar);
            //udate=v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_showhide=v.findViewById(R.id.ll_showhide);
            uname=v.findViewById(R.id.uname);
            detail_name=v.findViewById(R.id.detail_name);
            tv_purchaseprice=v.findViewById(R.id.tv_purchaseprice);
            tv_sellingprice=v.findViewById(R.id.tv_sellingprice);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Product clicked"+item.getName()+"\n"+item.getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext, ProductDetail.class);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pid",String.valueOf(item.getPid()));
                    intent.putExtra("uid",String.valueOf(item.getUid()));
                    mContext.startActivity(intent);
                }
            });
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "delete clicked"+item.getName()+"\n"+item.getUid(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        public void setData(MyProductModel item) {
            this.item = item;
            uid=item.getUid();
            final String pid=item.getPid();
            if(PrefManager.isLogin(mContext)){
               myid= PrefManager.getLoginDetail(mContext,"id");
               }
               else{
                myid=String.valueOf(uid);
            }
            ratingBar.setRating(Float.parseFloat(String.valueOf(item.getRating())));
            likeButton.setUnlikeDrawableRes(R.drawable.like);
            likeButton.setLikeDrawableRes(R.drawable.like_un);
            uname.setText(item.getFullname());
            tv_name.setText(item.getName());
            //udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));
            tv_purchaseprice.setText("Rs: "+String.valueOf(item.getpCost()));
            likeButton.setUnlikeDrawableRes(R.drawable.like);
            likeButton.setLikeDrawableRes(R.drawable.like_un);
            //tv_purchaseprice.setPaintFlags(tv_purchaseprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_sellingprice.setText("Rs: "+String.valueOf(item.getsCost()));
              if (myid.equalsIgnoreCase(String.valueOf(uid)) || uid==0 || String.valueOf(uid)=="" || String.valueOf(uid)==null) {
                  iv_delete.setVisibility(View.VISIBLE);
              }
              else {
                  iv_delete.setVisibility(View.GONE);
              }
            Glide.with(mContext)
                    .load(item.getImage())
                    .apply(Config.options_product)
                    .into(imageView);
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public MyProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(mValues.get(position));
        //ratingv,uidv,idv
        //final float ratingv
        final int uidv=mValues.get(position).getUid();
        final String idv=mValues.get(position).getPid();
        int my_uid=uidv;
        if(my_uid==0){
            Vholder.likeButton.setEnabled(false);
        }
         if(mValues.get(position).getIsLike()==1){ //mValues.get(position).get
            Vholder.likeButton.setLiked(true);
            Vholder.tv_totallike.setTextColor(Color.RED);
        }else{
            Vholder.likeButton.setLiked(false);
            Vholder.tv_totallike.setTextColor(Color.BLACK);
        }
        if(mValues.get(position).getMore()!=null && mValues.get(position).getMore().equalsIgnoreCase("loadmore")){
            Vholder.iv_delete.setVisibility(View.GONE);
            Vholder.iv_edit.setVisibility(View.GONE);
        }
        Vholder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike =Integer.parseInt(Vholder.tv_totallike.getText().toString())+1;
                Vholder.tv_totallike.setTextColor(Color.RED);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(idv)+"&uid="+uidv+"&ptype=product";
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt( Vholder.tv_totallike.getText().toString())-1;
                Vholder.tv_totallike.setTextColor(Color.BLACK);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(idv)+"&uid="+idv+"&ptype=product";
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
        });
        Vholder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Delete it!"+mValues.get(position).getPid());
                builder.setMessage("Are you sure...");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mValues.remove(position);
                                notifyDataSetChanged();
                                //Toast.makeText(mContext,"deleted",Toast.LENGTH_LONG).show();
                                deleteProduct(mValues.get(position).getPid());
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                /*  mValues.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(mContext,"deleted",Toast.LENGTH_LONG).show();    */

            }
        });
        Vholder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, AddProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",mValues.get(position).getPid());
                mContext.startActivity(intent);
            }
        });
        Vholder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                Toast.makeText(mContext,""+ratingBar.getRating(),Toast.LENGTH_LONG).show();
                sendrating(ratingBar.getRating(),uidv,Integer.parseInt(idv));
            }
        });
        Vholder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                 intent.putExtra("type","product");
                  intent.putExtra("id",String.valueOf(mValues.get(position).getPid()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


        if(!PrefManager.getLoginDetail(mContext,"id").equalsIgnoreCase(String.valueOf(mValues.get(position).getUid()))){
            Vholder.iv_delete.setVisibility(View.GONE);
            Vholder.iv_edit.setVisibility(View.GONE);
        }
        else{
            Vholder.iv_delete.setVisibility(View.VISIBLE);
            Vholder.iv_edit.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(MyProductModel item);
    }
    public void sendrating(float rating,int uid,int id){
        String urlv="https://www.iampro.co/api/app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype=product&total_rate="+rating;

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
     public void deleteProduct(String pid){
         String url="https://www.iampro.co/api/app_service.php?type=delete_product&id="+Integer.parseInt(pid)+"&item_type=product";
         RequestQueue MyRequestQueue = Volley.newRequestQueue(mContext);
         StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     JSONObject jsonObject = new JSONObject(response);
                     String status=jsonObject.optString("status");
                      String msg=jsonObject.getString("msg");
                      if(status.equalsIgnoreCase("success")){
                          Toast.makeText(mContext,"Deleted successfully",Toast.LENGTH_LONG).show();
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
