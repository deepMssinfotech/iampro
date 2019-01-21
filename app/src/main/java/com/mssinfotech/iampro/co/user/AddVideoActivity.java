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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
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
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.mssinfotech.iampro.co.utils.Validate;

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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddVideoActivity extends AppCompatActivity {
    TextView tvlayouttype;
    TextInputLayout tilalbumname, tilvideoname, tilvideodetail;
    EditText etalbumname, etvideoname, etvideodetail;
    Spinner spcat, spvideo_album;
    Button add_video_button, create_album_button, ibVideoMoreVideo;
    private String albumname, videoname, videodetail, cat, video_album;
    private GridView gvGallery;
    private Bitmap bitmap = null;
    private GalleryAdapter galleryAdapter;
    private LinearLayout categoryLayout, albumLayout;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_add_video));
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
        videoView = (VideoView) findViewById(R.id.vv);
        ibVideoMoreVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        function.executeUrl(this, "get", Config.API_URL + "app_service.php?type=delete_temp_data&uid=" + PrefManager.getLoginDetail(this, "id"), null);
        getAlbumList();
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
        if (resultCode == this.RESULT_CANCELED) {
            Log.d("what","cancle");
            return;
        }
        if (requestCode == Config.GALLERY) {
            Log.d("what","gale");
            if (data != null) {
                Uri contentURI = data.getData();
                String selectedVideoPath = getPath(contentURI);
                Log.d("path",selectedVideoPath);
                saveVideoToInternalStorage(selectedVideoPath);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(contentURI);
                videoView.requestFocus();
                videoView.start();

            }

        } else if (requestCode == Config.CAMERA) {
            Uri contentURI = data.getData();
            String recordedVideoPath = getPath(contentURI);
            Log.d("frrr",recordedVideoPath);
            saveVideoToInternalStorage(recordedVideoPath);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(contentURI);
            videoView.requestFocus();
            videoView.start();
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
        cat = spcat.getSelectedItem().toString();
        video_album = spvideo_album.getSelectedItem().toString();
        String utype=tvlayouttype.getText().toString();
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
        Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        return;
        /*
        final ProgressDialog loading = ProgressDialog.show(this, "Processing...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.AJAX_URL + "uploadprocess.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Log.d("Lresponse", "" + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String status = jsonObject.getString("status");
                            String msgg = jsonObject.getString("msg");

                            Toast.makeText(getApplicationContext(), "" + msgg, Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")) {
                                //String urlv=jsonObject.getString("url");

                                etalbumname.setText(" ");
                                etvideoname.setText(" ");
                                etvideodetail.setText(" ");


                                Intent intent = new Intent(getApplicationContext(), MyImageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            loading.dismiss();
                            Log.d("JSoNExceptionv", e.getMessage());
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String palbumname = tvlayouttype.getText().toString();
                if (!palbumname.equalsIgnoreCase("new_album")) palbumname = albumname;
                Map<String, String> params = new Hashtable<String, String>();
                params.put("type", "uploadfiles");
                params.put("process_type", "android");
                params.put("palbumname", palbumname);
                //params.put("albumname",albumname);
                params.put("name", videoname);
                params.put("about_us", videodetail);
                params.put("category", cat);
                params.put("user_id", PrefManager.getLoginDetail(getApplicationContext(), "id"));
                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
        */
    }

    public void click_video_button(View v) {
        add_video_button.setBackgroundResource(R.drawable.black);
        add_video_button.setTextColor(getResources().getColor(R.color.white));
        create_album_button.setBackgroundResource(R.drawable.white);
        create_album_button.setTextColor(getResources().getColor(R.color.black));
        tvlayouttype.setText("add_iamge");
        albumLayout.setVisibility(View.VISIBLE);
        categoryLayout.setVisibility(View.GONE);
        tilalbumname.setVisibility(View.GONE);
        etalbumname.setVisibility(View.GONE);
        return;
    }

    public void click_album_button(View v) {
        add_video_button.setBackgroundResource(R.drawable.white);
        add_video_button.setTextColor(getResources().getColor(R.color.black));
        create_album_button.setBackgroundResource(R.drawable.black);
        create_album_button.setTextColor(getResources().getColor(R.color.white));
        tvlayouttype.setText("new_album");
        albumLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.VISIBLE);
        tilalbumname.setVisibility(View.VISIBLE);
        etalbumname.setVisibility(View.VISIBLE);
        return;
    }

    public void getAlbumList() {
        //Creating a string request
        String uid = PrefManager.getLoginDetail(this, "id");
        String url = Config.API_URL + "app_service.php?type=getMyAlbemsListt&search_type=image&uid=" + uid + "&my_id=" + uid;

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray result = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            result = new JSONArray(response);
                            //Storing the Array of JSON String to our JSON Array
                            //JSONArray result = j.getJSONArray("data");
                            ArrayList<String> students = new ArrayList<String>();
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
}