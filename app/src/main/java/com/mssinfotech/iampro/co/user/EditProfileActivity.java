package com.mssinfotech.iampro.co.user;

import android.app.Activity;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mssinfotech.iampro.co.adapter.GalleryAdapter;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.ImageProcess;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.CategoryItem;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

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
import java.util.UUID;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_CANCELED;
import static com.mssinfotech.iampro.co.common.ImageProcess.getStringImage;


public class EditProfileActivity extends Fragment {
    public  static int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=100;
    public static int CAMERA_CAPTURE_PICK_IMAGE_REQUEST=200;
    Bitmap bitmap; TextView tvlayouttype;
    CircleImageView userimage;
    public static final int REQUEST_IMAGE = 100;
    //upload image
    EditText imageName;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;
    Button UploadImageOnServerButton;
    public String idv, usenamev, fnamev, avatar;
    public String backgroundimagePath="";
    String simg_path=null,full_simg_path,banner_imagep,fbanner_imagep;
    Context context;
    TextInputEditText category,first_name,last_name,contact_no,email,dob,identity_type,identity_no,about_me,tag_line,address_tag,city,state,country;
    String uid,fname,background,banner_img;
    TextView username;
    ImageView userbackgroud,changeImage,changeBackground_Image;
    @BindView(R.id.img_profile)
    ImageView imgProfile;

    Spinner spprofession;
    private GalleryAdapter galleryAdapter;
    View view;
    public static String imageType;
    ProgressDialog progressdialog;
    int status = 0;
    Handler handler = new Handler();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_edit_profile, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        context = getContext();
        fname=PrefManager.getLoginDetail(context,"fname");
        uid=PrefManager.getLoginDetail(context,"id");
        avatar = Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(context,"img_url");
        background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(context,"banner_image");

        username = view.findViewById(R.id.username);
        userimage = view.findViewById(R.id.userimage);
        userbackgroud = view.findViewById(R.id.userbackgroud);
        tvlayouttype = view.findViewById(R.id.tvlayouttype);
        username.setText(PrefManager.getLoginDetail(context,"fname") +" "+PrefManager.getLoginDetail(context,"lname"));
        Glide.with(context).load(background).apply(Config.options_background).into(userbackgroud);
        Glide.with(context).load(avatar).apply(Config.options_avatar).into(userimage);
        changeImage = view.findViewById(R.id.changeImage);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(context,ProfileImageCroperActivity.class);
                startActivity(intent); */
                //finish();
                imageType="userImage";
                showImagePickerOptions();

            }
        });

        changeBackground_Image = view.findViewById(R.id.changeBackground_Image);
        changeBackground_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPictureDialog();

                imageType="backgroundImage";
                showImagePickerOptions();
            }
        });

        spprofession = view.findViewById(R.id.spprofession);
        function.getData(getActivity(), context, spprofession, "FRIEND");
        first_name= view.findViewById(R.id.first_name);
        last_name= view.findViewById(R.id.last_name);
        contact_no= view.findViewById(R.id.contact_no);
        email= view.findViewById(R.id.email);
        dob= view.findViewById(R.id.dob);
        identity_type= view.findViewById(R.id.identity_type);
        identity_no= view.findViewById(R.id.identity_no);
        about_me= view.findViewById(R.id.about_me);
        tag_line= view.findViewById(R.id.tag_line);
        address_tag= view.findViewById(R.id.address_tag);
        city= view.findViewById(R.id.city);
        state= view.findViewById(R.id.state);
        country=view.findViewById(R.id.country);
        //ImagePickerActivity.clearCache(getContext());
        getData();
        PrefManager.updateUserData(context,null);

    }
    public void SaveForm(View v)
    {
        if (!isInternetOn()){
            showInternetDialog();
            return;
        }
        String emailv=email.getText().toString();
        final ProgressDialog loading = ProgressDialog.show(context,"Processing...","Please wait...",false,false);
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
                            PrefManager.updateUserData(context,null);
                            Toast.makeText(context,""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){
                                updateProfile();
                            }
                        }
                        catch(JSONException e)
                        {
                            loading.dismiss();
                            Log.d("JSoNExceptionv",e.getMessage());
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(context,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public void updateProfile(){
        String idv=PrefManager.getLoginDetail(context,"id");
        if (!isInternetOn())  {
            showInternetDialog();
            return;
        }
        final ProgressDialog loading =ProgressDialog.show(context,"Processing...","Please wait...",false,false);
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
                            Toast.makeText(context,""+msgg,Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")){
                                ProfileActivity fragment = new ProfileActivity();
                                function.loadFragment(context,fragment,null);
                            }
                        }
                        catch(JSONException e)
                        {
                            loading.dismiss();
                            Log.d("JSoNExceptionv",e.getMessage());
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(context,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

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
        String url=Config.API_URL+"ajax.php?type=get_users_all_detail&uid="+PrefManager.getLoginDetail(context,"id");
        final ProgressDialog loading = ProgressDialog.show(context,"Processing...","Please wait...",false,false);
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
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void showInternetDialog()
    {
        new AlertDialog.Builder(context)
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
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
    //image uploading

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
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
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                    userbackgroud.setImageBitmap(FixBitmap);
                    backgroundimagePath = getPath(contentURI);
                    //UploadImageOnServerButton.setVisibility(View.VISIBLE);
                    sendData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            Uri contentURI = data.getData();
            try {
                FixBitmap = (Bitmap) data.getExtras().get("data");
                //ShowSelectedImage.setImageBitmap(FixBitmap);
                userbackgroud.setImageBitmap(FixBitmap);
                backgroundimagePath = getPath(contentURI);
                //UploadImageOnServerButton.setVisibility(View.VISIBLE);
                //  saveImage(thumbnail);
                sendData();
                //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    backgroundimagePath = getPath(getImageUri(context,bitmap));

                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void loadProfile(String url)   {
        Log.d("eProfile_uri", "Image cache path: " + url);
         if(imageType.equalsIgnoreCase("backgroundImage"))  {
             //backgroundimagePath = getPath(url);
             Glide.with(this).load(url)
                     .into(userbackgroud);
             userbackgroud.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
             sendData();
          }
           else if (imageType.equalsIgnoreCase("userImage")){
              //userimage
             //backgroundimagePath = getPath(url);
             Glide.with(this).load(url)
                     .into(userimage);
               userimage.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
               Toast.makeText(context,"Sending",Toast.LENGTH_LONG).show();
              sendUserPic();
         }
    }
    public void sendData() {
        if (!Config.haveNetworkConnection(context)) {
            Config.showInternetDialog(context);
            return;
        }
           CreateProgressDialog();
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "banner_img") //Adding file
                    .addParameter("type","banner_img")//Adding text parameter to the request
                    .addParameter("process_type","android")
                    .addParameter("page_url","page/update_profile.html")
                    .addParameter("userid",PrefManager.getLoginDetail(context,"id"))
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
             ShowProgressDialog();
            ProfileActivity fragment = new ProfileActivity();
            function.loadFragment(context,fragment,null);
            //getActivity().finish();
        } catch (Exception exc) {
            Toast.makeText(context, ""+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
     public void sendUserPic(){
         if (!Config.haveNetworkConnection(context)) {
             Config.showInternetDialog(context);
             return;
         }
         CreateProgressDialog();
         //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
         //return;

         try {
             String uploadId=UUID.randomUUID().toString();
             //Creating a multi part request
             new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                     .addFileToUpload(backgroundimagePath, "avatar") //Adding file
                     .addParameter("type","profile_pic")//Adding text parameter to the request
                     .addParameter("process_type","android")
                     .addParameter("page_url","page/update_profile.html")
                     .addParameter("user_id",PrefManager.getLoginDetail(context,"id"))
                     .addParameter("userid",PrefManager.getLoginDetail(context,"id"))
                     .addParameter("fname",PrefManager.getLoginDetail(context,"fname"))
                     .addParameter("email",PrefManager.getLoginDetail(context,"email"))
                     .addParameter("country",PrefManager.getLoginDetail(context,"country"))
                     .addParameter("state",PrefManager.getLoginDetail(context,"state"))
                     .addParameter("image_name",backgroundimagePath)
                     //.setNotificationConfig(new UploadNotificationConfig())
                     .setMaxRetries(2)
                     .startUpload(); //Starting the upload
                   ShowProgressDialog();
             ProfileActivity fragment = new ProfileActivity();
             function.loadFragment(context,fragment,null);

              /*
               String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "profile_video_gallery") //Adding file
                    .addParameter("type","update_img_video_banner")//Adding text parameter to the request
                    .addParameter("process_type","android")
                    .addParameter("page_url","page/update_profile.html")
                    .addParameter("userid",PrefManager.getLoginDetail(context,"id"))
                    .addParameter("fname",PrefManager.getLoginDetail(context,"fname"))
                    .addParameter("email",PrefManager.getLoginDetail(context,"email"))
                    .addParameter("country",PrefManager.getLoginDetail(context,"country"))
                    .addParameter("state",PrefManager.getLoginDetail(context,"state"))
                    .addParameter("image_name",backgroundimagePath)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            ProfileActivity fragment = new ProfileActivity();
            function.loadFragment(context,fragment,null);
               */

             //getActivity().finish();
         } catch (Exception exc) {
             Toast.makeText(context,""+exc.getMessage(), Toast.LENGTH_SHORT).show();
         }
         /*
          type: profile_pic
          process_type: android
          page_url: page/update_profile.html
          user_id: 693
          avatar: (binary)

          */
     }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {

                Toast.makeText(context, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }
    private void launchCameraIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    public void CreateProgressDialog()
    {
        progressdialog = new ProgressDialog(EditProfileActivity.this.getContext());
        progressdialog.setIndeterminate(false);
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setCancelable(true);
        progressdialog.setMax(100);
        progressdialog.show();
    }

    public void ShowProgressDialog()
    {
        status = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){
                    status +=1;
                    try{
                        Thread.sleep(200);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressdialog.setProgress(status);

                            if(status == 100){

                                progressdialog.dismiss();
                            }
                        }
                    });
                }
            }
        }).start();

    }

}
