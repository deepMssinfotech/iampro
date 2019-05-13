package com.mssinfotech.iampro.co.common;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.SearchedActivity;

public class IncludeHeader extends RelativeLayout {
    private LayoutInflater inflater;

    public IncludeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_header, this, true);
        ((ImageView)this.findViewById(R.id.browser)).setOnClickListener(browserOnClickListener);
        ((ImageView)this.findViewById(R.id.iampro)).setOnClickListener(iamproOnClickListener);
        ((ImageView)this.findViewById(R.id.wallet)).setOnClickListener(walletOnClickListener);
        this.findViewById(R.id.tvsearchbox).setOnClickListener(searchboxOnClickListener);
    }

    private OnClickListener browserOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            function.OpenBrowser(getContext(),Config.URL_ROOT);
        }
    };
    //searchboxOnClickListener
    private OnClickListener searchboxOnClickListener = new OnClickListener() {
     public void onClick(View v) {
         SearchActivity searchedActivity=new SearchActivity();
         function.loadFragment(getContext(),searchedActivity,null);
     }
    };

    private OnClickListener iamproOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            function.OpenBrowser(getContext(),Config.URL_ROOT);
        }
    };

    private OnClickListener walletOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            function.OpenWallet(getContext());
        }
    };
}