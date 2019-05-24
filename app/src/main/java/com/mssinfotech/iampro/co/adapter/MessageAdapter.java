package com.mssinfotech.iampro.co.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.ChatToUser;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.data.MessageItem;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context context;
    private List<MessageItem> notifyList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time, detail;
        public TextView count_chat;
        public RelativeLayout viewBackground, viewForeground;
        public CircleImageView user_image;
        FrameLayout message_item;
        public MyViewHolder(View view) {
            super(view);
            //product_name = view.findViewById(R.id.product_name);
            //user_name = view.findViewById(R.id.user_count);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            count_chat = view.findViewById(R.id.count_chat);
            user_image = view.findViewById(R.id.user_image);
            detail = view.findViewById(R.id.detail);
            title = view.findViewById(R.id.title);
            time = view.findViewById(R.id.time);
            message_item=view.findViewById(R.id.message_item);
        }
    }
    public MessageAdapter(Context context, List<MessageItem> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_message_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final MessageItem item = notifyList.get(position);
        holder.detail.setText(item.getmsg());
        holder.title.setText(item.getfullname());
        holder.count_chat.setText(item.getunread());
        if((item.getunread()).equals("0")){
            holder.count_chat.setVisibility(View.GONE);
        }
        Glide.with(context).load(item.getavatar()).into(holder.user_image);

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"detail",Toast.LENGTH_LONG).show();
            }
        });
        holder.message_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatToUser.class);
                 intent.putExtra("id",notifyList.get(position).getid().toString());
                 context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public void removeItem(int position) {
        notifyList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
    public void restoreItem(MessageItem item, int position) {
        notifyList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}