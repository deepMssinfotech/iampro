package com.mssinfotech.iampro.co.image;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.mssinfotech.iampro.co.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.common.Config;


import org.json.JSONObject;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;

public class ImageDetail extends AppCompatActivity {
    public  String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        uid=getIntent().getExtras().getString("uid");
    }
    protected void getImageDetail(){
        String url="https://www.iampro.co/api/app_service.php?type=get_image_detail&id=15&update_type=image&uid=1&login_id="+uid+"&my_id="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(ImageDetail.this);

        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Prod_detail",response.toString());
                        // Process the JSON
                        try{
                            JSONObject responses=new JSONObject(response);

                            // Get the current student (json object) data
                            String id =responses.optString("id");
                            String albemid =responses.optString("albemid");
                            String name =responses.optString("name");
                            String category=responses.optString("category");
                            String albem_type=responses.optString("albem_type");
                            String image=responses.optString("image");
                            String avatar_path=AVATAR_URL+image;
                            String udate=responses.optString("udate");
                            String about_us=responses.optString("about_us");
                            String group_id=responses.optString("group_id");
                            String is_featured=responses.optString("is_featured");
                            String status=responses.optString("status");
                            //String is_featured=responses.optString("is_featured");
                           // String is_featured=responses.optString("is_featured");
                            String other_image=responses.optString("other_image");

                            String other_image_path=Config.OTHER_IMAGE_URL+other_image;

                            // Display the formatted json data in text view
                           /*  Glide.with(ImageDetail.this)
                                    .load(other_image_path)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(expandedImage);
                            Glide.with(ImageDetail.this)
                                    .load(avatar_path)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(user_image); */

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
