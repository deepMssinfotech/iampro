package com.mssinfotech.iampro.co.user;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.AllFeedAdapter;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.data.FeedItem;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
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

import de.hdodenhof.circleimageview.CircleImageView;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
//import com.mikepenz.community_material_typeface_library.CommunityMaterial;
//import com.mikepenz.iconics.IconicsDrawable;

public class ProfileActivity extends AppCompatActivity implements AllFeedAdapter.ItemListener,SwipeRefreshLayout.OnRefreshListener  {
 //,OnLikeListener,OnAnimationEndListener
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int FEED_LIMIT=15;
    private static int FEED_START=0;

    private List<FeedItem> feedItems;
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,myuid;
    private String URL_FEED = "",uid="", fid = "";
    private Integer start=0,limit=20;
    private LinearLayoutManager mLayoutManager;
    protected Handler handler;
    Intent intent;
    RecyclerView vFeed;
    AllFeedAdapter adapter;
    ArrayList<FeedModel> mValues=new ArrayList<>();
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View currentFocusedLayout, oldFocusedLayout;
    CardView userDetail_Layout,ll_dashboard;
    TextView tv_name,tv_dob,tv_categories,tv_email,tv_gender,tv_address,tv_city,tv_state,tv_country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_profile));
        context = getApplicationContext();
        LinearLayout edit_layout = findViewById(R.id.edit_layout);
        intent = getIntent();
        userDetail_Layout = findViewById(R.id.userDetail_Layout);
        fid = intent.getStringExtra("uid");
        ll_dashboard=findViewById(R.id.ll_dashboard);
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        tv_name=findViewById(R.id.tv_name);
        tv_dob=findViewById(R.id.tv_dob);
        tv_categories=findViewById(R.id.tv_categories);
        tv_email=findViewById(R.id.tv_email);
        tv_gender=findViewById(R.id.tv_gender);
        tv_address=findViewById(R.id.tv_address);
        tv_city=findViewById(R.id.tv_city);
        tv_state=findViewById(R.id.tv_state);
        tv_country=findViewById(R.id.tv_country);

        vFeed=findViewById(R.id.rvFeed);
        uid= PrefManager.getLoginDetail(this,"id");
        if(fid == null || fid.equals(uid)) {
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
            username.setText(fname +" "+lname);
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            userbackgroud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"banner_image"), null);
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"img_url"), null);
                }
            });
            edit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            PrefManager.updateUserData(this,null);
            tv_name.setText(fname+" "+lname);
            tv_dob.setText(PrefManager.getLoginDetail(this,"dob"));
            tv_categories.setText(PrefManager.getLoginDetail(this,"category"));
            tv_email.setText(PrefManager.getLoginDetail(this,"email"));
            tv_gender.setText(PrefManager.getLoginDetail(this,"gender"));
            tv_address.setText(PrefManager.getLoginDetail(this,"address"));
            tv_city.setText(PrefManager.getLoginDetail(this,"city"));
            tv_state.setText(PrefManager.getLoginDetail(this,"state"));
            tv_country.setText(PrefManager.getLoginDetail(this,"country"));
            userDetail_Layout.setVisibility(View.GONE);
            fid=uid;
            ll_dashboard.setVisibility(View.VISIBLE);
        }else{
            uid= fid;
            gteUsrDetail(fid);
            ll_dashboard.setVisibility(View.INVISIBLE);
        }
        IncludeShortMenu includeShortMenu = findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(this,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        getFeed(FEED_START);

        vFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Toast.makeText(getApplicationContext(),"Scroll1",Toast.LENGTH_LONG).show();
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    Toast.makeText(getApplicationContext(),"Scroll",Toast.LENGTH_LONG).show();
                    //get the recyclerview position which is completely visible and first
                    int positionView = ((LinearLayoutManager)vFeed.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    Log.i("VISISBLE",   ""+positionView);
                    if (positionView >= 0) {
                        if (oldFocusedLayout != null) {
                            //Stop the previous video playback after new scroll
                            //VideoView vv_dashboard = (VideoView) oldFocusedLayout.findViewById(R.id.vv_dashboard);
                            //vv_dashboard.stopPlayback();
                            Toast.makeText(getApplicationContext(),mValues.get(positionView)+"",Toast.LENGTH_LONG).show();
                        }


                        currentFocusedLayout = ((LinearLayoutManager) vFeed.getLayoutManager()).findViewByPosition(positionView);
                        //VideoView vv_dashboard = (VideoView) currentFocusedLayout.findViewById(R.id.vv_dashboard);
                        //to play video of selected recylerview, videosData is an array-list which is send to recyclerview adapter to fill the view. Here we getting that specific video which is displayed through recyclerview.
                        //playVideo(mValues.get(positionView));
                        Toast.makeText(getApplicationContext(),mValues.get(positionView)+"",Toast.LENGTH_LONG).show();
                        oldFocusedLayout = currentFocusedLayout;
                    }
                }

            }

        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "mss popup",  Toast.LENGTH_LONG).show();
    }
    private void gteUsrDetail(String id){
        String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + id + "&uid=" + uid;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            Log.d(Config.TAG, response);
                            result = new JSONObject(response);
                            String fname=result.getString("fname");
                            String lname=result.getString("lname");
                            final String avatarX=result.getString("avatar");
                            final String backgroundX=result.getString("banner_image");
                            username = findViewById(R.id.username);
                            userimage = findViewById(R.id.userimage);
                            userbackgroud = findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname);
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+"h/250/"+backgroundX).apply(Config.options_background).into(userbackgroud);
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+"250/250/"+avatarX).apply(Config.options_avatar).into(userimage);
                            userbackgroud.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+backgroundX, null);
                                }
                            });
                            userimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+avatarX, null);
                                }
                            });
                            tv_name.setText(fname+" "+lname);
                            tv_dob.setText(result.getString("dob"));
                            tv_categories.setText(result.getString("category"));
                            tv_email.setText(result.getString("email"));
                            tv_gender.setText(result.getString("gender"));
                            tv_address.setText(result.getString("address"));
                            tv_city.setText(result.getString("city"));
                            tv_state.setText(result.getString("state"));
                            tv_country.setText(result.getString("country"));
                            userDetail_Layout.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Config.TAG, error.toString());
                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void  loadFeedList(Integer mStart,Integer mLimit){
        URL_FEED = Config.API_URL+ "feed_service.php?type=AllFeeds&start=" +mStart.toString()+ "&limit=" +mLimit.toString()+ "&fid=" +fid+ "&uid=" +uid+ "&my_id=" +uid;
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

    public void getFeed(int start){
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        URL_FEED = Config.API_URL+ "feed_service.php?type=AllFeeds&start="+start+"&limit="+FEED_LIMIT+"&fid=" +fid+ "&uid=" +uid+ "&my_id=" +uid;
        Log.e(Config.TAG,URL_FEED);
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_FEED,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Prod_detaili_profile",""+response);
                        String detail_name="";
                        int selling_cost=0;
                        int purchese_cost=0;
                        // Process the JSON
                        try{
                              String feedTotal=response.getString("feedTotal");
                              String status=response.optString("status");
                               int is_favrait=0;
                              if(status.equalsIgnoreCase("success")) {
                                  JSONArray jsonArray = response.getJSONArray("data");
                                  for (int i = 0; i < jsonArray.length(); i++) {
                                      JSONObject jsonObject = jsonArray.getJSONObject(i);
                                      String image = jsonObject.getString("first_image");
                                      String detail = jsonObject.getString("detail");

                                      String fimage_path = Config.URL_ROOT + "uploads/avatar/150/150/'" + image;
                                      int id = jsonObject.getInt("id");
                                      String shareid = jsonObject.getString("shareid");
                                      String fullname = jsonObject.getString("fullname");
                                      int uid = jsonObject.getInt("uid");
                                      String avatar = jsonObject.getString("avatar");
                                      String avatar_path = Config.AVATAR_URL + avatar;
                                      String udate = jsonObject.getString("udate");
                                      Long timespam = jsonObject.getLong("timespam");
                                      String is_block = jsonObject.getString("is_block");
                                      String type = jsonObject.getString("type");
                                    /* if (type.equalsIgnoreCase("PRODUCT") || type.equalsIgnoreCase("PROVIDE") || type.equalsIgnoreCase("DEMAND")) {
                                         JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                         String detail_name = jsonObjectdetail.getString("name");
                                         String selling_cost = jsonObjectdetail.getString("selling_cost");
                                         String purchese_cost = jsonObjectdetail.getString("purchese_cost");
                                     } */
                                      ArrayList<String> imageArray = new ArrayList<>();
                                      if (type.equalsIgnoreCase("VIDEO")) {
                                          JSONArray jsonArrayImage = jsonObject.getJSONArray("video_array");
                                          for (int j = 0; j < jsonArrayImage.length(); j++) {
                                              imageArray.add(jsonArrayImage.optString(j));
                                          }
                                      } else  if (type.equalsIgnoreCase("IMAGE")) {
                                          JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                          for (int j = 0; j < jsonArrayImage.length(); j++) {
                                              imageArray.add(jsonArrayImage.optString(j));
                                          }
                                      }
                                      else  if (type.equalsIgnoreCase("PRODUCT")) {
                                          JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                          for (int j = 0; j < jsonArrayImage.length(); j++) {
                                              imageArray.add(jsonArrayImage.optString(j));
                                          }
                                      }
                                      else  if (type.equalsIgnoreCase("PROVIDE")) {
                                          JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                          for (int j = 0; j < jsonArrayImage.length(); j++) {
                                              imageArray.add(jsonArrayImage.optString(j));
                                          }
                                          is_favrait=jsonObject.getInt("is_favrait");
                                      }
                                      else  if (type.equalsIgnoreCase("DEMAND")) {
                                          JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                          for (int j = 0; j < jsonArrayImage.length(); j++) {
                                              imageArray.add(jsonArrayImage.optString(j));
                                          }
                                          is_favrait=jsonObject.getInt("is_favrait");
                                      }
                                      int comment = jsonObject.getInt("comment");
                                      int likes = jsonObject.getInt("likes");
                                      // int is_favrait = jsonObject.getInt("is_favrait");
                                      int mylikes = jsonObject.getInt("mylikes");
                                      //average_rating
                                      int all_rating = jsonObject.getInt("average_rating");
                                      //type all_comment average_rating


                                      if (type.equalsIgnoreCase("IMAGE")) {
                                          fimage_path = Config.URL_ROOT + "uploads/album/150/150/" + image;
                                      } else if (type.equalsIgnoreCase("VIDEO")) {
                                          ///uploads/album/400/500/' /uploads/v_image/'
                                          fimage_path = Config.URL_ROOT + "uploads/video/" + detail;
                                      }
                                      //PRODUCT
                                      else if (type.equalsIgnoreCase("PRODUCT")) {
                                          fimage_path = Config.URL_ROOT + "/uploads/product/400/400/" + image;

                                          JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                          detail_name = jsonObjectdetail.getString("name");
                                          selling_cost = jsonObjectdetail.getInt("selling_cost");
                                          purchese_cost = jsonObjectdetail.getInt("purchese_cost");

                                      } else if (type.equalsIgnoreCase("PROVIDE")) {
                                          fimage_path = Config.URL_ROOT + "/uploads/product/400/400/" + image;

                                          JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                          detail_name = jsonObjectdetail.getString("name");
                                          selling_cost = jsonObjectdetail.getInt("selling_cost");
                                          purchese_cost = jsonObjectdetail.getInt("purchese_cost");
                                      } else if (type.equalsIgnoreCase("DEMAND")) {
                                          fimage_path = Config.URL_ROOT + "/uploads/product/400/400/" + image;

                                          JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                          detail_name = jsonObjectdetail.getString("name");
                                          selling_cost = jsonObjectdetail.getInt("selling_cost");
                                          purchese_cost = jsonObjectdetail.getInt("purchese_cost");
                                      }
                                      Log.d("image_setdata", "" + fimage_path);
                                      int all_comment = jsonObject.getInt("all_comment");
                                      int average_rating = jsonObject.getInt("average_rating");
                                      if (type.equalsIgnoreCase("PRODUCT") || type.equalsIgnoreCase("PROVIDE") || type.equalsIgnoreCase("DEMAND")) {
                                            mValues.add(new FeedModel(id, shareid, fullname, uid, avatar_path, udate, timespam, is_block, imageArray, fimage_path, comment, likes, mylikes, all_rating, type, all_comment, average_rating,detail_name,selling_cost,purchese_cost,is_favrait));
                                      } else{
                                            mValues.add(new FeedModel(id, shareid, fullname, uid, avatar_path, udate, timespam, is_block, imageArray, fimage_path, comment, likes, mylikes, all_rating, type, all_comment, average_rating));
                                      }
                             }
                            adapter=new AllFeedAdapter(ProfileActivity.this,mValues,ProfileActivity.this);
                            vFeed.setLayoutManager(new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.VERTICAL, false));
                             //vFeed.setNestedScrollingEnabled(false);
                                  ViewCompat.setNestedScrollingEnabled(vFeed, false);
                                  //vFeed.setLayoutFrozen(true);
                            vFeed.setAdapter(adapter);
                            //String msg=response.optString("msg");
                           // Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                              }
                              loading.dismiss();
                        }
                        catch (Exception e){
                            loading.dismiss();
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        loading.dismiss();
                        // Do something when error occurred
                        Toast.makeText(ProfileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
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
                    image_path = Config.URL_ROOT + "uploads/v_image/h/300/" + image;
                }else if(feed_type.equalsIgnoreCase("PRODUCT")){
                    image_path = Config.URL_ROOT + "uploads/product/h/300/" + image;
                }else if(feed_type.equalsIgnoreCase("PROVIDE")){
                    image_path = Config.URL_ROOT + "uploads/product/h/300/" + image;
                }else if(feed_type.equalsIgnoreCase("DEMAND")){
                    image_path = Config.URL_ROOT + "uploads/product/h/300/" + image;
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
    @Override
    public void onItemClick(FeedModel item) {

    }
    @Override
    public void onRefresh() {
        FEED_START=0;
        if(!mValues.isEmpty())
        mValues.clear();
        getFeed(FEED_START);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // Stop animation (This will be after 3 seconds)
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 4000); // Delay in millis
    }
      public void loadMore(View view){
          FEED_START=FEED_START+FEED_LIMIT;
          getFeed(FEED_START);
      }
     public void dashboard(View view){
         if (userDetail_Layout.getVisibility() == View.VISIBLE) {
             userDetail_Layout.setVisibility(View.GONE);
         } else {
             userDetail_Layout.setVisibility(View.VISIBLE);
         }
     }
}
