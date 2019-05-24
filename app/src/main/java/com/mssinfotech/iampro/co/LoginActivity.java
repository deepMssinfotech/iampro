package com.mssinfotech.iampro.co;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {
    TextView btnforgetPassword;
    Button btnprocess;
    TextInputLayout tilemail,tilpassword;
    EditText etemail,etpassword;
    View view;
    Context context;
    TextView btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);
        context =LoginActivity.this;
        tilemail = findViewById(R.id.tilemail);
        etemail = findViewById(R.id.etemail);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnprocess=findViewById(R.id.btnprocess);
        tilpassword =findViewById(R.id.tilpassword);
        btnforgetPassword=findViewById(R.id.btnforgetPassword);
        etpassword =findViewById(R.id.etpassword);

        etemail.setEnabled(true);
        etpassword.setEnabled(true);
        tilemail.setEnabled(true);
        tilpassword.setEnabled(true);

        btnprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin(v);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUp.setEnabled(false);
                SignupActivity fragmentz = new SignupActivity();
                function.loadFragment(LoginActivity.this,fragmentz,null);
                btnSignUp.setEnabled(true);
            }
        });
        btnforgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(v);
            }
        });
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_login, parent, false);
    }
     */
    /*@Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        context = getContext();
        super.onCreate(savedInstanceState);
        tilemail = view.findViewById(R.id.tilemail);
        etemail = view.findViewById(R.id.etemail);
        btnSignUp=view.findViewById(R.id.btnSignUp);
         btnprocess=view.findViewById(R.id.btnprocess);
        tilpassword = view.findViewById(R.id.tilpassword);
        btnforgetPassword=view.findViewById(R.id.btnforgetPassword);
        etpassword = view.findViewById(R.id.etpassword);

        etemail.setEnabled(true);
        etpassword.setEnabled(true);
        tilemail.setEnabled(true);
        tilpassword.setEnabled(true);

        btnprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLogin(v);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUp.setEnabled(false);
                SignupActivity fragmentz = new SignupActivity();
                function.loadFragment(getContext(),fragmentz,null);
                btnSignUp.setEnabled(true);
            }
        });
        btnforgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect(v);
            }
        });
    }*/
    public void redirect(View v){
        Intent i_login = new Intent(context, ForgetActivity.class);
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
        if (view != null) {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void sendData(final String unamee,final String passwordd)
    {
        if (!Config.haveNetworkConnection(context)){
            Config.showInternetDialog(context);
            return;
        }
        final Dialog loading = new Dialog(context);
        loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading.setContentView(R.layout.progress_dialog);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.AJAX_URL+"signup.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(LoginActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.d("Lresponse",""+s);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(s);
                            String status=jsonObject.getString("status");
                            String msgg=jsonObject.getString("msg");


                            Toast.makeText(context.getApplicationContext(),""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){

                                //String name=fnamem+"\t"+lnamem;
                                //String imgurl=avatarv;
                                String id = jsonObject.getString("id");
                                String username = jsonObject.getString("username");
                                String img_url= jsonObject.getString("avatar");
                                String dob = jsonObject.getString("dob");
                                String mobile = jsonObject.getString("mobile");
                                String fname = jsonObject.getString("fname");
                                String lname = jsonObject.getString("lname");
                                String email = jsonObject.getString("email");
                                String banner_image = jsonObject.getString("banner_image");
                                String img_banner_image = jsonObject.getString("img_banner_image");
                                String video_banner_image = jsonObject.getString("video_banner_image");
                                String profile_image_gallery = jsonObject.getString("profile_image_gallery");
                                String profile_video_gallery = jsonObject.getString("profile_video_gallery");
                                Context context=LoginActivity.this;
                                PrefManager.saveLoginDetails(username,img_url,id,mobile,fname,lname,email,dob,banner_image,  img_banner_image,  video_banner_image,  profile_image_gallery, profile_video_gallery,context);
                                PrefManager.setLogin(true);
                                PrefManager.updateCountFromServer(context,id);
                                etemail.setText(" ");
                                etpassword.setText(" ");
                                Intent intent=new Intent(context.getApplicationContext(),WelcomeActivity.class);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                loading.dismiss();
                            }
                            else{
                                loading.dismiss();
                            }
                        }
                        catch(JSONException e)
                        {
                            loading.dismiss();
                            Log.d("JSoNExceptionv",e.getMessage());
                            Toast.makeText(context.getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(context.getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
