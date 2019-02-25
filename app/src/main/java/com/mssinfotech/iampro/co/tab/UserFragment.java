package com.mssinfotech.iampro.co.tab;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewAdapter;
import com.mssinfotech.iampro.co.adapter.RecyclerViewDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;

import com.mssinfotech.iampro.co.adapter.UserItemTouchHelper;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionDataModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.SingleItemModel;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

public class UserFragment extends Fragment implements UserDataAdapter.ItemListener,UserItemTouchHelper.UserItemTouchHelperListener {
    //ArrayList<SectionDataModel> allSampleData=new ArrayList<>();
    ArrayList<UserModel> allSampleData=new ArrayList<>();
    RecyclerView my_recycler_view,recycler_view_load_more;
    LinearLayout ll;
     UserDataAdapter adapter;
      Button btn_load_more;
     int uid;
      public static int start_limit=0;
        public static int end_limit=20;

    ArrayList<MyImageModel> item = new ArrayList<>();
    //MyProductAdapter adapterr;
    MyImageVideoDataAdapter adapterr;
    TreeMap<String,String> item_name=new TreeMap<>();
    ArrayList<SectionImageModel> allSampleDatamore=new ArrayList<>();

     ImageView luser_iv;

    private View view;
    private boolean add = false;
    private Paint p = new Paint();

    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        //oolbar =view.findViewById(R.id.toolbar);
        //view.findViewById(R.id.title_tv).setTag("User");

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //createDummyData();
        if (PrefManager.isLogin(getContext())) {
            String id = PrefManager.getLoginDetail(getContext(), "id");
            uid = Integer.parseInt(id);
        }
        getUser(15);
        my_recycler_view =view.findViewById(R.id.my_recycler_view);
        recycler_view_load_more=view.findViewById(R.id.recycler_view_load_more);
        ll=view.findViewById(R.id.ll);
          btn_load_more=view.findViewById(R.id.btn_load_more);

        luser_iv=view.findViewById(R.id.luser_iv);
               luser_iv.setBackground(getContext().getResources().getDrawable(R.drawable.user));
        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getProductmore();
                //recycler_view_load_more.setVisibility(View.VISIBLE);
                //getAllAlbum();

                //getUser(int limitss);
                start_limit=start_limit+end_limit;
                 getUserMores(start_limit,end_limit);

            }
        });

        initSwipe();
    }
    /*public void getAllAlbum(){
        //String url="https://www.iampro.co/api/app_service.php?type=getAlbemsListt&search_type=video&uid="+uid;
        String url="https://www.iampro.co/api/app_service.php?type=get_category&name=FRIEND&uid="+uid;
        RequestQueue requestQueue=Volley.newRequestQueue(getContext());

        final ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
        pDialog.setMessage("Loading...!");
        pDialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        if(!item_name.isEmpty()){
                            item_name.clear();
                        }
                        if(!item.isEmpty()){
                            item.clear();
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject student1 = response.getJSONObject(i);
                                int id=student1.optInt("id");
                                String name=student1.optString("name");
                                int product_count=student1.optInt("product_count");
                                //item_name.add(name1);
                                //item_name.put(name1,album_name)
                                if(product_count>0) {
                                    item_name.put(name, String.valueOf(id));
                                    //Toast.makeText(getContext(),""+product_count,Toast.LENGTH_LONG).show();
                                }
                                else{

                                }
                            }
                            Log.d("allsampledataname",item_name.toString());
                            for (String data:item_name.keySet()){
                                //getVideo(data);
                                    getUserMores(data);
                                Log.d("Keyset",""+data);
                            }
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("verror",""+error.getMessage());
                        pDialog.dismiss();
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    }
    public void getUserMores(final String cname){
        final ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
        pDialog.setMessage("Loading...!");
        pDialog.show();
        //String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid;
        // String url=Config.API_URL+"app_service.php?type=getMyAlbemsListt&search_type=image&uid="+uid+"&my_id="+uid+"&album_id="+aid;
        String url="https://www.iampro.co/api/app_service.php?type=search_all_items&search_type=PRODUCT&category="+cname+"&search_data=&uid="+uid+"&my_id="+uid;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        SectionImageModel dm = new SectionImageModel();
                        dm.setHeaderTitle(cname);
                        dm.setAlbemId(item_name.get(cname));
                        dm.setMore("loadmore");
                        //ArrayList<MyImageModel> singleItem = new ArrayList<>();
                        //ArrayList<MyProductModel> item = new ArrayList<>();
                        ArrayList<MyImageModel> item = new ArrayList<>();
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
                                JSONArray jsonArrayPics=student.getJSONArray("pro_detail");
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
                                    //String rating=pics.optString("rating");
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
                                    //selling_cost
                                    int scost=userDetail.optInt("selling_cost");
                                    int pcost=userDetail.optInt("purchese_cost");
                                    //String ratingv=userDetail.getString("avg_rating");
                                    String rating="4";
                                    String more="loadmore";
                                    //item.add(new MyImageModel(String.valueOf(id),String.valueOf(albemid),name,category,String.valueOf(albem_type),image,udate,about_us,String.valueOf(group_id),String.valueOf(is_featured),String.valueOf(status),is_block,String.valueOf(comments),String.valueOf(totallike),String.valueOf(like_unlike),rating,String.valueOf(user_id),more,avatar,fullname));
                                    //item.add(new DataModel(name,image,udate,category,totallike,like_unlike,comments,udate,Float.parseFloat(rating),uid,fullname,avatar,id,IMAGE_TYPE));
                                    //item.add(new MyImageModel(name,image,udate,categoryy,totallike,comments,scost,pcost,Float.parseFloat(ratingv),uid,fullname,avatar,String.valueOf(user_id),more,like_unlike));
                                    //item.add(new MyImageModel(String.valueOf(id),String.valueOf(albemid),name,category,String.valueOf(albem_type),image,udate,about_us,String.valueOf(group_id),String.valueOf(is_featured),String.valueOf(status),is_block,String.valueOf(comments),String.valueOf(totallike),String.valueOf(like_unlike),rating,String.valueOf(user_id),more,avatar,fullname));

       allSampleData.add(new UserModel(uid,name,image,udate,category,total_images,total_videos,total_users,total_products,total_provides,total_demands));


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
                            String type="friend";
                            //adapterr = new MyImageVideoDataAdapter(getContext(), allSampleDatamore,item_loadmore,type);
                            //recycler_view_load_more.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            //recycler_view_load_more.setAdapter(adapterr);

                            adapter = new UserDataAdapter(getContext(), allSampleData,UserFragment.this);
                            my_recycler_view.setAdapter(adapter);
                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            pDialog.dismiss();
                            Toast.makeText(getContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("catch_f",""+e.getMessage());
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        Log.d("verror",""+error.getMessage());
                    }
                }
        );
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProvide();
    } */

    public void getUser(int limitss){
        final String url = "https://www.iampro.co/api/app_service.php?type=getSelectedUser&limit="+limitss+"&uid="+uid+"&my_id="+uid;

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
                        if(!singleItem.isEmpty()){
                            singleItem.clear();
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        }
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("fname");
                                   uid=student.getInt("id");
                                String identity_type=student.getString("identity_type");
                                String category=student.getString("category");
                                String imagev=student.getString("avatar");
                                String image= Config.AVATAR_URL+"200/200/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);

                                String total_images=student.optString("total_image");
                                String total_videos=student.optString("total_video");
                                String total_users=student.optString("total_friends");
                                String total_products=student.optString("total_product");
                                String total_provides=student.optString("total_provide");
                                String total_demands=student.optString("total_demend");

                                //allSampleData.add(new UserModel(uid,name,image,udate,category));
                                //String total_image,String total_video,String total_friend
                                allSampleData.add(new UserModel(uid,name,image,udate,category,total_images,total_videos,total_users,total_products,total_provides,total_demands));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                           // my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());


                            adapter = new UserDataAdapter(getContext(), allSampleData,UserFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);

                            //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
                            //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_load_more);

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
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        //getProduct();
    }
    @Override
    public void onItemClick(UserModel item) {
        ProfileActivity fragment = new ProfileActivity();
        Bundle args = new Bundle();
        args.putString("uid", String.valueOf(uid));
        function.loadFragment(getContext(),fragment,args);

    }

    public void getUserMores(int start_limit,int end_limit){
        //final String url = "https://www.iampro.co/api/app_service.php?type=getSelectedUser&limit="+limitss+"&uid="+uid+"&my_id="+uid;
         final String url="https://www.iampro.co/api/app_service.php?type=getAllUser&name=&uid="+uid+"&my_id="+uid+"&start_limit="+start_limit+"&end_limit="+end_limit;
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
                        }
                        if(!allSampleData.isEmpty()){
                            allSampleData.clear();
                        } */
                        try{
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                String name = student.getString("fname");
                                uid=student.getInt("id");
                                String identity_type=student.getString("identity_type");
                                String category=student.getString("category");
                                String imagev=student.getString("avatar");
                                String image= Config.AVATAR_URL+"200/200/"+imagev;
                                String udate=student.getString("udate");
                                Log.d("pdata",""+name+""+category+""+image+""+udate);

                                String total_images=student.optString("total_image");
                                String total_videos=student.optString("total_video");
                                String total_users=student.optString("total_friends");
                                String total_products=student.optString("total_product");
                                String total_provides=student.optString("total_provide");
                                String total_demands=student.optString("total_demend");

                                //allSampleData.add(new UserModel(uid,name,image,udate,category));
                                //String total_image,String total_video,String total_friend
                                allSampleData.add(new UserModel(uid,name,image,udate,category,total_images,total_videos,total_users,total_products,total_provides,total_demands));

                            }
                            Log.d("bdm",singleItem.toString());
                            dm.setAllItemsInSection(singleItem);
                            Log.d("adm",singleItem.toString());
                            Log.d("dmm",dm.toString());
                            //allSampleData.add(dm);
                            Log.d("allsampledatav", allSampleData.toString());
                            // my_recycler_view.setHasFixedSize(true);
                            Log.d("allSampleDatas",""+allSampleData.size()+"--"+allSampleData.toString());


                            adapter = new UserDataAdapter(getContext(), allSampleData,UserFragment.this);
                            my_recycler_view.setAdapter(adapter);

                            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            my_recycler_view.setLayoutManager(manager);
                             adapter.notifyDataSetChanged();

                            my_recycler_view.setItemAnimator(new DefaultItemAnimator());
                             my_recycler_view.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

                      ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new UserItemTouchHelper(0, ItemTouchHelper.LEFT,UserFragment.this);
                      new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_load_more);

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
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

       @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        Toast.makeText(getContext(),"swiped",Toast.LENGTH_LONG).show();
        if (viewHolder instanceof UserDataAdapter.ViewHolder) {
        // get the removed item name to display it in snack bar
        String name =allSampleData.get(viewHolder.getAdapterPosition()).getName();

        // backup of removed item for undo purpose
       final UserModel deletedItem =allSampleData.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        // remove the item from recycler view
        adapter.removeItem(viewHolder.getAdapterPosition());

        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(ll, name + " removed from list!", Snackbar.LENGTH_LONG);

        snackbar.setAction("UNDO", new View.OnClickListener() {
   @Override
   public void onClick(View view) {

        // undo is selected, restore the deleted item
        adapter.restoreItem(deletedItem, deletedIndex);
        }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        }
        }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                   // adapter.removeItem(position);
                    Toast.makeText(getContext(),"left",Toast.LENGTH_LONG).show();
                } else {
                   /* removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Country");
                    et_country.setText(countries.get(position));
                    alertDialog.show(); */

                    Toast.makeText(getContext(),"left",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(my_recycler_view);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

        }