package com.mssinfotech.iampro.co.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetail;
import com.mssinfotech.iampro.co.user.AddProductActivity;
import com.mssinfotech.iampro.co.user.AddProvideActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProvideAdapter extends RecyclerView.Adapter<MyProvideAdapter.ViewHolder> {
    ArrayList<MyProductModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    static String myid,pid;
    public MyProvideAdapter(Context context, ArrayList<MyProductModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView  imageView,imageView_user,imageView_icon,iv_comments,image,iv_favourite,ivLike,iv_delete,iv_edit;
        VideoView videoView;
        TextView tv_name,uname,udate,tv_comments,tv_totallike,detail_name,tv_sellingprice;
        RatingBar ratingBar;
        LinearLayout ll_showhide,ll_comment;
        MyProductModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView=v.findViewById(R.id.imageView);
            tv_name=v.findViewById(R.id.tv_name);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            ll_comment=v.findViewById(R.id.ll_comment);
            iv_comments=v.findViewById(R.id.iv_comments);
            iv_favourite=v.findViewById(R.id.iv_favourite);
            iv_edit = v.findViewById(R.id.iv_edit);
            image=v.findViewById(R.id.imageView);
            iv_delete=v.findViewById(R.id.iv_delete);
            videoView=v.findViewById(R.id.videoView);
            ratingBar=v.findViewById(R.id.ratingBar);
            ivLike=v.findViewById(R.id.ivLike);
            //udate=v.findViewById(R.id.udate);
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_showhide=v.findViewById(R.id.ll_showhide);
            uname=v.findViewById(R.id.uname);
            detail_name=v.findViewById(R.id.detail_name);
            tv_sellingprice=v.findViewById(R.id.tv_sellingprice);

            ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, CommentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, "Provide clicked"+item.getName()+"\n"+item.getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mContext, ProvideDetail.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("pid",String.valueOf(item.getPid()));
                    intent.putExtra("uid",String.valueOf(item.getUid()));
                    mContext.startActivity(intent);
                }
            });

        }
        public void setData(MyProductModel item) {
            this.item = item;
            uid=item.getUid();
            pid=item.getPid();
            if(PrefManager.isLogin(mContext))
                myid= PrefManager.getLoginDetail(mContext,"id");
            ratingBar.setRating(Float.parseFloat(String.valueOf(item.getRating())));
            uname.setText(item.getFullname());
            tv_name.setText(item.getName());
            //udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));

            tv_sellingprice.setText("Rs: "+String.valueOf(item.getsCost()));
            if (myid.equalsIgnoreCase(String.valueOf(uid)) || uid==0 || String.valueOf(uid)=="" || String.valueOf(uid)==null) {
                iv_delete.setVisibility(View.VISIBLE);
            }
            else {
                iv_delete.setVisibility(View.GONE);
            }
            Glide.with(mContext)
                    .load(item.getImage())
                    .apply(Config.options_provide)
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
    public MyProvideAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_provide_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Vholder, final int position) {
        Vholder.setData(mValues.get(position));
        Vholder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Delete it!");
                builder.setMessage("Are you sure...");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mValues.remove(position);
                                notifyDataSetChanged();
                                deleteProvide();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Vholder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, AddProvideActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",String.valueOf(pid));
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(MyProductModel item);
    }

    public void deleteProvide(){
         String url="https://www.iampro.co/api/app_service.php?type=delete_product&id="+pid+"&item_type=provide";
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

