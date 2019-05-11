package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 15/01/19.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.SearchedActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.tab.ImageFragment;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {
    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    public RecyclerViewDataAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }
    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }
    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {
        final String sectionName = dataList.get(i).getHeaderTitle();
        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();
        Log.e(Config.TAG,sectionName);
        //Toast.makeText(mContext, "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
        if(sectionName.equalsIgnoreCase("Product")){
           Glide.with(mContext).load(R.drawable.latestproduct).into(itemRowHolder.itemTitle);
        }else if(sectionName.equalsIgnoreCase("provide")){
            Glide.with(mContext).load(R.drawable.latestprovide).into(itemRowHolder.itemTitle);
        }else if(sectionName.equalsIgnoreCase("demand")){
            Glide.with(mContext).load(R.drawable.latestdemand).into(itemRowHolder.itemTitle);
        }else if(sectionName.equalsIgnoreCase("user")){
            Glide.with(mContext).load(R.drawable.user).into(itemRowHolder.itemTitle);
        }else if(sectionName.equalsIgnoreCase("Images")){
            Glide.with(mContext).load(R.drawable.latestphotos).into(itemRowHolder.itemTitle);
        }else if(sectionName.equalsIgnoreCase("Video")){
            Glide.with(mContext).load(R.drawable.latestvideo).into(itemRowHolder.itemTitle);
        }
        else{
            Glide.with(mContext).load(R.drawable.latestphotos).into(itemRowHolder.itemTitle);
        }
        if (sectionName.equalsIgnoreCase("User")){
             //SectionListUserDataAdapter
            /* SectionListUserDataAdapter itemListDataAdapter = new SectionListUserDataAdapter(mContext, singleSectionItems);
             itemRowHolder.recycler_view_list.setHasFixedSize(true);
             itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
             itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
             notifyDataSetChanged(); */
         }
         else {
             SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems,sectionName);
             itemRowHolder.recycler_view_list.setHasFixedSize(true);
             //itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
            itemRowHolder.recycler_view_list.setLayoutManager(manager);
            itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        }
        itemRowHolder.btnMore.setEnabled(false);
        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
             if (sectionName.equalsIgnoreCase("Images")){
                 //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
                 /*ImageFragment fragment = new ImageFragment();
                 Bundle args = new Bundle();
                 args.putString("uid", PrefManager.getLoginDetail(mContext,"id"));
                 function.loadFragment(mContext,fragment,args); */

                 /*SearchActivity fragments = new SearchActivity();
                 Bundle args= new Bundle();
                 args.putString("type",sectionName);
                 function.loadFragment(mContext,fragments,args); */
                 //SearchType, SearchCat
                 Intent a=new Intent(mContext,SearchedActivity.class);
                  a.putExtra("SearchType",sectionName);
                  a.putExtra("SearchCat","");
                  mContext.startActivity(a);


             }
             else if (sectionName.equalsIgnoreCase("Video")){
                 //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
                /* SearchActivity fragments = new SearchActivity();
                 Bundle args= new Bundle();
                 args.putString("type",sectionName);
                 function.loadFragment(mContext,fragments,args);*/

                 Intent a=new Intent(mContext,SearchedActivity.class);
                 a.putExtra("SearchType",sectionName);
                 a.putExtra("SearchCat","");
                 mContext.startActivity(a);

             }
             else if (sectionName.equalsIgnoreCase("Product")){
                // Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                 /*SearchActivity fragments = new SearchActivity();
                 Bundle args= new Bundle();
                 args.putString("type",sectionName);
                 function.loadFragment(mContext,fragments,args); */

                 Intent a=new Intent(mContext,SearchedActivity.class);
                 a.putExtra("SearchType",sectionName);
                 a.putExtra("SearchCat","");
                 mContext.startActivity(a);

             }
             else if (sectionName.equalsIgnoreCase("Provide")){
                 //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                 /*SearchActivity fragments = new SearchActivity();
                 Bundle args= new Bundle();
                 args.putString("type",sectionName);
                 function.loadFragment(mContext,fragments,args);*/

                 Intent a=new Intent(mContext,SearchedActivity.class);
                 a.putExtra("SearchType",sectionName);
                 a.putExtra("SearchCat","");
                 mContext.startActivity(a);

             }
             else if (sectionName.equalsIgnoreCase("Demand")){
                 //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                 /*SearchActivity fragments = new SearchActivity();
                 Bundle args= new Bundle();
                 args.putString("type",sectionName);
                 function.loadFragment(mContext,fragments,args);*/

                 Intent a=new Intent(mContext,SearchedActivity.class);
                 a.putExtra("SearchType",sectionName);
                 a.putExtra("SearchCat","");
                 mContext.startActivity(a);

             }
             else if (sectionName.equalsIgnoreCase("FRIEND" ) || sectionName.equalsIgnoreCase("User")){
                 //Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                 /*SearchActivity fragments = new SearchActivity();
                 Bundle args= new Bundle();
                 args.putString("type",sectionName);
                 function.loadFragment(mContext,fragments,args);*/

                 Intent a=new Intent(mContext,SearchedActivity.class);
                 a.putExtra("SearchType",sectionName);
                 a.putExtra("SearchCat","");
                 mContext.startActivity(a);

             }
            }
        });
       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView totallike,comments,daysago,user_name;

        protected ImageView itemTitle;

        protected RecyclerView recycler_view_list;

        protected de.hdodenhof.circleimageview.CircleImageView btnMore,user_image;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (ImageView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= view.findViewById(R.id.btnMore);
            this.totallike=view.findViewById(R.id.tv_totallike);
            this.comments=view.findViewById(R.id.tv_comments);
            this.daysago=view.findViewById(R.id.tv_daysago);
            this.user_image=view.findViewById(R.id.user_image);
            this.user_name=view.findViewById(R.id.tv_user_name);
        }
    }
}
