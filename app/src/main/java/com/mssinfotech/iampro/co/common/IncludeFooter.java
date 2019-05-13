package com.mssinfotech.iampro.co.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.LoginActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.NotificationActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.SignupActivity;
import com.mssinfotech.iampro.co.WelcomeActivity;
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
public class IncludeFooter  extends RelativeLayout {
    private LayoutInflater inflater;
    private boolean isLogin = false;
    //NavigationView navigationView;
    //DrawerLayout drawer;
    private Context context;
    //TextView title;
    private Dialog myDialog;
    public IncludeFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_footer, this, true);
         this.context=context;
        TextView count_chat = this.findViewById(R.id.count_chat);
        TextView count_notify = this.findViewById(R.id.count_notify);
        TextView count_cart = this.findViewById(R.id.count_cart);
        count_chat.setText(PrefManager.getLoginDetail(context,"chatcount"));
        count_notify.setText(PrefManager.getLoginDetail(context,"my_notification"));
        count_cart.setText(PrefManager.getLoginDetail(context,"cart_count"));
        (this.findViewById(R.id.btn_menu_more)).setOnClickListener(moreOnClickListener);
        (this.findViewById(R.id.btn_menu_search)).setOnClickListener(searchOnClickListener);
        (this.findViewById(R.id.btn_menu_cart)).setOnClickListener(cartOnClickListener);
        (this.findViewById(R.id.btn_menu_iampro)).setOnClickListener(iamproOnClickListener);
        (this.findViewById(R.id.btn_menu_notice)).setOnClickListener(noticeOnClickListener);
        (this.findViewById(R.id.btn_menu_message)).setOnClickListener(messageOnClickListener);
        (this.findViewById(R.id.btn_menu_user)).setOnClickListener(userOnClickListener);
        if (PrefManager.isLogin(getContext())) {
            isLogin = true;
            String avatar = Config.AVATAR_URL + "250/250/" + PrefManager.getLoginDetail(getContext(), "img_url");
            Glide.with(getContext()).load(avatar).apply(Config.options_avatar).into((ImageView) this.findViewById(R.id.btn_menu_user));
        }
    }
    private OnClickListener moreOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            ImageView menu_btn_dashboard_nonuser,menu_btn_login,menu_btn_register,menu_btn_search,menu_btn_shareit,menu_btn_review,menu_btn_wallet,menu_btn_browser;
            ImageView menu_btn_dashboard,menu_btn_myprofile,menu_btn_editprofile,menu_btn_changepassword,menu_btn_joinedfriend,menu_btn_friendrequest,menu_btn_myphoto,menu_btn_myvideo,menu_btn_myproduct,menu_btn_myprovide,menu_btn_demand,menu_btn_mymessage,menu_btn_mycart,menu_btn_myselling,menu_btn_mypurchase,menu_btn_share,menu_btn_mywhishlist,menu_btn_logout;
            myDialog = new Dialog(getContext());
            myDialog.setContentView(R.layout.include_user_menu);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(myDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            myDialog.getWindow().setAttributes(lp);
            myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

            LinearLayout nonuser_menu = myDialog.findViewById(R.id.nonuser_menu);
            LinearLayout user_menu = myDialog.findViewById(R.id.user_menu);
            TextView count_whishlist = myDialog.findViewById(R.id.count_whishlist);
            TextView count_friend_request = myDialog.findViewById(R.id.count_friend_request);
            TextView count_cart = myDialog.findViewById(R.id.count_cart);
            TextView  count_message = myDialog.findViewById(R.id.count_message);
            count_cart.setText(PrefManager.getLoginDetail(context,"cart_count"));
            count_message.setText(PrefManager.getLoginDetail(context,"chatcount"));
            count_whishlist.setText(PrefManager.getLoginDetail(context,"my_wishlist"));
            count_friend_request.setText(PrefManager.getLoginDetail(context,"panding_friend"));
            TextView username = myDialog.findViewById(R.id.username);
            if(PrefManager.isLogin(getContext())) {
                menu_btn_dashboard = myDialog.findViewById(R.id.menu_btn_dashboard);
                menu_btn_myprofile = myDialog.findViewById(R.id.menu_btn_myprofile);
                menu_btn_editprofile = myDialog.findViewById(R.id.menu_btn_editprofile);
                menu_btn_changepassword = myDialog.findViewById(R.id.menu_btn_changepassword);
                menu_btn_joinedfriend = myDialog.findViewById(R.id.menu_btn_joinedfriend);
                menu_btn_friendrequest = myDialog.findViewById(R.id.menu_btn_friendrequest);
                menu_btn_myphoto = myDialog.findViewById(R.id.menu_btn_myphoto);
                menu_btn_myvideo = myDialog.findViewById(R.id.menu_btn_myvideo);
                menu_btn_myproduct = myDialog.findViewById(R.id.menu_btn_myproduct);
                menu_btn_myprovide = myDialog.findViewById(R.id.menu_btn_myprovide);
                menu_btn_demand = myDialog.findViewById(R.id.menu_btn_demand);
                menu_btn_mymessage = myDialog.findViewById(R.id.menu_btn_mymessage);
                menu_btn_mycart = myDialog.findViewById(R.id.menu_btn_mycart);
                menu_btn_myselling = myDialog.findViewById(R.id.menu_btn_myselling);
                menu_btn_mypurchase = myDialog.findViewById(R.id.menu_btn_mypurchase);
                menu_btn_share = myDialog.findViewById(R.id.menu_btn_share);
                menu_btn_mywhishlist = myDialog.findViewById(R.id.menu_btn_mywhishlist);
                menu_btn_logout = myDialog.findViewById(R.id.menu_btn_logout);
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
                String avatar = Config.AVATAR_URL + "250/250/" + PrefManager.getLoginDetail(getContext(), "img_url");
                String background = Config.AVATAR_URL + "h/250/" + PrefManager.getLoginDetail(getContext(), "banner_image");

                ImageView userbackgroud = myDialog.findViewById(R.id.userbackgroud);
                CircleImageView userimage = myDialog.findViewById(R.id.userimage);
                username.setText(PrefManager.getLoginDetail(getContext(), "fname") + " " + PrefManager.getLoginDetail(getContext(), "lname"));
                Glide.with(getContext()).load(background).apply(Config.options_background).into(userbackgroud);
                Glide.with(getContext()).load(avatar).apply(Config.options_avatar).into(userimage);
                nonuser_menu.setVisibility(View.GONE);
                user_menu.setVisibility(View.VISIBLE);
            }else{
                menu_btn_dashboard_nonuser = myDialog.findViewById(R.id.menu_btn_dashboard_nonuser);
                menu_btn_login = myDialog.findViewById(R.id.menu_btn_login);
                menu_btn_register = myDialog.findViewById(R.id.menu_btn_register);
                menu_btn_search = myDialog.findViewById(R.id.menu_btn_search);
                menu_btn_shareit = myDialog.findViewById(R.id.menu_btn_shareit);
                menu_btn_review = myDialog.findViewById(R.id.menu_btn_review);
                menu_btn_wallet = myDialog.findViewById(R.id.menu_btn_wallet);
                menu_btn_browser = myDialog.findViewById(R.id.menu_btn_browser);
                menu_btn_dashboard_nonuser.setOnClickListener(dashboardOnClickListener);
                menu_btn_login.setOnClickListener(loginOnClickListener);
                menu_btn_register.setOnClickListener(registerOnClickListener);
                menu_btn_search.setOnClickListener(searchOnClickListener);
                menu_btn_shareit.setOnClickListener(shareOnClickListener);
                menu_btn_review.setOnClickListener(reviewOnClickListener);
                menu_btn_wallet.setOnClickListener(walletOnClickListener);
                menu_btn_browser.setOnClickListener(browserOnClickListener);
                username.setText("Welcome To Iampro");
                nonuser_menu.setVisibility(View.VISIBLE);
                user_menu.setVisibility(View.GONE);
            }
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
    };
    private View.OnClickListener dashboardOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_dashboard"))return;
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getContext().startActivity(intent);
            removeFragment();
        }
    };
    private View.OnClickListener myprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_profile"))return;
            ProfileActivity fragment = new ProfileActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener editprofileOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_edit_profile"))return;
            EditProfileActivity fragment = new EditProfileActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener changepasswordOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            ChangePasswordActivity fragment= new ChangePasswordActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener joinedfriendOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            JoinFriendActivity fragment = new JoinFriendActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener friendrequestOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            FriendRequestActivity fragment = new FriendRequestActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener myphotoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_my_image"))return;
            MyImageActivity fragment = new MyImageActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener myvideoOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyVideoActivity fragment = new MyVideoActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener myproductOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyProductActivity fragment = new MyProductActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener myprovideOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyProvideActivity fragment = new MyProvideActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener demandOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyDemandActivity fragment = new MyDemandActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener mymessageOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MessageActivity fragment = new MessageActivity();
            Bundle args = new Bundle();
            args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
            function.loadFragment(getContext(),fragment,args);
            removeFragment();
        }
    };
    private View.OnClickListener mycartOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
              if (PrefManager.isLogin(getContext())){
                  //Toast.makeText(context,""+"LoggedIn...",Toast.LENGTH_LONG).show();
                  CartActivity fragment = new CartActivity();
                  Bundle args = new Bundle();
                  args.putString("uid",PrefManager.getLoginDetail(getContext(),"id"));
                  function.loadFragment(getContext(),fragment,args);
                  removeFragment();
              }
              else{
                  Toast.makeText(getContext(),""+"First Login and try again...",Toast.LENGTH_LONG).show();
                     return;
              }
        }
    };
    private View.OnClickListener mysellingOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MySellingActivity fragment = new MySellingActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener mypurchaseOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyPurchsaeActivity fragment = new MyPurchsaeActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            String shareBody = "Share to you friend and family to join "+Config.URL_ROOT;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Iampro App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            getContext().startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));
            removeFragment();
        }
    };
    private View.OnClickListener mywhishlistOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            MyWhishlistActivity fragment = new MyWhishlistActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener logoutOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            PrefManager.logout(getContext());
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            //getContext().getActivity().finish();

            removeFragment();
        }
    };
    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_login"))return;
            LoginActivity fragment = new LoginActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener registerOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //if(function.isSamePage("activity_register"))return;
            SignupActivity fragment = new SignupActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener searchOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            SearchActivity fragment = new SearchActivity();
            function.loadFragment(getContext(),fragment,null);
            removeFragment();
        }
    };
    private View.OnClickListener reviewOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String appPackageName = getContext().getPackageName(); // package name of the app
            try {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    };
    private View.OnClickListener walletOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            function.OpenWallet(getContext());
        }
    };
    private View.OnClickListener browserOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            function.OpenBrowser(getContext(),Config.URL_ROOT);
        }
    };
    public void removeFragment(){
        if (myDialog!=null && myDialog.isShowing())
        myDialog.dismiss();
    }
    private OnClickListener cartOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (PrefManager.isLogin(getContext())) {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                CartActivity fragment = new CartActivity();
                Bundle args = new Bundle();
                args.putString("name", "mragank");
                function.loadFragment(getContext(), fragment, args);
            }
             else{
                  Toast.makeText(getContext(),""+"First Login and try again...",Toast.LENGTH_LONG).show();
            }
        }
    };
    private OnClickListener iamproOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), HomeActivity.class));
            if(function.isSamePage("activity_home"))function.finishFunction(getContext());
        }
    };
    private OnClickListener noticeOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            NotificationActivity fragment = new NotificationActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener messageOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MessageActivity fragment = new MessageActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);

        }
    };
    private OnClickListener userOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(isLogin) {
                ProfileActivity fragment = new ProfileActivity();
                function.loadFragment(context,fragment,null);
            }else {
                //getContext().startActivity(new Intent(context, LoginActivity.class));
                LoginActivity loginFragment = new LoginActivity();
                function.loadFragment(context, loginFragment, null);
            }
        }
    };

}
