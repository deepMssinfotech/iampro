package com.mssinfotech.iampro.co.popup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.LoginActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.SignupActivity;
import com.mssinfotech.iampro.co.WelcomeActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.user.ChangePasswordActivity;
import com.mssinfotech.iampro.co.user.EditProfileActivity;
import com.mssinfotech.iampro.co.user.FriendRequestActivity;
import com.mssinfotech.iampro.co.user.JoinFriendActivity;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyPurchsaeActivity;
import com.mssinfotech.iampro.co.user.MySellingActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.MyWhishlistActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import de.hdodenhof.circleimageview.CircleImageView;


public class MenuPopup extends Activity{
    ImageView menu_btn_dashboard_nonuser,menu_btn_login,menu_btn_register,menu_btn_search,menu_btn_shareit,menu_btn_review,menu_btn_wallet,menu_btn_browser;
    ImageView menu_btn_dashboard,menu_btn_myprofile,menu_btn_editprofile,menu_btn_changepassword,menu_btn_joinedfriend,menu_btn_friendrequest,menu_btn_myphoto,menu_btn_myvideo,menu_btn_myproduct,menu_btn_myprovide,menu_btn_demand,menu_btn_mymessage,menu_btn_mycart,menu_btn_myselling,menu_btn_mypurchase,menu_btn_share,menu_btn_mywhishlist,menu_btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_user_menu);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //int width = dm.widthPixels;
        //int height = dm.heightPixels;

        //getWindow().setLayout((int)(width*.8),(int)(height*.5));
        // Setup any handles to view objects here
        LinearLayout nonuser_menu = findViewById(R.id.nonuser_menu);
        LinearLayout user_menu = findViewById(R.id.user_menu);
        Config.count_whishlist = findViewById(R.id.count_whishlist);
        Config.count_friend_request = findViewById(R.id.count_friend_request);
        Config.count_cart = findViewById(R.id.count_cart);
        Config.count_message = findViewById(R.id.count_message);
        TextView username = findViewById(R.id.username);
        if(PrefManager.isLogin(getApplicationContext())) {
            menu_btn_dashboard = findViewById(R.id.menu_btn_dashboard);
            menu_btn_myprofile = findViewById(R.id.menu_btn_myprofile);
            menu_btn_editprofile = findViewById(R.id.menu_btn_editprofile);
            menu_btn_changepassword = findViewById(R.id.menu_btn_changepassword);
            menu_btn_joinedfriend = findViewById(R.id.menu_btn_joinedfriend);
            menu_btn_friendrequest = findViewById(R.id.menu_btn_friendrequest);
            menu_btn_myphoto = findViewById(R.id.menu_btn_myphoto);
            menu_btn_myvideo = findViewById(R.id.menu_btn_myvideo);
            menu_btn_myproduct = findViewById(R.id.menu_btn_myproduct);
            menu_btn_myprovide = findViewById(R.id.menu_btn_myprovide);
            menu_btn_demand = findViewById(R.id.menu_btn_demand);
            menu_btn_mymessage = findViewById(R.id.menu_btn_mymessage);
            menu_btn_mycart = findViewById(R.id.menu_btn_mycart);
            menu_btn_myselling = findViewById(R.id.menu_btn_myselling);
            menu_btn_mypurchase = findViewById(R.id.menu_btn_mypurchase);
            menu_btn_share = findViewById(R.id.menu_btn_share);
            menu_btn_mywhishlist = findViewById(R.id.menu_btn_mywhishlist);
            menu_btn_logout = findViewById(R.id.menu_btn_logout);
            menu_btn_dashboard.setOnClickListener(dashboardOnClickListener);
            menu_btn_myprofile.setOnClickListener(myprofileOnClickListener);
            menu_btn_editprofile.setOnClickListener(editprofileOnClickListener);
            menu_btn_changepassword.setOnClickListener(changepasswordOnClickListener);
            menu_btn_joinedfriend.setOnClickListener(joinedfriendOnClickListener);
            menu_btn_friendrequest.setOnClickListener(friendrequestOnClickListener);
            menu_btn_myphoto.setOnClickListener(myphotoOnClickListener);
            menu_btn_myvideo.setOnClickListener(myvideoOnClickListener);
            menu_btn_myproduct.setOnClickListener(myproductOnClickListener);
            menu_btn_myprovide.setOnClickListener(myprovideOnClickListener);
            menu_btn_demand.setOnClickListener(demandOnClickListener);
            menu_btn_mymessage.setOnClickListener(mymessageOnClickListener);
            menu_btn_mycart.setOnClickListener(mycartOnClickListener);
            menu_btn_myselling.setOnClickListener(mysellingOnClickListener);
            menu_btn_mypurchase.setOnClickListener(mypurchaseOnClickListener);
            menu_btn_share.setOnClickListener(shareOnClickListener);
            menu_btn_mywhishlist.setOnClickListener(mywhishlistOnClickListener);
            menu_btn_logout.setOnClickListener(logoutOnClickListener);
            String avatar = Config.AVATAR_URL + "250/250/" + PrefManager.getLoginDetail(getApplicationContext(), "img_url");
            String background = Config.AVATAR_URL + "h/250/" + PrefManager.getLoginDetail(getApplicationContext(), "banner_image");

            ImageView userbackgroud = findViewById(R.id.userbackgroud);
            CircleImageView userimage = findViewById(R.id.userimage);
            username.setText(PrefManager.getLoginDetail(getApplicationContext(), "fname") + " " + PrefManager.getLoginDetail(getApplicationContext(), "lname"));
            Glide.with(this).load(background).apply(Config.options_background).into(userbackgroud);
            Glide.with(this).load(avatar).apply(Config.options_avatar).into(userimage);
            nonuser_menu.setVisibility(View.GONE);
            user_menu.setVisibility(View.VISIBLE);
        }else{
            menu_btn_dashboard_nonuser = findViewById(R.id.menu_btn_dashboard_nonuser);
            menu_btn_login = findViewById(R.id.menu_btn_login);
            menu_btn_register = findViewById(R.id.menu_btn_register);
            menu_btn_search = findViewById(R.id.menu_btn_search);
            menu_btn_shareit = findViewById(R.id.menu_btn_shareit);
            menu_btn_review = findViewById(R.id.menu_btn_review);
            menu_btn_wallet = findViewById(R.id.menu_btn_wallet);
            menu_btn_browser = findViewById(R.id.menu_btn_browser);
            menu_btn_dashboard_nonuser.setOnClickListener(dashboardOnClickListener);
            menu_btn_login.setOnClickListener(loginOnClickListener);
            menu_btn_register.setOnClickListener(registerOnClickListener);
            menu_btn_search.setOnClickListener(searchOnClickListener);
            menu_btn_shareit.setOnClickListener(shareOnClickListener);
            menu_btn_register.setOnClickListener(reviewOnClickListener);
            menu_btn_wallet.setOnClickListener(walletOnClickListener);
            menu_btn_browser.setOnClickListener(browserOnClickListener);
            username.setText("Welcome To Iampro");
            nonuser_menu.setVisibility(View.VISIBLE);
            user_menu.setVisibility(View.GONE);
        }
        
    }
    public void removeFragment(){
        //getActivity().finish();
    }
    private View.OnClickListener dashboardOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_dashboard"))return;
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getApplicationContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_profile"))return;
            ProfileActivity fragment = new ProfileActivity();
            Bundle args = new Bundle();
            args.putString("uid", PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener editprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_edit_profile"))return;
            EditProfileActivity fragment = new EditProfileActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener changepasswordOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            ChangePasswordActivity fragment= new ChangePasswordActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener joinedfriendOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            JoinFriendActivity fragment = new JoinFriendActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener friendrequestOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            FriendRequestActivity fragment = new FriendRequestActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener myphotoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_image"))return;
            MyImageActivity fragment = new MyImageActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener myvideoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyVideoActivity fragment = new MyVideoActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener myproductOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyProductActivity fragment = new MyProductActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener myprovideOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyProvideActivity fragment = new MyProvideActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener demandOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyDemandActivity fragment = new MyDemandActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener mymessageOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MessageActivity fragment = new MessageActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener mycartOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            CartActivity fragment = new CartActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getApplicationContext(),"id"));
            function.loadFragment(getApplicationContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener mysellingOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MySellingActivity fragment = new MySellingActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener mypurchaseOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyPurchsaeActivity fragment = new MyPurchsaeActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String shareBody = "Share to you friend and family to join "+ Config.URL_ROOT;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Iampro App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            removeFragment();
        }
    };
    private View.OnClickListener mywhishlistOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyWhishlistActivity fragment = new MyWhishlistActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            PrefManager.logout(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            finish();
            removeFragment();
        }
    };
    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_login"))return;
            LoginActivity fragment = new LoginActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_register"))return;
            SignupActivity fragment = new SignupActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            SearchActivity fragment = new SearchActivity();
            function.loadFragment(getApplicationContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener reviewOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String appPackageName = getApplicationContext().getPackageName(); // package name of the app
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    };
    private View.OnClickListener walletOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            function.OpenWallet(getApplicationContext());
        }
    };
    private View.OnClickListener browserOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            function.OpenBrowser(getApplicationContext(),Config.URL_ROOT);
        }
    };
}
