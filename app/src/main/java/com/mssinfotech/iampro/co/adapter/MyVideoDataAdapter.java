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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        if(dataList.get(i).getMore()!=null && dataList.get(i).getMore().equalsIgnoreCase("loadmore")){
            itemRowHolder.btnMore.setVisibility(View.INVISIBLE);
        }
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

            }
        });
       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }
    private void deleteAlbum(String albumId){
        String url="https://www.iampro.co/api/app_service.php?type=delete_album&id="+Integer.parseInt(albumId)+"&album_type=1";
        //String url="https://www.iampro.co/ajax/profile.php?type=deleteAlbemimage&id="+Integer.parseInt(pid);
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
