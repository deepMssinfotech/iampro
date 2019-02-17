package com.mssinfotech.iampro.co.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.adapter.MyImageAdapter;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.user.JoinFriendActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.JoinFriendAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.JoinFriendItem;
import com.mssinfotech.iampro.co.helper.JoinFriendItemTouchHelper;
import com.mssinfotech.iampro.co.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinFriendActivity extends AppCompatActivity implements JoinFriendItemTouchHelper.RecyclerItemTouchHelperListener {
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private RecyclerView recyclerView;
    private List<JoinFriendItem> JoinFriendItemList;
    private JoinFriendAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private static String NOTIFY_URL  = "";
    private String URL_FEED = "",uid="";
    Context context;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinfriend);
        intent = getIntent();
        context = getApplicationContext();
        uid= PrefManager.getLoginDetail(this,"id");
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_joinfriend));
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        String id = intent.getStringExtra("uid");
        uid= PrefManager.getLoginDetail(this,"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
            username.setText("My Friends");
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
            PrefManager.updateUserData(this,null);
        }else{
            uid= id;
            gteUsrDetail(id);
        }

        IncludeShortMenu includeShortMenu = findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(this,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);

        NOTIFY_URL  = Config.API_URL+"app_service.php?type=view_friend_list&id="+ PrefManager.getLoginDetail(this,"id")+"&my_id="+ PrefManager.getLoginDetail(this,"id")+"&status=2";
        recyclerView = findViewById(R.id.recycler_view);
        JoinFriendItemList = new ArrayList<JoinFriendItem>();
        //mAdapter = new JoinFriendAdapter(this, JoinFriendItemList);
        constraintLayout = findViewById(R.id.constraintLayout);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new JoinFriendItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        // making http call and fetching menu json
        prepareWhishList();
        getJoinedFriend();
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
                            String fname=result.optString("fname");
                            String lname=result.optString("lname");
                            final String avatarX=result.getString("avatar");
                            final String backgroundX=result.getString("banner_image");
                            username = findViewById(R.id.username);
                            userimage = findViewById(R.id.userimage);
                            userbackgroud = findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Friends");
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

    /**
     * method make volley network call and parses json
     */
    private void prepareWhishList() {
        //Log.d(Config.TAG,WHISH_LIST_URL);
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET,
                NOTIFY_URL  , null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    parseJsonFeed(response);
                   // mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(JoinFriendActivity.this, "Empty Record!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Config.TAG,"onErrorResponse 96 : "+error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
    private void parseJsonFeed(JSONArray response) {
        try {
            //JSONArray feedArray = response.getJSONArray("data");
            if(response.length()>0) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject feedObj = (JSONObject) response.get(i);
                    JoinFriendItem item = new JoinFriendItem();

                    String image_path = "";
                    JSONObject user_detail = feedObj.getJSONObject("user_detail");
                    String user_image = Config.AVATAR_URL + "80/80/" + user_detail.getString("avatar");
                    item.setId(feedObj.getInt("id"));
                    item.setAvatar(user_image);
                    item.setUser_id(feedObj.getInt("user_id"));
                    item.setFriend_id(feedObj.getInt("friend_id"));
                    item.setCategory(user_detail.getString("category"));
                    item.setCity(user_detail.getString("city"));
                    item.setState(user_detail.getString("state"));
                    item.setCountry(user_detail.getString("country"));
                    item.setFullname(user_detail.getString("fullname"));
                    item.setUsername(user_detail.getString("username"));
                    item.setTotal_friend(feedObj.getInt("total_friend"));
                    item.setTotal_img(feedObj.getInt("total_img"));
                    item.setTotal_product(feedObj.getInt("total_product"));
                    item.setTotal_product_provide(feedObj.getInt("total_product_provide"));
                    item.setTotal_product_demand(feedObj.getInt("total_product_demand"));
                    item.setTotal_video(feedObj.getInt("total_video"));

                    JoinFriendItemList.add(item);
                }
            }else{
                ImageView no_rodr = findViewById(R.id.no_record_found);
                no_rodr.setVisibility(View.VISIBLE);
            }

            // notify data changes to list adapater
        } catch (JSONException e) {

            Log.d(Config.TAG,"printStackTrace 167" + e.getMessage() + "  Error Message");
            e.printStackTrace();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof JoinFriendAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            Integer id = JoinFriendItemList.get(viewHolder.getAdapterPosition()).getId();
            Integer Userid = JoinFriendItemList.get(viewHolder.getAdapterPosition()).getUser_id();
            Integer Friendid = JoinFriendItemList.get(viewHolder.getAdapterPosition()).getFriend_id();

            // backup of removed item for undo purpose
            final JoinFriendItem deletedItem = JoinFriendItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            String url=Config.API_URL+"app_service.php?type=delete_friend&id="+Friendid.toString()+"&tid="+id.toString();
            String responc = function.executeUrl(getApplicationContext(),"get",url,null);
            Log.e(Config.TAG,"result : "+responc+"url - "+url);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(constraintLayout, "Notification removed ", Snackbar.LENGTH_LONG);
            snackbar.setAction("Close", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    //mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void getJoinedFriend(){
        String url="https://www.iampro.co/api/app_service.php?type=view_friend_list&id="+uid+"&status=2&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                  JSONObject user_detaiis=student.getJSONObject("user_detail");
                                    String id=user_detaiis.getString("id");
                                 String fname=user_detaiis.getString("fname");
                                 String lname=user_detaiis.getString("lname");
                                String avatar=user_detaiis.getString("avatar");
                                String category=user_detaiis.getString("category");

                                    JoinFriendItemList.add(new JoinFriendItem(avatar,fname,category));
                            }
                            Log.d("bdm",singleItem.toString());
                            // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("allsampledatav",JoinFriendItemList.toString());
                             mAdapter= new JoinFriendAdapter(getApplicationContext(),JoinFriendItemList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setNestedScrollingEnabled(false);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
}
