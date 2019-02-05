package com.mssinfotech.iampro.co.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.IntroActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SignupActivity;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.CategoryItem;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import javax.net.ssl.HttpsURLConnection;

import static com.mssinfotech.iampro.co.common.ImageProcess.getStringImage;

public class EditProfileActivity extends AppCompatActivity {
    public  static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=100;
    public static int CAMERA_CAPTURE_PICK_IMAGE_REQUEST=200;
    Bitmap bitmap;
    CircleImageView userimage;
    //upload image
    EditText imageName;
    Bitmap FixBitmap;
    String ImageTag = "image_tag" ;
    String ImageName = "image_data" ;
    ProgressDialog progressDialog ;
    ByteArrayOutputStream byteArrayOutputStream ;
    byte[] byteArray ;
    String ConvertImage ;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection ;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;
    Button UploadImageOnServerButton;
    String idv,usenamev,fnamev, avatar;
    String simg_path=null,full_simg_path,banner_imagep,fbanner_imagep;
    Context context=EditProfileActivity.this;
    ImageView view;
    TextInputEditText category,first_name,last_name,contact_no,email,dob,identity_type,identity_no,about_me,tag_line,address_tag,city,state,country;
    String uid,fname,background;
    TextView username;
    ImageView userbackgroud,changeImage;
    Spinner spprofession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_edit_profile));
        fname=PrefManager.getLoginDetail(this,"fname");
        uid=PrefManager.getLoginDetail(this,"id");
        avatar = Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
        background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText(PrefManager.getLoginDetail(this,"fname") +" "+PrefManager.getLoginDetail(this,"lname"));
        Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
        Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
        changeImage = findViewById(R.id.changeImage);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditProfileActivity.this,ProfileImageCroperActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spprofession = findViewById(R.id.spprofession);
        function.getData(EditProfileActivity.this, this, spprofession, "FRIEND");
        first_name= findViewById(R.id.first_name);
        last_name= findViewById(R.id.last_name);
        contact_no= findViewById(R.id.contact_no);
        email= findViewById(R.id.email);
        dob= findViewById(R.id.dob);
        identity_type= findViewById(R.id.identity_type);
        identity_no= findViewById(R.id.identity_no);
        about_me= findViewById(R.id.about_me);
        tag_line= findViewById(R.id.tag_line);
        address_tag= findViewById(R.id.address_tag);
        city= findViewById(R.id.city);
        state= findViewById(R.id.state);
        country=findViewById(R.id.country);
        getData();
        PrefManager.updateUserData(this,null);
    }
    public void SaveForm(View v)
    {
        if (!isInternetOn()){
            showInternetDialog();
            return;
        }
        String emailv=email.getText().toString();
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,Config.API_URL+"ajax.php?type=checkemail&email="+emailv+"&uid="+idv,
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
                            PrefManager.updateUserData(getApplicationContext(),null);
                            Toast.makeText(getApplicationContext(),""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){
                                updateProfile();
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
                });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void updateProfile(){
        String idv=PrefManager.getLoginDetail(this,"id");
        if (!isInternetOn())  {
            showInternetDialog();
            return;
        }
        final ProgressDialog loading =ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,Config.AJAX_URL+"signup.php?type=update_profile&uid="+idv+"&fname="+first_name.getText()+"&lname="+last_name.getText()+"&mobile="+contact_no.getText()+"&email="+email.getText()+"&dob="+dob.getText()+"&identity_type="+identity_type.getText()+"&identity_number="+identity_no.getText()+"&about_me="+about_me.getText()+"&tag_line="+tag_line.getText()+"&address="+address_tag.getText()+"&city="+city.getText()+"&state="+state.getText()+"&country="+country.getText(),
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
                            //String urlv=jsonObject.getString("url");
                            Toast.makeText(getApplicationContext(),""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){
                                Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
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
                });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;
    }
    public void getData(){

        String url=Config.API_URL+"ajax.php?type=get_users_all_detail&uid="+PrefManager.getLoginDetail(this,"id");
        final ProgressDialog loading = ProgressDialog.show(this,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            loading.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            String fnamev=jsonObject.getString("fname");
                            first_name.setText(fnamev);
                            String lnamev=jsonObject.getString("lname");
                            last_name.setText(lnamev);
                            String mobilev=jsonObject.getString("mobile");
                            contact_no.setText(mobilev);
                            String emailv=jsonObject.getString("email");
                            email.setText(emailv);
                            String dobv=jsonObject.getString("dob");
                            dob.setText(dobv);
                            String identity_typev=jsonObject.getString("identity_type");
                            identity_type.setText(identity_typev);
                            String identity_numberv=jsonObject.getString("identity_number");
                            identity_no.setText(identity_numberv);
                            String about_mev=jsonObject.getString("about_me");
                            about_me.setText(about_mev);
                            String genderv=jsonObject.getString("gender");

                            String tag_linev=jsonObject.getString("tag_line");
                            tag_line.setText(tag_linev);
                            String addressv=jsonObject.getString("address");
                            address_tag.setText(addressv);
                            String cityv=jsonObject.getString("city");
                            city.setText(cityv);
                            String statev=jsonObject.getString("state");
                            state.setText(statev);
                            String countryv=jsonObject.getString("country");
                            country.setText(countryv);
                        }
                        catch(JSONException e)
                        {
                            loading.dismiss();
                            Log.d("JSoNExceptionv",e.getMessage());
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Config.ResponceResult = error.getMessage();
                //Log.d(Config.TAG,"error : "+error.getMessage());
            }
        });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void showInternetDialog()
    {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("You are offline!")
                .setMessage("Check your network connectivity and try again...")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                    }
                }).show();

    }
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    private void showFileChooser() {
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            //finish();
        }else {

            final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),CAMERA_CAPTURE_PICK_IMAGE_REQUEST);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(ProfileActivity.this, s , Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("msg");
                        }
                        catch(JSONException e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                //params.put("id", id);
                params.put("type","uploadUserProfilePic");
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //image uploading
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    userimage.setImageBitmap(FixBitmap);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            try {
                FixBitmap = (Bitmap) data.getExtras().get("data");
                //ShowSelectedImage.setImageBitmap(FixBitmap);
                userimage.setImageBitmap(FixBitmap);
                UploadImageOnServerButton.setVisibility(View.VISIBLE);
                //  saveImage(thumbnail);
                //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }


    public void UploadImageToServer(){
        Log.d("tagg",GetImageNameFromEditText);
        usenamev=PrefManager.getLoginDetail(this,"username");
        fnamev=PrefManager.getLoginDetail(this,"fname");
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(EditProfileActivity.this,"Image is Uploading","Please Wait",false,false);
            }
            @Override
            protected String doInBackground(Void... params) {

                EditProfileActivity.ImageProcessClass imageProcessClass = new EditProfileActivity.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("image_tag",fnamev.toString().trim());

                HashMapParams.put("image_data", ConvertImage);
                HashMapParams.put("user_id",idv.toString().trim());
                HashMapParams.put("user_name",usenamev.toString().trim());
                //HashMapParams.put("user_password",new PrefManager(getApplicationContext()).getPassword());
                String FinalData = imageProcessClass.ImageHttpRequest("http://www.iampro.co/upload-image-to-server.php", HashMapParams);
                //Log.d("iddtt",idv);
                //Log.d("user_name",usenamev.toString().trim());
                return FinalData;
            }
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                progressDialog.dismiss();
                UploadImageOnServerButton.setVisibility(View.GONE);
                //Toast.makeText(EditProfile.this,string1,Toast.LENGTH_LONG).show();
                Log.d("responsesss",string1);
                try {
                    JSONObject jsonObject = new JSONObject(string1);
                    String statusv=jsonObject.getString("status");
                    String messagev=jsonObject.getString("msg");
                    String img_path=jsonObject.getString("avatar");
                    if (statusv.equalsIgnoreCase("success")) {

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("img_url",img_path).apply();

                        //String img_patth=PrefManager.getImagePath();
                        //Log.d("aimg_path",img_patth);
                        Toast.makeText(getApplicationContext(),messagev,Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),messagev,Toast.LENGTH_LONG).show();
                }
                catch (JSONException e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }

        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {

                Toast.makeText(EditProfileActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }
}
