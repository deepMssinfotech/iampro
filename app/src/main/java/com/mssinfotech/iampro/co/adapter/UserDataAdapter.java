package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 18/01/19.
 */
//import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
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
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.mssinfotech.iampro.co.ChatToUser;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.UserModel;
//import com.mssinfotech.iampro.co.swipe.SwipeRevealLayout;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.user.FriendRequestActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONObject;

import java.util.ArrayList;
public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {
    ArrayList<UserModel> mValues;
    Context mContext;
    protected ItemListener mListener;
       int user_block;
    String FrindStatus;
    public UserDataAdapter(Context context, ArrayList<UserModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,tv_category,tv_images,tv_videos,tv_users,tv_products,tv_provides,tv_demands,tv_viewProfile,tv_fRequest,tv_sendMessage,tv_blockUser;
        de.hdodenhof.circleimageview.CircleImageView imageView,imageView_frequest,imageView_message,imageView_block,imageView_viewProfile;
           LinearLayout ll_frequest,ll_message,ll_block,ll_viewProfile,ll_imageView;
        UserModel item;

        protected ImageButton infoButton;
        protected ImageButton editButton;
        SwipeRevealLayout root;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView =v.findViewById(R.id.textView);
            imageView =v.findViewById(R.id.imageView);
            tv_category=v.findViewById(R.id.tvcategory);
            tv_images=v.findViewById(R.id.tv_images);
            tv_videos=v.findViewById(R.id.tv_videos);
            tv_users=v.findViewById(R.id.tv_users);
            tv_products=v.findViewById(R.id.tv_products);
            // ll_frequest,ll_message,ll_block,ll_viewProfile
            ll_frequest=v.findViewById(R.id.ll_frequest);
            ll_message=v.findViewById(R.id.ll_message);
            ll_block=v.findViewById(R.id.ll_block);
            ll_viewProfile=v.findViewById(R.id.ll_viewProfile);
            ll_imageView=v.findViewById(R.id.ll_imageView);
            tv_provides=v.findViewById(R.id.tv_provides);
                root=v.findViewById(R.id.root);
            tv_demands=v.findViewById(R.id.tv_demands);
            infoButton = v.findViewById(R.id.info_button);
            editButton= v.findViewById(R.id.edit_button);
            imageView_frequest=v.findViewById(R.id.imageView_frequest);
            imageView_message=v.findViewById(R.id.imageView_message);
            imageView_block=v.findViewById(R.id.imageView_block);
            imageView_viewProfile=v.findViewById(R.id.imageView_viewProfile);

            tv_viewProfile=v.findViewById(R.id.tv_viewProfile);
            tv_fRequest=v.findViewById(R.id.tv_fRequest);
            tv_sendMessage=v.findViewById(R.id.tv_sendMessage);
            tv_blockUser=v.findViewById(R.id.tv_blockUser);
        }
        public void setData(UserModel item) {
            this.item = item;
            textView.setText(item.getName());
             tv_category.setText(item.getCategory());
            tv_images.setText(item.getTotal_image());
             tv_videos.setText(item.getTotal_video());
              tv_users.setText(item.getTotal_friend());
               tv_products.setText(item.getTotal_product());
               tv_provides.setText(item.getTotal_provide());
                tv_demands.setText(item.getTotal_demand());
            String url=item.getImage();
            //Toast.makeText(mContext,"image:"+url,Toast.LENGTH_LONG).show();

            Glide.with(mContext)
                    .load(url)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .fitCenter())
                    .into(imageView);
            //imageView.setImageResource(item.image);

        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_items,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholders, final int position) {
         final ViewHolder Vholder=Vholders;
        Vholders.setData(mValues.get(position));
        Vholders.ll_block.setClickable(true);
        Vholders.ll_frequest.setClickable(true);
        Vholders.ll_imageView.setClickable(true);
        Vholders.ll_message.setClickable(true);
        Vholders.ll_imageView.setClickable(true);
        user_block=mValues.get(position).getIs_block();
        try {
            Vholder.imageView_message.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.user_slide_info_message));
            Vholder.imageView_frequest.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.user_slide_nfo));
            Vholder.imageView_viewProfile.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.user_slide_info_view_profile));
            Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.unblockicone));
        }
        catch (Exception e){
            Log.d("imageView_error",""+e.getMessage());
        }
         if (mValues.get(position).getIs_block()==1){
             Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext,R.drawable.unblockicone));
             //tv_viewProfile,tv_fRequest,tv_sendMessage,tv_blockUser
             Vholder.tv_viewProfile.setText("View Profile");
              Vholder.tv_fRequest.setText("Delete "+mValues.get(position).getName());

               Vholder.tv_sendMessage.setText("Message \t"+mValues.get(position).getName());
                Vholder.tv_blockUser.setText("UnBlock");
         }
         else if (mValues.get(position).getIs_block()==2){
             Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext,R.drawable.blockicone));


             Vholder.tv_viewProfile.setText("View Profile");
             Vholder.tv_fRequest.setText("You ");
             Vholder.tv_sendMessage.setText("Message \t"+mValues.get(position).getName());
             Vholder.tv_blockUser.setText("Block");
         }
        FrindStatus =mValues.get(position).getFriend_status();
         if (FrindStatus==null){
             FrindStatus="";
         }
        if(FrindStatus.equals("NO")){
            Vholder.tv_fRequest.setText("Add to Friend");
        }else if(FrindStatus.equals("PANDING")){
            Vholder.tv_fRequest.setText("Cancel Request");
        }else{
            Vholder.tv_fRequest.setText("Remove Friend");
        }
         Vholder.ll_imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ProfileActivity fragment = new ProfileActivity();
                 Bundle args = new Bundle();
                 args.putString("uid", String.valueOf(mValues.get(position).getId()));
                 function.loadFragment(mContext,fragment,args);

             }
         });

         //imageView_frequest,imageView_message,imageView_block
        Vholder.ll_frequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "FREQUEST CLICKED"+position,Toast.LENGTH_SHORT).show();
                 if (user_block==1){
                     Toast.makeText(mContext,"This User is blocked can't send Your request...",Toast.LENGTH_LONG).show();
                     return;
                 }
                 else if (user_block==2){
                     FriendRequestActivity fragment = new FriendRequestActivity();
                     Bundle args = new Bundle();
                     args.putString("uid", String.valueOf(mValues.get(position).getId()));
                     function.loadFragment(mContext,fragment,args);
                 }
                 else {
                     FriendRequestActivity fragment = new FriendRequestActivity();
                     Bundle args = new Bundle();
                     args.putString("uid", String.valueOf(mValues.get(position).getId()));
                     function.loadFragment(mContext,fragment,args);
                 }

                String myid=PrefManager.getLoginDetail(mContext,"id");
                 int fid=mValues.get(position).getId();
                if(FrindStatus.equals("NO")){
                    function.executeUrl(mContext,"get", Config.API_URL+"app_service.php?type=join_friend&fid="+fid+"&uid="+myid,null);
                    Vholder.tv_fRequest.setText("Delete Request");
                    FrindStatus = "PANDING";
                }else if(FrindStatus.equals("PANDING")){
                    function.executeUrl(mContext,"get",Config.API_URL+"app_service.php?type=delete_friend&id="+fid+"&tid="+myid,null);
                    Vholder.tv_fRequest.setText("Add to Friend");
                    FrindStatus = "NO";
                }else{
                    function.executeUrl(mContext,"get",Config.API_URL+"app_service.php?type=delete_friend&id="+fid+"&tid="+myid,null);
                    Vholder.tv_fRequest.setText("Add To Friend");
                    FrindStatus = "NO";
                }

            }
        });
        Vholders.ll_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Redirecting to message1111..."+position, Toast.LENGTH_SHORT).show();
                if (user_block==1){
                    Toast.makeText(mContext,"This User is blocked so you can't do any conversation...",Toast.LENGTH_LONG).show();
                    return;
                }
                else if (user_block==2) {
                    Toast.makeText(v.getContext(), "Redirecting to message..."+position, Toast.LENGTH_SHORT).show();
                    Intent intentMessage=new Intent(mContext, ChatToUser.class);
                    intentMessage.putExtra("id",String.valueOf(mValues.get(position).getId()));
                    mContext.startActivity(intentMessage);
                }
              else{
                    Toast.makeText(v.getContext(), "Redirecting to message...."+position, Toast.LENGTH_SHORT).show();
                    Intent intentMessage=new Intent(mContext, ChatToUser.class);
                    intentMessage.putExtra("id",String.valueOf(mValues.get(position).getId()));
                    mContext.startActivity(intentMessage);
                }
            }
        });
        Vholder.ll_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "BLOCK CLICKED"+position, Toast.LENGTH_SHORT).show();
                if (PrefManager.isLogin(mContext)){
                    final String fid = String.valueOf(mValues.get(position).getId());

                    /*if (user_block == 1) {
                        // Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.unblockicone));

                        Glide.with(mContext)
                                .load(R.drawable.unblockicone)
                                .into(Vholder.imageView_block);

                        Vholder.tv_blockUser.setText("UnBlock");
                        //user_block=2;

                    } else if (user_block == 2) {
                        //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.blockicone));
                        Glide.with(mContext)
                                .load(R.drawable.blockicone)
                                .into(Vholder.imageView_block);
                        Vholder.tv_blockUser.setText("Block");
                        //user_block=1;
                    }*/

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setMessage("Are you sure, ");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                int statuss=blockUser(String.valueOf(mValues.get(position).getId()));
                                user_block=statuss;
                                if (statuss== 1) {
                                    //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.unblockicone));

                                    Glide.with(mContext)
                                            .load(R.drawable.blockicone)
                                            .apply(Config.options_avatar)
                                            .into(Vholder.imageView_block);
                                    Vholder.tv_blockUser.setText("UnBlock");
                                    //user_block=2;

                                } else if (statuss== 2) {
                                    //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.blockicone));
                                    Glide.with(mContext)
                                            .load(R.drawable.unblockicone)
                                            .apply(Config.options_avatar)
                                            .into(Vholder.imageView_block);
                                    Vholder.tv_blockUser.setText("Block");
                                    //user_block=1;

                                }
                            }
                        });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

               /* if (user_block == 1) {
                   // Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.unblockicone));

                    Glide.with(mContext)
                            .load(R.drawable.unblockicone)
                            .into(Vholder.imageView_block);

                    Vholder.tv_blockUser.setText("UnBlock");
                    user_block=2;

                } else if (user_block == 2) {
                    //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.blockicone));
                    Glide.with(mContext)
                            .load(R.drawable.blockicone)
                            .into(Vholder.imageView_block);
                    Vholder.tv_blockUser.setText("Block");
                    user_block=1;
                } */
            }
                else {
                     Toast.makeText(mContext,""+"First Login and try again...",Toast.LENGTH_LONG).show();
                     return;
                }
            }
        });
        //imageView_viewProfile
        Vholder.ll_viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "VIEWPROFILE CLICKED"+position, Toast.LENGTH_SHORT).show();
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(mValues.get(position).getId()));
                function.loadFragment(mContext,fragment,args);
            }
        });
        if (String.valueOf(mValues.get(position).getId()).equalsIgnoreCase(PrefManager.getLoginDetail(mContext,"id"))){
            Vholder.imageView_frequest.setEnabled(false);
            Vholder.imageView_message.setEnabled(false);
            Vholder.imageView_block.setEnabled(false);

        }
        Vholder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Toast.makeText(mContext,""+position,Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(UserModel item);
    }

    public void removeItem(int position) {
        mValues.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(UserModel item, int position) {
        mValues.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
     public int blockUser(String fid){
         String url=Config.API_URL+ "app_service.php?type=get_block_user_detail&uid="+ PrefManager.getLoginDetail(mContext,"id")+"&fid="+fid;
          int statuss=0;
         RequestQueue requestQueue = Volley.newRequestQueue(mContext);
         JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,null,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         System.out.println(response);
                         try {
                             String status = response.getString("status");
                             String  msg = response.getString("msg");
                            user_block = response.optInt("user_block");
                            Toast.makeText(mContext,""+msg,Toast.LENGTH_LONG).show();
                             //statuss=response.optInt("user_block");
                         }
                         catch(Exception e) {
                             Toast.makeText(mContext,""+e.getMessage(),Toast.LENGTH_LONG).show();
                         }
                         //return response.optInt("user_block");
                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {

                     }
                 });
         requestQueue.add(jsObjRequest);
    return user_block;
     }
}