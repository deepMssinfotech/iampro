package com.mssinfotech.iampro.co;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class IncludeHeader extends RelativeLayout {
    private LayoutInflater inflater;

    public IncludeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_header, this, true);

        ((ImageView)this.findViewById(R.id.browser)).setOnClickListener(browserOnClickListener);
        ((ImageView)this.findViewById(R.id.iampro)).setOnClickListener(iamproOnClickListener);
        ((ImageView)this.findViewById(R.id.wallet)).setOnClickListener(walletOnClickListener);
    }

    private OnClickListener browserOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //getContext().startActivity(new Intent(getContext(), ScreenshotActivity.class));
        }
    };

    private OnClickListener iamproOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //getContext().startActivity(new Intent(getContext(), LiveViewActivity.class));
        }
    };

    private OnClickListener walletOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //getContext().startActivity(new Intent(getContext(), MapViewActivity.class));
        }
    };
}