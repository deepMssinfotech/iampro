package com.mssinfotech.iampro.co.user;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
public class JoinFriendActivity extends AppCompatActivity implements JoinFriendItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<JoinFriendItem> JoinFriendItemList;
    private JoinFriendAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private static String NOTIFY_URL  = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinfriend);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_joinfriend));
        NOTIFY_URL  = Config.API_URL+"app_service.php?type=view_friend_list&id="+ PrefManager.getLoginDetail(this,"id")+"&my_id="+ PrefManager.getLoginDetail(this,"id")+"&status=2";
        recyclerView = findViewById(R.id.recycler_view);
        JoinFriendItemList = new ArrayList<JoinFriendItem>();
        mAdapter = new JoinFriendAdapter(this, JoinFriendItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        constraintLayout = findViewById(R.id.constraintLayout);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new JoinFriendItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        // making http call and fetching menu json
        prepareWhishList();

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

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
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

}
