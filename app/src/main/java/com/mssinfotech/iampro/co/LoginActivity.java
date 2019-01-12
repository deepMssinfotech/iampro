package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

    TextView btnforgetPassword;
    Button btnprocess;
    TextInputLayout tilemail,tilpassword;
    EditText etemail,etpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilemail = findViewById(R.id.tilemail);
        etemail = findViewById(R.id.etemail);

        tilpassword = findViewById(R.id.tilpassword);
        etpassword = findViewById(R.id.etpassword);

    }
    public void redirect(View v){
        Intent i_login = new Intent(LoginActivity.this, ForgetActivity.class);
        LoginActivity.this.startActivity(i_login);
    }
    public void processLogin(View v){
        String unamev=etemail.getText().toString();
        String passwordv=etpassword.getText().toString();
        if (!Validate.Email(unamev)) {
            tilemail.setErrorEnabled(true);
            tilemail.setError("Not a valid email address!");
            return ;
        } else if (Validate.Password(passwordv) || Validate.isNull(passwordv)){
            tilemail.setErrorEnabled(false);
            tilpassword.setErrorEnabled(true);
            tilpassword.setError("Not a valid Password");
            return;
        } else {
            hideKeyboard();
            tilpassword.setErrorEnabled(false);
            sendData(unamev, passwordv);
        }
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void sendData(final String unamee,final String passwordd)
    {

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
                                //String urlv=jsonObject.getString("url");
                                String avatarv=jsonObject.getString("avatar");
                                String id=jsonObject.getString("id");
                                String mobilev=jsonObject.getString("mobile");
                                String fnamem=jsonObject.getString("fname");
                                String lnamem=jsonObject.getString("lname");
                                String email=jsonObject.getString("email");
                                String banner_imagev=jsonObject.getString("banner_image");
                                String name=fnamem+"\t"+lnamem;
                                String imgurl=avatarv;
                                saveLoginDetails(unamee,passwordd,avatarv,id,mobilev,fnamem,lnamem,email,banner_imagev);
                                etemail.setText(" ");
                                etpassword.setText(" ");

                                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
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
                Map<String,String> params = new Hashtable<String, String>();
                params.put("type","login");
                params.put("username",unamee);
                params.put("pass",passwordd);
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void saveLoginDetails(String username, String password,String img_url,String id,String mobile,String fname,String lname,String email,String banner_imagev) {
        new PrefManager(this).saveLoginDetails(username, password,img_url,id,mobile,fname,lname,email,banner_imagev);
    }
}
