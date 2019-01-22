package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.data.CartItem;
import com.mssinfotech.iampro.co.data.NotificationItem;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {
    private Context context;
    private List<CartItem> notifyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView from_name, plist_price_text, detail,cart_product_quantity_tv;
        public ImageView list_image;

        public MyViewHolder(View view) {
            super(view);
            list_image = view.findViewById(R.id.list_image);
            plist_price_text = view.findViewById(R.id.plist_price_text);
            from_name = view.findViewById(R.id.from_name);
            cart_product_quantity_tv = view.findViewById(R.id.cart_product_quantity_tv);
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
        holder.plist_price_text.setText("₹" +item.getselling_cost());
        holder.from_name.setText(item.getp_nane());
        holder.cart_product_quantity_tv.setText(item.getqty());

        Glide.with(context).load(item.getp_image()).into(holder.list_image);
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