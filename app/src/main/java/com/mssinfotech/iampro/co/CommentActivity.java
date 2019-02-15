package com.mssinfotech.iampro.co;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import technolifestyle.com.imageslider.FlipperLayout;

public class CommentActivity extends AppCompatActivity {
    EditText et_comment;
    String user_id;
    ImageView imageView;
    VideoView videoView;
    FlipperLayout flipperLayout;
    TextView fullname,udate,tv_comments,tv_totallike;
    CircleImageView imageView_user,imageView_icon;
    String data_type,data_id;
    LikeButton likeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        et_comment = findViewById(R.id.et_comment);
        if (PrefManager.isLogin(this)) {
            user_id = PrefManager.getLoginDetail(this, "id");
        }
        data_type=getIntent().getExtras().getString("type");
        data_id=getIntent().getExtras().getString("id");
        flipperLayout = (FlipperLayout) findViewById(R.id.flipper_layout);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        imageView_user = findViewById(R.id.imageView_user);
        imageView_icon = findViewById(R.id.imageView_icon);
        fullname = findViewById(R.id.fullname);
        tv_comments = findViewById(R.id.tv_comments);
        tv_totallike = findViewById(R.id.tv_totallike);
        udate = findViewById(R.id.udate);
        likeButton = findViewById(R.id.likeButton);
        getDetail();
        allComment();
    }
    public void getDetail(){
        String myurl = Config.API_URL + "app_service.php?type=get_image_detail&id="+data_id+"&update_type="+data_type+"&uid="+user_id+"&login_id="+user_id+"&my_id="+user_id;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            JSONObject userDetail = result.getJSONObject("user_detail");
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+userDetail.getString("avatar")).apply(Config.options_image).into(imageView_user);
                            fullname.setText(userDetail.getString("fullname"));
                            udate.setText(result.getString("udate"));
                            tv_comments.setText(result.getString("comments"));
                            tv_totallike.setText(result.getString("totallike"));
                            likeButton.setUnlikeDrawableRes(R.drawable.like);
                            likeButton.setLikeDrawableRes(R.drawable.like_un);
                            int my_uid=Integer.parseInt(user_id);
                            if(my_uid==0){
                                likeButton.setEnabled(false);
                            }
                            int Isliked = result.getInt("like_unlike");
                            if(Isliked==1){
                                likeButton.setLiked(true);
                                tv_totallike.setTextColor(Color.RED);
                            }else{
                                likeButton.setLiked(false);
                                tv_totallike.setTextColor(Color.BLACK);
                            }
                            likeButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {
                                    int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())+1;
                                    tv_totallike.setTextColor(Color.RED);
                                    tv_totallike.setText(String.valueOf(newlike));
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(data_id)+"&uid="+user_id+"&ptype="+data_type;
                                    Log.e(Config.TAG,url);
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }

                                @Override
                                public void unLiked(LikeButton likeButton) {
                                    int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())-1;
                                    tv_totallike.setTextColor(Color.BLACK);
                                    tv_totallike.setText(String.valueOf(newlike));
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(data_id)+"&uid="+user_id+"&ptype="+data_type;
                                    Log.e(Config.TAG,url);
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }
                            });
                            if(data_type.equalsIgnoreCase("image")){
                                imageView.setVisibility(View.VISIBLE);
                                String ImageHol=Config.URL_ROOT+"uploads/album/"+result.getString("image");
                                Log.d(Config.TAG, ImageHol);
                                Glide.with(getApplicationContext()).load(ImageHol).apply(Config.options_image).into(imageView);
                                Glide.with(getApplicationContext()).load(R.drawable.image_icon).into(imageView_icon);
                            }
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
    public void allComment(){

    }
    public void sendComment(View view)
    {
        final String comment=et_comment.getText().toString();
        final String url="https://www.iampro.co/api/app_service.php?type=product_review&data_id="+data_id+"&comment="+comment+"&id="+user_id+"&data_type="+data_type;
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
               url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String msg=response.optString("msg");
                        Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                });

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
