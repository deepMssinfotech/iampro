package com.mssinfotech.iampro.co.user;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.FeedListAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.data.FeedItem;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    ImageView userimage,userbackgroud;
    TextView username;
    private String URL_FEED = "",uid="";
    private Integer start=0,limit=20;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_profile));
        String fname=PrefManager.getLoginDetail(this,"fname");
        uid=PrefManager.getLoginDetail(this,"id");
        String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
        String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");

        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText(PrefManager.getLoginDetail(this,"fname") +" "+PrefManager.getLoginDetail(this,"lname"));
        Picasso.get()
                .load(avatar)
                .placeholder(R.drawable.iampro)
                .transform(new CircleTransform())
                .error(R.drawable.image)
                .into(userimage);
        Picasso.get()
                .load(background)
                .placeholder(R.drawable.profile_background)
                .error(R.drawable.profile_background)
                .into(userbackgroud);

        feedItems = new ArrayList<FeedItem>();

        handler = new Handler();

        loadFeedList(start,limit);

        mRecyclerView = (RecyclerView ) findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        listAdapter = new FeedListAdapter(feedItems, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(listAdapter);

        listAdapter.setOnLoadMoreListener(new FeedListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                feedItems.add(null);
                listAdapter.notifyItemInserted(feedItems.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        feedItems.remove(feedItems.size() - 1);
                        listAdapter.notifyItemRemoved(feedItems.size());
                        //add items one by one
                        int start = feedItems.size();
                        int end = start + 20;
                        loadFeedList(end,limit);
                        listAdapter.setLoaded();
                    }
                }, 2000);
            }
        });
    }
    private void  loadFeedList(Integer mStart,Integer mLimit){
        URL_FEED = Config.API_URL+ "feed_service.php?type=AllFeeds&start=" +mStart.toString()+ "&limit=" +mLimit.toString()+ "&fid=" +uid+ "&uid=" +uid+ "&my_id=" +uid;
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("data");

            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                String image_path="";

                String feed_type=feedObj.getString("type");
                String image = feedObj.isNull("first_image") ? null : feedObj.getString("first_image");
                if(feed_type.equalsIgnoreCase("IMAGE")){
                    image_path = Config.URL_ROOT+"uploads/album/450/500/"+image;
                }else if(feed_type.equalsIgnoreCase("VIDEO")){
                    image_path = Config.URL_ROOT + "uploads/v_image/" + image;
                }else if(feed_type.equalsIgnoreCase("PRODUCT")){
                    image_path = Config.URL_ROOT + "uploads/product/" + image;
                }else if(feed_type.equalsIgnoreCase("PROVIDE")){
                    image_path = Config.URL_ROOT + "uploads/product/" + image;
                }else if(feed_type.equalsIgnoreCase("DEMAND")){
                    image_path = Config.URL_ROOT + "uploads/product/" + image;
                }

                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("fullname"));
                item.setImge(image_path);
                item.setStatus(feedObj.getString("type"));
                item.setProfilePic(Config.AVATAR_URL+"200/200/"+feedObj.getString("avatar"));
                item.setTimeStamp(feedObj.getString("udate"));
                // url might be null sometimes
                //String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
                //item.setUrl(feedUrl);
                feedItems.add(item);
            }

            // notify data changes to list adapater
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
