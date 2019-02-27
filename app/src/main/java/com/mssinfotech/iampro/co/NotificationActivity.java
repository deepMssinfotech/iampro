package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
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
import com.mssinfotech.iampro.co.adapter.NotificationAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.NotificationItem;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.helper.NotificationItemTouchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends Fragment implements NotificationItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<NotificationItem> NotificationItemList;
    private NotificationAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private static String NOTIFY_URL  = "";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_notification, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v ;
        NOTIFY_URL  = Config.API_URL+"app_service.php?type=getall_notification&uid="+ PrefManager.getLoginDetail(getContext(),"id");
        recyclerView = view.findViewById(R.id.recycler_view);
        NotificationItemList = new ArrayList<NotificationItem>();
        mAdapter = new NotificationAdapter(getContext(), NotificationItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new NotificationItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        // making http call and fetching menu json
        prepareWhishList();
    }
    /**
     * method make volley network call and parses json
     */
    private void prepareWhishList() {
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Processing...","Please wait...",false,false);
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET,
                NOTIFY_URL  , null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    loading.dismiss();
                    parseJsonFeed(response);
                    mAdapter.notifyDataSetChanged();
                }else{
                    loading.dismiss();
                    Toast.makeText(getContext(), "Empty Record!", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
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
                    NotificationItem item = new NotificationItem();

                    String image_path = "";
                    JSONObject product_detail = feedObj.getJSONObject("product_detail");
                    JSONObject notifi_user_detail = feedObj.getJSONObject("notifi_user_detail");
                    Log.d(Config.TAG, "mss product_detail : " + product_detail.toString());
                    Log.d(Config.TAG, "mss notifi_user_detail : " + notifi_user_detail.toString());
                    String feed_type = feedObj.getString("p_type");
                    String type_image = "";
                    String product_image = product_detail.isNull("image") ? null : product_detail.getString("image");
                    if (feed_type.equalsIgnoreCase("IMAGE")) {
                        image_path = Config.URL_ROOT + "uploads/album/450/500/" + product_image;
                        type_image = "image_icon";
                    } else if (feed_type.equalsIgnoreCase("VIDEO")) {
                        image_path = Config.URL_ROOT + "uploads/v_image/h/300/" + product_image;
                        type_image = "video_icon";
                    } else if (feed_type.equalsIgnoreCase("PRODUCT")) {
                        image_path = Config.URL_ROOT + "uploads/product/h/300/" + product_image;
                        type_image = "product_icon";
                    } else if (feed_type.equalsIgnoreCase("PROVIDE")) {
                        image_path = Config.URL_ROOT + "uploads/product/h/300/" + product_image;
                        type_image = "porvide_icon";
                    } else if (feed_type.equalsIgnoreCase("DEMAND")) {
                        image_path = Config.URL_ROOT + "uploads/product/h/300/" + product_image;
                        type_image = "demand_icon";
                    }
                    String user_image = Config.AVATAR_URL + "80/80/" + notifi_user_detail.getString("avatar");

                    item.setId(feedObj.getInt("id"));
                     item.setPid(product_detail.getString("id"));
                     //item.setAlbemId(product_detail.getString("albemid"));
                    item.setDetail(notifi_user_detail.getString("fullname") + " " + feedObj.getString("n_type") + " on your " + feed_type);
                     item.setUid(notifi_user_detail.getString("id"));
                    item.setUser_image(user_image);
                    item.setProduct_image(image_path);
                    Log.d(Config.TAG, "image :" + image_path + "--" + user_image);
                    item.setNotify_type(feedObj.getString("n_type"));
                     item.setP_type(feedObj.getString("p_type"));

                    item.setUdate(feedObj.getString("ndate"));
                    item.setUser_name(notifi_user_detail.getString("fullname"));
                    item.setProduct_name(product_detail.getString("name"));
                    NotificationItemList.add(item);
                }
            }else{
                ImageView no_rodr = view.findViewById(R.id.no_record_found);
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
        if (viewHolder instanceof NotificationAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = NotificationItemList.get(viewHolder.getAdapterPosition()).getProduct_name();
            Integer id = NotificationItemList.get(viewHolder.getAdapterPosition()).getId();

            // backup of removed item for undo purpose
            final NotificationItem deletedItem = NotificationItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            String url=Config.API_URL+"app_service.php?type=delete_notification&id="+id.toString();
            String responc = function.executeUrl(getContext(),"get",url,null);
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
