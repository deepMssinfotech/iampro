package com.mssinfotech.iampro.co;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
 EditText et_comment;
 String user_id;
 String data_type,data_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        et_comment = findViewById(R.id.et_comment);
        if (PrefManager.isLogin(this)) {
            user_id = PrefManager.getLoginDetail(this, "id");
        }
        data_type=getIntent().getExtras().getString("data_type");
        data_id=getIntent().getExtras().getString("data_id");
        getDetail();
        allComment();
    }
    public void getDetail(){
        String myurl = Config.API_URL + "app_service.php?type=get_multi_image_video_detail&id="+data_id+"&update_type="+data_type+"&uid="+user_id+"&login_id="+user_id+"&my_id="+user_id;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            Log.d(Config.TAG, response);
                            result = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Config.TAG, error.toString());
                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void allComment(){

    }
    public void sendComment(View view)
    {
        final String comment=et_comment.getText().toString();
        final String url="https://www.iampro.co/api/app_service.php?type=product_review&data_id="+data_id+"&comment="+comment+"&id="+user_id+"&data_type="+data_type;
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
               url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String msg=response.optString("msg");
                        Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                });

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
