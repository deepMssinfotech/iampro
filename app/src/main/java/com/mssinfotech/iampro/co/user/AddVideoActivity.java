package com.mssinfotech.iampro.co.user;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.VideoView;

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
import com.mssinfotech.iampro.co.services.SingleUploadBroadcastReceiver;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

//import net.gotev.uploadservice.MultipartUploadRequest;
//import net.gotev.uploadservice.UploadNotificationConfig;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class AddVideoActivity extends AppCompatActivity  implements SingleUploadBroadcastReceiver.Delegate {
    TextView tvlayouttype;
    TextInputLayout tilalbumname, tilvideoname, tilvideodetail;
    EditText etalbumname, etvideoname, etvideodetail;
    Spinner spcat, spvideo_album;
    Button add_video_button, create_album_button, ibVideoMoreVideo;
    private String albumname, videoname, videodetail, cat, video_album="fashion", myVideoPath;
    private GridView gvGallery;
    private Bitmap bitmap = null;
    private GalleryAdapter galleryAdapter;
    private LinearLayout categoryLayout, albumLayout;
    private VideoView videoView;
    Intent i;
    private final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
     ArrayList<String> students = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_add_video));
        i = new Intent(this, AddVideoActivity.class);
        getSupportActionBar().hide();
        tvlayouttype = findViewById(R.id.tvlayouttype);
        tilalbumname = findViewById(R.id.tilalbumname);
        etalbumname = findViewById(R.id.etalbumname);
        tilvideoname = findViewById(R.id.tilvideoname);
        etvideoname = findViewById(R.id.etvideoname);
        tilvideodetail = findViewById(R.id.tilvideodetail);
        etvideodetail = findViewById(R.id.etvideodetail);
        spcat = findViewById(R.id.spcat);
        spvideo_album = findViewById(R.id.spvideo_album);
        add_video_button = findViewById(R.id.add_video_button);
        create_album_button = findViewById(R.id.create_album_button);
        function.getData(AddVideoActivity.this, this, spcat, "VIDEO");
        categoryLayout = findViewById(R.id.categoryLayout);
        albumLayout = findViewById(R.id.albumLayout);
        gvGallery = findViewById(R.id.gv);
        ibVideoMoreVideo = findViewById(R.id.ibVideoMoreVideo);
        videoView = findViewById(R.id.vv);
        ibVideoMoreVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        function.executeUrl(this, "get", Config.API_URL + "app_service.php?type=delete_temp_data&uid=" + PrefManager.getLoginDetail(this, "id"), null);
        getAlbumList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
    }
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);

            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.iampro)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
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
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select Video from gallery",
                "Record Video from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ImageProcess.chooseVideoFromGallary(AddVideoActivity.this);
                                break;
                            case 1:
                                ImageProcess.takeVideoFromCamera(AddVideoActivity.this);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result",""+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Log.d("what","cancle");
            return;
        }
        if (requestCode == Config.GALLERY) {
            Log.d("what","gale");
            if (data != null) {
                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
                myVideoPath = selectedVideoPath;
                Log.d("path",selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(contentURI);
                videoView.requestFocus();
                //videoView.start();
            }

        } else if (requestCode == Config.CAMERA) {
            Uri contentURI = data.getData();
            String recordedVideoPath = getPath(contentURI);
            myVideoPath=recordedVideoPath;
            Log.d("frrr",recordedVideoPath);
            saveVideoToInternalStorage(recordedVideoPath);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(contentURI);
            videoView.requestFocus();
            //videoView.start();
        }
    }
    private void saveVideoToInternalStorage (String filePath) {
        File newfile;
        try {

            File currentFile = new File(filePath);
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + Config.VIDEO_DIRECTORY);
            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            if(currentFile.exists()){

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("vii", "Video file saved successfully.");
            }else{
                Log.v("vii", "Video saving failed. Source file missing.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    public void processAddVideo(View v) {
            albumname = etalbumname.getText().toString();
            videoname = etvideoname.getText().toString();
            videodetail = etvideodetail.getText().toString();
            if (spcat.getSelectedItem()!=null)
            cat = spcat.getSelectedItem().toString();
            if (spvideo_album.getSelectedItem() != null)
                video_album = spvideo_album.getSelectedItem().toString();
            String utype = tvlayouttype.getText().toString();
            if (Validate.isNull(albumname) && utype.equalsIgnoreCase("new_album")) {
                resetError();
                tilalbumname.setErrorEnabled(true);
                tilalbumname.setError("Enter Album Name ");
                return;
            } else if (Validate.isNull(videoname)) {
                resetError();
                tilvideoname.setErrorEnabled(true);
                tilvideoname.setError("Enter Video  Name");
                return;
            } else if (Validate.isNull(videodetail)) {
                resetError();
                tilvideodetail.setErrorEnabled(true);
                tilvideodetail.setError("Enter Image Detail");
                return;
            } else {
                hideKeyboard();
                resetError();
                sendData();
            }

    }
    public void resetError(){
        tilalbumname.setErrorEnabled(false);
        tilvideoname.setErrorEnabled(false);
        tilvideodetail.setErrorEnabled(false);
    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void sendData() {
        if (!Config.haveNetworkConnection(this)) {
            Config.showInternetDialog(this);
            return;
        }
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate((SingleUploadBroadcastReceiver.Delegate) this);
            uploadReceiver.setUploadID(uploadId);
            //Creating a multi part request
            String palbumname= tvlayouttype.getText().toString();
            if(!palbumname.equalsIgnoreCase("videonew"))palbumname=spvideo_album.getSelectedItem().toString();

            //Creating a multi part request
            String boundary = "---------------------------14737809831466499882746641449";
            new MultipartUploadRequest(this, uploadId, Config.AJAX_URL + "uploadprocess.php")
                    .addHeader("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .addFileToUpload(myVideoPath, "myfile") //Adding file
                    .addParameter("type","uploadvideo")//Adding text parameter to the request
                    .addParameter("process_type","native_android")
                    .addParameter("palbumname",palbumname)
                    .addParameter("albumname",albumname)
                    .addParameter("video_name",videoname)
                    .addParameter("about_us",videodetail)
                    .addParameter("category",cat)
                    .addParameter("user_id",PrefManager.getLoginDetail(getApplicationContext(),"id"))
                    .addParameter("my_id",PrefManager.getLoginDetail(getApplicationContext(),"id"))
                    .addParameter("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"))
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            MyVideoActivity fragment = new MyVideoActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(AddVideoActivity.this,"id"));
            function.loadFragment(AddVideoActivity.this,fragment,args);
            //finish();
        } catch (Exception exc) {
            Toast.makeText(this, ""+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void click_video_button(View v) {
        if (!students.isEmpty()) {
            add_video_button.setBackgroundResource(R.drawable.black);
            add_video_button.setTextColor(getResources().getColor(R.color.white));
            create_album_button.setBackgroundResource(R.drawable.white);
            create_album_button.setTextColor(getResources().getColor(R.color.black));
            tvlayouttype.setText("add_video");
            albumLayout.setVisibility(View.VISIBLE);
            categoryLayout.setVisibility(View.GONE);
            tilalbumname.setVisibility(View.GONE);
            etalbumname.setVisibility(View.GONE);
            return;
        }
        else{
            add_video_button.setEnabled(false);
        }
    }
    public void click_album_button(View v) {
        add_video_button.setBackgroundResource(R.drawable.white);
        add_video_button.setTextColor(getResources().getColor(R.color.black));
        create_album_button.setBackgroundResource(R.drawable.black);
        create_album_button.setTextColor(getResources().getColor(R.color.white));
        tvlayouttype.setText("videonew");
        albumLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.VISIBLE);
        tilalbumname.setVisibility(View.VISIBLE);
        etalbumname.setVisibility(View.VISIBLE);
        return;
    }

    public void getAlbumList() {
        //Creating a string request
        String uid = PrefManager.getLoginDetail(this, "id");
        String url = Config.API_URL + "app_service.php?type=getMyAlbemsListt&search_type=video&uid=" + uid + "&my_id=" + uid;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray result = null;
                        if (!students.isEmpty())
                             students.clear();
                        try {
                            //Parsing the fetched Json String to JSON Object
                            result = new JSONArray(response);
                            //Storing the Array of JSON String to our JSON Array
                            //JSONArray result = j.getJSONArray("data");
                            //ArrayList<String> students = new ArrayList<String>();
                            //Calling method getStudents to get the students from the JSON Array
                            //Log.d(TAG,result.toString());
                            for (int i = 0; i < result.length(); i++) {
                                try {
                                    //Getting json object
                                    JSONObject json = result.getJSONObject(i);
                                    //Adding the name of the student to array list
                                    students.add(json.getString("name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            spvideo_album.setAdapter(new ArrayAdapter<String>(AddVideoActivity.this, android.R.layout.simple_spinner_dropdown_item, students));
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

    @Override
    public void onProgress(int progress) {
        Toast.makeText(AddVideoActivity.this,""+progress+"\t"+"Percentage Completed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        //Toast.makeText(AddVideoActivity.this,""+"Video Uploading Completed...",Toast.LENGTH_LONG).show();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddVideoActivity.this);
        alertDialog.setTitle("Uploading Status!");
        alertDialog.setMessage("Video Uploading Completed...");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog.cancel();

            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
    @Override
    public void onCancelled() {
    }
    @Override
    public void onResume() {
        super.onResume();
        //LocalBroadcastManager.getInstance(AddVideoActivity.this).registerReceiver(receiver, new IntentFilter("otp"));
        uploadReceiver.register(AddVideoActivity.this);

    }
    @Override
    public void onPause() {
        super.onPause();
       // LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
        uploadReceiver.unregister(AddVideoActivity.this);
    }

}