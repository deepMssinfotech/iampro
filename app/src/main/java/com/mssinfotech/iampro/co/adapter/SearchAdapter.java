package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.SearchItem;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.GlideApp;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements SearchView.OnQueryTextListener {
    Context mContext;
    private List<SearchItem> searchList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,product_price, user_name, detail,fullname,like,comment,textView,city,tvcategory,purchese_cost;
        public TextView tv_images,tv_videos,tv_users,tv_products,tv_provides,tv_demands;
        public ImageView product_image,video_imageView,playIcon;
        public CircleImageView imageView_icon,imageView_user,imageView;
        public LinearLayout video_imageView_ll,pplayout,otherlayout;
        public FrameLayout userlayout;
        public MyViewHolder(View view) {
            super(view);
            like = view.findViewById(R.id.like);
            comment = view.findViewById(R.id.comment);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            purchese_cost = view.findViewById(R.id.purchese_cost);
            product_image = view.findViewById(R.id.product_image);
            detail = view.findViewById(R.id.detail);
            imageView_user = view.findViewById(R.id.imageView_user);
            imageView_icon = view.findViewById(R.id.imageView_icon);
            fullname = view.findViewById(R.id.fullname);
            video_imageView_ll = view.findViewById(R.id.video_imageView_ll);
            video_imageView = view.findViewById(R.id.video_imageView);
            playIcon=view.findViewById(R.id.playIcon);
            pplayout =view.findViewById(R.id.pplayout);
            otherlayout = view.findViewById(R.id.otherlayout);
            userlayout=view.findViewById(R.id.userlayout);
            textView=view.findViewById(R.id.textView);
            imageView=view.findViewById(R.id.imageView);
            city=view.findViewById(R.id.city);
            tvcategory=view.findViewById(R.id.tvcategory);
            tv_images=view.findViewById(R.id.tv_images);
            tv_videos=view.findViewById(R.id.tv_videos);
            tv_users=view.findViewById(R.id.tv_users);
            tv_products=view.findViewById(R.id.tv_products);
            tv_provides=view.findViewById(R.id.tv_provides);
            tv_demands=view.findViewById(R.id.tv_demands);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        String text = query;
        Toasty.info(mContext,query,Toasty.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        Toasty.info(mContext,text,Toasty.LENGTH_LONG).show();
        return false;
    }

    public SearchAdapter(Context context, List<SearchItem> searchList) {
        mContext = context;
        this.searchList = searchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SearchItem item = searchList.get(position);
        final String user_id=item.getUser_Id();
        final String id= String.valueOf(item.getId());
        final String p_type=item.getType();
        String avatar = Config.AVATAR_URL+item.getUser_avatar();
        holder.userlayout.setVisibility(View.GONE);
        holder.video_imageView_ll.setVisibility(View.GONE);
        holder.pplayout.setVisibility(View.GONE);
        holder.playIcon.setVisibility(View.GONE);
        holder.purchese_cost.setVisibility(View.GONE);
        holder.fullname.setText(item.getUser_fullname());
        Glide.with(mContext)
                .load(avatar)
                .apply(Config.options_avatar)
                .into(holder.imageView_user);
        if(p_type.equalsIgnoreCase("friends")){
            holder.userlayout.setVisibility(View.VISIBLE);
            holder.otherlayout.setVisibility(View.GONE);
            holder.textView.setText(item.getUser_fullname());
            Glide.with(mContext)
                    .load(avatar)
                    .apply(Config.options_avatar)
                    .into(holder.imageView);
            holder.city.setText(item.getCity());
            holder.tvcategory.setText(item.getCategory());
            holder.tv_images.setText(item.getTotal_image());
            holder.tv_videos.setText(item.getTotal_video());
            holder.tv_users.setText(item.getTotal_friends());
            holder.tv_products.setText(item.getTotal_product());
            holder.tv_provides.setText(item.getTotal_provide());
            holder.tv_demands.setText(item.getTotal_demend());

        }else if (p_type.equalsIgnoreCase("image")) {
            holder.video_imageView_ll.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.ALBUM_URL+item.getImage())
                    .apply(Config.options_video)
                    .into(holder.video_imageView);
            Glide.with(mContext)
                    .load(R.drawable.image_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }else if (p_type.equalsIgnoreCase("video")) {
            holder.playIcon.setVisibility(View.VISIBLE);
            holder.video_imageView_ll.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.V_URL+item.getImage())
                    .apply(Config.options_video)
                    .into(holder.video_imageView);
            Glide.with(mContext)
                    .load(R.drawable.video_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        else if (p_type.equalsIgnoreCase("product")) {
            holder.product_name.setText(item.getName());
            holder.product_price.setText("Rs. "+item.getSelling_cost());
            holder.like.setText(item.getTotallike()+" Like ");
            holder.comment.setText(item.getComments()+" Comment");
            holder.purchese_cost.setText(item.getPurchese_cost());
            holder.pplayout.setVisibility(View.VISIBLE);
            holder.purchese_cost.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.PRODUCT_URL+item.getImage())
                    .apply(Config.options_demand)
                    .into(holder.product_image);
            Glide.with(mContext)
                    .load(R.drawable.product_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        else if (p_type.equalsIgnoreCase("provide")) {
            holder.product_name.setText(item.getName());
            holder.product_price.setText("Rs. "+item.getSelling_cost());
            holder.like.setText(item.getTotallike()+" Like ");
            holder.comment.setText(item.getComments()+" Comment");
            holder.pplayout.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.PROVIDE_URL+item.getImage())
                    .apply(Config.options_demand)
                    .into(holder.product_image);
            Glide.with(mContext)
                    .load(R.drawable.provide_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        else if (p_type.equalsIgnoreCase("demand")) {
            holder.product_name.setText(item.getName());
            holder.product_price.setText("Rs. "+item.getSelling_cost());
            holder.like.setText(item.getTotallike()+" Like ");
            holder.comment.setText(item.getComments()+" Comment");
            holder.pplayout.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(Config.DEMAND_URL+item.getImage())
                    .apply(Config.options_demand)
                    .into(holder.product_image);
            Glide.with(mContext)
                    .load(R.drawable.demand_icon)
                    .apply(Config.options_avatar)
                    .into(holder.imageView_icon);
        }
        holder.userlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(user_id));
                function.loadFragment(mContext,fragment,args);
            }
        });
        holder.otherlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p_type.equalsIgnoreCase("IMAGE")) {
                    Intent intent = new Intent(mContext, ImageDetail.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "image");
                    intent.putExtra("uid", String.valueOf(user_id));
                    mContext.startActivity(intent);
                }else if (p_type.equalsIgnoreCase("VIDEO")) {
                    Intent intent = new Intent(mContext, ImageDetail.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", "video");
                    intent.putExtra("uid", String.valueOf(user_id));
                    mContext.startActivity(intent);
                }else if (p_type.equalsIgnoreCase("PRODUCT")) {
                    Intent intent = new Intent(mContext, ProductDetail.class);
                    intent.putExtra("pid", String.valueOf(id));
                    intent.putExtra("uid", String.valueOf(user_id));
                    mContext.startActivity(intent);
                }else if (p_type.equalsIgnoreCase("PROVIDE")) {
                    Intent intent = new Intent(mContext, ProvideDetailActivity.class);
                    intent.putExtra("pid", String.valueOf(id));
                    intent.putExtra("uid", String.valueOf(user_id));
                    mContext.startActivity(intent);
                }else if (p_type.equalsIgnoreCase("DEMAND")) {
                    Intent intent = new Intent(mContext, DemandDetailActivity.class);
                    intent.putExtra("pid", String.valueOf(id));
                    intent.putExtra("uid", String.valueOf(user_id));
                    mContext.startActivity(intent);
                }
            }
        });
        holder.imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileActivity myFragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(user_id));
                myFragment.setArguments(args);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, myFragment,myFragment.getClass().getName())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        holder.imageView_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,""+notifyList.get(position).getP_type()+"\t"+n_type,Toast.LENGTH_LONG).show();
                if (p_type.equalsIgnoreCase("IMAGE")) {
                    MyImageActivity fragment = new MyImageActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment,fragment.getClass().getName())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (p_type.equalsIgnoreCase("VIDEO")) {
                    MyVideoActivity fragment = new MyVideoActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment,fragment.getClass().getName())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (p_type.equalsIgnoreCase("PRODUCT")) {
                    MyProductActivity fragment = new MyProductActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment,fragment.getClass().getName())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (p_type.equalsIgnoreCase("PROVIDE")) {
                    MyProvideActivity fragment = new MyProvideActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment,fragment.getClass().getName())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                } else if (p_type.equalsIgnoreCase("DEMAND")) {
                    MyDemandActivity fragment = new MyDemandActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(user_id));
                    fragment.setArguments(args);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .add(android.R.id.content, fragment,fragment.getClass().getName())
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

}