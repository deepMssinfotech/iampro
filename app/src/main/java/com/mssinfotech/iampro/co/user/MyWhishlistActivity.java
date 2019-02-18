package com.mssinfotech.iampro.co.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.WhishListAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.WhishListItem;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.helper.WhishListItemTouchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyWhishlistActivity extends Fragment implements WhishListItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private List<WhishListItem> whishListItemList;
    private WhishListAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private static String WHISH_LIST_URL = "";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_my_whishlist, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view =v;
        WHISH_LIST_URL  = Config.API_URL+"app_service.php?type=getall_wishlist&uid="+ PrefManager.getLoginDetail(getContext(),"id");
        recyclerView = view.findViewById(R.id.recycler_view);
        //whishListItemList = new ArrayList<>();
        whishListItemList = new ArrayList<WhishListItem>();
        mAdapter = new WhishListAdapter(getContext(), whishListItemList);
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
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new WhishListItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);

        // making http call and fetching menu json
        prepareWhishList();
    }
    /**
     * method make volley network call and parses json
     */
    private void prepareWhishList() {
        //Log.d(Config.TAG,WHISH_LIST_URL);
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Processing...","Please wait...",false,false);
        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.GET,
                WHISH_LIST_URL , null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //VolleyLog.d(Config.TAG, " Response : " + response.toString());
                if (response != null) {
                    loading.dismiss();
                    //Log.d(Config.TAG,response.toString());
                    parseJsonFeed(response);
                    mAdapter.notifyDataSetChanged();
                }else{
                    loading.dismiss();
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
            if(response.length()>0) {
                //JSONArray feedArray = response.getJSONArray("data");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject feedObj = (JSONObject) response.get(i);
                    WhishListItem item = new WhishListItem();
                    String image_path = "";
                    JSONObject product_detail = feedObj.getJSONObject("product_detail");

                    Log.d(Config.TAG, product_detail.toString());
                    String feed_type = feedObj.getString("product_type");
                    String type_image = "";
                    String image = product_detail.isNull("image") ? null : product_detail.getString("image");
                    if (feed_type.equalsIgnoreCase("IMAGE")) {
                        image_path = Config.URL_ROOT + "uploads/album/450/500/" + image;
                        type_image = "image_icon";
                    } else if (feed_type.equalsIgnoreCase("VIDEO")) {
                        image_path = Config.URL_ROOT + "uploads/v_image/h/300/" + image;
                        type_image = "video_icon";
                    } else if (feed_type.equalsIgnoreCase("PRODUCT")) {
                        image_path = Config.URL_ROOT + "uploads/product/h/300/" + image;
                        type_image = "product_icon";
                    } else if (feed_type.equalsIgnoreCase("PROVIDE")) {
                        image_path = Config.URL_ROOT + "uploads/product/h/300/" + image;
                        type_image = "porvide_icon";
                    } else if (feed_type.equalsIgnoreCase("DEMAND")) {
                        image_path = Config.URL_ROOT + "uploads/product/h/300/" + image;
                        type_image = "demand_icon";
                    }

                    item.setType_image(type_image);
                    item.setId(feedObj.getInt("id"));
                    item.setName(product_detail.getString("name"));
                    item.setImage(image_path);
                    item.setStatus(product_detail.getString("status"));
                    item.setPrice(product_detail.getString("selling_cost"));
                    whishListItemList.add(item);
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
        if (viewHolder instanceof WhishListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = whishListItemList.get(viewHolder.getAdapterPosition()).getName();
            Integer id = whishListItemList.get(viewHolder.getAdapterPosition()).getId();

            // backup of removed item for undo purpose
            final WhishListItem deletedItem = whishListItemList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            String url=Config.API_URL+"app_service.php?type=delete_wishlist&id="+id.toString();
            String responc = function.executeUrl(getContext(),"get",url,null);
            Log.e(Config.TAG,"result : "+responc);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar.make(constraintLayout, name + " removed from whishlist!", Snackbar.LENGTH_LONG);
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
