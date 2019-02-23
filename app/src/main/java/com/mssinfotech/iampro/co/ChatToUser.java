package com.mssinfotech.iampro.co;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.ImageAdapter;
import com.mssinfotech.iampro.co.adapter.MyImageVideoDataAdapter;
import com.mssinfotech.iampro.co.adapter.UserChatAdapter;
import com.mssinfotech.iampro.co.adapter.UserDataAdapter;
import com.mssinfotech.iampro.co.model.ChatList;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.MyImageModel;
import com.mssinfotech.iampro.co.model.SectionImageModel;
import com.mssinfotech.iampro.co.model.UserModel;
import com.mssinfotech.iampro.co.models.Chat;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatToUser extends AppCompatActivity implements UserDataAdapter.ItemListener{
  String id;
  ArrayList<String> item_name=new ArrayList<>();
   RecyclerView recycler_view_chat;
   EditText et_chat;
    ImageView btnSend;
    ArrayList<ChatList> allSampleDatamore=new ArrayList<>();
    UserChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_to_user);
        recycler_view_chat=findViewById(R.id.recycler_view_chat);
        et_chat=findViewById(R.id.et_chat);
        btnSend=findViewById(R.id.btnSend);
        id=getIntent().getExtras().getString("id");
        getUserDetail(id);
        getChatList(id);

    }
      public void getUserDetail(String id){
          String url="https://www.iampro.co/api/app_service.php?type=get_user_detail&id="+id;
          RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
          JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,null,
                  new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          System.out.println(response);
                          try {
                              String id = response.getString("id");
                              String  role = response.getString("role");
                              String username = response.getString("username");
                              String avatar= response.getString("avatar");
                              String fullname = response.getString("fullname");

                               getSupportActionBar().setIcon(R.drawable.iampro);

                               getSupportActionBar().setTitle(fullname);
                          }
                           catch(Exception e) {
                           Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                           }
                      }
                  },
                  new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {

                      }
                  });
          requestQueue.add(jsObjRequest);
    }

    public void getChatList(String id){
          String myid= PrefManager.getLoginDetail(this,"id");
        String url="https://www.iampro.co/api/chat.php?type=fetchChat&lang=en&uid="+id+"&myid="+myid;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            JSONArray jsonArray=response.getJSONArray("list");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject student1 =jsonArray.getJSONObject(i);
                                String from_id= student1.optString("from_id");
                                String to_id = student1.optString("to_id");
                                 String avatar = student1.optString("avatar");
                                 String messageType = student1.optString("messageType");
                                 String msg = student1.optString("msg");
                                 String udate = student1.optString("udate");
                                allSampleDatamore.add(new ChatList(from_id,to_id,avatar,messageType,msg,udate));
                            }
                            //adapter = new UserChatAdapter(getApplicationContext(),allSampleDatamore,ChatToUser.this);
                            recycler_view_chat.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            //recycler_view_chat.setAdapter(adapter);
                        }
                        catch(Exception e) {
                            Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onItemClick(UserModel item) {

    }
}
