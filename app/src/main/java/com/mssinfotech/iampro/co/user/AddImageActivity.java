package com.mssinfotech.iampro.co.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class AddImageActivity extends AppCompatActivity {

    TextInputLayout tilalbumname,tilimagename,tilimagedetail;
    EditText etalbumname,etimagename,etimagedetail;
    Spinner spcat;
    private String albumname, imagename, imagedetail,cat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        tilalbumname = findViewById(R.id.tilalbumname);
        etalbumname = findViewById(R.id.etalbumname);
        tilimagename = findViewById(R.id.tilimagename);
        etimagename = findViewById(R.id.etimagename);
        tilimagedetail = findViewById(R.id.tilimagedetail);
        etimagedetail = findViewById(R.id.etimagedetail);
        spcat= findViewById(R.id.spcat);

    }
    public void processAddImage(View v){
        albumname=etalbumname.getText().toString();
        imagename=etimagename.getText().toString();
        imagedetail=etimagedetail.getText().toString();
        cat=spcat.getSelectedItem().toString();

         if (!Validate.isNull(albumname)) {
             tilalbumname.setErrorEnabled(true);
             tilalbumname.setError("Enter Album Neme ");
            return ;
        } else if (!Validate.isNull(imagename)) {
             tilalbumname.setErrorEnabled(false);
             tilimagename.setErrorEnabled(true);
             tilimagename.setError("Enter Image  Neme");
            return;
        } else if (!Validate.isNull(imagedetail)) {
             tilimagename.setErrorEnabled(false);
             tilimagedetail.setErrorEnabled(true);
             tilimagedetail.setError("Enter Image Detail");
            return;
        }
        else {
            hideKeyboard();
             tilimagedetail.setErrorEnabled(false);
            sendData();
        }
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void sendData()
    {

        if (!Config.haveNetworkConnection(this)){
            Config.showInternetDialog(this);
            return;
        }

        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config.AJAX_URL+"uploadprocess.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Log.d("Lresponse",""+s);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(s);
                            String status=jsonObject.getString("status");
                            String msgg=jsonObject.getString("msg");

                            Toast.makeText(getApplicationContext(),""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){
                                //String urlv=jsonObject.getString("url");

                                etalbumname.setText(" ");
                                etimagename.setText(" ");
                                etimagedetail.setText(" ");


                                Intent intent=new Intent(getApplicationContext(),MyImageActivity.class);
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
                params.put("type","uploadfiles");
                params.put("process_type","android");
                params.put("albumname",albumname);
                params.put("name",imagename);
                params.put("Detail",imagedetail);
                params.put("category",cat);
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
