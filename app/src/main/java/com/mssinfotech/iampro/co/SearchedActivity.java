package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

public class SearchedActivity extends AppCompatActivity {
 RecyclerView my_recycler_view;
 ImageView searched_iv;
  String SearchType,SearchCat,SearchData;
    MyImageVideoDataAdapter adapterr;
    TreeMap<String,String> item_name = new TreeMap<>();
     String uid;
    ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched);
        my_recycler_view=findViewById(R.id.my_recycler_view);
         searched_iv=findViewById(R.id.searched_iv);
        /*
          intent.putExtra("SearchType",SearchType);
                intent.putExtra("SearchCat",SearchCat);
                intent.putExtra("SearchData",SearchData); */
        SearchType=getIntent().getExtras().getString("SearchType");
        SearchCat=getIntent().getExtras().getString("SearchCat");
        //SearchData=getIntent().getExtras().getString("SearchData");
        uid= PrefManager.getLoginDetail(getApplicationContext(),"id");
        getSearchedItem();
    }
    public void getSearchedItem(){
       // final ProgressDialog pDialog = new ProgressDialog(getApplicationContext()); //Your Activity.this
       // pDialog.setMessage("Loading...!");
       // pDialog.show();
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
                       // pDialog.dismiss();
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
                            //pDialog.dismiss();
                            Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        //pDialog.dismiss();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
}
