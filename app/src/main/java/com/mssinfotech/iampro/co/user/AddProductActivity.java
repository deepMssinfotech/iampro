package com.mssinfotech.iampro.co.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mssinfotech.iampro.co.ForgetActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.LoginActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.ImageProcess;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {


    private ImageView imageview;
    TextInputLayout tilproductname,tilbrandname,tilpurchesecost,tilsellingcost,tilproductdetail;
    EditText etproductname,etbrandname,etpurchesecost,etsellingcost,etproductdetail;
    Spinner spcat;
    private String productname, brandname, purchesecost,sellingcost, productdetail,cat;
    Button ibproductimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        tilproductname = findViewById(R.id.tilproductname);
        etproductname = findViewById(R.id.etproductname);
        tilbrandname = findViewById(R.id.tilbrandname);
        etbrandname = findViewById(R.id.etbrandname);

        tilpurchesecost = findViewById(R.id.tilpurchesecost);
        etpurchesecost = findViewById(R.id.etpurchesecost);
        tilsellingcost = findViewById(R.id.tilsellingcost);
        etsellingcost = findViewById(R.id.etsellingcost);
        tilproductdetail=findViewById(R.id.tilproductdetail);
        etproductdetail = findViewById(R.id.etproductdetail);
        spcat= findViewById(R.id.spcat);
        Config.getData(AddProductActivity.this, this, spcat, "PRODUCT");

        imageview  = (ImageView) findViewById(R.id.iv);
        ibproductimage = findViewById(R.id.ibproductimage);
        ibproductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageProcess.showPictureDialog(this,AddProductActivity.this);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == Config.GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = ImageProcess.saveImage(this,bitmap);
                    Toast.makeText(AddProductActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProductActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == Config.CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(thumbnail);
            ImageProcess.saveImage(this,thumbnail);
            Toast.makeText(AddProductActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public void processAddProduct(View v){
        productname=etproductname.getText().toString();
        brandname=etbrandname.getText().toString();
        purchesecost=etpurchesecost.getText().toString();
        sellingcost=etsellingcost.getText().toString();
        productdetail=etproductdetail.getText().toString();
        cat=spcat.getSelectedItem().toString();

        if (!Validate.isNull(productname)) {
            tilproductname.setErrorEnabled(true);
            tilproductname.setError("Enter Product Neme ");
            return ;
        } else if (!Validate.isNull(brandname)) {
            tilproductname.setErrorEnabled(false);
            tilbrandname.setErrorEnabled(true);
            tilbrandname.setError("Enter Brand  Neme");
            return;
        } else if (!Validate.isNull(purchesecost)) {
            tilbrandname.setErrorEnabled(false);
            tilpurchesecost.setErrorEnabled(true);
            tilpurchesecost.setError("Enter Product Purchese Cost");
            return;
        }
        else if (!Validate.isNull(sellingcost)) {
            tilpurchesecost.setErrorEnabled(false);
            tilsellingcost.setErrorEnabled(true);
            tilsellingcost.setError("Enter Product Selling Cost");
            return;
        }
        else if (!Validate.isNull(productdetail)) {
            tilsellingcost.setErrorEnabled(false);
            tilproductdetail.setErrorEnabled(true);
            tilproductdetail.setError("Enter Product Detail");
            return;
        }else {
            hideKeyboard();
            tilproductdetail.setErrorEnabled(false);
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

                                etproductname.setText(" ");
                                etbrandname.setText(" ");
                                etpurchesecost.setText(" ");
                                etsellingcost.setText(" ");
                                etproductdetail.setText(" ");

                                Intent intent=new Intent(getApplicationContext(),MyProductActivity.class);
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
                params.put("type","add_product");
                params.put("process_type","android");
                params.put("name",productname);
                params.put("purchese_cost",purchesecost);
                params.put("selling_cost",sellingcost);
                params.put("brand_name",brandname);
                params.put("detail",productdetail);
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
