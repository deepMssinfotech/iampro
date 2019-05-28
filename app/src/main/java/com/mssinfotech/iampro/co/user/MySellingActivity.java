package com.mssinfotech.iampro.co.user;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;

public class MySellingActivity extends Fragment {
    View view;
     ImageView no_record_found;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_my_selling, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        no_record_found=v.findViewById(R.id.no_record_found);
        no_record_found.setVisibility(View.VISIBLE);
    }
}
