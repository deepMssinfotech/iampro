package com.mssinfotech.iampro.co;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
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
