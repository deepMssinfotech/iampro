package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 15/01/19.
 */

import android.content.Context;
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
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;

import java.util.ArrayList;
import java.util.HashMap;

public class MyVideoDataAdapter extends RecyclerView.Adapter<MyVideoDataAdapter.ItemRowHolder> {

    private ArrayList<SectionImageModel> dataList;
    private Context mContext;
    HashMap<String,String> item_name;
    public MyVideoDataAdapter(Context context, ArrayList<SectionImageModel> dataList,HashMap<String,String> item_name) {
        this.dataList = dataList;
        this.mContext = context;
        this.item_name=item_name;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();
        Log.e(Config.TAG,sectionName);
        //Toast.makeText(mContext, "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
        itemRowHolder.itemTitle.setText(sectionName);
       /* if(sectionName.equalsIgnoreCase("Product")){
            Glide.with(mContext).load(R.drawable.latestproduct).into(itemRowHolder.itemTitle);
        }
        else{
            Glide.with(mContext).load(R.drawable.latestphotos).into(itemRowHolder.itemTitle);

        } */
        //SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);

        SectionVideoAdapter itemListDataAdapter = new SectionVideoAdapter(mContext, singleSectionItems);


        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        //itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        GridLayoutManager manager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        itemRowHolder.recycler_view_list.setLayoutManager(manager);

        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();
                if (sectionName=="Images"){

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
        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected de.hdodenhof.circleimageview.CircleImageView btnMore,user_image;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = view.findViewById(R.id.itemTitle);
            this.recycler_view_list = view.findViewById(R.id.recycler_view_list);
            this.btnMore= view.findViewById(R.id.btnMore);

            this.totallike=view.findViewById(R.id.tv_totallike);
            this.comments=view.findViewById(R.id.tv_comments);
            this.daysago=view.findViewById(R.id.tv_daysago);
            this.user_image=view.findViewById(R.id.user_image);
            this.user_name=view.findViewById(R.id.tv_user_name);

        }

    }

}
