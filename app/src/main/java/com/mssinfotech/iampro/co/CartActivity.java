package com.mssinfotech.iampro.co;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.mssinfotech.iampro.co.adapter.CartItemAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.data.CartItem;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class CartActivity extends Fragment {
    private static final String TAG = "ShoppingCartActivity";
    private static String CART_URL  = "";
    private CartItemAdapter mAdapter;
    ListView lvCartItems;
    Button bClear;
    Button bShop;
    TextView tvTotalPrice;
    private RecyclerView recyclerView;
    private List<CartItem> CartItemList;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_cart, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        CART_URL  = Config.API_URL+"cart.php?type=refreshCart&uid="+ PrefManager.getLoginDetail(getContext(),"id")+"&ip_address="+Config.IP_ADDRESS;
        recyclerView = view.findViewById(R.id.recycler_view);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        CartItemList = new ArrayList<CartItem>();
        mAdapter = new CartItemAdapter(getContext(), CartItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareCart();
    }
    public void refreshFregment(){
        //Fragment frg = null;
        //AppCompatActivity activity = (AppCompatActivity) getContext();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    public void prepareCart() {
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,CART_URL,
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
            JSONObject jsonObject = new JSONObject(response);
            tvTotalPrice.setText("₹ " +jsonObject.getString("total"));
            JSONObject obj = new JSONObject(response.toString());
            JSONArray heroArray = obj.getJSONArray("all_product");
            if(heroArray.length()>0) {
                for (int i = 0; i < heroArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) heroArray.get(i);
                    CartItem item = new CartItem();
                    String product_image = feedObj.isNull("p_image") ? null : feedObj.getString("p_image");
                    String image_path = Config.URL_ROOT + "uploads/product/h/300/" + product_image;
                    item.setp_image(image_path);
                    item.setid(feedObj.getInt("id"));
                    item.setuid(feedObj.getString("uid"));
                    item.setpid(feedObj.getString("pid"));
                    item.setqty(feedObj.getString("qty"));
                    item.setunit_rate(feedObj.getString("unit_rate"));
                    item.settotal_rate(feedObj.getString("total_rate"));
                    item.setp_type(feedObj.getString("p_type"));
                    item.setudate(feedObj.getString("udate"));
                    item.setstatus(feedObj.getString("status"));
                    item.setp_cat(feedObj.getString("p_cat"));
                    item.setp_nane(feedObj.getString("p_name"));
                    item.setselling_cost(feedObj.getString("selling_cost"));
                    CartItemList.add(item);
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
}