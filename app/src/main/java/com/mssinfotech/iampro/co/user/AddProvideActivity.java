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

public class AddProvideActivity extends AppCompatActivity {


    TextInputLayout tilprovidename,tilbrandname,tilsellingcost,tilprovidedetail;
    EditText etprovidename,etbrandname,etsellingcost,etprovidedetail;
    Spinner spcat;
    private String providename, brandname, sellingcost, providedetail,cat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provide);

        tilprovidename = findViewById(R.id.tilprovidename);
        etprovidename = findViewById(R.id.etprovidename);
        tilbrandname = findViewById(R.id.tilbrandname);
        etbrandname = findViewById(R.id.etbrandname);

        tilsellingcost = findViewById(R.id.tilsellingcost);
        etsellingcost = findViewById(R.id.etsellingcost);

        tilprovidedetail=findViewById(R.id.tilprovidedetail);
        etprovidedetail = findViewById(R.id.etprovidedetail);
        spcat= findViewById(R.id.spcat);

    }
    public void processAddProduct(View v){
        providename=etprovidename.getText().toString();
        brandname=etbrandname.getText().toString();
        sellingcost=etsellingcost.getText().toString();

        cat=spcat.getSelectedItem().toString();

        providedetail=etprovidedetail.getText().toString();

        if (!Validate.isNull(providename)) {
            tilprovidename.setErrorEnabled(true);
            tilprovidename.setError("Enter Product Neme ");
            return ;
        } else if (!Validate.isNull(brandname)) {
            tilprovidename.setErrorEnabled(false);
            tilbrandname.setErrorEnabled(true);
            tilbrandname.setError("Enter Brand  Neme");
            return;
        } else if (!Validate.isNull(sellingcost)) {
            tilbrandname.setErrorEnabled(false);
            tilsellingcost.setErrorEnabled(true);
            tilsellingcost.setError("Enter Product Purchese Cost");
            return;
        }

        else if (!Validate.isNull(providedetail)) {
            tilsellingcost.setErrorEnabled(false);
            tilprovidedetail.setErrorEnabled(true);
            tilprovidedetail.setError("Enter Product Detail");
            return;
        }else {
            hideKeyboard();
            tilprovidedetail.setErrorEnabled(false);
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

                                etprovidename.setText(" ");
                                etbrandname.setText(" ");
                                etsellingcost.setText(" ");

                                etprovidedetail.setText(" ");

                                Intent intent=new Intent(getApplicationContext(),MyProvideActivity.class);
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
                params.put("type","add_product_classified");
                params.put("process_type","android");
                params.put("product_type","PROVIDE");
                params.put("name",providename);
                params.put("selling_cost",sellingcost);
                params.put("brand_name",brandname);
                params.put("detail",providedetail);
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
