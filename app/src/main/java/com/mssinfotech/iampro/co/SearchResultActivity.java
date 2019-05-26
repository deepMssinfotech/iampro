package com.mssinfotech.iampro.co;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.MyDemandAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.MyProductAdapter;
import com.mssinfotech.iampro.co.adapter.MyProvideAdapter;
import com.mssinfotech.iampro.co.adapter.SearchAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.data.SearchItem;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.MyProductModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SearchResultActivity extends AppCompatActivity implements UserDataAdapter.ItemListener,MyProductAdapter.ItemListener{
    private RecyclerView recyclerView;
    private SearchView searchView;
    String SearchType,SearchCat,SearchData,uid;
    Context context;
    RecyclerView my_recycler_view;
    MyImageVideoDataAdapter adapterr;
    UserDataAdapter adapter;
    TreeMap<String,String> item_name = new TreeMap<>();
    ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();
    ArrayList<SectionDataModel> allSampleDataP=new ArrayList<>();
    ImageView  no_rodr;
    ArrayList<UserModel> allSampleData=new ArrayList<>();
    MyProductAdapter productAdapter;
    MyProvideAdapter provideAdapter;
    MyDemandAdapter demandAdapter;
    private SearchAdapter searchAdapter;
    private List<SearchItem> SearchItemList;
    int addedBy;
    ArrayList<MyProductModel> item = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        context = getApplicationContext();
        SearchType=getIntent().getExtras().getString("SearchType");
        SearchCat=getIntent().getExtras().getString("SearchCat");
        SearchData=getIntent().getExtras().getString("SearchData");
        if(PrefManager.isLogin(context))
            uid= PrefManager.getLoginDetail(context,"id");

        my_recycler_view=findViewById(R.id.my_recycler_view);
        no_rodr=findViewById(R.id.no_record_found);
        if (SearchType.equalsIgnoreCase("FRIEND")){
            getUser();
        }
        else if (SearchType.equalsIgnoreCase("PRODUCT") || SearchType.equalsIgnoreCase("PROVIDE") || SearchType.equalsIgnoreCase("DEMAND")){
            getProductData();
        }
        else if (SearchType.equalsIgnoreCase("IMAGE") || SearchType.equalsIgnoreCase("VIDEO")){
            getSearchedItem();
        }else {
            getSerchAllData();
            SearchItemList = new ArrayList<SearchItem>();
            searchAdapter = new SearchAdapter(context, SearchItemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            my_recycler_view.setLayoutManager(mLayoutManager);
            my_recycler_view.setItemAnimator(new DefaultItemAnimator());
            my_recycler_view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            my_recycler_view.setAdapter(searchAdapter);
            //Toast.makeText(context, SearchType + " - " + SearchCat + " - " + SearchData, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void getProductData(){
        final Dialog pDialog = new Dialog(this);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        String url=null;
        try {
            String cat = URLEncoder.encode(SearchCat,"utf-8");
            String data = URLEncoder.encode(SearchData,"utf-8");
            String type = URLEncoder.encode(SearchType,"utf-8");
            url = Config.API_URL+ "app_service.php?type=search_all_items&search_type="+type+"&category=" + cat + "&search_data="+data+"&uid=" + uid + "&my_id=" + uid;
            // Initialize a new RequestQueue instance
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url =  Config.API_URL+ "app_service.php?type=search_all_items&search_type="+SearchType+"&category=" +SearchCat+ "&search_data="+SearchData+"&uid=" + uid + "&my_id=" + uid;

        }
        Log.d("product_search",""+url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        if (!item.isEmpty()){
                            item.clear();
                        }
                        if (pDialog.isShowing())
                            pDialog.dismiss();


                        SectionDataModel dm = new SectionDataModel();

                        dm.setHeaderTitle(SearchCat);
                        dm.setAddedBy(String.valueOf(addedBy));
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                String id=student.getString("id");
                                String name=student.getString("name");
                                String category_type=student.getString("category_type");
                                String detail=student.getString("detail");
                                String image=student.getString("image");
                                String language=student.getString("language");
                                String status=student.getString("status");

                                String cat_name=student.getString("cat_name");


                                //String category=student.getString("category");

                                JSONArray jsonArrayPro=student.getJSONArray("pro_detail");
                                for(int j=0;j<jsonArrayPro.length();j++) {
                                    JSONObject studentPro =jsonArrayPro.getJSONObject(i);
                                    String idv = studentPro.getString("id");
                                    String added_by = studentPro.getString("added_by");

                                    String totallike=studentPro.getString("totallike");
                                    String comments=studentPro.getString("comments");

                                    String namev = studentPro.getString("name");
                                    String category = studentPro.getString("category");
                                    //name,image,udate,categoryv,totallike,comments,scost,pcost,ratingv,uid,fullname,avatar,idv,like_unlike
                                    String scost = studentPro.getString("selling_cost");
                                    String pcost = studentPro.getString("purchese_cost");
                                    //udate
                                    String pudate = studentPro.getString("udate");
                                    //avg_rating,fullname,avatar,like_unlike
                                    String avg_rating = studentPro.getString("avg_rating");
                                    String like_unlike = studentPro.getString("like_unlike");
                                    JSONObject jsonObject=studentPro.getJSONObject("user_detail");
                                    String fullname=jsonObject.getString("fullname");
                                    String avatar=jsonObject.getString("avatar");
                                    item.add(new MyProductModel(name,image,pudate,category,Integer.parseInt(totallike),Integer.parseInt(comments),Integer.parseInt(scost),Integer.parseInt(pcost),Float.parseFloat(avg_rating),Integer.parseInt(added_by),fullname,avatar,idv,Integer.parseInt(like_unlike)));

                                }

                                //int id=student.optInt("id");
                               /* String idv=String.valueOf(id);
                                int added_by=student.optInt("added_by");
                                    addedBy=added_by;

                                int scost=student.optInt("selling_cost");
                                int pcost=student.optInt("purchese_cost");

                                //String name = student.optString("name");
                                String categoryv=student.optString("category");
                                //tv_category.setText(categoryv);

                                String imagev=student.optString("image");
                                String image= Config.URL_ROOT + "uploads/product/" +imagev;
                                String udate=student.optString("udate");
                                int totallike=Integer.parseInt(student.getString("likes"));
                                int comments=student.optInt("comments");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);

                                // String daysago=student.getString("ago");
                                int like_unlike=student.optInt("like_unlike");
                                String rating=String.valueOf(student.optInt("average_rating"));
                                float ratingv=Float.parseFloat(rating);

                                JSONObject userDetail=student.getJSONObject("user_name");
                                int uid=userDetail.getInt("id");
                                String fname=userDetail.getString("fname");
                                String lname=userDetail.getString("lname");
                                String fullname=fname+"\t"+lname;
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar"); */

                                //if (SearchType.equalsIgnoreCase("PRODUCT"))
                                // item.add(new MyProductModel(name,image,udate,categoryv,totallike,comments,scost,pcost,ratingv,uid,fullname,avatar,idv,like_unlike));
                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleDataP.add(dm);
                            Log.d("allsampledatav",item.toString());
                            TreeMap<String,String> item_loadmore=new TreeMap<>();
                            item_loadmore.put("loadmore","loadmore");
                            //if (SearchType.equalsIgnoreCase("PRODUCT"))
                            productAdapter = new MyProductAdapter(context,item,SearchResultActivity.this);

                            my_recycler_view.setAdapter(adapter);
                            GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
    public void getSearchedItem(){
        final Dialog pDialog = new Dialog(this);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pDialog.setContentView(R.layout.progress_dialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
        String url=null;
        try {
            String cat = URLEncoder.encode(SearchCat,"utf-8");
            String data = URLEncoder.encode(SearchData,"utf-8");
            String type = URLEncoder.encode(SearchType,"utf-8");
            url = Config.API_URL+ "app_service.php?type=search_all_items&search_type="+type+"&category=" + cat + "&search_data="+data+"&uid=" + uid + "&my_id=" + uid;
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url = Config.API_URL+ "app_service.php?type=search_all_items&search_type="+SearchType+"&category=" +SearchCat+ "&search_data="+SearchData+"&uid=" + uid + "&my_id=" + uid;

        }
        Log.d("serched_url",""+url+"\t"+SearchType+"\t"+SearchCat);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();

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
                                    if (SearchType.equalsIgnoreCase("Video")) {
                                        image=pics.optString("v_image");
                                    }
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
                            adapterr = new MyImageVideoDataAdapter(context, allSampleDatamore,item_loadmore);
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
                        Toast.makeText(getApplicationContext(),""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void getUser(){
        String url=null;
        try {
            String cat = URLEncoder.encode(SearchCat,"utf-8");
            String data = URLEncoder.encode(SearchData,"utf-8");
            String type = URLEncoder.encode(SearchType,"utf-8");
            url = Config.API_URL+ "app_service.php?type=search_all_friends&search_type="+type+"&category=" + cat + "&search_data="+data+"&uid=" + uid + "&my_id=" + uid;
            // Initialize a new RequestQueue instance
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url = Config.API_URL+ "app_service.php?type=search_all_friends&search_type="+SearchType+"&category=" +SearchCat+ "&search_data="+SearchData+"&uid=" + uid + "&my_id=" + uid;

        }
        //final String url = Config.API_URL+ "app_service.php?type=getSelectedUser&limit=15&uid="+uid+"&my_id="+uid;
        Log.d("user_search",""+url);
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue=Volley.newRequestQueue(context);

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
                                    //String identity_type = student.getString("identity_type");
                                    String category = student.getString("cat_name");
                                    String imagev = student.getString("avatar");
                                    String image = Config.AVATAR_URL + "200/200/" + imagev;
                                    String udate = student.getString("dob");
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
                                    String tid = student.optString("id");
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


                                adapter = new UserDataAdapter(context, allSampleData, SearchResultActivity.this);
                                my_recycler_view.setAdapter(adapter);

                                LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                my_recycler_view.setLayoutManager(manager);

                                //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
                                //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_load_more);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(context, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                        no_rodr.setVisibility(View.VISIBLE);
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProduct();

    }
    public void getSerchAllData(){
        String url=null;
        try {
            String data = URLEncoder.encode(SearchData,"utf-8");
            url = Config.API_URL+ "app_service.php?type=search_all&search_data="+data+"&uid=" + uid + "&my_id=" + uid;
            // Initialize a new RequestQueue instance
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            url = Config.API_URL+ "app_service.php?type=search_all&search_data="+SearchData+"&uid=" + uid + "&my_id=" + uid;
        }
        Log.d("user_search",""+url);
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue=Volley.newRequestQueue(context);

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            no_rodr.setVisibility(View.GONE);
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    SearchItem item = new SearchItem();
                                    // Get current json object
                                    JSONObject student = response.getJSONObject(i);
                                    String type = student.optString("category_type");
                                    int id = student.getInt("id");
                                    item.setType(type);
                                    item.setId(id);
                                    if(type.equalsIgnoreCase("FRIENDS")){
                                        item.setUser_fullname(student.optString("fullname"));
                                        item.setUser_avatar(student.optString("avatar"));
                                        item.setCategory(student.optString("category"));
                                        item.setCity(student.optString("city"));
                                        item.setUser_Id(student.optString("id"));

                                        item.setTotal_product(student.optString("total_product"));
                                        item.setTotal_provide(student.optString("total_provide"));
                                        item.setTotal_demend(student.optString("total_demend"));
                                        item.setTotal_friends(student.optString("total_friends"));
                                        item.setTotal_image(student.optString("total_image"));
                                        item.setTotal_video(student.optString("total_video"));

                                    }else if(type.equalsIgnoreCase("IMAGE")){

                                        JSONArray jsonArrayPics=student.getJSONArray("img_detail");

                                        for (int j=0;j<jsonArrayPics.length();j++) {
                                            JSONObject pics = jsonArrayPics.getJSONObject(j);
                                            item.setName(pics.optString("name"));
                                            item.setCategory(pics.optString("category"));
                                            item.setImage(pics.optString("image"));
                                            JSONObject userDetail=pics.getJSONObject("user_detail");
                                            item.setUser_Id(userDetail.optString("id"));
                                            item.setUser_fullname(userDetail.optString("fullname"));
                                            item.setUser_avatar(userDetail.optString("avatar"));
                                        }

                                    }else if(type.equalsIgnoreCase("VIDEO")){
                                        JSONArray jsonArrayPics=student.getJSONArray("img_detail");

                                        for (int j=0;j<jsonArrayPics.length();j++) {
                                            JSONObject pics = jsonArrayPics.getJSONObject(j);
                                            item.setName(pics.optString("name"));
                                            item.setCategory(pics.optString("category"));
                                            item.setImage(pics.optString("v_image"));
                                            JSONObject userDetail=pics.getJSONObject("user_detail");
                                            item.setUser_Id(userDetail.optString("id"));
                                            item.setUser_fullname(userDetail.optString("fullname"));
                                            item.setUser_avatar(userDetail.optString("avatar"));
                                        }

                                    }else if(type.equalsIgnoreCase("PRODUCT") || type.equalsIgnoreCase("PROVIDE") || type.equalsIgnoreCase("DEMAND")){

                                        JSONArray jsonArrayPics=student.getJSONArray("pro_detail");
                                        for (int j=0;j<jsonArrayPics.length();j++) {
                                            JSONObject pics = jsonArrayPics.getJSONObject(j);
                                            item.setName(pics.optString("name"));
                                            item.setSelling_cost(pics.optString("selling_cost"));
                                            item.setPurchese_cost(pics.optString("purchese_cost"));
                                            item.setCategory(pics.optString("category"));
                                            item.setImage(pics.optString("image"));
                                            item.setAvg_rating(pics.optString("avg_rating"));
                                            item.setComments(pics.optString("comments"));
                                            item.setTotallike(pics.optString("totallike"));
                                            JSONObject userDetail=pics.getJSONObject("user_detail");
                                            item.setUser_Id(userDetail.optString("id"));
                                            item.setUser_fullname(userDetail.optString("fullname"));
                                            item.setUser_avatar(userDetail.optString("avatar"));
                                        }
                                    }

                                    SearchItemList.add(item);
                                    Log.e("asdsa",item.toString());
                                }
                                searchAdapter.notifyDataSetChanged();
                            }catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("catch_f", "" + e.getMessage());
                                no_rodr.setVisibility(View.VISIBLE);
                            }
                        }else{
                            no_rodr.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(context, "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
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
    @Override
    public void onItemClick(MyProductModel item) {

    }
}
