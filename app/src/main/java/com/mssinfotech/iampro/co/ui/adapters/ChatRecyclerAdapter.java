package com.mssinfotech.iampro.co.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.models.Chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    String DateToStrme ,DateToStrother;
    private List<Chat> mChats;

    public ChatRecyclerAdapter(List<Chat> chats) {
        mChats = chats;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);

        String alphabet = chat.sender.substring(0, 1);
         /*
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        DateToStrme = format.format(curDate);
 */
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date currenTimeZone=null;
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(chat.getTimestamp() * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

             currenTimeZone = (Date) calendar.getTime();
            //return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }

        long timestampString =  Long.parseLong(String.valueOf(chat.getTimestamp()));
        String value = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").
                format(new java.util.Date(timestampString * 1000));

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date datet=null;
        try {
             datet = format.parse(chat.getTimestamp()+"");
            //return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.txtUserAlphabet.setText(alphabet);
        //myChatViewHolder.txtTime.setText(sdf.format(currenTimeZone));
            myChatViewHolder.txtTime.setText(DateFormat.format("dd-MM-yyyy |HH:m:s)",chat.getTimestamp()));
       //myChatViewHolder.txtTime.setText(new SimpleDateFormat("dd-MM-yyyy |HH:m:s").format(chat.getTime()));
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);
        String alphabet = chat.sender.substring(0, 1);
/*
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        DateToStrother = format.format(curDate);
*/
        SimpleDateFormat value = new java.text.SimpleDateFormat("dd-MM|HH:m:s");
                //format(new java.util.Date(value));
        String fdate=value.format(new java.util.Date());
        otherChatViewHolder.txtChatMessage.setText(chat.message);
        otherChatViewHolder.txtUserAlphabet.setText(alphabet);
        //otherChatViewHolder.txttTime.setText(fdate);
       otherChatViewHolder.txttTime.setText(DateFormat.format("dd-MM-yyyy |HH:m:s)",chat.getTimestamp()));
        //otherChatViewHolder.txttTime.setText(new SimpleDateFormat("dd-MM-yyyy |HH:m:s").format(chat.getTime()));
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet,txtTime;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txtTime=(TextView)itemView.findViewById(R.id.text_view_user_time);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet,txttTime;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            txttTime=(TextView)itemView.findViewById(R.id.text_view_user_timee);
        }
    }
}
