package com.mssinfotech.iampro.co.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class AddDemandActivity extends AppCompatActivity {


    TextInputLayout tildemandname,tilbrandname,tilsellingcost,tildemanddetail;
    EditText etprovidename,etbrandname,etsellingcost,etprovidedetail;
    private String providename;String brandname;String sellingcost; String providedetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provide);



    }
}
