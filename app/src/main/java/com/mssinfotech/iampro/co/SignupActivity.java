package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener{
    TextView btnLogin;
    private Button btnRegister;
    private Spinner spprofession;
    private TextInputLayout tilFirstname,tilLastname,tilMobile,tilEmail;
    private EditText etLastname,etMobile,etEmail,etFirstname;
    Random r = new Random();
    public static int randomNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        tilFirstname = findViewById(R.id.tilFirstname);
        tilLastname = findViewById(R.id.tilLastName);

        tilMobile = findViewById(R.id.tilMobile);
        tilEmail = findViewById(R.id.tilEmail);


        etFirstname = findViewById(R.id.etFirstname);
        etLastname =findViewById(R.id.etLastname);
        spprofession=findViewById(R.id.spprofession);

        etMobile =findViewById(R.id.etMobile);
        etEmail =findViewById(R.id.etEmail);

        Config.getData(SignupActivity.this, this, spprofession, "FRIEND");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btnLogin:
                Intent i_login = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(i_login);
                break;
            case R.id.btnRegister:
                Log.d("btnRegister","btnRegister");
                if (validateFirstname() && validateLastname() && validateMobile() && validateEmail()) {
                    Log.d("btnRegisterin","btnRegister");
                    sendOtp();
                    /*if (etPassword.getText().toString().equalsIgnoreCase(etcPassword.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Password and ConfirmPassword doesnot match", Toast.LENGTH_SHORT).show();
                    }*/
                   /* else {
                        Toast.makeText(getApplicationContext(), "Validated", Toast.LENGTH_SHORT).show();
                        sendOtp();
                    }*/
                      }
                break;
            default:
                break;
        }
    }
    public void sendOtp(){
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
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(LoginActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.d("Lresponse",""+s);

                        Intent intent=new Intent(getApplicationContext(),OtpRegistrationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("otp",String.valueOf(randomNumber));
                        intent.putExtra("fname",etFirstname.getText().toString().trim());
                        intent.putExtra("lname",etLastname.getText().toString().trim());
                        intent.putExtra("mobile",etMobile.getText().toString().trim());
                        intent.putExtra("email",etEmail.getText().toString().trim());
                        intent.putExtra("category",spprofession.getSelectedItem().toString().trim());

                        startActivity(intent);
                        finish();

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
                params.put("mobile",etMobile.getText().toString());
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private boolean validateFirstname() {
        final String data = etFirstname.getText().toString();
        // Check if firstname is entered
        if (data.length() == 0) {
            if (!tilFirstname.isErrorEnabled()) {
                tilFirstname.setErrorEnabled(true);
            }
            tilFirstname.setError("FirstName Required");
            return false;
        } else {
            if (tilFirstname.isErrorEnabled()) {
                tilFirstname.setErrorEnabled(false);
            }
            return true;
        }
    }
    private boolean validateLastname() {
        final String data = etLastname.getText().toString();
        // Check if lastname is entered
        if (data.length() == 0) {
            if (!tilLastname.isErrorEnabled()) {
                tilLastname.setErrorEnabled(true);
            }
            tilLastname.setError("LastName Required");
            return false;
        } else {
            if (tilLastname.isErrorEnabled()) {
                tilLastname.setErrorEnabled(false);
            }
            return true;
        }
    }
    private boolean validateMobile() {
        final String data = etMobile.getText().toString();
        // Check if mobile is entered
        if (data.length() == 0) {
            if (!tilMobile.isErrorEnabled()) {
                tilMobile.setErrorEnabled(true);
            }
            tilMobile.setError("ContactNo Required");
            return false;
        } else {
            if (tilMobile.isErrorEnabled()) {
                tilMobile.setErrorEnabled(false);
            }
            return true;
        }
    }
    private boolean validateEmail() {
        final String data = etEmail.getText().toString();
        // Check if mobile is entered
        if (data.length() == 0) {
            if (!tilEmail.isErrorEnabled()) {
                tilEmail.setErrorEnabled(true);
            }
            tilEmail.setError("EmailId Required");
            return false;
        } else {
            if (tilEmail.isErrorEnabled()) {
                tilEmail.setErrorEnabled(false);
            }
            return true;
        }
    }
}
