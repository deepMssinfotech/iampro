package com.mssinfotech.iampro.co.uppertab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.utils.Validate;

/**
 * Created by mssinfotech on 12/01/19.
 */

public class VideoFragment extends Fragment {
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.video_fragment, container, false);
    }
}
