package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.mssinfotech.iampro.co.adapter.MessageAdapter;
import com.mssinfotech.iampro.co.adapter.NotificationAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.MessageItem;
import com.mssinfotech.iampro.co.data.NotificationItem;
import com.mssinfotech.iampro.co.helper.MessageItemTouchHelper;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements MessageItemTouchHelper.RecyclerItemTouchHelperListener {

        private RecyclerView recyclerView;
    private List<MessageItem> MessageItemList;
    private MessageAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private static String MESSAGE_URL  = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_message));
        String uid=PrefManager.getLoginDetail(this,"id");
        MESSAGE_URL  = Config.API_URL+"chat.php?type=nearuser&uid="+uid+"&my_id="+ uid;
        recyclerView = findViewById(R.id.recycler_view);
        MessageItemList = new ArrayList<MessageItem>();
        mAdapter = new MessageAdapter(this, MessageItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        constraintLayout = findViewById(R.id.constraintLayout);
        // making http call and fetching menu json
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new MessageItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        prepareWhishList();
    }
    /**
     * method make volley network call and parses json
     */
    private void prepareWhishList() {
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,MESSAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        parseJsonFeed(response);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Config.ResponceResult = error.getMessage();
                Log.d(Config.TAG,"error : "+error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void parseJsonFeed(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray heroArray = obj.getJSONArray("list");
            if(heroArray.length()>0) {
                for (int i = 0; i < heroArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) heroArray.get(i);
                    MessageItem item = new MessageItem();
                    String user_image = Config.AVATAR_URL + "80/80/" + feedObj.getString("avatar");
                    item.setid(feedObj.getInt("id"));
                    item.setavatar(user_image);
                    item.setmsg(feedObj.getString("msg"));
                    item.setfullname(feedObj.getString("fullname"));
                    item.setunread(feedObj.getString("unread"));
                    item.setis_block(feedObj.getString("is_block"));
                    MessageItemList.add(item);
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
            if (viewHolder instanceof MessageAdapter.MyViewHolder) {
                // get the removed item name to display it in snack bar
                //String name = MessageItemList.get(viewHolder.getAdapterPosition()).getProduct_name();
                Integer id = MessageItemList.get(viewHolder.getAdapterPosition()).getid();

                // backup of removed item for undo purpose
                final MessageItem deletedItem = MessageItemList.get(viewHolder.getAdapterPosition());
                final int deletedIndex = viewHolder.getAdapterPosition();

                // remove the item from recycler view
                mAdapter.removeItem(viewHolder.getAdapterPosition());
                String url=Config.API_URL+"chat.php?type=delete_my_chat&my_id="+PrefManager.getLoginDetail(this,"id")+"&id="+id.toString();
                String responc = function.executeUrl(getApplicationContext(),"get",url,null);
                Log.e(Config.TAG,"result : "+responc+"url - "+url);
                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(constraintLayout, "All Chat Removed ", Snackbar.LENGTH_LONG);
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
