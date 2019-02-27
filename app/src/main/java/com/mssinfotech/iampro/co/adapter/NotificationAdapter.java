package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.NotificationItem;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private List<NotificationItem> notifyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name, user_name, detail;
        public ImageView user_image,product_image;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            //product_name = view.findViewById(R.id.product_name);
            //user_name = view.findViewById(R.id.user_count);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            product_image = view.findViewById(R.id.product_image);
            user_image = view.findViewById(R.id.user_image);
            detail = view.findViewById(R.id.detail);
        }
    }


    public NotificationAdapter(Context context, List<NotificationItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NotificationItem item = notifyList.get(position);
        holder.detail.setText(item.getDetail());
        //holder.price.setText("₹" + item.getPrice());
        Glide.with(context).load(item.getPoduct_image()).into(holder.product_image);
        Glide.with(context).load(item.getUser_image()).into(holder.user_image);

         holder.user_image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                /* Intent intent=new Intent(context,ProfileActivity.class);
                    intent.putExtra("uid",notifyList.get(position).getUid());
                    context.startActivity(intent); */

                 ProfileActivity fragment = new ProfileActivity();
                 Bundle args = new Bundle();
                 args.putString("uid",notifyList.get(position).getUid());
                 function.loadFragment(context,fragment,args);
             }
         });
          holder.product_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String p_type=notifyList.get(position).getP_type();
                  String n_type=notifyList.get(position).getNotify_type();
                  //Toast.makeText(context,""+notifyList.get(position).getP_type()+"\t"+n_type,Toast.LENGTH_LONG).show();
                   if (p_type.equalsIgnoreCase("image") && n_type.equalsIgnoreCase("LIKE")){
                       ImageDetail fragment = new ImageDetail();
                       Bundle args = new Bundle();
                       args.putString("id", String.valueOf(notifyList.get(position).getPid()));
                       args.putString("type", "image");
                       args.putString("uid",notifyList.get(position).getUid());
                       function.loadFragment(context,fragment,args);
                   }
                   else if (p_type.equalsIgnoreCase("video") && n_type.equalsIgnoreCase("LIKE")){
                       ImageDetail fragment = new ImageDetail();
                       Bundle args = new Bundle();
                       args.putString("id", String.valueOf(notifyList.get(position).getPid()));
                       args.putString("type", "video");
                       args.putString("uid",notifyList.get(position).getUid());
                       function.loadFragment(context,fragment,args);
                   }
                   else if (p_type.equalsIgnoreCase("product") && n_type.equalsIgnoreCase("LIKE")){
                        /*ProductDetail fragment = new ProductDetail();
                       Bundle args = new Bundle();
                       args.putString("id", String.valueOf(notifyList.get(position).getId()));
                       args.putString("type", "image");
                       args.putString("uid",notifyList.get(position).getUid());
                       function.loadFragment(context,fragment,args); */
                        Intent imageDetail=new Intent(context,ProductDetail.class);
                          imageDetail.putExtra("id", String.valueOf(notifyList.get(position).getPid()));
                         //imageDetail.putExtra("id", String.valueOf(notifyList.get(position).getAlbemId()));
                         imageDetail.putExtra("type", "image");
                          imageDetail.putExtra("uid",notifyList.get(position).getUid());
                           context.startActivity(imageDetail);
                   }
                   else if (p_type.equalsIgnoreCase("provide") && n_type.equalsIgnoreCase("LIKE")){
                       Intent imageDetail=new Intent(context,ProvideDetailActivity.class);
                        imageDetail.putExtra("id", String.valueOf(notifyList.get(position).getPid()));
                       //imageDetail.putExtra("id", String.valueOf(notifyList.get(position).getAlbemId()));
                       imageDetail.putExtra("type", "image");
                       imageDetail.putExtra("uid",notifyList.get(position).getUid());
                       context.startActivity(imageDetail);

                   }
                   else if (p_type.equalsIgnoreCase("demand") && n_type.equalsIgnoreCase("LIKE")){
                       Intent imageDetail=new Intent(context,DemandDetailActivity.class);
                       imageDetail.putExtra("id", String.valueOf(notifyList.get(position).getPid()));
                       //imageDetail.putExtra("id", String.valueOf(notifyList.get(position).getAlbemId()));
                       imageDetail.putExtra("type", "image");
                       imageDetail.putExtra("uid",notifyList.get(position).getUid());
                       context.startActivity(imageDetail);
                   }

                  else if (p_type.equalsIgnoreCase("image") && n_type.equalsIgnoreCase("comment")){
                       Intent intent=new Intent(context, CommentActivity.class);
                       intent.putExtra("id",String.valueOf(notifyList.get(position).getPid()));
                       intent.putExtra("type","image");
                       intent.putExtra("uid",notifyList.get(position).getUid());
                        context.startActivity(intent);
                  }
                  else if (p_type.equalsIgnoreCase("video") && n_type.equalsIgnoreCase("comment")){
                       Intent intent=new Intent(context, CommentActivity.class);
                       intent.putExtra("id",String.valueOf(notifyList.get(position).getPid()));
                       intent.putExtra("type","video");
                       intent.putExtra("uid",notifyList.get(position).getUid());
                        context.startActivity(intent);
                  }
                  else if (p_type.equalsIgnoreCase("product") && n_type.equalsIgnoreCase("comment")){
                       Intent intent=new Intent(context, CommentActivity.class);
                       intent.putExtra("id",String.valueOf(notifyList.get(position).getPid()));
                       intent.putExtra("type","product");
                       intent.putExtra("uid",notifyList.get(position).getUid());
                       context.startActivity(intent);
                  }
                  else if (p_type.equalsIgnoreCase("provide") && n_type.equalsIgnoreCase("comment")){
                       Intent intent=new Intent(context, CommentActivity.class);
                       intent.putExtra("id",String.valueOf(notifyList.get(position).getPid()));
                       intent.putExtra("type","provide");
                       intent.putExtra("uid",notifyList.get(position).getUid());
                       context.startActivity(intent);
                  }
                  else if (p_type.equalsIgnoreCase("demand") && n_type.equalsIgnoreCase("comment")){
                       Intent intent=new Intent(context, CommentActivity.class);
                       intent.putExtra("id",String.valueOf(notifyList.get(position).getPid()));
                       intent.putExtra("type","demand");
                       intent.putExtra("uid",notifyList.get(position).getUid());
                       context.startActivity(intent);
                  }

              }
          });
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public void removeItem(int position) {
        notifyList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationItem item, int position) {
        notifyList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}