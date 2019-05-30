package com.mssinfotech.iampro.co;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;

import org.json.JSONException;
import org.json.JSONObject;

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
        tilemail= findViewById(R.id.tilemail);
        etemail = findViewById(R.id.etemail);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btnLogin:
                /*LoginActivity fragment = new LoginActivity();
                function.loadFragment(ForgetActivity.this,fragment,null); */
                Intent intent=new Intent(ForgetActivity.this,LoginActivity.class);
                  startActivity(intent);
                break;

            default:
                break;
        }
    }
    public void sendOtp(View v){
        if (validateemail()) {
            if (!Config.haveNetworkConnection(this)) {
                Config.showInternetDialog(this);
                return;
            }
            final String url = Config.API_URL + "app_service.php";
            //tv.setText(String.valueOf(randomNumber));
            final Dialog loading = new Dialog(this);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.setContentView(R.layout.progress_dialog);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d("Lresponse", "" + s);
                            loading.dismiss();
                            try {

                                JSONObject jsonObject = new JSONObject(s);
                                String status=jsonObject.getString("status");
                                String msg=jsonObject.getString("msg");
                                Toast.makeText(ForgetActivity.this, msg, Toast.LENGTH_LONG).show();
                                //if (status.equalsIgnoreCase("success")) {
                                    String vcode = jsonObject.getString("vcode");
                                    Log.d("Lresponse", "" + vcode);
                                   /* OtpForgetActivity fragment = new OtpForgetActivity();
                                    Bundle arg = new Bundle();
                                    arg.putString("vcode", vcode);
                                    arg.putString("email", etemail.getText().toString().trim());
                                    function.loadFragment(ForgetActivity.this,fragment,arg); */

                                     Intent intent=new Intent(ForgetActivity.this,OtpForgetActivity.class);
                                     intent.putExtra("vcode",String.valueOf(vcode));
                                      intent.putExtra("email",etemail.getText().toString().trim());
                                      startActivity(intent);

                                //}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();
                            Log.d("error", volleyError.getMessage() + "--" + url.toString());
                            Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //String image = getStringImage(fingerData.FingerImage());
                    //Creating parameters
                    Map<String, String> params = new Hashtable<String, String>();

                    params.put("type", "ForgetPassword");
                    params.put("email", etemail.getText().toString());
                    params.put("process_type","android");
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
