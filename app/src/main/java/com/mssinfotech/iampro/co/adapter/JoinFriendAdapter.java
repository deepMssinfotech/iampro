package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.ChatToUser;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.JoinFriendItem;
import com.mssinfotech.iampro.co.user.FriendRequestActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinFriendAdapter extends RecyclerView.Adapter<JoinFriendAdapter.MyViewHolder> {
    private Context context;
    private List<JoinFriendItem> notifyList;
    int user_block;
    String FrindStatus;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView  fullname, category,city,total_image,total_video,total_user,total_product,total_provide,total_demand,tv_viewProfile,tv_fRequest,tv_sendMessage,tv_blockUser;
        public CircleImageView userimage,imageView_frequest,imageView_message,imageView_block,imageView_viewProfile;
        public RelativeLayout viewBackground, viewForeground;
        LinearLayout ll_frequest,ll_message,ll_block,ll_viewProfile,ll_imageView;
        public MyViewHolder(View view) {
            super(view);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            userimage = view.findViewById(R.id.userimage);
            fullname = view.findViewById(R.id.fullname);
            category = view.findViewById(R.id.category);
            city = view.findViewById(R.id.city);
            total_image = view.findViewById(R.id.total_image);
            ll_frequest=view.findViewById(R.id.ll_frequest);
            ll_message=view.findViewById(R.id.ll_message);
            ll_block=view.findViewById(R.id.ll_block);
            ll_viewProfile=view.findViewById(R.id.ll_viewProfile);
            ll_imageView=view.findViewById(R.id.ll_imageView);

            total_video = view.findViewById(R.id.total_video);
            total_user = view.findViewById(R.id.total_user);
            total_product = view.findViewById(R.id.total_product);
            total_provide = view.findViewById(R.id.total_provide);
            total_demand = view.findViewById(R.id.total_demand);

            imageView_frequest=view.findViewById(R.id.imageView_frequest);
            imageView_message=view.findViewById(R.id.imageView_message);
            imageView_block=view.findViewById(R.id.imageView_block);
            imageView_viewProfile=view.findViewById(R.id.imageView_viewProfile);

            tv_viewProfile=view.findViewById(R.id.tv_viewProfile);
            tv_fRequest=view.findViewById(R.id.tv_fRequest);
            tv_sendMessage=view.findViewById(R.id.tv_sendMessage);
            tv_blockUser=view.findViewById(R.id.tv_blockUser);
        }
    }
    public JoinFriendAdapter(Context context, List<JoinFriendItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friendrequest_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,  int positions) {
        final MyViewHolder Vholder=holder;
        final int position=positions;
        final JoinFriendItem item = notifyList.get(position);
        user_block=notifyList.get(position).getIs_block();
        holder.category.setText(item.getCategory());
        //holder.city.setText(item.getCity());
        holder.fullname.setText(item.getFullname());
       // holder.total_demand.setText(String.valueOf(item.getTotal_product_demand()));
       // holder.total_image.setText(String.valueOf(item.getTotal_img()));
       // holder.total_product.setText(String.valueOf(item.getTotal_product()));
        //holder.total_provide.setText(String.valueOf(item.getTotal_product_provide()));
        //holder.total_video.setText(String.valueOf(item.getTotal_video()));
        //holder.total_user.setText(String.valueOf(item.getTotal_friend()));
        Glide.with(context).load(item.getImage()).apply(Config.options_avatar).into(holder.userimage);
         holder. userimage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 /*Intent intent=new Intent(context, ProfileActivity.class);
                  intent.putExtra("uid",notifyList.get(position).getUser_id());
                   context.startActivity(intent);*/

                 ProfileActivity fragment = new ProfileActivity();
                 Bundle args = new Bundle();
                 args.putString("uid",String.valueOf(notifyList.get(position).getUser_id()));
                 function.loadFragment(context,fragment,args);
             }
         });

        //Vholder.imageView_message.setBackground(AppCompatResources.getDrawable(context,R.drawable.user_slide_info_message));
        Glide.with(context)
                .load(R.drawable.user_slide_info_message)
                .into(Vholder.imageView_message);

        //Vholder.imageView_frequest.setBackground(AppCompatResources.getDrawable(context,R.drawable.user_slide_nfo));
        Glide.with(context)
                .load(R.drawable.user_slide_nfo)
                .into(Vholder.imageView_frequest);
        //Vholder.imageView_viewProfile.setBackground(AppCompatResources.getDrawable(context,R.drawable.user_slide_info_view_profile));

        Glide.with(context)
                .load(R.drawable.user_slide_info_view_profile)
                .into(Vholder.imageView_viewProfile);

        //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(context,R.drawable.unblockicone));
        Glide.with(context)
                .load(R.drawable.unblockicone)
                .into(Vholder.imageView_block);

        if (notifyList.get(position).getIs_block()==1){
            //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(context,R.drawable.unblockicone));
            Glide.with(context)
                    .load(R.drawable.unblockicone)
                    .into(Vholder.imageView_block);
            //tv_viewProfile,tv_fRequest,tv_sendMessage,tv_blockUser
            Vholder.tv_viewProfile.setText("View Profile");
            Vholder.tv_fRequest.setText("Delete "+notifyList.get(position).getFullname());

            Vholder.tv_sendMessage.setText("Message \t"+notifyList.get(position).getFullname());
            Vholder.tv_blockUser.setText("UnBlock");
        }
        else if (notifyList.get(position).getIs_block()==2){
            //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(context,R.drawable.blockicone));
            Glide.with(context)
                    .load(R.drawable.blockicone)
                    .into( Vholder.imageView_block);

            Vholder.tv_viewProfile.setText("View Profile");
            Vholder.tv_fRequest.setText("You ");
            Vholder.tv_sendMessage.setText("Message \t"+notifyList.get(position).getFullname());
            Vholder.tv_blockUser.setText("Block");
        }
        FrindStatus =notifyList.get(position).getFriend_status();
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
        Vholder.userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(notifyList.get(position).getUser_id()));
                function.loadFragment(context,fragment,args);
                Toast.makeText(context, ""+notifyList.get(position).getUser_id(), Toast.LENGTH_SHORT).show();
            }
        });
        //imageView_frequest,imageView_message,imageView_block
        Vholder.ll_frequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefManager.isLogin(context)) {
                    //Toast.makeText(v.getContext(), "FREQUEST CLICKED"+position,Toast.LENGTH_SHORT).show();
                    if (PrefManager.isLogin(context) && user_block == 1) {
                        Toast.makeText(context, "This User is blocked can't send Your request...", Toast.LENGTH_LONG).show();
                        return;
                    } else if (PrefManager.isLogin(context) && user_block == 2) {
                        FriendRequestActivity fragment = new FriendRequestActivity();
                        Bundle args = new Bundle();
                        args.putString("uid", String.valueOf(notifyList.get(position).getId()));
                        function.loadFragment(context, fragment, args);
                    } else {
                        FriendRequestActivity fragment = new FriendRequestActivity();
                        Bundle args = new Bundle();
                        args.putString("uid", String.valueOf(notifyList.get(position).getId()));
                        function.loadFragment(context, fragment, args);
                    }

                    String myid = PrefManager.getLoginDetail(context, "id");
                    int fid = notifyList.get(position).getId();
                    if (FrindStatus.equals("NO")) {
                        function.executeUrl(context, "get", Config.API_URL + "app_service.php?type=join_friend&fid=" + fid + "&uid=" + myid, null);
                        Vholder.tv_fRequest.setText("Delete Request");
                        FrindStatus = "PANDING";
                    } else if (FrindStatus.equals("PANDING")) {
                        function.executeUrl(context, "get", Config.API_URL + "app_service.php?type=delete_friend&id=" + fid + "&tid=" + myid, null);
                        Vholder.tv_fRequest.setText("Add to Friend");
                        FrindStatus = "NO";
                    } else {
                        function.executeUrl(context, "get", Config.API_URL + "app_service.php?type=delete_friend&id=" + fid + "&tid=" + myid, null);
                        Vholder.tv_fRequest.setText("Add To Friend");
                        FrindStatus = "NO";
                    }
                }
                else{
                     Toast.makeText(context,""+"First Login and try again...",Toast.LENGTH_LONG).show();
                     return;
                }
            }
        });
        Vholder.ll_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefManager.isLogin(context)) {
                    if (user_block == 1) {
                        Toast.makeText(context, "This User is blocked so you can't do any conversation...", Toast.LENGTH_LONG).show();
                        return;
                    } else if (user_block == 2) {
                        Toast.makeText(v.getContext(), "Redirecting to message..." + position, Toast.LENGTH_SHORT).show();
                        Intent intentMessage = new Intent(context, ChatToUser.class);
                        intentMessage.putExtra("id", String.valueOf(notifyList.get(position).getId()));
                        context.startActivity(intentMessage);
                    } else {
                        Toast.makeText(v.getContext(), "Redirecting to message...." + notifyList.get(position).getId(), Toast.LENGTH_SHORT).show();

                        Intent intentMessage = new Intent(context, ChatToUser.class);
                        intentMessage.putExtra("id", String.valueOf(notifyList.get(position).getId()));
                        context.startActivity(intentMessage);
                    }

                }
                else{
                     Toast.makeText(context,""+"First Login and try again...",Toast.LENGTH_LONG).show();
                     return;
                }
            }
        });
        Vholder.ll_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefManager.isLogin(context)) {
                    //Toast.makeText(v.getContext(), "BLOCK CLICKED"+position, Toast.LENGTH_SHORT).show();
                    final String fid = String.valueOf(notifyList.get(position).getId());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are you sure, ");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // blockUser(fid);
                                    int statuss = blockUser(String.valueOf(notifyList.get(position).getId()));
                                    user_block = statuss;
                                    if (statuss == 1) {
                                        //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.unblockicone));

                                        Glide.with(context)
                                                .load(R.drawable.blockicone)
                                                .apply(Config.options_avatar)
                                                .into(Vholder.imageView_block);
                                        Vholder.tv_blockUser.setText("UnBlock");
                                        //user_block=2;

                                    } else if (statuss == 2) {
                                        //Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.blockicone));
                                        Glide.with(context)
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

               /* if(user_block==1){
                    Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(context,R.drawable.unblockicone));
                }
                else if (user_block==2){
                    Vholder.imageView_block.setBackground(AppCompatResources.getDrawable(context,R.drawable.blockicone));

                } */
                }
                else {
                     Toast.makeText(context,""+"First Login and try again...",Toast.LENGTH_LONG).show();
                     return;
                }
            }
        });
        //imageView_viewProfile
        Vholder.ll_viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "VIEWPROFILE CLICKED"+position+"\t"+String.valueOf(notifyList.get(position).getUser_id()), Toast.LENGTH_SHORT).show();
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(notifyList.get(position).getUser_id()));
                function.loadFragment(context,fragment,args);
            }
        });
        if (String.valueOf(notifyList.get(position).getUser_id()).equalsIgnoreCase(PrefManager.getLoginDetail(context,"id"))){
            holder.imageView_frequest.setEnabled(false);
            Vholder.imageView_message.setEnabled(false);
            Vholder.imageView_block.setEnabled(false);

        }

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

    public void restoreItem(JoinFriendItem item, int position) {
        notifyList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public int blockUser(String fid){
        String url=Config.API_URL+ "app_service.php?type=get_block_user_detail&uid="+ PrefManager.getLoginDetail(context,"id")+"&fid="+fid;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            String status = response.getString("status");
                            String  msg = response.getString("msg");
                            user_block = response.optInt("user_block");
                            Toast.makeText(context,""+msg,Toast.LENGTH_LONG).show();

                        }
                        catch(Exception e) {
                            Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
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