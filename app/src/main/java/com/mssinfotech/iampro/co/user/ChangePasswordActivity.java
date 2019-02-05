package com.mssinfotech.iampro.co.user;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputLayout til_current_pass, til_new_pass, til_conform_pass;
    EditText et_currpass, et_newpass, et_confpass;
    String currpass, newpass, confpass, id;
    Button btn_cpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_change_password));

        til_current_pass = findViewById(R.id.til_current_pass);
        til_new_pass = findViewById(R.id.til_new_pass);
        til_conform_pass = findViewById(R.id.til_conform_pass);

        btn_cpassword = findViewById(R.id.btn_cpassword);
        et_currpass = findViewById(R.id.et_currpass);
        et_newpass = findViewById(R.id.et_newpass);
        et_confpass = findViewById(R.id.et_confpass);

        btn_cpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }


    public void changePassword() {
        currpass = et_currpass.getText().toString();
        newpass = et_newpass.getText().toString();
        confpass = et_confpass.getText().toString();
        Log.d("cPass",currpass+""+newpass+""+confpass);
        id = PrefManager.getLoginDetail(this, "id");

        if (validatenpassword()) {
            if (!Config.haveNetworkConnection(this)) {
                Config.showInternetDialog(this);
                return;
            }

            final String url = Config.AJAX_URL + "signup.php";
            //tv.setText(String.valueOf(randomNumber));
            final ProgressDialog loading = ProgressDialog.show(this, "Processing...", "Please wait...", false, false);
            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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

                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    //Toast.makeText("Password Update Successfully").show();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ChangePasswordActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
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

                    params.put("type", "changePass");
                    params.put("opass", currpass);
                    params.put("npass", newpass);
                    params.put("cpass", confpass);
                    params.put("uid", id);
                    //returning parameters
                    return params;
                }
            };
            //Creating a Request Queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            requestQueue.add(stringRequest);
        }
    }

    private boolean validatenpassword() {

        if (currpass.equalsIgnoreCase("")) {
            if (!til_current_pass.isErrorEnabled()) {
                til_current_pass.setErrorEnabled(true);
            }
            til_current_pass.setError("Please enter Old Password");
            return false;
        } else if (newpass.equalsIgnoreCase("")) {
            if (!til_new_pass.isErrorEnabled()) {
                til_current_pass.setErrorEnabled(false);
                til_new_pass.setErrorEnabled(true);
            }
            til_new_pass.setError("Please enter New Password");
            return false;
        } else if (confpass.equalsIgnoreCase("")) {
            if (!til_conform_pass.isErrorEnabled()) {
                til_new_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(true);
            }
            til_conform_pass.setError("Please enter Conform Password");
            return false;
        } else if (!confpass.equalsIgnoreCase(newpass)) {
            if (!til_conform_pass.isErrorEnabled()) {
                til_new_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(true);
            }
            til_conform_pass.setError("New Password And Conform Password must be same");
            return false;
        } else if (confpass.length() < 4) {
            til_conform_pass.setError("Password must contant minimum one number and one charecter and minimum length of password  5");
            return false;
        } else {
            til_current_pass.setErrorEnabled(false);
            til_new_pass.setErrorEnabled(false);
            til_conform_pass.setErrorEnabled(false);
            return true;
        }

    }
}
