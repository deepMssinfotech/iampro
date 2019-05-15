package com.mssinfotech.iampro.co.user;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.MyImageAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.MyProductAdapter;
import com.mssinfotech.iampro.co.adapter.MyVideoAdapter;
import com.mssinfotech.iampro.co.adapter.MyVideoDataAdapter;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.services.SingleUploadBroadcastReceiver;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

public class MyVideoActivity extends Fragment implements MyVideoAdapter.ItemListener,SingleUploadBroadcastReceiver.Delegate {
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private String URL_FEED = "",uid="", id="";
    Intent intent;
    Bitmap FixBitmap;
    private int GALLERY = 1, CAMERA = 2;
    public String backgroundimagePath="";
    ArrayList<MyImageModel> itemm = new ArrayList<>();
    MyVideoAdapter adapter;
    RecyclerView recyclerView;
    HashMap<String,String> item_name = new HashMap<>();
    ArrayList<SectionImageModel> allSampleData=new ArrayList<>();
    MyVideoDataAdapter adapterr;
    ImageView changeImage,changeBackground_Image ;
    Context context;
    View view;
     FloatingActionButton fab;
    public static final int REQUEST_IMAGE = 100;
    public static String imageType;
    Random r = new Random();
    public static int randomNumber;
    ProgressDialog progressdialog;
     int status = 0;
    Handler handler = new Handler();
    public static String img_name_avatar= "avatar_"+String.valueOf(System.currentTimeMillis())+".jpg";
    public static String img_name_background= "background_"+String.valueOf(System.currentTimeMillis())+".jpg";
    ProgressDialog dialog;

    private final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_my_video, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        context = getContext();
        intent = getActivity().getIntent();
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("uid")) {
            id = args.getString("uid").toString();
        }else {
            id = intent.getStringExtra("uid");
        }
        username = view.findViewById(R.id.username);
        userimage = view.findViewById(R.id.userimage);
        recyclerView=view.findViewById(R.id.recyclerView);
        tv_category=view.findViewById(R.id.tv_category);
        userbackgroud = view.findViewById(R.id.userbackgroud);
        changeImage = view.findViewById(R.id.changeImage);
         fab=view.findViewById(R.id.fab);
        changeBackground_Image = view.findViewById(R.id.changeBackground_Image);
        uid= PrefManager.getLoginDetail(context,"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(context,"fname");
            String lname=PrefManager.getLoginDetail(context,"lname");
            String avatar=Config.BANNER_URL+PrefManager.getLoginDetail(context,"profile_video_gallery");
            String background=Config.BANNER_URL+PrefManager.getLoginDetail(context,"video_banner_image");
            username.setText("My Videos");
            Glide.with(this).load(background).apply(Config.options_video).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            userbackgroud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.BANNER_URL+PrefManager.getLoginDetail(context,"video_banner_image"), null);
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.BANNER_URL+PrefManager.getLoginDetail(context,"profile_video_gallery"), null);
                }
            });
            changeImage.setVisibility(View.VISIBLE);
            changeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageType="userImage";
                    showImagePickerOptions();
                }
            });
            changeBackground_Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showPictureDialog();
                    imageType="backgroundImage";
                    showImagePickerOptions();
                }
            });
        }else{
            changeBackground_Image.setVisibility(View.GONE);
            changeImage.setVisibility(View.GONE);
            uid= id;
            gteUsrDetail(id);

        }
        IncludeShortMenu includeShortMenu = view.findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(context,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);
        //getVideo();
        getAllAlbum();
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i_signup = new Intent(context,AddVideoActivity.class);
                 MyVideoActivity.this.startActivity(i_signup);
             }
         });
        if (!PrefManager.isLogin(MyVideoActivity.this.getContext())){
            fab.hide();
        }
        else if (!PrefManager.getLoginDetail(MyVideoActivity.this.getContext(),"id").equalsIgnoreCase(id)) {
            fab.hide();
        }
    }
    private void gteUsrDetail(String id){
        String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + id + "&uid=" + uid;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            Log.d(Config.TAG, response);
                            result = new JSONObject(response);
                            String fname=result.optString("fname");
                            String lname=result.optString("lname");
                            final String avatarX=result.getString("profile_video_gallery");
                            final String backgroundX=result.getString("video_banner_image");
                            username = view.findViewById(R.id.username);
                            userimage = view.findViewById(R.id.userimage);
                            userbackgroud = view.findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+" Videos");
                            Glide.with(context).load(Config.BANNER_URL+"h/250/"+backgroundX).apply(Config.options_video).into(userbackgroud);
                            Glide.with(context).load(Config.BANNER_URL+"250/250/"+avatarX).apply(Config.options_avatar).into(userimage);
                            userbackgroud.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.BANNER_URL+backgroundX, null);
                                }
                            });
                            userimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.BANNER_URL+avatarX, null);
                                }
                            });

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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

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


   // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "mss popup",  Toast.LENGTH_LONG).show();
   // }

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
        else if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    backgroundimagePath = getPath(getImageUri(context,bitmap));

                    // loading profile image from local cache
                    loadImgData(uri.toString());
                    // loading profile image from local cache
                    //loadVideoData(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    public void sendData() {
        if (!Config.haveNetworkConnection(context)) {
            Config.showInternetDialog(context);
            return;
        }
        /*dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("Uploading photo, please wait.");
        dialog.setMax(100);
        dialog.setCancelable(true);
        dialog.show(); */
        CreateProgressDialog();

        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        try {
            //Creating a multi part request
            String boundary = "---------------------------14737809831466499882746641449";
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "video_banner_image") //Adding file
                    .addParameter("type","update_img_video_banner")//Adding text parameter to the request
                    .addParameter("process_type","android")
                    .addParameter("userid",PrefManager.getLoginDetail(context,"id"))
                    .addParameter("page_url","page/update_profile.html")
                    .addParameter("fname",PrefManager.getLoginDetail(context,"fname"))
                    .addParameter("email",PrefManager.getLoginDetail(context,"email"))
                    .addParameter("country",PrefManager.getLoginDetail(context,"country"))
                    .addParameter("state",PrefManager.getLoginDetail(context,"state"))
                    .addParameter("img_name",img_name_background)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            ShowProgressDialog();
            //ProfileActivity fragment = new ProfileActivity();
            //function.loadFragment(context,fragment,null);
            Toast.makeText(context, "Update Profile Background Image is processing please wait", Toast.LENGTH_SHORT).show();
            //getActivity().finish();
        } catch (Exception exc) {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void CreateProgressDialog()
    {
        progressdialog = new ProgressDialog(MyVideoActivity.this.getContext());
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


    @Override
    public void onItemClick(MyImageModel item) {

    }
    public void getAllAlbum(){
        String url=Config.API_URL+ "app_service.php?type=getAlbemsListt&search_type=video&uid="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(!item_name.isEmpty()){
                            item_name.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject student1 = response.getJSONObject(i);
                                String name1=student1.optString("id");
                                String album_name=student1.optString("album_name");
                                //item_name.add(name1);
                                item_name.put(name1,album_name);
                            }
                            Log.d("allsampledataname",item_name.toString());
                            for (String data:item_name.keySet()){
                                getVideo(data);
                                Log.d("Keyset",""+data);
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getVideo(final String aid){
        String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=video&uid="+uid+"&my_id="+uid+"&album_id="+aid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(item_name.get(aid));
                         dm.setAlbemId(aid);
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        //dm.setAddedBy(id);

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                String name1 = student.getString("name");
                                tv_category.setText(name1.toString());
                                JSONArray jsonArrayPics = student.getJSONArray("pics");
                                Log.d("picssss", jsonArrayPics.toString());
                                String added_byy=student.optString("added_by");
                                dm.setAddedBy(added_byy);
                                //JSONObject picss= jsonArrayPics.getJSONObject(0);
                                //Log.d("picssss11",""+picss.toString());
                                //String idd=picss.getString("albemid");
                                //Log.d("picssss1",""+idd);
                                for (int j = 0; j < jsonArrayPics.length(); j++) {
                                    JSONObject pics = jsonArrayPics.getJSONObject(j);
                                    String id = pics.getString("id");

                                    String albemid = pics.optString("albemid");
                                    String name = pics.optString("name");
                                    String category = pics.optString("category");
                                    String albem_type = pics.optString("albem_type");
                                    String imagev = pics.optString("image");
                                    String imagee = imagev.substring(0, imagev.lastIndexOf("."));
                                    String image = imagee + ".jpg";
                                     Log.d("jpg_image",""+image);
                                    //String image
                                    Log.d("imagebb", "" + image);
                                    String udate = pics.optString("udate");
                                    String about_us = pics.optString("about_us");
                                    String group_id = pics.optString("group_id");
                                    String is_featured = pics.optString("is_featured");
                                    String status = pics.optString("status");
                                    String is_block = pics.optString("is_block");
                                    String comments = pics.optString("comments");
                                    String totallike = pics.optString("totallike");
                                    String like_unlike = pics.optString("like_unlike");
                                    String rating = pics.optString("rating");
                                    item.add(new MyImageModel(id, albemid, name, category, albem_type, image, udate, about_us, group_id, is_featured, status, is_block, comments, totallike, like_unlike, rating, uid));

                                }
                            }

                            Log.d("bdm",singleItem.toString());
                            // dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("allsampledatav",item.toString());
                           /* adapter = new MyVideoAdapter(getApplicationContext(),item,MyVideoActivity.this);
                            recyclerView.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setNestedScrollingEnabled(false); */


                            dm.setAllItemsInSection(item);
                            Log.d("adm",item.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new MyVideoDataAdapter(context,allSampleData,item_name);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapterr);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
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
        if(imageType.equalsIgnoreCase("backgroundImage")) {
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        }else{
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        }
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
        if(imageType.equalsIgnoreCase("backgroundImage")) {
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 6);
        }else{
            intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
            intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void loadVideoData(String url)   {
        Log.d("eProfile_uri", "Image cache path: " + url);
        if(imageType.equalsIgnoreCase("backgroundImage"))  {
            Glide.with(this).load(url)
                    .into(userbackgroud);
        }
        else if (imageType.equalsIgnoreCase("userImage")){
            //userimage
            Glide.with(this).load(url)
                    .into(userimage);
        }
    }
    public void sendUserPic(){
        if (!Config.haveNetworkConnection(context)) {
            Config.showInternetDialog(context);
            return;
        }
        CreateProgressDialog();
        try {
            String uploadId =UUID.randomUUID().toString();
            uploadReceiver.setDelegate((SingleUploadBroadcastReceiver.Delegate) this);
            uploadReceiver.setUploadID(uploadId);
            //Creating a multi part request
            String boundary = "---------------------------14737809831466499882746641449";
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
                    .addParameter("img_name",img_name_avatar)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
             ShowProgressDialog();
            //ProfileActivity fragment = new ProfileActivity();
            //function.loadFragment(context,fragment,null);

        } catch (Exception exc) {
            Toast.makeText(context,""+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void loadImgData(String url)   {
        if(imageType.equalsIgnoreCase("backgroundImage"))  {
            Glide.with(this).load(url).apply(Config.options_background).into(userbackgroud);
            PrefManager.updateLoginDetail(context,"video_banner_image",img_name_background);
            sendData();
        }
        else if (imageType.equalsIgnoreCase("userImage")){
            //userimage
            Glide.with(this).load(url).apply(Config.options_avatar).into(userimage);
            PrefManager.updateLoginDetail(context,"profile_video_gallery",img_name_avatar);
            sendUserPic();
        }
    }
    @Override
    public void onProgress(int progress) {
        Toast.makeText(getContext(),""+progress+"\t"+"Percentage Completed",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }
    @Override
    public void onError(Exception exception) {

    }
    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        //Toast.makeText(getContext(),""+"Video uploaded successfully...",Toast.LENGTH_LONG).show();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(MyVideoActivity.this.getContext());
        builder.setCancelable(true);
        builder.setTitle("Confirm!");
        builder.setMessage("Video uploaded successfully...");
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        */
    }

    @Override
    public void onCancelled() {

    }
   @Override
    public void onResume() {

        super.onResume();
        uploadReceiver.register(context);

    }
    @Override
    public void onPause() {
        super.onPause();
        //uploadReceiver.unregister(context);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        uploadReceiver.register(context);
    }
}
