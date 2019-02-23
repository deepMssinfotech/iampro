package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.CartItem;
import com.mssinfotech.iampro.co.data.NotificationItem;
import com.mssinfotech.iampro.co.user.ChangePasswordActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import java.util.List;

import static java.security.AccessController.getContext;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {
    private Context context;
    private List<CartItem> notifyList;


    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView from_name, plist_price_text, detail,cart_product_quantity_tv, product_id;
        public ImageView list_image;
        public ImageView cart_minus_img,cart_plus_img;
        public MyViewHolder(View view) {
            super(view);
            product_id = view.findViewById(R.id.product_id);
            list_image = view.findViewById(R.id.list_image);
            plist_price_text = view.findViewById(R.id.plist_price_text);
            from_name = view.findViewById(R.id.from_name);
            cart_product_quantity_tv = view.findViewById(R.id.cart_product_quantity_tv);
            cart_minus_img = view.findViewById(R.id.cart_minus_img);
            cart_minus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer position=getPosition();
                    final CartItem item = notifyList.get(position);
                    String price=item.getselling_cost();
                    Integer qty= Integer.valueOf(item.getqty())-1;
                    Integer id = Integer.valueOf(item.id);
                    String url = "https://www.iampro.co/api/cart.php?type=update_cart&id="+id.toString()+"&qty="+qty.toString()+"&price="+price+"&uid="+ PrefManager.getLoginDetail(itemView.getContext(),"id") +"&ip_address="+ Config.IP_ADDRESS;
                    //Toast.makeText(itemView.getContext(), id.toString()+"--"+qty.toString()+"-Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
                    if(qty==0){
                        removeItem(position);
                        url = "https://www.iampro.co/api/cart.php?type=delete_cart_item&id="+id.toString();
                    }else{

                    }
                    function.executeUrl(itemView.getContext(),"get", url, null);
                    //TextView cart_product_quantity = v.getRootView().findViewById(R.id.cart_product_quantity_tv);
                    //cart_product_quantity.setText(qty);
                    notifyItemChanged(position);

                }
            });
            cart_plus_img = view.findViewById(R.id.cart_plus_img);
            cart_plus_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer position=getPosition();
                    final CartItem item = notifyList.get(position);
                    String price=item.getselling_cost();
                    Integer qty= Integer.valueOf(item.getqty())+1;
                    Integer id = Integer.valueOf(item.id);
                    String url = "https://www.iampro.co/api/cart.php?type=update_cart&id="+id.toString()+"&qty="+qty.toString()+"&price="+price+"&uid="+ PrefManager.getLoginDetail(itemView.getContext(),"id") +"&ip_address="+ Config.IP_ADDRESS;
                    function.executeUrl(itemView.getContext(),"get", url, null);
                    //TextView cart_product_quantity = v.getRootView().findViewById(R.id.cart_product_quantity_tv);
                    //cart_product_quantity.setText(qty);
                    notifyItemChanged(position);
                }
            });
        }
        @Override
        public void onClick(View v) {
            Toast.makeText(context,"The Item Clicked is: "+getPosition(), Toast.LENGTH_SHORT).show();
        }
    }


    public CartItemAdapter(Context context, List<CartItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_single_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CartItem item = notifyList.get(position);
        holder.plist_price_text.setText("₹ " +item.getselling_cost());
        holder.from_name.setText(item.getp_nane());
        holder.cart_product_quantity_tv.setText(item.getqty());
        Glide.with(context).load(item.getp_image()).into(holder.list_image);
        holder.product_id.setText(item.getid().toString());
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
    }
}