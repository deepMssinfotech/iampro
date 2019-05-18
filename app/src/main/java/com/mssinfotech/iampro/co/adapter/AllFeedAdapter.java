package com.mssinfotech.iampro.co.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.demand.DemandDetailActivity;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.FeedModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetailActivity;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.orientation.LandscapeOrientation;
import bg.devlabs.fullscreenvideoview.orientation.PortraitOrientation;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllFeedAdapter extends RecyclerView.Adapter<AllFeedAdapter.ViewHolder> {
    ArrayList<FeedModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    public AllFeedAdapter(Context context, ArrayList<FeedModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatImageView iv_comments, image, iv_favourite, ivLike,video_imageView;
        TextView fullname, udate, tv_comments, tv_totallike, detail_name, purchese_cost, selling_cost;
        LikeButton like_un, favButton;
        ImageView iv_buy;
        RatingBar ratingBar;
        LinearLayout ll_showhide, ll_comment,video_imageView_ll;
        FeedModel item;
        FullscreenVideoView fullscreenVideoView;
        CircleImageView imageView_user,imageView_icon;
        android.support.v7.widget.AppCompatImageView videoImage;
        FrameLayout videoLayout;
        SliderLayout imageSlider;
        private View currentFocusedLayout,oldFocusedLayout;
        int id;
        //int uid;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView_user = v.findViewById(R.id.imageView_user);
            imageView_icon = v.findViewById(R.id.imageView_icon);
            video_imageView_ll=v.findViewById(R.id.video_imageView_ll);
            video_imageView=v.findViewById(R.id.video_imageView);
            iv_comments = v.findViewById(R.id.iv_comments);
            imageSlider = v.findViewById(R.id.imageSlider);
            fullscreenVideoView = v.findViewById(R.id.fullscreenVideoView);
            videoImage = v.findViewById(R.id.videoImage);
            videoLayout = v.findViewById(R.id.videoLayout);
            iv_buy = v.findViewById(R.id.iv_buy);
            //iv_favourite=v.findViewById(R.id.iv_favourite);
            image = v.findViewById(R.id.imageView);
            ratingBar = v.findViewById(R.id.ratingBar);
            ll_comment = v.findViewById(R.id.ll_comment);
            //ivLike=v.findViewById(R.id.ivLike);
            fullname = v.findViewById(R.id.fullname);
            udate = v.findViewById(R.id.udate);
            tv_comments = v.findViewById(R.id.tv_comments);
            like_un = v.findViewById(R.id.likeButton);
            favButton = v.findViewById(R.id.favButton);
            tv_totallike = v.findViewById(R.id.tv_totallike);
            ll_showhide = v.findViewById(R.id.ll_showhide);
            detail_name = v.findViewById(R.id.detail_name);
            purchese_cost = v.findViewById(R.id.purchese_cost);
            selling_cost = v.findViewById(R.id.selling_cost);
        }
        public void setData(final FeedModel item) {
            this.item = item;
            //final String uid= PrefManager.getLoginDetail(mContext,"id");
            final String uid = String.valueOf(item.getUid());
            final String type = item.getType();
            final String id = String.valueOf(item.getId());
            //final String id=String.valueOf(item.getShareId());
            ratingBar.setRating(Float.parseFloat(String.valueOf(item.getAverage_rating())));
            fullname.setText(item.getFullname());
            udate.setText(item.getUdate());
            tv_comments.setText(String.valueOf(item.getComment()));
            tv_totallike.setText(String.valueOf(item.getLikes()));
             //like_un.setUnlikeDrawableRes(R.drawable.like);
             like_un.setUnlikeDrawable(AppCompatResources.getDrawable(mContext,R.drawable.like));
            //like_un.setLikeDrawableRes(R.drawable.like_un);
             like_un.setLikeDrawable(AppCompatResources.getDrawable(mContext,R.drawable.like_un));
            imageView_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity fragment = new ProfileActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                }
            });
            tv_totallike.setText(String.valueOf(item.getLikes()));
            String sid = item.getShareId();
            //final ArrayList<String> alsid=(ArrayList<String>)Arrays.asList(sid.split(","));
            final String[] animalsArray = sid.split(",");
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type.equalsIgnoreCase("DEMAND")) {
                        Intent intent = new Intent(mContext, DemandDetailActivity.class);
                        //intent.putExtra("id",String.valueOf(item.getPid()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("pid", String.valueOf(animalsArray[0]));
                        //intent.putExtra("pid", String.valueOf(animalsArray[0]));
                        intent.putExtra("id", String.valueOf(animalsArray[0]));
                        intent.putExtra("uid", String.valueOf(uid));
                        mContext.startActivity(intent);
                    } else if (type.equalsIgnoreCase("PROVIDE")) {
                        Intent intent = new Intent(mContext, ProvideDetailActivity.class);
                        intent.putExtra("pid", String.valueOf(animalsArray[0]));
                        intent.putExtra("id", String.valueOf(animalsArray[0]));
                        intent.putExtra("uid", String.valueOf(uid));
                        mContext.startActivity(intent);
                    } else if (type.equalsIgnoreCase("PRODUCT")) {
                        Intent intent = new Intent(mContext, ProductDetail.class);
                        intent.putExtra("pid", String.valueOf(animalsArray[0]));
                        intent.putExtra("id", String.valueOf(animalsArray[0]));
                        intent.putExtra("uid", String.valueOf(uid));
                        mContext.startActivity(intent);
                    }
                }
            });
            Glide.with(mContext)
                    .load(item.getAvatar_path())
                    .apply(new RequestOptions()
                            .circleCrop().bitmapTransform(new CircleCrop())
                            .fitCenter())
                    .into(imageView_user);

            Log.d("image_setdata", "" + item.getFimage_path());
            //String type=item.getType();
            if (!type.equalsIgnoreCase("VIDEO")) {
                image.setVisibility(View.VISIBLE);
                  Glide.with(mContext)
                        .load(item.getFimage_path())
                        .apply(Config.options_avatar)
                        .into(image);
            } else {
                image.setVisibility(View.GONE);
                //videoView.start();
                Uri video = Uri.parse(item.getFimage_path());
                //MediaController mc = new MediaController(mContext);
                //videoView.setMediaController(mc);
                //videoView.start();
            }
            if (type.equalsIgnoreCase("VIDEO")) {
                ll_showhide.setVisibility(View.GONE);
                //imageView_icon.setImageResource(R.drawable.video_icon);
                Glide.with(mContext)
                        .load(R.drawable.video_icon)
                        .apply(Config.options_product)
                        .into(imageView_icon);

            } else if (type.equalsIgnoreCase("IMAGE")) {
                ll_showhide.setVisibility(View.GONE);
                //imageView_icon.setImageResource(R.drawable.image_icon);
                Glide.with(mContext)
                        .load(R.drawable.image_icon)
                        .apply(Config.options_avatar)
                        .into(imageView_icon);

            } else if (type.equalsIgnoreCase("PRODUCT")) {
                ll_showhide.setVisibility(View.VISIBLE);
                //imageView_icon.setImageResource(R.drawable.product_icon);
                Glide.with(mContext)
                        .load(R.drawable.product_icon)
                        .apply(Config.options_product)
                        .into(imageView_icon);
                detail_name.setText(item.getDetail_name());

                purchese_cost.setText("Rs: " + String.valueOf(item.getPurchese_cost()));
                selling_cost.setText("Rs: " + String.valueOf(item.getSelling_cost()));
                iv_buy.setVisibility(View.VISIBLE);
                iv_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        function.addtocart(mContext, animalsArray[0], "1", selling_cost.toString());
                        CartActivity fragment = new CartActivity();
                        function.loadFragment(mContext,fragment,null);
                    }
                });
            } else if (type.equalsIgnoreCase("PROVIDE")) {
                ll_showhide.setVisibility(View.VISIBLE);
                //imageView_icon.setImageResource(R.drawable.provide_icon);
                Glide.with(mContext)
                        .load(R.drawable.provide_icon)
                        .apply(Config.options_provide)
                        .into(imageView_icon);
                // Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
                detail_name.setText(item.getDetail_name());
                purchese_cost.setText("");
                selling_cost.setText("Rs: " + String.valueOf(item.getSelling_cost()));
                // iv_favourite.setVisibility(View.VISIBLE);
            } else if (type.equalsIgnoreCase("DEMAND")) {
                ll_showhide.setVisibility(View.VISIBLE);
                //imageView_icon.setImageResource(R.drawable.demand_icon);
                Glide.with(mContext)
                        .load(R.drawable.demand_icon)
                        .apply(Config.options_demand)
                        .into(imageView_icon);
                //Toast.makeText(mContext,"Type: "+type,Toast.LENGTH_LONG).show();
                detail_name.setText(item.getDetail_name());
                purchese_cost.setText("");
                selling_cost.setText("Rs: " + String.valueOf(item.getSelling_cost()));
                //iv_favourite.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public AllFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(mValues.get(position));
        final String type = mValues.get(position).getType();
        final int uid = mValues.get(position).getUid();
        final int id = mValues.get(position).getId();

        final String sidd = mValues.get(position).getShareId();
        final String[] sharedId = sidd.split(",");
        int my_uid = mValues.get(position).getUid();
        if (PrefManager.isLogin(mContext))
            my_uid = Integer.parseInt(PrefManager.getLoginDetail(mContext, "id"));
        if (my_uid == 0) {
            Vholder.like_un.setEnabled(false);
        }
        if (mValues.get(position).getMylikes() == 1) {
            Vholder.like_un.setLiked(true);
            Vholder.tv_totallike.setTextColor(Color.RED);
        } else {
            Vholder.like_un.setLiked(false);
            Vholder.tv_totallike.setTextColor(Color.BLACK);
        }

        if (mValues.get(position).getIs_favrait() == 1) {
            Vholder.favButton.setLiked(true);
        } else {
            Vholder.favButton.setLiked(false);
        }
        Vholder.imageView_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("IMAGE")) {
                    MyImageActivity fragment = new MyImageActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                } else if (type.equalsIgnoreCase("VIDEO")) {
                    MyVideoActivity fragment = new MyVideoActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                } else if (type.equalsIgnoreCase("PRODUCT")) {
                    MyProductActivity fragment = new MyProductActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                } else if (type.equalsIgnoreCase("PROVIDE")) {
                    MyProvideActivity fragment = new MyProvideActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                } else if (type.equalsIgnoreCase("DEMAND")) {
                    MyDemandActivity fragment = new MyDemandActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                }
            }
        });
        String sid = mValues.get(position).getShareId();
        final String[] animalsArray = sid.split(",");
        if (type.equalsIgnoreCase("image")) {
            //Vholder.image.setVisibility(View.VISIBLE);
            //String ImageHol=Config.URL_ROOT+"uploads/album/w/500/"+mValues.get(position).getImage();
            String ImageHol = mValues.get(position).getFimage_path();
            Glide.with(mContext)
                    .load(ImageHol)
                    .apply(Config.options_image)
                    .into(Vholder.image);

            Vholder.imageSlider.setVisibility(View.VISIBLE);
            Vholder.image.setVisibility(View.GONE);
            //String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+mValues.get(position).getImage();
            Vholder.imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            Vholder.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            Vholder.imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
            Glide.with(mContext).load(R.drawable.product_icon).into(Vholder.imageView_icon);
            String shared_id = mValues.get(position).getShareId();
            final String[] sidArray = shared_id.split(",");
            if (mValues.get(position).getImageArray().size() > 0) {
                for (int i = 0; i < mValues.get(position).getImageArray().size(); i++) {
                    DefaultSliderView sliderView = new DefaultSliderView(mContext);
                    sliderView.setImageUrl(Config.URL_ROOT + "uploads/album/w/500/" + mValues.get(position).getImageArray().get(i));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    final int finalI = i;
                    //sliderView.setDescription("setDescription " + sidArray[finalI]);
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                            ImageDetail fragment = new ImageDetail();
                            Bundle args = new Bundle();
                            args.putString("id", sidArray[finalI]);
                            args.putString("type", "image");
                            args.putString("uid", String.valueOf(mValues.get(position).getUid()));
                            fragment.setArguments(args);
                            function.loadFragment(mContext,fragment,args);
                        }
                    });

                    //at last add this view in your layout :
                    Vholder.imageSlider.addSliderView(sliderView);
                }
            }

            Glide.with(mContext).load(R.drawable.image_icon).into(Vholder.imageView_icon);
        }
        else if (type.equalsIgnoreCase("video")) {
            Vholder.image.setVisibility(View.GONE);
            Vholder.video_imageView.setVisibility(View.VISIBLE);
            Vholder.video_imageView_ll.setVisibility(View.VISIBLE);
           /*  Vholder.image.setVisibility(View.GONE);
           Vholder.videoLayout.setVisibility(View.GONE);
            Vholder.fullscreenVideoView.setVisibility(View.VISIBLE);
            String ImageHol = Config.URL_ROOT + "uploads/video/" + mValues.get(position).getFimage_path();
            Log.d(Config.TAG + "video tag", ImageHol);
            Vholder.fullscreenVideoView.videoUrl(mValues.get(position).getFimage_path())
                    //.enableAutoStart()
                    .fastForwardSeconds(5)
                    .rewindSeconds(5)
                    .addSeekBackwardButton()
                    .addSeekForwardButton()
                    .portraitOrientation(PortraitOrientation.DEFAULT)
                    .landscapeOrientation(LandscapeOrientation.DEFAULT); */

            /*
            String imgsVideo[] = (mValues.get(position).getFimage_path()).split(".");
            String ImageHol = Config.URL_ROOT + "uploads/v_image/" + imgsVideo[0]+".jpg";
            Log.d(Config.TAG + "video tag", ImageHol) ;
            Glide.with(mContext).load(ImageHol).into(Vholder.videoImage);*/
            Glide.with(mContext).load(mValues.get(position).getFimage_path()).into(Vholder.video_imageView);

            String shared_id = mValues.get(position).getShareId();
            final String[] sidArray = shared_id.split(",");
            Vholder.video_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDetail fragment = new ImageDetail();
                    Bundle args = new Bundle();
                    args.putString("id", sidArray[0]);
                    args.putString("type", "video");
                    args.putString("uid", String.valueOf(mValues.get(position).getUid()));
                    fragment.setArguments(args);
                    function.loadFragment(mContext,fragment,args);
                }
            });
            Glide.with(mContext).load(R.drawable.video_icon).into(Vholder.imageView_icon);

        }
        else if (type.equalsIgnoreCase("product")) {
            Vholder.imageSlider.setVisibility(View.VISIBLE);
            Vholder.image.setVisibility(View.GONE);
            //String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+mValues.get(position).getImage();
            String ImageHol = mValues.get(position).getFimage_path();
            Vholder.imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            Vholder.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            Vholder.imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
            Glide.with(mContext).load(R.drawable.product_icon).into(Vholder.imageView_icon);
            DefaultSliderView sliderView1 = new DefaultSliderView(mContext);
            sliderView1.setImageUrl(ImageHol);
            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("setDescription " + (i + 1));
            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Intent intent = new Intent(mContext, ProductDetail.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid", String.valueOf(mValues.get(position).getShareId()));
                    intent.putExtra("uid", String.valueOf(mValues.get(position).getUid()));
                    mContext.startActivity(intent);
                }
            });

            //at last add this view in your layout :  other_image.length()>0
            Vholder.imageSlider.addSliderView(sliderView1);
            if (mValues.get(position).getImageArray().size() > 0) {
                for (int i = 0; i < mValues.get(position).getImageArray().size(); i++) {
                    DefaultSliderView sliderView = new DefaultSliderView(mContext);
                    sliderView.setImageUrl(Config.URL_ROOT + "uploads/product/w/500/" + mValues.get(position).getImageArray().get(i));
                    Log.d("feed_product", Config.URL_ROOT + "uploads/product/w/500/" + mValues.get(position).getImageArray().get(i));
                    Log.d("feed_arrayyy", "" + mValues.get(position).getImageArray());
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    //sliderView.setDescription("setDescription " + (i + 1));
                    final int finalI = i;
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                            Intent intent = new Intent(mContext, ProductDetail.class);
                            //intent.putExtra("id",String.valueOf(item.getPid()));
                            intent.putExtra("pid", String.valueOf(mValues.get(position).getShareId()));
                            intent.putExtra("uid", String.valueOf(mValues.get(position).getUid()));
                            mContext.startActivity(intent);
                        }
                    });

                    //at last add this view in your layout :
                    Vholder.imageSlider.addSliderView(sliderView);
                }
            }
            Glide.with(mContext).load(R.drawable.product_icon).into(Vholder.imageView_icon);
        }
        else if (type.equalsIgnoreCase("provide")) {
            Vholder.imageSlider.setVisibility(View.VISIBLE);
            Vholder.image.setVisibility(View.GONE);
            Vholder.favButton.setVisibility(View.VISIBLE);
            //JSONArray other_image = result.getJSONArray("myother_img");
            //String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+mValues.get(position).getImageArray().get(position);
            String ImageHol = mValues.get(position).getFimage_path();
            Vholder.imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            Vholder.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            Vholder.imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
            Glide.with(mContext).load(R.drawable.product_icon).into(Vholder.imageView_icon);
            DefaultSliderView sliderView1 = new DefaultSliderView(mContext);
            sliderView1.setImageUrl(ImageHol);
            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("setDescription " + (i + 1));
            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Intent intent = new Intent(mContext, ProvideDetailActivity.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid", String.valueOf(mValues.get(position).getShareId()));
                    intent.putExtra("uid", String.valueOf(mValues.get(position).getUid()));
                    mContext.startActivity(intent);
                }
            });

            //at last add this view in your layout :
            Vholder.imageSlider.addSliderView(sliderView1);
            if (mValues.get(position).getImageArray().size() > 0) {
                for (int i = 0; i < mValues.get(position).getImageArray().size(); i++) {
                    DefaultSliderView sliderView = new DefaultSliderView(mContext);
                    sliderView.setImageUrl(Config.URL_ROOT + "uploads/product/w/500/" + mValues.get(position).getImageArray().get(i));
                    Log.d(Config.TAG, Config.URL_ROOT + "uploads/product/w/500/" + mValues.get(position).getImageArray().get(i));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    //sliderView.setDescription("setDescription " + (i + 1));
                    final int finalI = i;
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                            Intent intent = new Intent(mContext, ProvideDetailActivity.class);
                            //intent.putExtra("id",String.valueOf(item.getPid()));
                            intent.putExtra("pid", String.valueOf(mValues.get(position).getShareId()));
                            intent.putExtra("uid", String.valueOf(mValues.get(position).getUid()));
                            mContext.startActivity(intent);
                        }
                    });

                    //at last add this view in your layout :
                    Vholder.imageSlider.addSliderView(sliderView);
                }
            }
            Glide.with(mContext).load(R.drawable.provide_icon).into(Vholder.imageView_icon);
        }
        else if (type.equalsIgnoreCase("demand")) {
            Vholder.imageSlider.setVisibility(View.VISIBLE);
            Vholder.image.setVisibility(View.GONE);
            Vholder.favButton.setVisibility(View.VISIBLE);
            //JSONArray other_image = result.getJSONArray("myother_img");
            //String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+result.getString("image");
            String ImageHol = mValues.get(position).getFimage_path();
            //mValues.get(position).getFimage_path()
            Vholder.imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            Vholder.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            Vholder.imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
            Glide.with(mContext).load(R.drawable.product_icon).into(Vholder.imageView_icon);
            DefaultSliderView sliderView1 = new DefaultSliderView(mContext);
            sliderView1.setImageUrl(ImageHol);
            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("setDescription " + (i + 1));
            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Intent intent = new Intent(mContext, DemandDetailActivity.class);
                    //intent.putExtra("id",String.valueOf(item.getPid()));
                    intent.putExtra("pid", String.valueOf(mValues.get(position).getShareId()));
                    intent.putExtra("uid", String.valueOf(mValues.get(position).getUid()));
                    mContext.startActivity(intent);
                }
            });

            //at last add this view in your layout :
            Vholder.imageSlider.addSliderView(sliderView1);
            if (mValues.get(position).getImageArray().size() > 0) {
                for (int i = 0; i < mValues.get(position).getImageArray().size(); i++) {
                    DefaultSliderView sliderView = new DefaultSliderView(mContext);
                    sliderView.setImageUrl(Config.URL_ROOT + "uploads/product/w/500/" + mValues.get(position).getImageArray().get(i));
                    Log.d(Config.TAG, Config.URL_ROOT + "uploads/product/w/500/" + mValues.get(position).getImageArray().get(i));
                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                    //sliderView.setDescription("setDescription " + (i + 1));
                    final int finalI = i;
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                            Intent intent = new Intent(mContext, DemandDetailActivity.class);
                            //intent.putExtra("id",String.valueOf(item.getPid()));
                            intent.putExtra("pid", String.valueOf(mValues.get(position).getShareId()));
                            intent.putExtra("uid", String.valueOf(mValues.get(position).getUid()));
                            mContext.startActivity(intent);
                        }
                    });
                    //at last add this view in your layout :
                    Vholder.imageSlider.addSliderView(sliderView);
                }
            }
            Glide.with(mContext).load(R.drawable.demand_icon).into(Vholder.imageView_icon);
        }
        Vholder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(mContext, "" + ratingBar.getRating(), Toast.LENGTH_LONG).show();
                float ratingb = ratingBar.getRating();
                //sendrating(ratingb, uid, id, type, sharedId);
                String url=null;
                if(type.equalsIgnoreCase("image")) {
                    if (animalsArray.length > 1) {
                        url = Config.API_URL + "app_service.php?type=rate_me&id=" + String.valueOf(id) + "&uid=" + uid + "&ptype=feed&total_rate="+rating;
                    } else {
                        url = Config.API_URL + "app_service.php?type=rate_me&id=" + String.valueOf(animalsArray[0]) + "&uid=" + uid + "&ptype=" + type+"&total_rate="+rating;
                    }
                }else{
                    url = Config.API_URL + "app_service.php?type=rate_me&id=" + String.valueOf(animalsArray[0]) + "&uid=" + uid + "&ptype=" + type+"&total_rate="+rating;
                }
                Log.e(Config.TAG, url);
                function.executeUrl(mContext, "get", url, null);
            }
        });
        Vholder.like_un.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike = Integer.parseInt(Vholder.tv_totallike.getText().toString()) + 1;
                Vholder.tv_totallike.setTextColor(Color.RED);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url=null;
                if(type.equalsIgnoreCase("image")) {
                    if (animalsArray.length > 1) {
                        url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(id) + "&uid=" + uid + "&ptype=feed";
                    } else {
                        url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(animalsArray[0]) + "&uid=" + uid + "&ptype=" + type;
                    }
                }else{
                    url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(animalsArray[0]) + "&uid=" + uid + "&ptype=" + type;
                }
                Log.e(Config.TAG, url);
                function.executeUrl(mContext, "get", url, null);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(Vholder.tv_totallike.getText().toString()) - 1;
                Vholder.tv_totallike.setTextColor(Color.BLACK);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url=null;
                if(type.equalsIgnoreCase("image")) {
                    if (animalsArray.length > 1) {
                        url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(id) + "&uid=" + uid + "&ptype=feed";
                    } else {
                        url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(animalsArray[0]) + "&uid=" + uid + "&ptype=" + type;
                    }
                }else{
                    url = Config.API_URL + "app_service.php?type=like_me&id=" + String.valueOf(animalsArray[0]) + "&uid=" + uid + "&ptype=" + type;
                }
                Log.e(Config.TAG, url);
                function.executeUrl(mContext, "get", url, null);
            }
        });

        //
        Vholder.favButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String url = Config.API_URL + "app_service.php?type=provide_favo&pid=" + String.valueOf(id) + "&uid=" + uid + "&product_type=" + type;
                Log.e(Config.TAG, url);
                function.executeUrl(mContext, "get", url, null);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String url = Config.API_URL + "app_service.php?type=provide_favo&pid=" + String.valueOf(id) + "&uid=" + uid + "&product_type=" + type;
                Log.e(Config.TAG, url);
                function.executeUrl(mContext, "get", url, null);
            }
        });
        //Vholder.c
         //  if (PrefManager.isLogin(mContext)) {
             Vholder.ll_comment.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (type.equalsIgnoreCase("IMAGE")) {
                         Intent intent = new Intent(mContext, CommentActivity.class);
                         intent.putExtra("type", "feed_image");
                         intent.putExtra("id", String.valueOf(id));
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         mContext.startActivity(intent);
                     } else if (type.equalsIgnoreCase("VIDEO")) {
                         Intent intent = new Intent(mContext, CommentActivity.class);
                         intent.putExtra("type", "video");
                         intent.putExtra("id", String.valueOf(sharedId[0]));
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         mContext.startActivity(intent);
                     } else if (type.equalsIgnoreCase("PRODUCT")) {
                         Intent intent = new Intent(mContext, CommentActivity.class);
                         intent.putExtra("type", "product");
                         intent.putExtra("id", String.valueOf(sharedId[0]));
                         intent.putExtra("pid", String.valueOf(sharedId[0]));
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         mContext.startActivity(intent);
                         // Toast.makeText(mContext,"Product"+id,Toast.LENGTH_LONG).show();
                     } else if (type.equalsIgnoreCase("PROVIDE")) {
                         Intent intent = new Intent(mContext, CommentActivity.class);
                         intent.putExtra("type", "provide");
                         intent.putExtra("id", String.valueOf(sharedId[0]));
                         intent.putExtra("pid", String.valueOf(sharedId[0]));
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         mContext.startActivity(intent);
                         //Toast.makeText(mContext,"Provide"+id,Toast.LENGTH_LONG).show();
                     } else if (type.equalsIgnoreCase("DEMAND")) {
                         Intent intent = new Intent(mContext, CommentActivity.class);
                         intent.putExtra("type", "demand");
                         intent.putExtra("id", String.valueOf(sharedId[0]));
                         intent.putExtra("pid", String.valueOf(sharedId[0]));
                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         mContext.startActivity(intent);
                         //Toast.makeText(mContext,"Demand"+id,Toast.LENGTH_LONG).show();
                     }
                 }
             });
        /* }
         else{
               //Toast.makeText(mContext,"First Login and try again...",Toast.LENGTH_LONG).show();
         } */
         if (PrefManager.isLogin(mContext) && PrefManager.getLoginDetail(mContext,"id").equalsIgnoreCase(String.valueOf(mValues.get(position).getUid()))){
            //Vholder.
             Vholder.like_un.setEnabled(true);
             Vholder.ratingBar.setFocusable(false);
             if (type.equalsIgnoreCase("product")){
                 Vholder.iv_buy.setEnabled(true);
             }
             else if (type.equalsIgnoreCase("product") || type.equalsIgnoreCase("provide")){
                 Vholder.favButton.setEnabled(true);
             }
         }
         else{
             if (type.equalsIgnoreCase("product")){
               Vholder.iv_buy.setEnabled(false);
             }
             else if (type.equalsIgnoreCase("provide") || type.equalsIgnoreCase("demand")){
                  Vholder.favButton.setEnabled(false);
             }
             Vholder.like_un.setEnabled(false);
             Vholder.ratingBar.setFocusable(true);
         }
         /*Vholder.video_imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         }); */
        if (PrefManager.isLogin(mContext)) {
           Vholder.like_un.setEnabled(true);
           Vholder.ratingBar.setFocusable(true);
            Vholder.ratingBar.setIsIndicator(false);
            //holder.ratingBar.setClickable(true);
        }
        else {
           Vholder.like_un.setEnabled(false);
            Vholder.ratingBar.setFocusable(false);
            Vholder.ratingBar.setIsIndicator(true);
            //holder.ratingBar.setClickable(false);

        }
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(FeedModel item);
    }
}

