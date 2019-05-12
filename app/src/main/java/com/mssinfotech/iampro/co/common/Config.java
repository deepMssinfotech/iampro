package com.mssinfotech.iampro.co.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Config
{
    public static final String TAG="Imapro tag";
    public static boolean doubleBackToExitPressedOnce = false;
    public static final String API_URL="https://www.iampro.co/api/";
    public static final String URL_ROOT="https://www.iampro.co/";
    public static final String AJAX_URL="https://www.iampro.co/ajax/";
    public static final String AVATAR_URL="https://www.iampro.co/uploads/avatar/";

    public static final String OTHER_IMAGE_URL="https://www.iampro.co/uploads/product/300/250/";
    public static final String V_URL="https://www.iampro.co/uploads/v_image/300/250/";
    public static final String BANNER_URL="https://www.iampro.co/uploads/media/";
    public static final String MEDIA_URL="https://www.iampro.co/uploads/media/";
   // public static final String ALBUM_URL="https://www.iampro.co/uploads/album/w/500/";
    public static final String ALBUM_URL="https://www.iampro.co/uploads/album/160/95/";
    public static final String TOP_SLIDER="https://www.iampro.co/uploads/slider/h/280/";
    public static final String IP_ADDRESS="";
    public static TextView count_chat = null, count_notify = null, count_cart = null, count_message = null , count_whishlist = null, count_friend_request = null;
    public static final String IMAGE_DIRECTORY = "/iampro/image";
    public static final String VIDEO_DIRECTORY = "/iampro/video";
    public static final Integer GALLERY = 1, CAMERA = 2, PICK_IMAGE_MULTIPLE = 3;
    public static String layoutName="";
    public static String ResponceResult="";
    public static String PAGE_TAG="page_tag";
    public static String PREVIOUS_PAGE_TAG = null;
    private static final float BITMAP_SCALE = 0.4f;
    private static final int BLUR_RADIUS = 8;
    public static final boolean SHOW_LOGS = true;
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
    public static RequestOptions options_avatar = new RequestOptions()
            .centerCrop()
            .circleCrop()
            .placeholder(R.drawable.iampro)
            .error(R.drawable.iampro)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_background = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_product = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_image = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_video= new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_provide = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static RequestOptions options_demand = new RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.profile_background)
            .error(R.drawable.profile_background)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    public static int STORAGE_PERMISSION_CODE = 123;
    public Config(){

    }
    public static void setLayoutName(String name){
        layoutName=name;
    }
    public static String getLayoutName(){
        return layoutName;
    }
    /**  Network connectivity  */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi=true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile=true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public static void showInternetDialog(Context context)
    {
        new AlertDialog.Builder(context)
                .setTitle("Oops you are offline!")
                .setMessage("Check your network connectivity and try again...")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                }).show();
    }

    public static Bitmap fastblur(Bitmap sentBitmap) {
        float scale = BITMAP_SCALE;
        int radius = BLUR_RADIUS;
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);

    }

    public static void sendRequestToServer(Context context) {
        if (PrefManager.isLogin(context)) {
            //Log.d(TAG, "test servide for 5 sec");
            String api_url = Config.API_URL + "chat.php?type=chat_count&myid=" + PrefManager.getLoginDetail(context, "id");
            //Log.d(Config.TAG, api_url);
             {
                StringRequest stringRequest = new StringRequest(api_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject result = null;
                                //Log.d(Config.TAG, response);
                                try {
                                    result = new JSONObject(response);
                                    //Storing the Array of JSON String to our JSON Array
                                    if (count_chat != null) count_chat.setText(result.getString("chatcount"));
                                    if(count_notify != null) count_notify.setText(result.getString("my_notification"));
                                    if(count_cart != null) count_cart.setText(result.getString("cart_count"));
                                    if(count_message != null) count_message.setText(result.getString("chatcount"));
                                    if(count_whishlist != null) count_whishlist.setText(result.getString("my_wishlist"));
                                    if(count_friend_request != null) count_friend_request.setText(result.getString("panding_friend"));
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
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                //Adding request to the queue
                requestQueue.add(stringRequest);
            }
        }
    }
}
