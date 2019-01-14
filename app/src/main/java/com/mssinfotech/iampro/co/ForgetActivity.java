package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class ForgetActivity extends AppCompatActivity   implements View.OnClickListener{
    private Button btnRegister;
    TextView btnLogin,btnforgetProcess;
    private TextInputLayout tilemail;
    private EditText etemail;
    Random r = new Random();
    public static int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnforgetProcess = findViewById(R.id.btnforgetProcess);
        btnforgetProcess.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btnLogin:
                Intent i_login = new Intent(ForgetActivity.this, LoginActivity.class);
                ForgetActivity.this.startActivity(i_login);
                break;
            case R.id.btnforgetProcess:
                // get click action
                Log.d("btnforgetProcess","btnforgetProcess");
                if (validateemail()) {
                    Log.d("btnforgetProcess","btnforgetProcess");
                    sendOtp(v);
                }
                break;
            default:
                break;
        }
    }
    public void sendOtp(View v){
        if (!Config.haveNetworkConnection(this)){
            Config.showInternetDialog(this);
            return;
        }
        randomNumber = r.nextInt(10000);
        final String url=Config.API_URL+"ajax.php";
        //tv.setText(String.valueOf(randomNumber));
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject result = null;
                        loading.dismiss();
                        try {

                            //Parsing the fetched Json String to JSON Object
                            result = new JSONObject(s);
                            String status=result.getString("status");
                            String msg=result.getString("msg");
                            if(status.equals("success")) {
                                String vcode = result.getString("vcode");
                                Log.d("Lresponse", "" + vcode);
                                Intent intent = new Intent(getApplicationContext(), OtpForgetActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("vcode", vcode);
                                intent.putExtra("email", etemail.getText().toString().trim());
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ForgetActivity.this, msg , Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Showing toast message of the response
                        //Toast.makeText(LoginActivity.this, s , Toast.LENGTH_LONG).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.d("error",volleyError.getMessage()+"--" + url.toString());
                        Toast.makeText(getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(fingerData.FingerImage());
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                params.put("type","sendotp");
                params.put("otp",String.valueOf(randomNumber));
                //params.put("mobile",etMobile.getText().toString());
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private boolean validateemail() {
        final String data = etemail.getText().toString();
        // Check if mobile is entered
        if (data.length() == 0) {
            if (!tilemail.isErrorEnabled()) {
                tilemail.setErrorEnabled(true);
            }
            tilemail.setError("EmailId Required");
            return false;
        } else {
            if (tilemail.isErrorEnabled()) {
                tilemail.setErrorEnabled(false);
            }
            return true;
        }
    }
}
