package com.mssinfotech.iampro.co;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OtpForgetActivity extends AppCompatActivity {

    private Button btnforgetotpprocess;

    private TextInputLayout tilotp,tilnpassword,tilcpassword;
    private EditText etotp,etnpassword,etcpassword;
    Random r = new Random();
    public static String email, vcode;
    public static int randomNumber;
    View view;
    Intent intent;
    Context context;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_forget);
        tilotp= findViewById(R.id.tilotp);
        etotp = findViewById(R.id.etotp);
        tilnpassword= findViewById(R.id.tilnpassword);
        etnpassword = findViewById(R.id.etnpassword);
        tilcpassword= findViewById(R.id.tilcpassword);
        context=OtpForgetActivity.this;
        etcpassword =findViewById(R.id.etcpassword);
        //Bundle args = getArguments();   //getIntent().getB
        //fid = getArguments().getString("uid");
         String args=getIntent().getStringExtra("email");
         intent=getIntent();
        if (intent != null) {
            /*email = args.getStringExtra("email").toString();
            vcode = args.getString("vcode").toString(); */

            email = intent.getStringExtra("email");
            vcode = intent.getStringExtra("vcode");
        }else{
            //email = intent.getStringExtra("email");
            //vcode = intent.getStringExtra("vcode");
        }
        btnforgetotpprocess = findViewById(R.id.btnforgetotpprocess);
        btnforgetotpprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_password(v);
            }
        });
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_otp_forget, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        tilotp= view.findViewById(R.id.tilotp);
        etotp = view.findViewById(R.id.etotp);
        tilnpassword= view.findViewById(R.id.tilnpassword);
        etnpassword = view.findViewById(R.id.etnpassword);
        tilcpassword= view.findViewById(R.id.tilcpassword);
        etcpassword = view.findViewById(R.id.etcpassword);
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null) {
            email = args.getString("email").toString();
            vcode = args.getString("vcode").toString();
        }else{
            email = intent.getStringExtra("email");
            vcode = intent.getStringExtra("vcode");
        }
        btnforgetotpprocess = view.findViewById(R.id.btnforgetotpprocess);
        btnforgetotpprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_password(v);
            }
        });
        //checkAndRequestPermissions();
    } */

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                String separated = message.replaceAll("[^0-9]", "");
                //TextView tv = (TextView) findViewById(R.id.txtview);
                etotp.setText(separated);
                Toast.makeText(OtpForgetActivity.this , "OTP is : "+separated, Toast.LENGTH_LONG).show();
                Log.e("otp is",separated);
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {

                Toast.makeText(context, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }
    public void update_password(View v){
        if (validatenpassword()) {
            if (!Config.haveNetworkConnection(context)) {
                Config.showInternetDialog(context);
                return;
            }
            randomNumber = r.nextInt(10000);
            final String url = Config.API_URL + "app_service.php";
            //tv.setText(String.valueOf(randomNumber));
            final Dialog loading = new Dialog(OtpForgetActivity.this);
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.setContentView(R.layout.progress_dialog);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.show();
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
                                    /*LoginActivity fragment = new LoginActivity();
                                    function.loadFragment(context,fragment,null); */
                                    //finish();
                                    Intent intent=new Intent(OtpForgetActivity.this,LoginActivity.class);
                                     OtpForgetActivity.this.startActivity(intent);

                                } else {
                                    Toast.makeText(OtpForgetActivity.this , msg, Toast.LENGTH_LONG).show();
                                }
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
                            Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    //String image = getStringImage(fingerData.FingerImage());
                    //Creating parameters
                    Map<String, String> params = new Hashtable<String, String>();

                    params.put("type", "resetpassword");
                    params.put("vcode", etotp.getText().toString());
                    params.put("npass",etnpassword.getText().toString());
                    params.put("cpass", etcpassword.getText().toString());
                    params.put("process_type", "android");
                    params.put("email",email);
                    //returning parameters
                    return params;
                }
            };
            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            //Adding request to the queue
            requestQueue.add(stringRequest);
        }
    }
    private boolean validatenpassword() {
        final String data = etnpassword.getText().toString();
        final String cpass = etcpassword.getText().toString();
        final String newotp=  etotp.getText().toString();
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
        } else if (!vcode.equals(newotp)) {
            if (!tilcpassword.isErrorEnabled()) {
                tilcpassword.setErrorEnabled(false);
                tilotp.setErrorEnabled(true);
            }
            tilotp.setError("Security Code Is Incorrect");
            return false;
        } else {
            tilnpassword.setErrorEnabled(false);
            tilcpassword.setErrorEnabled(false);
            tilotp.setErrorEnabled(false);
            return true;
        }
    }
}
