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

public class AddDemandActivity extends AppCompatActivity {


    TextInputLayout tildemandname,tilbrandname,tilsellingcost,tildemanddetail;
    EditText etdemandname,etbrandname,etsellingcost,etdemanddetail;
    Spinner spcat;
    private String demandname, brandname, sellingcost, demanddetail, cat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provide);

        tildemandname = findViewById(R.id.tildemandname);
        etdemandname = findViewById(R.id.etdemandname);
        tilbrandname = findViewById(R.id.tilbrandname);
        etbrandname = findViewById(R.id.etbrandname);

        tilsellingcost = findViewById(R.id.tilsellingcost);
        etsellingcost = findViewById(R.id.etsellingcost);

        tildemanddetail=findViewById(R.id.tildemanddetail);
        etdemanddetail = findViewById(R.id.etdemanddetail);

        spcat= findViewById(R.id.spcat);
    }
    public void processAddDemand(View v){
        demandname=etdemandname.getText().toString();
        brandname=etbrandname.getText().toString();
        sellingcost=etsellingcost.getText().toString();
        cat=spcat.getSelectedItem().toString();

        demanddetail=etdemanddetail.getText().toString();

        if (!Validate.isNull(demandname)) {
            tildemandname.setErrorEnabled(true);
            tildemandname.setError("Enter Product Neme ");
            return ;
        } else if (!Validate.isNull(brandname)) {
            tildemandname.setErrorEnabled(false);
            tilbrandname.setErrorEnabled(true);
            tilbrandname.setError("Enter Brand  Neme");
            return;
        } else if (!Validate.isNull(sellingcost)) {
            tilbrandname.setErrorEnabled(false);
            tilsellingcost.setErrorEnabled(true);
            tilsellingcost.setError("Enter Product Purchese Cost");
            return;
        }

        else if (!Validate.isNull(demanddetail)) {
            tilsellingcost.setErrorEnabled(false);
            tildemanddetail.setErrorEnabled(true);
            tildemanddetail.setError("Enter Product Detail");
            return;
        }else {
            hideKeyboard();
            tildemanddetail.setErrorEnabled(false);
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

                                etdemandname.setText(" ");
                                etbrandname.setText(" ");
                                etsellingcost.setText(" ");

                                etdemanddetail.setText(" ");

                                Intent intent=new Intent(getApplicationContext(),MyDemandActivity.class);
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
                params.put("product_type","DEMAND");
                params.put("name",demandname);
                params.put("selling_cost",sellingcost);
                params.put("brand_name",brandname);
                params.put("detail",demanddetail);
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
