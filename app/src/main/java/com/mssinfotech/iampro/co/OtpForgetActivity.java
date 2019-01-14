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
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class OtpForgetActivity extends AppCompatActivity {

    private Button btnforgetotpprocess;

    private TextInputLayout tilotp,tilnpassword,tilcpassword;
    private EditText etotp,etnpassword,etcpassword;
    Random r = new Random();
    public static String email, vcode;
    public static int randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_forget);
        tilotp= findViewById(R.id.tilotp);
        etotp = findViewById(R.id.etotp);
        tilnpassword= findViewById(R.id.tilnpassword);
        etnpassword = findViewById(R.id.etnpassword);
        tilcpassword= findViewById(R.id.tilcpassword);
        etcpassword = findViewById(R.id.etcpassword);
        email=getIntent().getExtras().getString("email");
        vcode=getIntent().getExtras().getString("vcode");

    }



    public void update_password(View v){
        if (validatenpassword()) {
            if (!Config.haveNetworkConnection(this)) {
                Config.showInternetDialog(this);
                return;
            }
            randomNumber = r.nextInt(10000);
            final String url = Config.API_URL + "app_service.php";
            //tv.setText(String.valueOf(randomNumber));
            final ProgressDialog loading = ProgressDialog.show(this, "Processing...", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject result = null;
                            loading.dismiss();
                            try {

                                //Parsing the fetched Json String to JSON Object
                                result = new JSONObject(s);
                                String status = result.getString("status");
                                String msg = result.getString("msg");
                                if (status.equals("success")) {

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(OtpForgetActivity.this, msg+"database code/"+vcode+"/inputcode"+etotp, Toast.LENGTH_LONG).show();
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

                    params.put("type", "resetpassword");
                    params.put("vcode", vcode);
                    params.put("npass",etnpassword.getText().toString());
                    params.put("cpass", etcpassword.getText().toString());
                    params.put("process_type", "process_type");
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
    }
    private boolean validatenpassword() {
        final String data = etnpassword.getText().toString();
        final String cpass = etcpassword.getText().toString();
        // Check if mobile is entered
        if (data.length() == 0) {
            if (!tilnpassword.isErrorEnabled()) {
                tilnpassword.setErrorEnabled(true);
            }
            tilnpassword.setError("New Password Required");
            return false;
        }else if (cpass.length() == 0) {
            if (!tilcpassword.isErrorEnabled()) {
                tilnpassword.setErrorEnabled(false);
                tilcpassword.setErrorEnabled(true);
            }
            tilnpassword.setError("Conform Password Required");
            return false;
        } else {
            tilcpassword.setErrorEnabled(false);
            return true;
        }
    }
}
