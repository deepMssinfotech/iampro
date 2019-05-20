package com.mssinfotech.iampro.co.user;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mssinfotech.iampro.co.adapter.FriendRequestSwipeAdapter;
import com.mssinfotech.iampro.co.swipecontroller.SwipeController;
import com.mssinfotech.iampro.co.swipecontroller.SwipeControllerActions;
import com.mssinfotech.iampro.co.user.FriendRequestActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.FriendRequestAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.FriendRequestItem;
import com.mssinfotech.iampro.co.helper.FriendRequestItemTouchHelper;
import com.mssinfotech.iampro.co.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
public class FriendRequestActivity extends Fragment  {
 // implements FriendRequestItemTouchHelper.RecyclerItemTouchHelperListener
    private RecyclerView recyclerView;
    private List<FriendRequestItem> FriendRequestItemList;
    private FriendRequestAdapter mAdapter;
     private FriendRequestSwipeAdapter adapter_swipe;
    private ConstraintLayout constraintLayout;
    private static String NOTIFY_URL  = "";
     SwipeController swipeController = null;
     Context context;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_friendrequest, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        super.onCreate(savedInstanceState);
        context = getContext();
        NOTIFY_URL  = Config.API_URL+"app_service.php?type=view_friend_list&id="+ PrefManager.getLoginDetail(context,"id");
        recyclerView = view.findViewById(R.id.recycler_view);
        FriendRequestItemList = new ArrayList<FriendRequestItem>();
        mAdapter = new FriendRequestAdapter(context, FriendRequestItemList);
        adapter_swipe = new FriendRequestSwipeAdapter(context, FriendRequestItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        //recyclerView.setAdapter(mAdapter);
         //recyclerView.setAdapter(adapter_swipe);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new FriendRequestItemTouchHelper(0, ItemTouchHelper.LEFT, this);
       // new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        // making http call and fetching menu json
        prepareWhishList();


        swipeController = new SwipeController(FriendRequestActivity.this.context,new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                //reject
                //mAdapter.players.remove(position);
                //mAdapter.notifyItemRemoved(position);
                //mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());
                int uid= adapter_swipe.notifyList.get(position).getUser_id();
                int fid=adapter_swipe.notifyList.get(position).getFriend_id();
                int id=adapter_swipe.notifyList.get(position).getId();
                String url=Config.API_URL+ "app_service.php?type=delete_friend&tid="+id;
                function.executeUrl(context,"get",url,null);
                int perNoti = Integer.parseInt(PrefManager.getLoginDetail(context,"panding_friend"))-1;
                PrefManager.updateLoginDetail(context,"panding_friend",(perNoti)+"");
                Config.count_friend_request.setText(perNoti+"");
                adapter_swipe.notifyList.remove(position);
                adapter_swipe.notifyItemRemoved(position);
                adapter_swipe.notifyItemRangeChanged(position,adapter_swipe.getItemCount());
                if(perNoti==0){
                    ImageView no_rodr = view.findViewById(R.id.no_record_found);
                    no_rodr.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
            public void onLeftClicked(int position) {
                //accept
                int uid= adapter_swipe.notifyList.get(position).getUser_id();
                int tid=adapter_swipe.notifyList.get(position).getFriend_id();
                int id=adapter_swipe.notifyList.get(position).getId();
                String url=Config.API_URL+ "app_service.php?type=approve_friend&id="+uid +"&tid="+id;
                function.executeUrl(context,"get",url,null);
                //Toast.makeText(context,"Left Clicked "+position+"- "+id+"/"+tid,Toast.LENGTH_LONG).show();

                int perNoti = Integer.parseInt(PrefManager.getLoginDetail(context,"panding_friend"))-1;
                PrefManager.updateLoginDetail(context,"panding_friend",(perNoti)+"");
                Config.count_friend_request.setText(perNoti+"");
                int newfriend = Integer.parseInt(PrefManager.getLoginDetail(context,"total_count_friend"))+1;
                PrefManager.updateLoginDetail(context,"total_count_friend",(newfriend)+"");
                Config.user_text.setText(newfriend+"");

                adapter_swipe.notifyList.remove(position);
                adapter_swipe.notifyItemRemoved(position);
                adapter_swipe.notifyItemRangeChanged(position,adapter_swipe.getItemCount());
                if(perNoti==0){
                    ImageView no_rodr = view.findViewById(R.id.no_record_found);
                    no_rodr.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

       ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        if (FriendRequestItemList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            ImageView no_rodr = view.findViewById(R.id.no_record_found);
            no_rodr.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            ImageView no_rodr = view.findViewById(R.id.no_record_found);
            no_rodr.setVisibility(View.GONE);
        }
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
                    mAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(context, "Empty Record!", Toast.LENGTH_SHORT).show();
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
                    FriendRequestItem item = new FriendRequestItem();

                    String image_path = "";
                    int is_block=Integer.parseInt(feedObj.optString("is_block"));
                    item.setIs_block(is_block);

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

                    JSONObject friendstatus =feedObj.getJSONObject("friendstatus");

                     String friend_status=friendstatus.optString("friend_status");
                     item.setFriend_status(friend_status);
                    FriendRequestItemList.add(item);
                }
            }else{
                ImageView no_rodr = view.findViewById(R.id.no_record_found);
                no_rodr.setVisibility(View.VISIBLE);
            }
             recyclerView.setAdapter(adapter_swipe);
            // notify data changes to list adapater
        } catch (JSONException e) {
            Log.d(Config.TAG,"printStackTrace 167" + e.getMessage() + "  Error Message");
            e.printStackTrace();
            ImageView no_rodr = view.findViewById(R.id.no_record_found);
            no_rodr.setVisibility(View.VISIBLE);
        }
    }
}
