package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.tab.UserFragment;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

public class SearchedActivity extends AppCompatActivity implements UserDataAdapter.ItemListener {
 RecyclerView my_recycler_view;
 ImageView searched_iv;
  String SearchType,SearchCat,SearchData;
    MyImageVideoDataAdapter adapterr;
    UserDataAdapter adapter;
    TreeMap<String,String> item_name = new TreeMap<>();
     String uid;
    ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();
     ImageView  no_rodr;

    ArrayList<UserModel> allSampleData=new ArrayList<>();
   //ImageView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched);
        my_recycler_view=findViewById(R.id.my_recycler_view);
         searched_iv=findViewById(R.id.searched_iv);
        no_rodr=findViewById(R.id.no_record_found);
        /*
          intent.putExtra("SearchType",SearchType);
                intent.putExtra("SearchCat",SearchCat);
                intent.putExtra("SearchData",SearchData); */
        SearchType=getIntent().getExtras().getString("SearchType");
        SearchCat=getIntent().getExtras().getString("SearchCat");
        //SearchData=getIntent().getExtras().getString("SearchData");
        uid= PrefManager.getLoginDetail(getApplicationContext(),"id");
        if (SearchType.equalsIgnoreCase("FRIEND")){
             getUser();
        }
        else {
            getSearchedItem();
        }
    }
    public void getSearchedItem(){
        final ProgressDialog pDialog = new ProgressDialog(SearchedActivity.this); //Your Activity.this
        pDialog.setMessage("Loading...!");
        pDialog.show();
        String url=null;
        try {
            String query = URLEncoder.encode(SearchCat,"utf-8");

            //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
            // String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
            url = "https://www.iampro.co/api/app_service.php?type=search_all_items&search_type="+SearchType+"&category=" + query + "&search_data=&uid=" + uid + "&my_id=" + uid;
            // Initialize a new RequestQueue instance
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url = "https://www.iampro.co/api/app_service.php?type=search_all_items&search_type=IMAGE&category=" +SearchCat+ "&search_data=&uid=" + uid + "&my_id=" + uid;

        }
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                       pDialog.dismiss();
                        if (SearchType.equalsIgnoreCase("Image")) {
                            searched_iv.setVisibility(View.VISIBLE);
                            searched_iv.setBackground(AppCompatResources.getDrawable(SearchedActivity.this, R.drawable.latestphotos));
                        }
                        else if (SearchType.equalsIgnoreCase("Video")) {
                            searched_iv.setVisibility(View.VISIBLE);
                            searched_iv.setBackground(AppCompatResources.getDrawable(SearchedActivity.this, R.drawable.latestvideo));
                        }
                        else if (SearchType.equalsIgnoreCase("Friend")) {
                            searched_iv.setVisibility(View.VISIBLE);
                            searched_iv.setBackground(AppCompatResources.getDrawable(SearchedActivity.this, R.drawable.latestphotos));
                        }
                        else if (SearchType.equalsIgnoreCase("Product")) {
                            searched_iv.setVisibility(View.VISIBLE);
                            searched_iv.setBackground(AppCompatResources.getDrawable(SearchedActivity.this, R.drawable.latestproduct));
                        }
                        else if (SearchType.equalsIgnoreCase("Provide")) {
                            searched_iv.setVisibility(View.VISIBLE);
                            searched_iv.setBackground(AppCompatResources.getDrawable(SearchedActivity.this, R.drawable.latestprovide));
                        }
                        else if (SearchType.equalsIgnoreCase("Demand")) {
                            searched_iv.setVisibility(View.VISIBLE);
                            searched_iv.setBackground(AppCompatResources.getDrawable(SearchedActivity.this, R.drawable.latestdemand));
                        }
                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(SearchCat);
                        dm.setAlbemId(item_name.get(SearchCat));
                        dm.setMore("loadmore");
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
                        //ArrayList<DataModel> item = new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object

                                JSONObject student = response.getJSONObject(i);
                                String category1=student.getString("category_type");
                                String idd=student.optString("id");
                                //String added_byy=student.optString("added_by");
                                String name1=student.optString("name");
                                //String atype=student.optString("atype");
                                //tv_category.setText(name1);
                                //tv_category.setVisibility(View.GONE);
                                JSONArray jsonArrayPics=student.getJSONArray("img_detail");
                                Log.d("picssss",jsonArrayPics.toString());

                                for (int j=0;j<jsonArrayPics.length();j++){
                                    JSONObject pics=jsonArrayPics.getJSONObject(j);
                                    int id=pics.optInt("id");

                                    int albemid=pics.optInt("albemid");
                                    String name=pics.optString("name");
                                    String category=pics.optString("category");
                                    int albem_type=pics.optInt("albem_type");

                                    String image=pics.optString("image");
                                    String udate=pics.optString("udate");
                                    String about_us=pics.optString("about_us");

                                    int group_id=pics.optInt("group_id");

                                    int is_featured=pics.optInt("is_featured");
                                    //status
                                    int status=pics.optInt("status");
                                    int totallike=pics.optInt("totallike");
                                    int comments=pics.optInt("comments");
                                    int like_unlike=pics.optInt("like_unlike");
                                    String rating=pics.optString("rating");
                                    String is_block=pics.optString("is_block");

                                    JSONObject userDetail=pics.getJSONObject("user_detail");
                                    int user_id=userDetail.optInt("id");

                                    String username=userDetail.optString("username");
                                    String avatar=userDetail.optString("avatar");
                                    String email=userDetail.optString("email");
                                    String mobile=userDetail.optString("mobile");
                                    String about_me=userDetail.optString("about_me");
                                    String country=userDetail.optString("country");
                                    String state=userDetail.optString("state");
                                    String city=userDetail.optString("city");
                                    String gender=userDetail.optString("gender");
                                    String dob=userDetail.optString("dob");
                                    String categoryy=userDetail.optString("category");
                                    String is_featuredd=userDetail.optString("is_featured");
                                    String fullname=userDetail.optString("fullname");

                                    String more="loadmore";
                                    item.add(new MyImageModel(String.valueOf(id),String.valueOf(albemid),name,category,String.valueOf(albem_type),image,udate,about_us,String.valueOf(group_id),String.valueOf(is_featured),String.valueOf(status),is_block,String.valueOf(comments),String.valueOf(totallike),String.valueOf(like_unlike),rating,String.valueOf(user_id),more,avatar,fullname));
                                    //item.add(new DataModel(name,image,udate,category,totallike,like_unlike,comments,udate,Float.parseFloat(rating),uid,fullname,avatar,id,IMAGE_TYPE));

                                }
                            }
                            Log.d("allsampledatav",item.toString());

                            dm.setAllItemsInSection(item);
                            Log.d("adm",item.toString());
                            Log.d("dmm",dm.toString());
                            allSampleDatamore.add(dm);
                            Log.d("allsampledatav", allSampleDatamore.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleDatamore.size()+"--"+allSampleDatamore.toString());
                            TreeMap<String,String> item_loadmore=new TreeMap<>();
                            item_loadmore.put("loadmore","loadmore");
                            adapterr = new MyImageVideoDataAdapter(getApplicationContext(), allSampleDatamore,item_loadmore);
                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            my_recycler_view.setAdapter(adapterr);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                            ImageView no_rodr = findViewById(R.id.no_record_found);
                            no_rodr.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        Log.d("verror",""+error.getMessage());
                        ImageView no_rodr = findViewById(R.id.no_record_found);
                        no_rodr.setVisibility(View.VISIBLE);
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (Config.doubleBackToExitPressedOnce) {
                super.onBackPressed();
                //this.finish();
                // return;
            }
            Config.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Config.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    public void getUser(){
        /*String url=null;
        try {
            String query = URLEncoder.encode(SearchCat,"utf-8");

            //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
            // String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
            url = "https://www.iampro.co/api/app_service.php?type=search_all_items&search_type="+SearchType+"&category=" + query + "&search_data=&uid=" + uid + "&my_id=" + uid;
            // Initialize a new RequestQueue instance
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url = "https://www.iampro.co/api/app_service.php?type=search_all_items&search_type=IMAGE&category=" +SearchCat+ "&search_data=&uid=" + uid + "&my_id=" + uid;

        } */
        final String url = "https://www.iampro.co/api/app_service.php?type=getSelectedUser&limit=15&uid="+uid+"&my_id="+uid;

        // Initialize a new RequestQueue instance
            RequestQueue requestQueue=Volley.newRequestQueue(SearchedActivity.this);

            // Initialize a new JsonArrayRequest instance
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new com.android.volley.Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("responsef", response.toString());
                            if (response.length() > 0)  {
                                //SectionImageModel dm = new SectionImageModel();
                                //dm.setHeaderTitle(SearchCat);
                               // dm.setAlbemId(item_name.get(SearchCat));
                                //dm.setMore("loadmore");
                                no_rodr.setVisibility(View.GONE);
                                //SectionDataModel dm = new SectionDataModel();
                               // dm.setHeaderTitle("User");
                                SectionDataModel dm = new SectionDataModel();
                                dm.setHeaderTitle("User");
                                ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                                if (!singleItem.isEmpty()) {
                                    singleItem.clear();
                                }
                                if (!allSampleData.isEmpty()) {
                                    allSampleData.clear();
                                }
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        // Get current json object
                                        JSONObject student = response.getJSONObject(i);

                                        String name = student.optString("fname");
                                        int uid = student.getInt("id");
                                        String identity_type = student.getString("identity_type");
                                        String category = student.getString("category");
                                        String imagev = student.getString("avatar");
                                        String image = Config.AVATAR_URL + "200/200/" + imagev;
                                        String udate = student.getString("udate");
                                        Log.d("pdata", "" + name + "" + category + "" + image + "" + udate);

                                        String total_images = student.optString("total_image");
                                        String total_videos = student.optString("total_video");
                                        String total_users = student.optString("total_friends");
                                        String total_products = student.optString("total_product");
                                        String total_provides = student.optString("total_provide");
                                        String total_demands = student.optString("total_demend");

                                        //is_friend,friend_status,tid,is_block,user_url
                                        String is_friend = student.optString("is_friend");
                                        String friend_status = student.optString("friend_status");
                                        String tid = student.optString("tid");
                                        int is_block = student.optInt("is_block");
                                        String user_url = student.optString("user_url");

                                        //allSampleData.add(new UserModel(uid,name,image,udate,category));
                                        //String total_image,String total_video,String total_friend     //is_friend,friend_status,tid,is_block,user_url
                                        allSampleData.add(new UserModel(uid, name, image, udate, category, total_images, total_videos, total_users, total_products, total_provides, total_demands, is_friend, friend_status, tid, is_block, user_url));

                                    }
                                    Log.d("bdm", singleItem.toString());
                                    dm.setAllItemsInSection(singleItem);
                                    Log.d("adm", singleItem.toString());
                                    Log.d("dmm", dm.toString());
                                    //allSampleData.add(dm);
                                    Log.d("allsampledatav", allSampleData.toString());
                                    // my_recycler_view.setHasFixedSize(true);
                                    Log.d("allSampleDatas", "" + allSampleData.size() + "--" + allSampleData.toString());


                                    adapter = new UserDataAdapter(SearchedActivity.this, allSampleData, SearchedActivity.this);
                                    my_recycler_view.setAdapter(adapter);

                                    LinearLayoutManager manager = new LinearLayoutManager(SearchedActivity.this, LinearLayoutManager.VERTICAL, false);
                                    my_recycler_view.setLayoutManager(manager);

                                    //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
                                    //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_load_more);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(SearchedActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("catch_f", "" + e.getMessage());
                                    no_rodr.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                no_rodr.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            // Do something when error occurred
                            //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(SearchedActivity.this, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("verror",error.getMessage());
                            no_rodr.setVisibility(View.VISIBLE);
                        }
                    }
            );
            // Add JsonArrayRequest to the RequestQueue
            requestQueue.add(jsonArrayRequest);
            //getProduct();

    }

    @Override
    public void onItemClick(UserModel item) {

    }
}
