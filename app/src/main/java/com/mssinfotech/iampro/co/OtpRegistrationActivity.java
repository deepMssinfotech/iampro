package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class OtpRegistrationActivity extends AppCompatActivity {
    private Button btnprocess;
    private TextInputLayout tilotp,tilpassword,tilcpassword;
    private EditText etotp,etpassword,etcpassword;

    String otp;
    String fname,lname,mobile,email,password,cpassword,category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_registration);
        etotp = findViewById(R.id.etotp);
        etpassword = findViewById(R.id.etpassword);
        etcpassword = findViewById(R.id.etcpassword);
        otp=getIntent().getExtras().getString("otp");
        fname=getIntent().getExtras().getString("fname");
        lname=getIntent().getExtras().getString("lname");
        mobile=getIntent().getExtras().getString("mobile");
        email=getIntent().getExtras().getString("email");
        category=getIntent().getExtras().getString("category");
        btnprocess=findViewById(R.id.btnprocess);
    }
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }
     public void resend(){
        Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
        intent.putExtra("resend","resend");
        startActivity(intent);
     }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                etotp.setText(message);
                //Do whatever you want with the code here
            }
        }
    };

    public void sendData(View v) {
        password = etpassword.getText().toString();
        cpassword = etcpassword.getText().toString();
        if (!Config.haveNetworkConnection(this)){
            Config.showInternetDialog(this);
            return;
        }
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.AJAX_URL+"signup.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(LoginActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.d("Lresponse",""+s);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(s);
                            String status=jsonObject.getString("status");
                            String msgg=jsonObject.getString("msg");


                            Toast.makeText(getApplicationContext(),""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){

                                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                        catch(JSONException e)
                        {
                            loading.dismiss();
                            Log.d("JSoNExceptionv",e.getMessage());
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(fingerData.FingerImage());
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put("type", "user_registration");
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("password", password);
                params.put("cpassword", cpassword);
                params.put("category", category);
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
