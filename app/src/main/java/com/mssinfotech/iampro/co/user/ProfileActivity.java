package com.mssinfotech.iampro.co.user;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.ChatToUser;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.AllFeedAdapter;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.IncludeShortMenu;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.data.FeedItem;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import net.gotev.uploadservice.MultipartUploadRequest;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

public class ProfileActivity extends Fragment implements AllFeedAdapter.ItemListener,SwipeRefreshLayout.OnRefreshListener  {
 //,OnLikeListener,OnAnimationEndListener
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int FEED_LIMIT=15;
    private static int FEED_START=0;

    private List<FeedItem> feedItems;
    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username,myuid;
    private String URL_FEED = "",uid="", fid = "";
    private Integer start=0,limit=20;
    private LinearLayoutManager mLayoutManager,dashboard_layout;
    protected Handler handler;
    Intent intent;
    RecyclerView vFeed;
    AllFeedAdapter adapter;
    ArrayList<FeedModel> mValues=new ArrayList<>();
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View currentFocusedLayout, oldFocusedLayout;
    CardView userDetail_Layout,ll_dashboard,ll_frienddashboard;
    TextView tv_name,tv_dob,tv_categories,tv_email,tv_gender,tv_address,tv_city,tv_state,tv_country,tv_detail,tv_message;
    public static String img_name_avatar= "avatar_"+String.valueOf(System.currentTimeMillis())+".jpg";
    public static String img_name_background= "background_"+String.valueOf(System.currentTimeMillis())+".jpg";
    View view;
    String FrindStatus = "";
    // ProgressDialog loading = ProgressDialog.show(getContext(),"Processing...","Please wait...",false,false);
    public static String imageType;
    ImageView  changeImage,changeBackground_Image;
    public static final int REQUEST_IMAGE = 100;
    private int GALLERY = 1, CAMERA = 2;
    Bitmap FixBitmap;
    public String backgroundimagePath="";

    ProgressDialog progressdialog;
    int status = 0;
    Handler handlers = new Handler();
    FloatingActionButton floatingActionButton;

    public void onBackPressed()
    {
        FragmentManager fm =new ProfileActivity().getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_profile, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view =v;
        context = getContext();
        Config.getCountFromServer(getContext());
        LinearLayout edit_layout = view.findViewById(R.id.edit_layout);
        LinearLayout dashboard_layout = view.findViewById(R.id.dashboard_layout);
        LinearLayout chat_layout = view.findViewById(R.id.chat_layout);
        userDetail_Layout = view.findViewById(R.id.userDetail_Layout);
        intent = getActivity().getIntent();
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("uid")) {
            fid = args.getString("uid").toString();
        }else {
            fid = intent.getStringExtra("uid");
        }
        uid= PrefManager.getLoginDetail(context,"id");
        //Toast.makeText(context,uid+"---"+fid,Toast.LENGTH_LONG).show();
        Log.d(Config.TAG,uid+"--"+fid+" addedby");
        ll_dashboard=view.findViewById(R.id.ll_dashboard);
        ll_frienddashboard=view.findViewById(R.id.ll_frienddashboard);
        username = view.findViewById(R.id.username);
        userimage = view.findViewById(R.id.userimage);
        userbackgroud = view.findViewById(R.id.userbackgroud);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        tv_name=view.findViewById(R.id.tv_name);
        tv_dob=view.findViewById(R.id.tv_dob);
        tv_categories=view.findViewById(R.id.tv_categories);
        tv_email=view.findViewById(R.id.tv_email);
        tv_gender=view.findViewById(R.id.tv_gender);
        tv_address=view.findViewById(R.id.tv_address);
        tv_city=view.findViewById(R.id.tv_city);
        tv_state=view.findViewById(R.id.tv_state);
        tv_country=view.findViewById(R.id.tv_country);

        vFeed=view.findViewById(R.id.rvFeed);

        if(fid == null || fid.isEmpty() || fid.equals(uid)) {
            String fname=PrefManager.getLoginDetail(context,"fname");
            String lname=PrefManager.getLoginDetail(context,"lname");
            String avatar=Config.AVATAR_URL+PrefManager.getLoginDetail(context,"img_url");
            String background=Config.AVATAR_URL+PrefManager.getLoginDetail(context,"banner_image");
            username.setText(fname +" "+lname);
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            userbackgroud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"banner_image"), null);
                }
            });
            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+PrefManager.getLoginDetail(context,"img_url"), null);
                }
            });
            edit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditProfileActivity fragment = new EditProfileActivity();
                    function.loadFragment(context,fragment,null);
                }
            });
            PrefManager.updateUserData(context,null);
            tv_name.setText(fname+" "+lname);
            tv_dob.setText(PrefManager.getLoginDetail(context,"dob"));
            tv_categories.setText(PrefManager.getLoginDetail(context,"category"));
            tv_email.setText(PrefManager.getLoginDetail(context,"email"));
            tv_gender.setText(PrefManager.getLoginDetail(context,"gender"));
            tv_address.setText(PrefManager.getLoginDetail(context,"address"));
            tv_city.setText(PrefManager.getLoginDetail(context,"city"));
            tv_state.setText(PrefManager.getLoginDetail(context,"state"));
            tv_country.setText(PrefManager.getLoginDetail(context,"country"));
            userDetail_Layout.setVisibility(View.GONE);
            fid=uid;
            ll_dashboard.setVisibility(View.VISIBLE);
            ll_frienddashboard.setVisibility(View.GONE);
        }else{
            uid= fid;


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gteUsrDetail(fid);
                }
            }, 1000);

            ll_dashboard.setVisibility(View.GONE);
            ll_frienddashboard.setVisibility(View.VISIBLE);

        }
        IncludeShortMenu includeShortMenu = view.findViewById(R.id.includeShortMenu);
        includeShortMenu.updateCounts(context,uid);
        TextView myuid= includeShortMenu.findViewById(R.id.myuid);
        myuid.setText(uid);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getFeed(FEED_START);
            }
        }, 1000);

        vFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Toast.makeText(context,"Scroll1",Toast.LENGTH_LONG).show();
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    Toast.makeText(context,"Scroll",Toast.LENGTH_LONG).show();
                    //get the recyclerview position which is completely visible and first
                    int positionView = ((LinearLayoutManager)vFeed.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    Log.i("VISISBLE",   ""+positionView);
                    if (positionView >= 0) {
                        if (oldFocusedLayout != null) {
                            //Stop the previous video playback after new scroll
                            //VideoView vv_dashboard = (VideoView) oldFocusedLayout.findViewById(R.id.vv_dashboard);
                            //vv_dashboard.stopPlayback();
                            Toast.makeText(context,mValues.get(positionView)+"",Toast.LENGTH_LONG).show();
                        }
                        currentFocusedLayout = ((LinearLayoutManager) vFeed.getLayoutManager()).findViewByPosition(positionView);
                        //VideoView vv_dashboard = (VideoView) currentFocusedLayout.findViewById(R.id.vv_dashboard);
                        //to play video of selected recylerview, videosData is an array-list which is send to recyclerview adapter to fill the view. Here we getting that specific video which is displayed through recyclerview.
                        //playVideo(mValues.get(positionView));
                        Toast.makeText(context,mValues.get(positionView)+"",Toast.LENGTH_LONG).show();
                        oldFocusedLayout = currentFocusedLayout;
                    }
                }

            }

        });
        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.scroll_view);
        if (scroller != null) {
            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.d(TAG,scrollY+"-----"+v.getMeasuredHeight() +"/"+ v.getChildAt(0).getMeasuredHeight()+"====="+(v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight()));
                    if (scrollY == ( v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight() )) {
                        Log.i(TAG, "BOTTOM SCROLL");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FEED_START=FEED_START+FEED_LIMIT;
                                getFeed(FEED_START);
                            }
                        }, 2000);
                    }
                }
            });
        }
        dashboard_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userDetail_Layout.getVisibility() == View.VISIBLE) {
                    userDetail_Layout.setVisibility(View.GONE);
                } else {
                    userDetail_Layout.setVisibility(View.VISIBLE);
                }
            }
        });
        chat_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"redirect to chat page...",Toast.LENGTH_LONG).show();
                 Intent intent=new Intent(getContext(), ChatToUser.class);
                  intent.putExtra("id",String.valueOf(fid));
                  getContext().startActivity(intent);
            }
        });

        changeImage = view.findViewById(R.id.changeImage);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        if (PrefManager.isLogin(ProfileActivity.this.getContext())){
            changeBackground_Image.setVisibility(View.VISIBLE);
            changeImage.setVisibility(View.VISIBLE);
        }
        else{
            changeBackground_Image.setVisibility(View.GONE);
            changeImage.setVisibility(View.GONE);
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
    private void gteUsrDetail(String id){
        String myurl = Config.API_URL + "ajax.php?type=friend_detail&id=" + fid + "&uid=" + PrefManager.getLoginDetail(context,"id");
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            Log.d(Config.TAG, response);
                            result = new JSONObject(response);
                            String fname=result.getString("fname");
                            String lname=result.getString("lname");
                            final String avatarX=result.getString("avatar");
                            final String backgroundX=result.getString("banner_image");
                            username = view.findViewById(R.id.username);
                            userimage = view.findViewById(R.id.userimage);
                            userbackgroud = view.findViewById(R.id.userbackgroud);
                            username.setText(fname +" "+lname);
                            Glide.with(context).load(Config.AVATAR_URL+"h/250/"+backgroundX).apply(Config.options_background).into(userbackgroud);
                            Glide.with(context).load(Config.AVATAR_URL+"250/250/"+avatarX).apply(Config.options_avatar).into(userimage);
                            userbackgroud.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+backgroundX, null);
                                }
                            });
                            userimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, Config.AVATAR_URL+avatarX, null);
                                }
                            });
                            tv_name.setText(fname+" "+lname);
                            tv_dob.setText(result.getString("dob"));
                            tv_categories.setText(result.getString("category"));
                            tv_email.setText(result.getString("email"));
                            tv_gender.setText(result.getString("gender"));
                            tv_address.setText(result.getString("address"));
                            tv_city.setText(result.getString("city"));
                            tv_state.setText(result.getString("state"));
                            tv_country.setText(result.getString("country"));
                            userDetail_Layout.setVisibility(View.VISIBLE);
                            final TextView friend_request = view.findViewById(R.id.friend_request);
                            FrindStatus = result.getString("friend_status");
                            if(FrindStatus.equals("NO")){
                                friend_request.setText("Add to Friend");
                            }else if(FrindStatus.equals("PANDING")){
                                friend_request.setText("Cancel Request");
                            }else{
                                friend_request.setText("Remove Friend");
                            }
                            friend_request.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String myid=PrefManager.getLoginDetail(context,"id");
                                    if(FrindStatus.equals("NO")){
                                       function.executeUrl(context,"get",Config.API_URL+"app_service.php?type=join_friend&fid="+fid+"&uid="+myid,null);
                                       friend_request.setText("Delete Request");
                                       FrindStatus = "PANDING";
                                    }else if(FrindStatus.equals("PANDING")){
                                        function.executeUrl(context,"get",Config.API_URL+"app_service.php?type=delete_friend&id="+fid+"&tid="+myid,null);
                                        friend_request.setText("Add to Friend");
                                        FrindStatus = "NO";
                                    }else{
                                        function.executeUrl(context,"get",Config.API_URL+"app_service.php?type=delete_friend&id="+fid+"&tid="+myid,null);
                                        friend_request.setText("Add To Friend");
                                        FrindStatus = "NO";
                                    }
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
    private void loadVideoData(String url)   {
        Log.d("eProfile_uri", "Image cache path: " + url);
        if(imageType.equalsIgnoreCase("backgroundImage"))  {
            Glide.with(this).load(url).apply(Config.options_background).into(userbackgroud);
        }
        else if (imageType.equalsIgnoreCase("userImage")){
            //userimage
            Glide.with(this).load(url).apply(Config.options_avatar).into(userimage);
        }
    }
    private void loadImgData(String url)   {
        if(imageType.equalsIgnoreCase("backgroundImage"))  {
            Glide.with(this).load(url).apply(Config.options_background).into(userbackgroud);
            PrefManager.updateLoginDetail(context,"banner_image",img_name_background);
            sendData();
        }
        else if (imageType.equalsIgnoreCase("userImage")){
            //userimage
            Glide.with(this).load(url).apply(Config.options_avatar).into(userimage);
            PrefManager.updateLoginDetail(context,"img_url",img_name_avatar);
            sendUserPic();
        }
    }
    public void sendData() {
        if (!Config.haveNetworkConnection(context)) {
            Config.showInternetDialog(context);
            return;
        }

        //CreateProgressDialog();
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
                    .addParameter("img_name",img_name_background)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            //ShowProgressDialog();
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
        Glide.with(this).load(backgroundimagePath).into(userimage);
        //CreateProgressDialog();
        //Toast.makeText(getApplicationContext(), "Video upload remain pleasw wait....", Toast.LENGTH_LONG).show();
        //return;

        try {
            String uploadId=UUID.randomUUID().toString();
            //Creating a multi part request
            String image_name=System.currentTimeMillis()+".jpg";
            new MultipartUploadRequest(context, uploadId, Config.AJAX_URL + "signup.php")
                    .addFileToUpload(backgroundimagePath, "avatar") //Adding file
                    .addParameter("type","profile_pic")//Adding text parameter to the request
                    .addParameter("process_type","android")
                    .addParameter("page_url","page/update_profile.html")
                    .addParameter("user_id",PrefManager.getLoginDetail(context,"id"))
                    .addParameter("image_name",image_name)
                    .addParameter("userid",PrefManager.getLoginDetail(context,"id"))
                    .addParameter("fname",PrefManager.getLoginDetail(context,"fname"))
                    .addParameter("email",PrefManager.getLoginDetail(context,"email"))
                    .addParameter("country",PrefManager.getLoginDetail(context,"country"))
                    .addParameter("state",PrefManager.getLoginDetail(context,"state"))
                    .addParameter("image_name",backgroundimagePath)
                    .addParameter("img_name",img_name_avatar)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
            PrefManager.updateLoginDetail(context,"options_avatar",image_name);
            ProfileActivity fragment = new ProfileActivity();
            function.loadFragment(context,fragment,null);

        } catch (Exception exc) {
            Toast.makeText(context,""+exc.getMessage(), Toast.LENGTH_SHORT).show();
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

                Toast.makeText(context, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

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
            //userbackgroud.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
            sendData();
        }
        else if (imageType.equalsIgnoreCase("userImage")){
            //userimage
            //backgroundimagePath = getPath(url);
            Glide.with(this).load(url)
                    .into(userimage);
            //userimage.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
            Toast.makeText(context,"Sending",Toast.LENGTH_LONG).show();
            sendUserPic();
        }
    }

    public void join_friend(String uid,String id){

    }
    public void delete_friend(String uid,String id){

    }
    public void getFeed(int start){
        String My_id=PrefManager.getLoginDetail(context,"id");
        //final ProgressDialog loading = ProgressDialog.show(context,"Processing...","Please wait...",false,false);
        URL_FEED = Config.API_URL+ "feed_service.php?type=AllFeeds&start="+start+"&limit="+FEED_LIMIT+"&fid=" +fid+ "&uid=" +My_id+ "&my_id=" +My_id;
        Log.e(Config.TAG,URL_FEED);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_FEED,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Prod_detaili_profile",""+response);
                        String detail_name="";
                        int selling_cost=0;
                        int purchese_cost=0;
                        // Process the JSON
                        try {
                            String feedTotal = response.getString("feedTotal");
                            String status = response.optString("status");
                            int is_favrait = 0;
                            if (status.equalsIgnoreCase("success")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String image = jsonObject.getString("first_image");
                                    //JSONObject detail_obj = jsonObject.optJSONObject("detail");
                                    String detail = jsonObject.getString("detail");
                                    //String detail = detail_obj.getString("name");

                                    String fimage_path = Config.URL_ROOT + "uploads/avatar/150/150/'" + image;
                                    int id = jsonObject.getInt("id");
                                    String shareid = jsonObject.getString("shareid");
                                    String fullname = jsonObject.getString("fullname");
                                    int uid = jsonObject.getInt("uid");
                                    String avatar = jsonObject.getString("avatar");
                                    String avatar_path = Config.AVATAR_URL + avatar;
                                    String udate = jsonObject.getString("udate");
                                    Long timespam = jsonObject.getLong("timespam");
                                    String is_block = jsonObject.getString("is_block");
                                    String type = jsonObject.getString("type");
                                    /* if (type.equalsIgnoreCase("PRODUCT") || type.equalsIgnoreCase("PROVIDE") || type.equalsIgnoreCase("DEMAND")) {
                                         JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                         String detail_name = jsonObjectdetail.getString("name");
                                         String selling_cost = jsonObjectdetail.getString("selling_cost");
                                         String purchese_cost = jsonObjectdetail.getString("purchese_cost");
                                     } */
                                    ArrayList<String> imageArray = new ArrayList<>();
                                    if (type.equalsIgnoreCase("VIDEO")) {
                                        JSONArray jsonArrayImage = jsonObject.getJSONArray("video_array");
                                        for (int j = 0; j < jsonArrayImage.length(); j++) {
                                            imageArray.add(jsonArrayImage.optString(j));
                                        }
                                    } else if (type.equalsIgnoreCase("IMAGE")) {
                                        JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                        for (int j = 0; j < jsonArrayImage.length(); j++) {
                                            imageArray.add(jsonArrayImage.optString(j));
                                        }
                                    } else if (type.equalsIgnoreCase("PRODUCT")) {
                                        JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                        for (int j = 0; j < jsonArrayImage.length(); j++) {
                                            imageArray.add(jsonArrayImage.optString(j));
                                        }
                                    } else if (type.equalsIgnoreCase("PROVIDE")) {
                                        JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                        for (int j = 0; j < jsonArrayImage.length(); j++) {
                                            imageArray.add(jsonArrayImage.optString(j));
                                        }
                                        is_favrait = jsonObject.getInt("is_favrait");
                                    } else if (type.equalsIgnoreCase("DEMAND")) {
                                        JSONArray jsonArrayImage = jsonObject.getJSONArray("image_array");
                                        for (int j = 0; j < jsonArrayImage.length(); j++) {
                                            imageArray.add(jsonArrayImage.optString(j));
                                        }
                                        is_favrait = jsonObject.getInt("is_favrait");
                                    }
                                    int comment = jsonObject.getInt("comment");
                                    int likes = jsonObject.getInt("likes");
                                    // int is_favrait = jsonObject.getInt("is_favrait");
                                    int mylikes = jsonObject.getInt("mylikes");
                                    //average_rating
                                    int all_rating = jsonObject.getInt("average_rating");
                                    //type all_comment average_rating


                                    if (type.equalsIgnoreCase("IMAGE")) {
                                        fimage_path = Config.URL_ROOT + "uploads/album/150/150/" + image;
                                    } else if (type.equalsIgnoreCase("VIDEO")) {
                                        ///uploads/album/400/500/' /uploads/v_image/'
                                        fimage_path = Config.URL_ROOT + "uploads/video/" + detail;
                                    }
                                    //PRODUCT
                                    else if (type.equalsIgnoreCase("PRODUCT")) {
                                        fimage_path = Config.URL_ROOT + "/uploads/product/400/400/" + image;

                                        JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                        detail_name = jsonObjectdetail.getString("name");
                                        selling_cost = jsonObjectdetail.getInt("selling_cost");
                                        purchese_cost = jsonObjectdetail.getInt("purchese_cost");

                                    } else if (type.equalsIgnoreCase("PROVIDE")) {
                                        fimage_path = Config.URL_ROOT + "/uploads/product/400/400/" + image;

                                        JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                        detail_name = jsonObjectdetail.getString("name");
                                        selling_cost = jsonObjectdetail.getInt("selling_cost");
                                        purchese_cost = jsonObjectdetail.getInt("purchese_cost");
                                    } else if (type.equalsIgnoreCase("DEMAND")) {
                                        fimage_path = Config.URL_ROOT + "/uploads/product/400/400/" + image;

                                        JSONObject jsonObjectdetail = jsonObject.getJSONObject("detail");
                                        detail_name = jsonObjectdetail.getString("name");
                                        selling_cost = jsonObjectdetail.getInt("selling_cost");
                                        purchese_cost = jsonObjectdetail.getInt("purchese_cost");
                                    }
                                    Log.d("image_setdata", "" + fimage_path);
                                    int all_comment = jsonObject.getInt("all_comment");
                                    int average_rating = jsonObject.getInt("average_rating");
                                    if (type.equalsIgnoreCase("PRODUCT") || type.equalsIgnoreCase("PROVIDE") || type.equalsIgnoreCase("DEMAND")) {
                                        mValues.add(new FeedModel(id, shareid, fullname, uid, avatar_path, udate, timespam, is_block, imageArray, fimage_path, comment, likes, mylikes, all_rating, type, all_comment, average_rating, detail_name, selling_cost, purchese_cost, is_favrait));
                                    } else {
                                        mValues.add(new FeedModel(id, shareid, fullname, uid, avatar_path, udate, timespam, is_block, imageArray, fimage_path, comment, likes, mylikes, all_rating, type, all_comment, average_rating));
                                    }
                                }
                                adapter = new AllFeedAdapter(context, mValues, ProfileActivity.this);
                                vFeed.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                ViewCompat.setNestedScrollingEnabled(vFeed, false);
                                //loading.dismiss();
                                vFeed.setAdapter(adapter);
                            }else{
                                //loading.dismiss();
                            }
                        }
                        catch (Exception e){
                            //loading.dismiss();
                            e.printStackTrace();
                            Log.d("errorr",e.getMessage());
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //loading.dismiss();
                        // Do something when error occurred
                        error.printStackTrace();
                        Log.d("errorr",error.getMessage());
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onItemClick(FeedModel item) {

    }
    @Override
    public void onRefresh() {
        FEED_START=0;
        if(!mValues.isEmpty())
        mValues.clear();
        getFeed(FEED_START);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                // Stop animation (This will be after 3 seconds)
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 4000); // Delay in millis
    }

    public void CreateProgressDialog()
    {
        progressdialog = new ProgressDialog(ProfileActivity.this.getContext());
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

                    handlers.post(new Runnable() {
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
