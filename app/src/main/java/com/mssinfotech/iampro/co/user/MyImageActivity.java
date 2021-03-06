package com.mssinfotech.iampro.co.user;

//import android.app.Activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.GalleryAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

public class MyImageActivity extends Fragment implements MyImageAdapter.ItemListener,SwipeRefreshLayout.OnRefreshListener {
     SwipeRefreshLayout swiperefresh;
    CircleImageView userimage;
    TextView username,tv_category;
    private String URL_FEED = "",uid="", id="";
    Intent intent;
     RecyclerView recyclerView;
    MyImageAdapter adapter;
    Bitmap FixBitmap;
    private int GALLERY = 1, CAMERA = 2;
    public String backgroundimagePath="";
    ArrayList<MyImageModel> itemm= new ArrayList<>();
    //HashSet<String> item_name = new HashSet<>();
    TreeMap<String,String> item_name = new TreeMap<>();
    ArrayList<SectionImageModel> allSampleData=new ArrayList<>();
    MyImageVideoDataAdapter adapterr;
    ImageView userbackgroud ,changeImage,changeBackground_Image ;
    private GalleryAdapter galleryAdapter;
    Context context;
    View view;
    FloatingActionButton fab;
    public static final int REQUEST_IMAGE = 100;
    public static String img_name_avatar= "avatar_"+String.valueOf(System.currentTimeMillis())+".jpg";
    public static String img_name_background= "background_"+String.valueOf(System.currentTimeMillis())+".jpg";
    public static String imageType;
    ProgressDialog progressdialog;
    int status = 0;
    Handler handler = new Handler();
    String added_bys;
     Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_my_image, parent, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Config.allowRefresh) {
            Config.allowRefresh = false;
            //Toast.makeText(context, "click from BACK", Toast.LENGTH_SHORT).show();
            Fragment frg = null;
            AppCompatActivity activity = (AppCompatActivity) context;
            MyImageActivity fragment = new MyImageActivity();
            frg = activity.getSupportFragmentManager().findFragmentByTag(fragment.getClass().getName());
            final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view = v;
        context = getContext();
        intent = getActivity().getIntent();
        dialog=new Dialog(this.getContext());
        Log.e("mragank","img_name_avatar="+img_name_avatar+" \n img_name_background="+img_name_background);
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("uid")) {
            id = args.getString("uid").toString();
        }else {
            id = intent.getStringExtra("uid");
        }
        username = view.findViewById(R.id.username);
        userimage = view.findViewById(R.id.userimage);
        changeImage = view.findViewById(R.id.changeImage);
        swiperefresh=view.findViewById(R.id.swiperefresh);
         swiperefresh.setOnRefreshListener(this);
        // Configure the refreshing colors
         swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        changeBackground_Image = view.findViewById(R.id.changeBackground_Image);
        recyclerView=view.findViewById(R.id.recyclerView);
        tv_category=view.findViewById(R.id.tv_category);
        fab=view.findViewById(R.id.fab);
        if (!PrefManager.isLogin(MyImageActivity.this.getContext())){
             fab.hide();
        }
        else if (!(PrefManager.getLoginDetail(MyImageActivity.this.getContext(),"id").equalsIgnoreCase(id))){
            fab.hide();
        }
        userbackgroud = view.findViewById(R.id.userbackgroud);

        uid= PrefManager.getLoginDetail(context,"id");
        Log.d("image_uid",""+id+"\t"+uid+"\t"+PrefManager.getLoginDetail(MyImageActivity.this.getContext(),"id"));
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(context,"fname");
            String lname=PrefManager.getLoginDetail(context,"lname");
            String avatar=Config.BANNER_URL+PrefManager.getLoginDetail(context,"profile_image_gallery");
            String background=Config.BANNER_URL+PrefManager.getLoginDetail(context,"img_banner_image");
            username.setText("My Images");
            Glide.with(this).load(background).apply(Config.options_image).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            Log.e("mragank","avatar="+avatar+" \n background="+background);
            userbackgroud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.BANNER_URL+PrefManager.getLoginDetail(context,"img_banner_image"), null);
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.BANNER_URL+PrefManager.getLoginDetail(context,"profile_image_gallery"), null);
                }
            });
            changeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent=new Intent(context,ImageImageCroperActivity.class);
                    startActivity(intent);
                    getActivity().finish(); */
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
        //getImages();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllAlbum();
            }
        }, 300);

 //  getAllAlbum();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_signup = new Intent(getContext(),AddImageActivity.class);
                MyImageActivity.this.startActivity(i_signup);
            }
        });
    }
    private void gteUsrDetail(String id)
    {
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
                            final String avatarX=result.getString("profile_image_gallery");
                            final String backgroundX=result.getString("img_banner_image");
                            username = view.findViewById(R.id.username);
                            userimage = view.findViewById(R.id.userimage);
                            userbackgroud = view.findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+" Images");
                            Glide.with(context).load(Config.BANNER_URL+"h/250/"+backgroundX).apply(Config.options_image).into(userbackgroud);
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
        else  if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    backgroundimagePath = getPath(getImageUri(context,bitmap));
                    // loading profile image from local cache
                    loadImgData(uri.toString());
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
         //CreateProgressDialog();
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        //final Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "img_banner_image") //Adding file
                    .addParameter("type","update_img_video_banner")//Adding text parameter to the request
                    .addParameter("process_type","android")
                    .addParameter("userid",PrefManager.getLoginDetail(context,"id"))
                    .addParameter("fname",PrefManager.getLoginDetail(context,"fname"))
                    .addParameter("email",PrefManager.getLoginDetail(context,"email"))
                    .addParameter("country",PrefManager.getLoginDetail(context,"country"))
                    .addParameter("state",PrefManager.getLoginDetail(context,"state"))
                    .addParameter("img_name",img_name_background)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
              //ShowProgressDialog();
            //ProfileActivity fragment = new ProfileActivity();
            //function.loadFragment(context,fragment,null);
            Toast.makeText(context, "Update Profile Background Image is processing please wait", Toast.LENGTH_SHORT).show();
            //getActivity().finish();
            if (dialog.isShowing())
                dialog.dismiss();
        } catch (Exception exc) {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    public void sendUserPic(){
        if (!Config.haveNetworkConnection(context)) {
            Config.showInternetDialog(context);
            return;
        }
        //CreateProgressDialog();
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "profile_image_gallery") //Adding file
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
             //ShowProgressDialog();
            //ProfileActivity fragment = new ProfileActivity();
            //function.loadFragment(context,fragment,null);
            //getActivity().finish();
            if (dialog.isShowing())
                dialog.dismiss();

        } catch (Exception exc) {
            Toast.makeText(context,""+exc.getMessage(), Toast.LENGTH_SHORT).show();
            if (dialog.isShowing())
                  dialog.dismiss();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
   /* public void redirect(View v){
        Intent i_signup = new Intent(getContext(),AddImageActivity.class);
        MyImageActivity.this.startActivity(i_signup);
    } */
    public void getAllAlbum(){
      String url=Config.API_URL+"app_service.php?type=getAlbemsListt&search_type=image&uid="+uid;
        Log.e("url",url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //pDialog.dismiss();
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
                                 getImages(data);
                                 Log.d("Keyset",""+data);
                             }
                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //pDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getImages(final String aid){
         //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
         String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
        // Initialize a new RequestQueue instance
        Log.d("urlimggg",""+url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //pDialog.dismiss();
                        if (!allSampleData.isEmpty()){
                            allSampleData.clear();
                        }
                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(item_name.get(aid));
                        dm.setAlbemId(aid);
                        //dm.setAddedBy(id);
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        //ArrayList<String> alAddedBy=new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object

                                JSONObject student = response.getJSONObject(i);
                                 String category1=student.getString("category");
                                  String idd=student.optString("id");
                                  String added_byy=student.optString("added_by");

                                dm.setAddedBy(added_byy);
                                   String name1=student.optString("name");
                                    String atype=student.optString("atype");
                                   tv_category.setText(name1);
                                   tv_category.setVisibility(View.GONE);
                                JSONArray jsonArrayPics=student.getJSONArray("pics");
                                Log.d("picssss",jsonArrayPics.toString());

                                 for (int j=0;j<jsonArrayPics.length();j++){
                                     JSONObject pics=jsonArrayPics.getJSONObject(j);
                                      String id=pics.getString("id");

                                     String albemid=pics.optString("albemid");
                                     String name=pics.optString("name");
                                     String category=pics.optString("category");
                                     String albem_type=pics.optString("albem_type");
                                     String image=pics.optString("image");
                                     String udate=pics.optString("udate");
                                     String about_us=pics.optString("about_us");
                                     String group_id=pics.optString("group_id");
                                     String is_featured=pics.optString("is_featured");
                                     //status
                                     String status=pics.optString("status");
                                     String is_block=pics.optString("is_block");
                                     String comments=pics.optString("comments");
                                     String totallike=pics.optString("totallike");
                                     String like_unlike=pics.optString("like_unlike");
                                     String rating=pics.optString("rating");
              item.add(new MyImageModel(id,albemid,name,category,albem_type,image,udate,about_us,group_id,is_featured,status,is_block,comments,totallike,like_unlike,rating,uid));
                                     //singleItem.add(new SingleItemModel(Integer.parseInt(id), name,image,udate,rating,Integer.parseInt(totallike),Integer.parseInt(comments),Integer.parseInt(uid),name,image,"image"));

                                 }
                                //adapter = new MyImageAdapter(getApplicationContext(),item,item_name,MyImageActivity.this);
                                //recyclerView.setAdapter(adapter);
                               // GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
                               //recyclerView.setLayoutManager(manager);
                              //recyclerView.setNestedScrollingEnabled(false);
                            }
                            Log.d("allsampledatav",item.toString());

                            //dm.setAddedBy(added_bys);
                            dm.setAllItemsInSection(item);
                            Log.d("adm",item.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new MyImageVideoDataAdapter(context, allSampleData,item_name);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapterr);
                            if (dialog.isShowing())
                                dialog.hide();
                                //dialog.dismiss();

                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                            if (dialog.isShowing())
                                 dialog.dismiss();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //pDialog.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                        if (dialog.isShowing())
                             dialog.dismiss();
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
        if (dialog.isShowing())
            dialog.dismiss();
    }
    @Override
    public void onItemClick(MyImageModel item) {

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

    private void loadImgData(String url)   {
        Log.d("eProfile_uri", "Image cache path: " + url);
        if(imageType.equalsIgnoreCase("backgroundImage"))  {
            Glide.with(this).load(url).apply(Config.options_background).into(userbackgroud);
            PrefManager.updateLoginDetail(context,"img_banner_image",img_name_background);
            //userbackgroud.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
            sendData();
        }
        else if (imageType.equalsIgnoreCase("userImage")){
            //userimage
            Glide.with(this).load(url).apply(Config.options_avatar).into(userimage);
            PrefManager.updateLoginDetail(context,"profile_image_gallery",img_name_avatar);
            //userimage.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
             sendUserPic();
        }
    }
    public void CreateProgressDialog()
    {
        progressdialog = new ProgressDialog(MyImageActivity.this.getContext());
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
    public void onRefresh() {
        getAllAlbum();
    }
}
