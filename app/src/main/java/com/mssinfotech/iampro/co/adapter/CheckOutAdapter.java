package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 08/05/19.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.CartItem;
import com.mssinfotech.iampro.co.data.NotificationItem;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.user.ChangePasswordActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static java.security.AccessController.getContext;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.MyViewHolder> {
    private Context context;
    private List<CartItem> notifyList;
    static int tcost=0;
    protected ItemListener itemListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView from_name,plist_price_text,detail,cart_product_quantity_tv,product_id;
        public ImageView list_image;
        public ImageView cart_minus_img,cart_plus_img;
        CartItem item;
        public MyViewHolder(View view) {
            super(view);
            product_id = view.findViewById(R.id.product_id);
            list_image = view.findViewById(R.id.list_image);
            plist_price_text = view.findViewById(R.id.plist_price_text);
            from_name = view.findViewById(R.id.from_name);
            cart_product_quantity_tv = view.findViewById(R.id.cart_product_quantity_tv);
            cart_minus_img = view.findViewById(R.id.cart_minus_img);
            cart_plus_img = view.findViewById(R.id.cart_plus_img);
        }
        public void setData(final CartItem item) {
            this.item = item;
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"The Item Clicked is: "+getPosition(), Toast.LENGTH_SHORT).show();
            if (itemListener != null) {
                itemListener.onItemClick(item,tcost);
            }
        }
    }
    public CheckOutAdapter(Context context, List<CartItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }
    public CheckOutAdapter(Context context, List<CartItem> notifyList,ItemListener itemListener) {
        this.context = context;
        this.notifyList = notifyList;
        this.itemListener=itemListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_single_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CartItem item = notifyList.get(position);
        final int positions=position;
        holder.plist_price_text.setText("₹ " +item.getselling_cost());
        tcost=Integer.parseInt(item.getselling_cost());
        holder.from_name.setText(item.getp_nane());
        holder.cart_product_quantity_tv.setText(item.getqty());
        Glide.with(context).load(item.getp_image()).into(holder.list_image);
        holder.product_id.setText(item.getid().toString());

        holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Integer position=getPosition();
                final CartItem item = notifyList.get(positions);
                String price=item.getselling_cost();
                Integer qty= Integer.valueOf(item.getqty())+1;
                Integer id = Integer.valueOf(item.id);
                notifyList.get(positions).setqty(String.valueOf(qty));

                tcost=tcost+Integer.parseInt(item.getselling_cost());

                Log.d("cartitem_count",String.valueOf(qty));
                addtocart(context,String.valueOf(id),String.valueOf(qty),price);
                //TextView cart_product_quantity = v.getRootView().findViewById(R.id.cart_product_quantity_tv);
                //cart_product_quantity.setText(qty);
                //notifyItemChanged(position);
                //notifyDataSetChanged();
                refreshCart(PrefManager.getLoginDetail(context,"id"));
                AppCompatActivity activity = (AppCompatActivity)context;
                CartActivity fragment = new CartActivity();
                Bundle args = new Bundle();
                args.putString("name", "mragank");
                //((AppCompatActivity) context).finish();
                //removeFragment(context,fragment,args);
                loadFragment(context,fragment,args);
                //((AppCompatActivity) context).finish();

                holder.plist_price_text.setText("₹ " +tcost);
                holder.from_name.setText(item.getp_nane());
                holder.cart_product_quantity_tv.setText(item.getqty());
                Glide.with(context).load(item.getp_image()).into(holder.list_image);
                holder.product_id.setText(item.getid().toString());
            }
        });
        holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CartItem item = notifyList.get(positions);
                String price=item.getselling_cost();
                Integer qty= Integer.valueOf(item.getqty())-1;
                Integer id = Integer.valueOf(item.id);
                Log.d("cartitem_countm",String.valueOf(qty));
                String url = Config.API_URL+ "cart.php?type=update_cart&id="+id.toString()+"&qty="+qty.toString()+"&price="+price+"&uid="+ PrefManager.getLoginDetail(context,"id") +"&ip_address="+ Config.IP_ADDRESS;
                //Toast.makeText(itemView.getContext(), id.toString()+"--"+qty.toString()+"-Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                if(qty==0){
                    notifyList.remove(positions);
                    notifyDataSetChanged();
                    //removeItem(positions);
                    url = Config.API_URL+ "cart.php?type=delete_cart_item&id="+id.toString();
                    removeFromCart(url);
                }else{

                }
                //function.executeUrl(context,"get", url, null);
                if (qty!=0) {
                    notifyList.get(positions).setqty(String.valueOf(qty));
                    removeFromCart(url);
                    holder.plist_price_text.setText("");
                } //
                refreshCart(PrefManager.getLoginDetail(context,"id"));
                //TextView cart_product_quantity = v.getRootView().findViewById(R.id.cart_product_quantity_tv);
                //cart_product_quantity.setText(qty);
                //notifyItemChanged(position);
                //notifyDataSetChanged();
                AppCompatActivity activity = (AppCompatActivity)context;
                CartActivity fragment = new CartActivity();
                Bundle args = new Bundle();
                args.putString("name", "mragank");
                args.putString("uid",PrefManager.getLoginDetail(context,"id"));
                //removeFragment(context,fragment,args);
                loadFragment(context,fragment,args);
                //((AppCompatActivity) context).finish();
                tcost=tcost-Integer.parseInt(item.getselling_cost());
                holder.plist_price_text.setText("₹ " +tcost);
                holder.from_name.setText(item.getp_nane());
                holder.cart_product_quantity_tv.setText(item.getqty());
                Glide.with(context).load(item.getp_image()).into(holder.list_image);
                holder.product_id.setText(item.getid().toString());
                //function.loadFragment(context,fragment,args);
                // fragment.prepareCart();
            }
        });
    }
    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public void removeItem(int position) {
        notifyList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(CartItem item, int position) {
        notifyList.add(position, item);
        notifyItemInserted(position);
        item.gettotal_rate();
    }
    public  void addtocart(final Context context, String pid, String qty, String price){
        String url = Config.API_URL+ "cart.php?type=addtocart&p_type=product&pid="+pid+"&qty="+qty+"&price="+price+"&uid="+ PrefManager.getLoginDetail(context,"id") +"&ip_address="+ Config.IP_ADDRESS;
        Log.d(Config.TAG+"cart",url);
        //function.executeUrl(context,"get", url, null);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status=response.optString("status");
                            // tcost=tcost+Integer.parseInt(selling_cost);
                            //plist_price_text
                            // Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public void removeFromCart(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status=response.optString("status");

                            //Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public void loadFragment(Context context, Fragment fragment, Bundle args) {
        // create a FragmentManager
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager(); //getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        if(args != null){
            fragment.setArguments(args);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        //fragmentTransaction.add(android.R.id.content, fragment);

        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        //fragmentTransaction.add(android.R.id.content, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }
    public void removeFragment(Context context, Fragment fragment, Bundle args){
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager(); //getFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        if(args != null){
            fragment.setArguments(args);
        }
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.remove(fragment);

        fragmentTransaction.commit(); // save the changes
    }
    public void refreshCart(String uid){

        String url=Config.API_URL+ "cart.php?type=refreshCart&uid="+uid+"&ip_address="+Config.IP_ADDRESS;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            String status=response.optString("status");
                            //selling_cost
                            JSONArray jsonArray=response.getJSONArray("all_product");
                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String selling_cost = jsonObject1.optString("selling_cost");
                            }
                            //plist_price_text
                            //Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public interface ItemListener {
        void onItemClick(CartItem item,int tcost);
    }
}