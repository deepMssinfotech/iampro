package com.mssinfotech.iampro.co;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.CartItemAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.data.CartItem;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class CartActivity extends Fragment implements CartItemAdapter.ItemListener,OnClickListener{
    private static final String TAG = "ShoppingCartActivity";
    private static String CART_URL  = "";
    private CartItemAdapter mAdapter;
    ListView lvCartItems;
    Button bClear;
    Button bShop;
    TextView tvTotalPrice;
    private RecyclerView recyclerView;
    private ArrayList<CartItem> CartItemList;
    View view;
    TextView tv_tPrice;
    //ProgressDialog loading = ProgressDialog.show(getContext(),"Processing...","Please wait...",false,false);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_cart, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        //CART_URL  = Config.API_URL+"cart.php?type=refreshCart&uid="+ PrefManager.getLoginDetail(getContext(),"id")+"&ip_address="+Config.IP_ADDRESS;
        recyclerView = view.findViewById(R.id.recycler_view);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        CartItemList = new ArrayList<CartItem>();
        bClear=view.findViewById(R.id.bClear);
        bShop=view.findViewById(R.id.bCheckout);
        tv_tPrice=view.findViewById(R.id.tv_tPrice);
        prepareCart();
        bClear.setOnClickListener(this);
        bShop.setOnClickListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        CART_URL  = Config.API_URL+"cart.php?type=refreshCart&uid="+ PrefManager.getLoginDetail(getContext(),"id")+"&ip_address="+Config.IP_ADDRESS;
          Log.d("CART_URL",""+CART_URL);
        prepareCart();
    }
    public void refreshFregment(){
        //Fragment frg = null;
        //AppCompatActivity activity = (AppCompatActivity) getContext();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
    public void prepareCart() {
        //loading.show()
       CART_URL  = Config.API_URL+"cart.php?type=refreshCart&uid="+ PrefManager.getLoginDetail(getContext(),"id")+"&ip_address="+Config.IP_ADDRESS;
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
                   tvTotalPrice.setText("");
            }
            // notify data changes to list adapater

            mAdapter = new CartItemAdapter(getContext(), CartItemList,CartActivity.this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
    public void onItemClick(CartItem item,int tcost) {
        Toast.makeText(getContext(),""+tcost,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
         //R.id.bClear
        switch (v.getId()) {
            case R.id.bClear:
                if (!PrefManager.isLogin(CartActivity.this.getContext())){
                    Toast.makeText(getContext(),""+"First Login and try again...",Toast.LENGTH_LONG).show();
                    break;
                }
                if (CartItemList.isEmpty()){
                    Toast.makeText(getContext(),""+"Your cart is empty...",Toast.LENGTH_LONG).show();
                    break;
                }
                else
                 clearCart();
                 if (!CartItemList.isEmpty()){
                     CartItemList.removeAll(CartItemList);

                     //CartItemList.remove(i);
                     mAdapter.notifyDataSetChanged();

                 }
               // Toast.makeText(getContext(),""+"clear",Toast.LENGTH_LONG).show();
                break;
            case R.id.bCheckout:
                if (CartItemList.size()>0) {
                    Intent intent = new Intent(getContext(), CheckOutActivity.class);
                    intent.putExtra("cart_item", CartItemList);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(),""+"Your cart is empty...",Toast.LENGTH_LONG).show();
                }
                 //Toast.makeText(getContext(),""+"Checkout",Toast.LENGTH_LONG).show();
                break;
        }
    }
      public void clearCart(){
          String url= Config.API_URL+ "cart.php?type=clear_cart&uid="+PrefManager.getLoginDetail(CartActivity.this.getContext(),"id");
          // Initialize a new RequestQueue instance
          final Dialog loading = new Dialog(getContext());
          loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
          loading.setContentView(R.layout.progress_dialog);
          loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
          loading.show();
          RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this.getContext());

          // Initialize a new JsonObjectRequest instance
          JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                  Request.Method.GET,url,
                  null,
                  new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          // Do something with response
                          //mTextView.setText(response.toString());
                          PrefManager.updateLoginDetail(getContext(),"cart_count","0");
                          Config.count_cart.setText("0");

                          String status=response.optString("status");
                          if (status.equalsIgnoreCase("success")) {
                              CartItemList.clear();
                              mAdapter.notifyDataSetChanged();
                          }
                          String msg=response.optString("msg");
                          Toast.makeText(CartActivity.this.getContext(),""+msg,Toast.LENGTH_LONG).show();
                        if (loading.isShowing())
                             loading.dismiss();
                      }
                  },
                  new Response.ErrorListener(){
                      @Override
                      public void onErrorResponse(VolleyError error){
                          // Do something when error occurred
                          Toast.makeText(CartActivity.this.getContext(),""+error.getMessage(),Toast.LENGTH_LONG).show();
                          if (loading.isShowing())
                               loading.dismiss();
                      }
                  }
          );

          // Add JsonObjectRequest to the RequestQueue
          requestQueue.add(jsonObjectRequest);
      }
//});
      //}
}