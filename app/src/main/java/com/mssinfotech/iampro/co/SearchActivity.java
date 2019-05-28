package com.mssinfotech.iampro.co;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;

//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.tab.HomeFragment;
import com.mssinfotech.iampro.co.utils.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Fragment implements AdapterView.OnItemSelectedListener,UserDataAdapter.ItemListener {
    private Spinner spnrSearchType;
    private Spinner spnrCategory;
    List<String> list = new ArrayList<String>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout llRvContent;
    private Button btnSearch;
    private Group groupForm;
    private EditText etSearchData;
    private TextView tvTitle;
    private ImageButton ibtnBack;
    private ImageButton ibtnFilter;
    private RecyclerView rvContent;
    Context context;
    String myType;

       //home
       int uid;
    SectionDataModel dm = new SectionDataModel();
    ArrayList<SingleItemModel> singleItem = new ArrayList<>();
    ArrayList<SectionDataModel> allSampleData=new ArrayList<>();
    //ArrayList<DataModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view,recycler_view_video,recycler_view_user,recycler_view_product,recycler_view_provide,recycler_view_demand,recycler_view_list;
    RecyclerViewAdapter adapter;
    RecyclerViewDataAdapter adapterr;
    UserDataAdapter user_adapter;
    ArrayList<UserModel> userSampleData=new ArrayList<>();
    View views;
    Boolean HindeBannerView = false;
    //sliderr
    private static ViewPager mPager,mPager2;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<com.mssinfotech.iampro.co.data.ImageModel> imageModelArrayList,imageModelArrayList2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         //setContentView(R.layout.activity_search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_search, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //llRvContent = view.findViewById(R.id.llRvContent);
        views=view;

        if(!singleItem.isEmpty()){
            singleItem.clear();
        }
        if(!allSampleData.isEmpty()){
            allSampleData.clear();
        }
        if(PrefManager.isLogin(getContext()))
            uid= Integer.parseInt(PrefManager.getLoginDetail(getContext(),"id"));

        my_recycler_view =view.findViewById(R.id.my_recycler_view);
        //recycler_view_list=view.findViewById(R.id.recycler_view_list);
        imageModelArrayList=new ArrayList<>();
        imageModelArrayList2=new ArrayList<>();

        btnSearch = view.findViewById(R.id.btnSearch);
        etSearchData = view.findViewById(R.id.etSearchData);
        tvTitle = view.findViewById(R.id.tvTitle);
        //ibtnBack = view.findViewById(R.id.ibtnBack);
        //ibtnFilter = view.findViewById(R.id.ibtnFilter);
        //rvContent = view.findViewById(R.id.rvContent);
        context = SearchActivity.this.getContext();
        list.add("Select Type");
        list.add("Image");
        list.add("Video");
        list.add("Friend");
        list.add("Product");
        list.add("Provide");
        list.add("Demand");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        spnrSearchType = view.findViewById(R.id.spnrSearchType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSearchType.setAdapter(dataAdapter);
        spnrCategory = view.findViewById(R.id.spnrCategory);
        spnrSearchType.setOnItemSelectedListener(this);


        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fm = getChildFragmentManager(); //getFragmentManager();

        //Fragment fragment = fm.findFragmentById(R.id.homesection);
        HomeFragment fragment= (HomeFragment) fm.findFragmentById(R.id.homesection);
        fragment.hideSliders();
        //HomeFragment homeFragment=new HomeFragment();
          //FragmentTransaction tc=fm.beginTransaction();
          //tc.replace(R.id.homesection,homeFragment);
          //tc.commit();
         //tc.attach(fragment);
 /*


        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.add(R.id.homesection, fragment, "HomeFragment");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
*/

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String SearchType = spnrSearchType.getSelectedItem().toString();
                String SearchCat = spnrCategory.getSelectedItem().toString();
                String SearchData = etSearchData.getText().toString();
                //Toast.makeText(context, "Clicked on: " + SearchType + " " + SearchCat + " " +SearchData, Toast.LENGTH_SHORT).show();
                SearchResultActivity fragment = new SearchResultActivity();
                Bundle arg=new Bundle();
                arg.putString("SearchType",SearchType);
                arg.putString("SearchCat",SearchCat);
                arg.putString("SearchData",SearchData);
                function.loadFragment(context,fragment,arg);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //getImage();
            }
        }, 500);
    }
   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
          this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    } */

    public void  getImage(){
        final String url = Config.API_URL+ "app_service.php?type=all_item&name=image&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());



        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // pDialog.dismiss();
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Images");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String type = student.optString("type");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("image");
                                //String image= Config.URL_ROOT+"uploads/album/450/500/"+imagev;
                                String image= Config.URL_ROOT+"uploads/album/300/250/"+imagev;
                                String udate=student.optString("udate");
                                //Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int isliked=student.getInt("like_unlike");
                                int comments=student.getInt("comments");

                                String rating=student.optString("rating");

                                JSONObject userDetail=student.optJSONObject("user_detail");

                                int uid=userDetail.getInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.optString("avatar");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();

                                //singleItem.add(new SingleItemModel(name,image,udate));
                                //singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"image"));
                                singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"image"));
                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            //my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            // my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            // my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                            // AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);

                            //my_recycler_view.setLayoutManager(layoutManager);


                            my_recycler_view.setAdapter(adapterr);
                            int numberOfColumns = 2;
                            GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                            my_recycler_view.setLayoutManager(manager);

                            /*GridLayoutManager recycler_view_list = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
                            my_recycler_view.setLayoutManager(manager); */

                            //adapter.notifyDataSetChanged();
                            //recycler_view_list


                            getVideo();
                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //pDialog.dismiss();
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getVideo();
    }
    public void getVideo(){
        final String url = Config.API_URL+ "app_service.php?type=all_item&name=video&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //pDialog.dismiss();
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Video");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("v_image");
                                String image=Config.URL_ROOT + "uploads/v_image/300/250/" + imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);

                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                String rating=student.optString("rating");
                                JSONObject userDetail=student.optJSONObject("user_detail");

                                int uid=userDetail.getInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.getString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");
                                int isliked=student.getInt("like_unlike");

                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,"video"));
                                singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"video"));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);

                            //getUser();
                            getProduct();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getUser();
    }
    public void getUser(){
        final String url = Config.API_URL+ "app_service.php?type=getSelectedUser&limit=15&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("User");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                       /* if(!singleItem.isEmpty()){
                            singleItem.clear();
                        } */
                        if(!userSampleData.isEmpty()){
                            userSampleData.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("fname");
                                String identity_type=student.optString("identity_type");
                                String category=student.optString("category");
                                String imagev=student.optString("avatar");
                                String image= Config.AVATAR_URL+"300/250/"+imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                //singleItem.add(new SingleItemModel(name,image,udate));
                                userSampleData.add(new UserModel(uid,name,image,udate,category));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("usersampledatav", userSampleData.toString());
                            // my_recycler_view.setHasFixedSize(true);
                            Log.d("userSampleDatas",""+userSampleData.size()+"--"+userSampleData.toString());


                            user_adapter = new UserDataAdapter(getContext(),userSampleData,SearchActivity.this);
                            my_recycler_view.setAdapter(user_adapter);

                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                            getProduct();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProduct();
    }

    public void getProduct(){
        final String url = Config.API_URL+ "app_service.php?type=all_product&uid="+uid+"&name=product&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Product");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.getString("category");
                                String imagev=student.optString("image");
                                String image=Config.URL_ROOT + "uploads/product/300/250/" +imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();

                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                JSONObject userDetail=student.optJSONObject("user_detail");
                                int uid=userDetail.getInt("id");
                                int id=student.getInt("id");
                                int isliked=student.getInt("like_unlike");
                                String rating=student.optString("rating");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.getString("avatar");

                                singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"product"));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);

                            getProvide();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getProvide(){
        final String url = Config.API_URL+ "app_service.php?type=all_product_classified&uid="+uid+"&name=PROVIDE&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Provide");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("image");
                                String image=Config.URL_ROOT + "uploads/product/300/250/" + imagev;
                                String udate=student.optString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();
                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                String rating=student.optString("rating");
                                int comments=student.getInt("comments");
                                JSONObject userDetail=student.optJSONObject("user_detail");
                                int uid=userDetail.optInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.optString("avatar");
                                int isliked=student.getInt("like_unlike");
                                singleItem.add(new SingleItemModel(id,name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"provide"));


                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);

                            getDemand();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getDemand();
    }
    public void getDemand(){
        final String url = Config.API_URL+ "app_service.php?type=all_product_classified&uid="+uid+"&name=DEMAND&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("responsef",response.toString());
                        SectionDataModel dm = new SectionDataModel();
                        dm.setHeaderTitle("Demand");
                        ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.optString("name");
                                String categoryv=student.optString("category");
                                String imagev=student.optString("image");
                                String image=Config.URL_ROOT + "uploads/product/300/250/" +imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+categoryv+""+image+""+udate);
                                //SectionDataModel dm = new SectionDataModel();
                                //dm.setHeaderTitle("Section " + i);
                                //Toast.makeText(getContext(),"rrrresponse_enterrr:",Toast.LENGTH_LONG).show();

                                String daysago=student.optString("ago");
                                int totallike=student.getInt("totallike");
                                int comments=student.getInt("comments");
                                String rating=student.optString("rating");
                                JSONObject userDetail=student.optJSONObject("user_detail");
                                int uid=userDetail.optInt("id");
                                int id=student.getInt("id");
                                String fullname=userDetail.optString("fullname");
                                String avatar=Config.AVATAR_URL+"250/250/"+userDetail.optString("avatar");
                                int isliked=student.getInt("like_unlike");
                                singleItem.add(new SingleItemModel(id, name,image,udate,daysago,totallike,comments,uid,fullname,avatar,isliked,rating,"demand"));


                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());
                            adapterr = new RecyclerViewDataAdapter(getContext(), allSampleData);

                            my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //my_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            my_recycler_view.setAdapter(adapterr);
                        }
                        catch (JSONException e){
                            //pDialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //pDialog.dismiss();
                        // Do something when error occurred
                        //Snackbar.make(getContext(),"Error...", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "verror"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        //jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        function.getData(SearchActivity.this.getActivity(), context, spnrCategory, list.get(position));
        //Toast.makeText(getApplicationContext(), "Clicked on: " + list.get(position), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onItemClick(UserModel item) {
        //Toast.makeText(getContext(), item.getName()+ " is clicked", Toast.LENGTH_SHORT).show();
    }

}
