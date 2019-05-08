package com.mssinfotech.iampro.co;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mssinfotech.iampro.co.adapter.CartItemAdapter;
import com.mssinfotech.iampro.co.adapter.CheckOutAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.data.CartItem;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckOutActivity extends AppCompatActivity implements CheckOutAdapter.ItemListener {
    private ArrayList<CartItem> CartItemList;
    CheckOutAdapter mAdapter;
     Button btn_placeOrder;

    TextView tvTotalPrice;
    private RecyclerView recyclerView;
     private static String CART_URL  = "";
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        CartItemList=(ArrayList<CartItem>)getIntent().getSerializableExtra("cart_item");
        Log.d("cart_item",""+CartItemList);
         btn_placeOrder=findViewById(R.id.btn_placeOrder);
        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        prepareCart();
    }


    public void prepareCart() {
        //loading.show()
        CART_URL  = Config.API_URL+"cart.php?type=refreshCart&uid="+ PrefManager.getLoginDetail(CheckOutActivity.this,"id")+"&ip_address="+Config.IP_ADDRESS;
        Log.d("CART_URL2",""+CART_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,CART_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //loading.dismiss();
                        parseJsonFeed(response);
                        /* if (CartItemList.size()>0)
                         mAdapter.notifyDataSetChanged();  */
                        //parseJsonFeed(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loading.dismiss();
                Config.ResponceResult = error.getMessage();
                Log.d(Config.TAG,"error : "+error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void parseJsonFeed(String response) {
        //Toast.makeText(getContext(),""+response,Toast.LENGTH_LONG).show();
        Log.d("CART_URL",""+CART_URL);
        if (!CartItemList.isEmpty()){
            CartItemList.clear();
        }
        Log.d("CART_URL_resp",""+response);
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
                Log.d("CartItemList",""+CartItemList);

            }else{
                ImageView no_rodr = view.findViewById(R.id.no_record_found);
                no_rodr.setVisibility(View.VISIBLE);
            }
            // notify data changes to list adapater

            mAdapter = new CheckOutAdapter(CheckOutActivity.this, CartItemList,CheckOutActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CheckOutActivity.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(CheckOutActivity.this, DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.d(Config.TAG,"printStackTrace 167" + e.getMessage() + "  Error Message");
            e.printStackTrace();
            ImageView no_rodr = view.findViewById(R.id.no_record_found);
            no_rodr.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(CartItem item, int tcost) {

    }
}
