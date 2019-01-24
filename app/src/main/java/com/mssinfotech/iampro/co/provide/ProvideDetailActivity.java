package com.mssinfotech.iampro.co.provide;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.mssinfotech.iampro.co.R;

public class ProvideDetailActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_detail);

        //Set toolbar title
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        //collapsingToolbar.setTitle("Product");
    }
}