package com.mssinfotech.iampro.co;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

public class IncludeFooter  extends RelativeLayout {
    private LayoutInflater inflater;
    private boolean isLogin = false;
    public IncludeFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_footer, this, true);

        ((ImageView)this.findViewById(R.id.btn_menu_more)).setOnClickListener(moreOnClickListener);
        ((ImageView)this.findViewById(R.id.btn_menu_search)).setOnClickListener(searchOnClickListener);
        ((ImageView)this.findViewById(R.id.btn_menu_cart)).setOnClickListener(cartOnClickListener);
        ((ImageView)this.findViewById(R.id.btn_menu_iampro)).setOnClickListener(iamproOnClickListener);
        ((ImageView)this.findViewById(R.id.btn_menu_notice)).setOnClickListener(noticeOnClickListener);
        ((ImageView)this.findViewById(R.id.btn_menu_message)).setOnClickListener(messageOnClickListener);
        ((ImageView)this.findViewById(R.id.btn_menu_user)).setOnClickListener(userOnClickListener);
        if(PrefManager.isLogin(getContext())){
            isLogin = true;
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(getContext(),"img_url");
            Picasso.get()
                    .load(avatar)
                    .placeholder(R.drawable.iampro)
                    .error(R.drawable.image)
                    .transform(new CircleTransform())
                    .into((ImageView)this.findViewById(R.id.btn_menu_user));
        }
    }
    private OnClickListener moreOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //getContext().startActivity(new Intent(getContext(), ScreenshotActivity.class));
        }
    };
    private OnClickListener searchOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private OnClickListener cartOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), CartActivity.class));
        }
    };
    private OnClickListener iamproOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), HomeActivity.class));
        }
    };
    private OnClickListener noticeOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), NotificationActivity.class));
        }
    };
    private OnClickListener messageOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), MessageActivity.class));
        }
    };
    private OnClickListener userOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(isLogin)
                getContext().startActivity(new Intent(getContext(), ProfileActivity.class));
            else
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
        }
    };


}
