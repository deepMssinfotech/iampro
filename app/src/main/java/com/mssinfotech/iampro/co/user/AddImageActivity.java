package com.mssinfotech.iampro.co.user;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.GalleryAdapter;
import com.mssinfotech.iampro.co.common.ImageProcess;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//import net.gotev.uploadservice.MultipartUploadRequest;
//import net.gotev.uploadservice.UploadNotificationConfig;

public class AddImageActivity extends AppCompatActivity {
    TextView tvlayouttype;
    TextInputLayout tilalbumname,tilimagename,tilimagedetail;
    EditText etalbumname,etimagename,etimagedetail;
    Spinner spcat,spimage_album;
    Button add_image_button,create_album_button,ibImageMoreImage;
    List<String> imagesEncodedList;
    String imageEncoded;
    private String albumname, imagename, imagedetail,cat,image_album="fashion";
    private GridView gvGallery;
    private Bitmap bitmap=null;
    private GalleryAdapter galleryAdapter;
    private LinearLayout categoryLayout,albumLayout;
     ArrayList<Uri> mArrayUri = new ArrayList<>();
    ArrayList<String> students = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
         getSupportActionBar().hide();
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_add_image));
        tvlayouttype = findViewById(R.id.tvlayouttype);
        tilalbumname = findViewById(R.id.tilalbumname);
        etalbumname = findViewById(R.id.etalbumname);
        tilimagename = findViewById(R.id.tilimagename);
        etimagename = findViewById(R.id.etimagename);
        tilimagedetail = findViewById(R.id.tilimagedetail);
        etimagedetail = findViewById(R.id.etimagedetail);
        spcat= findViewById(R.id.spcat);

        spimage_album= findViewById(R.id.spimage_album);

        add_image_button = findViewById(R.id.add_image_button);
        create_album_button = findViewById(R.id.create_album_button);
        function.getData(AddImageActivity.this, this, spcat, "IMAGE");
        categoryLayout = findViewById(R.id.categoryLayout);
        albumLayout = findViewById(R.id.albumLayout);
        gvGallery  = findViewById(R.id.gv);
        ibImageMoreImage = findViewById(R.id.ibImageMoreImage);
        ibImageMoreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMultipleImage();
            }
        });
        function.executeUrl(this,"get",Config.API_URL+"app_service.php?type=delete_temp_data&uid="+PrefManager.getLoginDetail(this,"id"),null);
        getAlbumList();
    }
    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Config.STORAGE_PERMISSION_CODE);
    }
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == Config.STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void selectMultipleImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Config.PICK_IMAGE_MULTIPLE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == Config.PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK  && null != data) {
            // Get the Image from data

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
              imagesEncodedList = new ArrayList<String>();
            if(data.getData()!=null){

                Uri mImageUri=data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    ImageProcess.saveTempImage(this,"image", bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();
                  //cursor.moveToNext();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded  = cursor.getString(columnIndex);
                cursor.close();

                //ArrayList<Uri> mArrayUri = new ArrayList<>();
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
                    //ArrayList<Uri> mArrayUri = new ArrayList<>();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            ImageProcess.saveTempImage(this,"image", bitmap);
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
            Toast.makeText(this, "You haven't picked Image",  Toast.LENGTH_LONG).show();
        }
    }
    public void processAddImage(View v){
        albumname=etalbumname.getText().toString();
        imagename=etimagename.getText().toString();
        imagedetail=etimagedetail.getText().toString();
        cat=spcat.getSelectedItem().toString();
        if (spimage_album.getSelectedItem()!=null)
        image_album=spimage_album.getSelectedItem().toString();
        Log.d("student_spinner2",""+image_album);
        String utype=tvlayouttype.getText().toString();
        if (Validate.isNull(albumname) && utype.equalsIgnoreCase("new_album")) {
            resetError();
            tilalbumname.setErrorEnabled(true);
            tilalbumname.setError("Enter Album Name");
            return;
        } else if (Validate.isNull(imagename)) {
            resetError();
            tilimagename.setErrorEnabled(true);
            tilimagename.setError("Enter Image  Name");
            return;
        } else if (Validate.isNull(imagedetail)) {
            resetError();
            tilimagedetail.setErrorEnabled(true);
            tilimagedetail.setError("Enter Image Detail");
            return;
        }
        else {
            hideKeyboard();
            resetError();
            sendData();
        }
    }
    public void resetError(){
        tilalbumname.setErrorEnabled(false);
        tilimagename.setErrorEnabled(false);
        tilimagedetail.setErrorEnabled(false);
    }
    private void hideKeyboard() {
         try {
             View view = getCurrentFocus();
             if (view != null) {
                 ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
             }
         }
         catch (Exception e){
              Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
              e.printStackTrace();
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
                         Log.d("addimage_sendData",""+s);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.d(Config.TAG,"output - "+jsonObject.toString());
                            String status=jsonObject.getString("status");
                            String msgg=jsonObject.getString("message");

                            Toast.makeText(getApplicationContext(),""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){
                                MyImageActivity fragment = new MyImageActivity();
                                Bundle args = new Bundle();
                                args.putString("uid",PrefManager.getLoginDetail(AddImageActivity.this,"id"));
                                function.loadFragment(AddImageActivity.this,fragment,args);
                                //AddImageActivity.this.finish();
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

                String palbumname= tvlayouttype.getText().toString();
                if(!palbumname.equalsIgnoreCase("imagenew"))palbumname=spimage_album.getSelectedItem().toString();
                Map<String,String> params = new Hashtable<String, String>();
                params.put("type","uploadfiles");
                params.put("process_type","android");
                params.put("palbumname",palbumname);
                params.put("albumname",albumname);
                params.put("name",imagename);
                params.put("about_us",imagedetail);
                params.put("category",cat);
                params.put("user_id",PrefManager.getLoginDetail(getApplicationContext(),"id"));
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void click_image_button(View v){
        if (students.size()>0) {
            add_image_button.setBackgroundResource(R.drawable.black);
            add_image_button.setTextColor(getResources().getColor(R.color.white));
            create_album_button.setBackgroundResource(R.drawable.white);
            create_album_button.setTextColor(getResources().getColor(R.color.black));
            tvlayouttype.setText("add_iamge");
            albumLayout.setVisibility(View.VISIBLE);
            categoryLayout.setVisibility(View.GONE);
            tilalbumname.setVisibility(View.GONE);
            etalbumname.setVisibility(View.GONE);
        }
        else{
            add_image_button.setEnabled(false);
        }
        //return;
    }
    public void click_album_button(View v){
        add_image_button.setBackgroundResource(R.drawable.white);
        add_image_button.setTextColor(getResources().getColor(R.color.black));
        create_album_button.setBackgroundResource(R.drawable.black);
        create_album_button.setTextColor(getResources().getColor(R.color.white));
        tvlayouttype.setText("imagenew");
        albumLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.VISIBLE);
        tilalbumname.setVisibility(View.VISIBLE);
        etalbumname.setVisibility(View.VISIBLE);
        return;
    }
    public void getAlbumList(){
        //Creating a string request
        String uid=PrefManager.getLoginDetail(this,"id");
        String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray result = null;
                        if (!students.isEmpty()){
                                students.clear();
                        }
                        try {
                            //Parsing the fetched Json String to JSON Object
                            result = new JSONArray(response);
                            //Storing the Array of JSON String to our JSON Array
                            //JSONArray result = j.getJSONArray("data");

                            //Calling method getStudents to get the students from the JSON Array
                            //Log.d(TAG,result.toString());
                            for(int i=0;i<result.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = result.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    students.add(json.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("spinner",""+e.getMessage());
                                }
                            }
                            Log.d("student_spinner",""+students.toString());
                            spimage_album.setAdapter(new ArrayAdapter<String>(AddImageActivity.this, android.R.layout.simple_spinner_dropdown_item, students));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}