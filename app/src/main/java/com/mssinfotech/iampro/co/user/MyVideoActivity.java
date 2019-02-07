package com.mssinfotech.iampro.co.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyVideoActivity extends AppCompatActivity implements MyVideoAdapter.ItemListener {

    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,tv_category;
    private String URL_FEED = "",uid="";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_video));
        intent = getIntent();
        String id = intent.getStringExtra("uid");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        recyclerView=findViewById(R.id.recyclerView);
        tv_category=findViewById(R.id.tv_category);
        userbackgroud = findViewById(R.id.userbackgroud);
        changeImage = findViewById(R.id.changeImage);

        uid= PrefManager.getLoginDetail(this,"id");
        if(id == null || id.equals(uid)) {
            String fname=PrefManager.getLoginDetail(this,"fname");
            String lname=PrefManager.getLoginDetail(this,"lname");
            String avatar=Config.BANNER_URL+"250/250/"+PrefManager.getLoginDetail(this,"profile_video_gallery");
            String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"video_banner_image");
            username.setText("My Video Gallery");
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            username.setVisibility(View.VISIBLE);
            username .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MyVideoActivity.this,VideoImageCroperActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            changeImage.setVisibility(View.GONE);
            uid= id;
            gteUsrDetail(id);
        }
        IncludeShortMenu includeShortMenu = findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(this,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);
        //getVideo();
        getAllAlbum();

        changeImage = findViewById(R.id.changeImage);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyVideoActivity.this,ProfileImageCroperActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        changeBackground_Image = findViewById(R.id.changeBackground_Image);
        changeBackground_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
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
                            String avatar=Config.AVATAR_URL+"250/250/"+result.optString("profile_video_gallery");
                            String background=Config.AVATAR_URL+"h/250/"+result.optString("video_banner_image");
                            username = findViewById(R.id.username);
                            userimage = findViewById(R.id.userimage);
                            userbackgroud = findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname+"'s Video Gallery");
                            Glide.with(getApplicationContext()).load(background).apply(Config.options_background).into(userbackgroud);
                            Glide.with(getApplicationContext()).load(avatar).apply(Config.options_avatar).into(userimage);

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


   // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "mss popup",  Toast.LENGTH_LONG).show();
   // }

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
                    userbackgroud.setImageBitmap(FixBitmap);
                    backgroundimagePath = getPath(contentURI);
                    //UploadImageOnServerButton.setVisibility(View.VISIBLE);
                    sendData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MyVideoActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
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

    public void sendData() {
        if (!Config.haveNetworkConnection(this)) {
            Config.showInternetDialog(this);
            return;
        }
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "video_banner_image") //Adding file
                    .addParameter("type","update_img_video_banner")//Adding text parameter to the request
                    .addParameter("process_type","android")
                    .addParameter("userid",PrefManager.getLoginDetail(getApplicationContext(),"id"))
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(this, "Update Profile Background Image is processing please wait", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void redirect(View v){
        Intent i_signup = new Intent(MyVideoActivity.this,AddVideoActivity.class);
        MyVideoActivity.this.startActivity(i_signup);
    }
    @Override
    public void onItemClick(MyImageModel item) {

    }
    public void getAllAlbum(){
        String url="https://www.iampro.co/api/app_service.php?type=getAlbemsListt&search_type=video&uid="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                 String name1=student.getString("name");
                                tv_category.setText(name1.toString());
                                JSONArray jsonArrayPics=student.getJSONArray("pics");
                                Log.d("picssss",jsonArrayPics.toString());

                                //JSONObject picss= jsonArrayPics.getJSONObject(0);
                                //Log.d("picssss11",""+picss.toString());
                                //String idd=picss.getString("albemid");
                                //Log.d("picssss1",""+idd);

                                for (int j=0;j<jsonArrayPics.length();j++){
                                    JSONObject pics=jsonArrayPics.getJSONObject(j);
                                    String id=pics.getString("id");

                                    String albemid=pics.optString("albemid");
                                    String name=pics.optString("name");
                                    String category=pics.optString("category");
                                    String albem_type=pics.optString("albem_type");
                                    String imagev=pics.optString("image");
                                    String imagee = imagev.substring(0, imagev.lastIndexOf("."));
                                     String image=imagee+".jpg";
                                      Log.d("imagebb",""+image);
                                    String udate=pics.optString("udate");
                                    String about_us=pics.optString("about_us");
                                    String group_id=pics.optString("group_id");
                                    String is_featured=pics.optString("is_featured");
                                    String status=pics.optString("status");
                                    String is_block=pics.optString("is_block");
                                    String comments=pics.optString("comments");
                                    String totallike=pics.optString("totallike");
                                    String like_unlike=pics.optString("like_unlike");
                                    String rating=pics.optString("rating");
                                    item.add(new MyImageModel(id,albemid,name,category,albem_type,image,udate,about_us,group_id,is_featured,status,is_block,comments,totallike,like_unlike,rating,uid));

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
                            adapterr = new MyVideoDataAdapter(MyVideoActivity.this,allSampleData,item_name);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MyVideoActivity.this, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapterr);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }

}
