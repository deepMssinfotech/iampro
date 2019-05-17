package com.mssinfotech.iampro.co.user;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class ChangePasswordActivity extends Fragment {
    TextInputLayout til_current_pass, til_new_pass, til_conform_pass;
    TextInputEditText et_currpass, et_newpass, et_confpass;
    String currpass, newpass, confpass, id;
    Button btn_cpassword;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_change_password, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view =v;
        til_current_pass = view.findViewById(R.id.til_current_pass);
        til_new_pass = view.findViewById(R.id.til_new_pass);
        til_conform_pass = view.findViewById(R.id.til_conform_pass);

        btn_cpassword = view.findViewById(R.id.btn_cpassword);
        et_currpass = view.findViewById(R.id.et_currpass);
        et_newpass = view.findViewById(R.id.et_newpass);
        et_confpass = view.findViewById(R.id.et_confpass);

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
        id = PrefManager.getLoginDetail(getContext(), "id");

        if (validatenpassword()) {
            if (!Config.haveNetworkConnection(getContext())) {
                Config.showInternetDialog(getContext());
                return;
            }
            final String url = Config.AJAX_URL + "signup.php";
            //tv.setText(String.valueOf(randomNumber));
            final Dialog loading = new Dialog(getContext());
            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loading.setContentView(R.layout.progress_dialog);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.show();
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
                                    ProfileActivity fragment = new ProfileActivity();
                                    function.loadFragment(getContext(),fragment,null);
                                } else {
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),""+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();
                            Log.d("error", volleyError.getMessage() + "--" + url.toString());
                            Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            requestQueue.add(stringRequest);
        }
    }

    private boolean validatenpassword() {

        if (currpass.equalsIgnoreCase("")) {
            if (!til_current_pass.isErrorEnabled()) {
                til_new_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(false);
                til_current_pass.setErrorEnabled(true);
            }
            til_current_pass.setError("Please enter Old Password");
            return false;
        } else if (newpass.equalsIgnoreCase("")) {
            if (!til_new_pass.isErrorEnabled()) {
                til_current_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(false);
                til_new_pass.setErrorEnabled(true);
            }
            til_new_pass.setError("Please enter New Password");
            return false;
        } else if (confpass.equalsIgnoreCase("")) {
            if (!til_conform_pass.isErrorEnabled()) {
                til_new_pass.setErrorEnabled(false);
                til_current_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(true);
            }
            til_conform_pass.setError("Please enter Conform Password");
            return false;
        } else if (!confpass.equalsIgnoreCase(newpass)) {
            if (!til_conform_pass.isErrorEnabled()) {
                til_new_pass.setErrorEnabled(false);
                til_current_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(true);
            }
            til_conform_pass.setError("New Password And Conform Password must be same");
            return false;
        } else if (confpass.length() < 4) {
            if (!til_conform_pass.isErrorEnabled()) {
                til_new_pass.setErrorEnabled(false);
                til_current_pass.setErrorEnabled(false);
                til_conform_pass.setErrorEnabled(true);
            }
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
