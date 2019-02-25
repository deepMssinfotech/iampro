package com.mssinfotech.iampro.co.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.GalleryAdapter;
import com.mssinfotech.iampro.co.common.ImageProcess;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddDemandActivity extends AppCompatActivity {

    private ImageView imageview; private GridView gvGallery;
    TextInputLayout tildemandname,tilbrandname,tilsellingcost,tildemanddetail;
    EditText etdemandname,etbrandname,etsellingcost,etdemanddetail;
    Spinner spcat;
    String imageEncoded;
    private Bitmap bitmap=null;
    private String URL_FEED = "",uid="", pid = "";
    private String demandname, brandname, sellingcost, demanddetail,cat;
    Button ibdemandimage,ibDemandMoreImage;
    List<String> imagesEncodedList;
    Intent intent;
    private GalleryAdapter galleryAdapter;
    ArrayList<Uri> mArrayUri = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_demand);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_add_demand));
        tildemandname = findViewById(R.id.tildemandname);
        etdemandname = findViewById(R.id.etdemandname);
        tilbrandname = findViewById(R.id.tilbrandname);
        etbrandname = findViewById(R.id.etbrandname);

        tilsellingcost = findViewById(R.id.tilsellingcost);
        etsellingcost = findViewById(R.id.etsellingcost);
        tildemanddetail=findViewById(R.id.tildemanddetail);
        etdemanddetail = findViewById(R.id.etdemanddetail);
        spcat= findViewById(R.id.spcat);
        function.getData(AddDemandActivity.this, this, spcat, "DEMAND");

        imageview  = findViewById(R.id.iv);
        ibdemandimage = findViewById(R.id.ibdemandimage);
        ibdemandimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        gvGallery  = findViewById(R.id.gv);
        ibDemandMoreImage = findViewById(R.id.ibDemandMoreImage);
        ibDemandMoreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMultipleImage();
            }
        });

        intent = getIntent();
        pid = intent.getStringExtra("id");
        uid= PrefManager.getLoginDetail(this,"id");
        if(pid == null ) {

        }else{
            gteDemandDetail(pid);
        }
    }

    private void gteDemandDetail(String id){
        String myurl = Config.API_URL + "app_service.php?type=get_product_detail&id=" + id + "&uid=" + uid+"&update_type=demand&my_id="+uid;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            Log.d(Config.TAG, response);
                            result = new JSONObject(response);

                            String name=result.getString("name");
                            String brand_name=result.getString("brand_name");
                            String id=result.getString("id");
                            String category=result.getString("category");
                            String city=result.getString("city");
                            String purchese_cost=result.getString("purchese_cost");
                            String selling_cost=result.getString("selling_cost");
                            String detail=result.getString("detail");
                            String pimage=Config.OTHER_IMAGE_URL+"250/250/"+result.getString("image");

                            etdemandname.setText(name);
                            etbrandname.setText(brand_name);
                            etsellingcost.setText(selling_cost);
                            etdemanddetail.setText(detail);
                            Glide.with(getApplicationContext()).load(pimage).into(imageview);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Config.TAG, error.toString());
                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void selectMultipleImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Config.PICK_IMAGE_MULTIPLE);
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ImageProcess.choosePhotoFromGallary(AddDemandActivity.this);
                                break;
                            case 1:
                                ImageProcess.takePhotoFromCamera(AddDemandActivity.this);
                                break;
                        }
                    }
                });
        pictureDialog.show();
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
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = ImageProcess.saveImage(this,Config.bitmap);
                    //Toast.makeText(AddDemandActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);
                    //ImageProcess.saveTempImage(this,"demand",bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddDemandActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == Config.CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageview.setImageBitmap(bitmap);
            ImageProcess.saveImage(this,bitmap);
            //ImageProcess.saveTempImage(this,"demand",bitmap);
            Toast.makeText(AddDemandActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == Config.PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK  && null != data) {
            // Get the Image from data

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            imagesEncodedList = new ArrayList<String>();
            if(data.getData()!=null){

                Uri mImageUri=data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    ImageProcess.saveTempImage(this,"demand", bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Get the cursor
                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded  = cursor.getString(columnIndex);
                cursor.close();

                //ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUri.add(mImageUri);
                galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                gvGallery.setAdapter(galleryAdapter);
                 galleryAdapter.notifyDataSetChanged();
                gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                        .getLayoutParams();
                mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    //ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            ImageProcess.saveTempImage(this,"demand", bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded  = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();
                        galleryAdapter = new GalleryAdapter(getApplicationContext(),mArrayUri);
                        gvGallery.setAdapter(galleryAdapter);
                          galleryAdapter.notifyDataSetChanged();
                        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery.getLayoutParams();
                        mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                    }
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                }
            }
        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void processAddDemand(View v){
        demandname=etdemandname.getText().toString();
        brandname=etbrandname.getText().toString();

        sellingcost=etsellingcost.getText().toString();
        demanddetail=etdemanddetail.getText().toString();
        cat=spcat.getSelectedItem().toString();

        if (Validate.isNull(demandname)) {
            tildemandname.setErrorEnabled(true);
            tildemandname.setError("Enter provide Name ");
            return ;
        } else if (Validate.isNull(brandname)) {
            tildemandname.setErrorEnabled(false);
            tilbrandname.setErrorEnabled(true);
            tilbrandname.setError("Enter Brand  Name");
            return;
        }
        else if (Validate.isNull(sellingcost)) {
            tilbrandname.setErrorEnabled(false);
            tilsellingcost.setErrorEnabled(true);
            tilsellingcost.setError("Enter provide Selling Cost");
            return;
        }
        else if (Validate.isNull(demanddetail)) {
            tilsellingcost.setErrorEnabled(false);
            tildemanddetail.setErrorEnabled(true);
            tildemanddetail.setError("Enter provide Detail");
            return;
        }else {
            hideKeyboard();
            tildemanddetail.setErrorEnabled(false);
            if(pid == null ) {
                sendData();
            }else{
                updateData();
            }
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
                        //Log.e("Lresponse",""+s);
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

                                MyDemandActivity fragment = new MyDemandActivity();
                                function.loadFragment(AddDemandActivity.this,fragment,null);
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
                String image=ImageProcess.getStringImage(bitmap);
                Map<String,String> params = new Hashtable<String, String>();
                params.put("type","add_product_classified");
                params.put("process_type","android");
                params.put("product_type","DEMAND");
                params.put("name",demandname);
                params.put("selling_cost",sellingcost);
                params.put("brand_name",brandname);
                params.put("detail",demanddetail);
                params.put("category",cat);
                params.put("myfile",image);
                params.put("added_by",PrefManager.getLoginDetail(getApplicationContext(),"id"));
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void updateData()
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

                                MyDemandActivity fragment = new MyDemandActivity();
                                function.loadFragment(AddDemandActivity.this,fragment,null);
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
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image=ImageProcess.getStringImage(bitmap);
                Map<String,String> params = new Hashtable<String, String>();
                params.put("type","update_product_classified");
                params.put("process_type","android");
                params.put("name",demandname);
                params.put("selling_cost",sellingcost);
                params.put("brand_name",brandname);
                params.put("detail",demanddetail);
                params.put("category",cat);
                // params.put("myfile",image);
                params.put("product_id",pid);
                params.put("product_type","DEMAND");
                params.put("added_by",PrefManager.getLoginDetail(getApplicationContext(),"id"));
                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}