package com.mssinfotech.iampro.co;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.services.SingleUploadBroadcastReceiver;
import com.mssinfotech.iampro.co.user.ImagePickerActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

public class OtpRegistrationActivity extends Fragment  implements SingleUploadBroadcastReceiver.Delegate{
    private Button btnprocess;
    private TextInputLayout tilotp,tilpassword,tilcpassword;
    private TextInputEditText etotp,etpassword,etcpassword;
    private TextView btnResedOTP;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int REQUEST_IMAGE = 100;
    private int GALLERY = 1, CAMERA = 2;
    private final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();
    String otp;
    String fname,lname,mobile,email,password,cpassword,category;
    public static String imageType;
    public String backgroundimagePath="",imageuri="";
    CircleImageView changeImage;
    View view;
    Intent intent;
    Context context;
    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_otp_registration, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view  = v;
        etotp = view.findViewById(R.id.etotp);
        etpassword = view.findViewById(R.id.etpassword);
        etcpassword = view.findViewById(R.id.etcpassword);

        context = getContext();
        intent = getActivity().getIntent();

        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null) {
            otp = args.getString("otp").toString();
            fname = args.getString("fname").toString();
            lname = args.getString("lname").toString();
            mobile = args.getString("mobile").toString();
            email = args.getString("email").toString();
            category = args.getString("category").toString();
        }else {
            otp = intent.getStringExtra("otp");
            fname = intent.getStringExtra("fname");
            lname = intent.getStringExtra("lname");
            mobile = intent.getStringExtra("mobile");
            email = intent.getStringExtra("email");
            category = intent.getStringExtra("category");
        }
        btnprocess=view.findViewById(R.id.btnprocess);
        btnprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        btnResedOTP=view.findViewById(R.id.btnResedOTP);
        btnResedOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });
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
        checkAndRequestPermissions();
    }
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
        uploadReceiver.register(context);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
        uploadReceiver.unregister(context);
    }
    public void resend(){
        if (!Config.haveNetworkConnection(context)){
            Config.showInternetDialog(context);
            return;
        }
        final String url=Config.API_URL+"ajax.php";
        //tv.setText(String.valueOf(randomNumber));
        final ProgressDialog loading = ProgressDialog.show(context,"Processing...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.d("error",volleyError.getMessage()+"--" + url.toString());
                        Toast.makeText(context,volleyError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(fingerData.FingerImage());
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                params.put("type","sendotp");
                params.put("otp",otp);
                params.put("mobile",mobile);
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                String[] separated = message.split("OTP is ");
                //TextView tv = (TextView) findViewById(R.id.txtview);
                etotp.setText(separated[1]);
                Log.e("otp is",message+" otp is "+separated[1]);
            }
        }
    };
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

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
    public void sendData(){
        if (!Config.haveNetworkConnection(context)) {
            Config.showInternetDialog(context);
            return;
        }
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("Uploading photo, please wait.");
        dialog.setMax(100);
        dialog.setCancelable(true);
        dialog.show();
        try {
            final String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate((SingleUploadBroadcastReceiver.Delegate) this);
            uploadReceiver.setUploadID(uploadId);
            //Creating a multi part request
            String boundary = "---------------------------14737809831466499882746641449";
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addHeader("Content-Type", "multipart/form-data; boundary="+boundary)
                    .addFileToUpload(backgroundimagePath, "avatar") //Adding file
                    .addParameter("type","user_registration")//Adding text parameter to the request
                    .addParameter("fname",fname)
                    .addParameter("lname",lname)
                    .addParameter("mobile",mobile)
                    .addParameter("email",email)
                    .addParameter("password",password)
                    .addParameter("category",category)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            //ProfileActivity fragment = new ProfileActivity();
            //function.loadFragment(context,fragment,null);
            //getActivity().finish();
        } catch (Exception exc) {
            Toast.makeText(context,""+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        else if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    backgroundimagePath = getPath(getImageUri(context,bitmap));
                    imageuri = uri.toString();
                    // loading profile image from local cache
                    loadImgData(imageuri);
                    // loading profile image from local cache
                    //loadVideoData(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void loadImgData(String url)   {
        Log.d("eProfile_uri", "Image cache path: " + url);
        //userimage
        Glide.with(this).load(url)
                .into(changeImage);
        //userimage.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
            //sendUserPic();
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
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onProgress(int progress) {
        Log.d("PROGRESS", "progress = " + progress);
        dialog.setProgress(progress);
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        dialog.dismiss();
        LoginActivity fragment = new LoginActivity();
        function.loadFragment(context,fragment,null);
        //finish();
    }

    @Override
    public void onCancelled() {

    }
}

