package com.mssinfotech.iampro.co.adapter;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.app.AppController;
import com.mssinfotech.iampro.co.data.FeedItem;
import com.mssinfotech.iampro.co.listviewfeed.FeedImageView;

import java.util.List;

public class FeedListAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private List<FeedItem> feedItems;
    private Activity activity;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private LayoutInflater inflater;



    public FeedListAdapter(List<FeedItem> flist, RecyclerView recyclerView) {
        feedItems = flist;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return feedItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.feed_item, parent, false);

            vh = new FeedViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedViewHolder) {


            FeedItem item = feedItems.get(position);

            ((FeedViewHolder) holder).name.setText(item.getName());
            ((FeedViewHolder) holder).timestamp.setText(item.getTimeStamp());
            ((FeedViewHolder) holder).txtStatusMsg.setText(item.getStatus());



            // user profile pic
            ((FeedViewHolder) holder).profilePic.setImageUrl(item.getProfilePic(), imageLoader);

            // Feed image
            if (item.getImge() != null) {
                ((FeedViewHolder) holder).feedImage1.setImageUrl(item.getImge(), imageLoader);
                ((FeedViewHolder) holder).feedImage1.setVisibility(View.VISIBLE);
            } else {
                ((FeedViewHolder) holder).feedImage1.setVisibility(View.GONE);
            }

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class FeedViewHolder extends RecyclerView.ViewHolder {

        public TextView name,timestamp,txtStatusMsg,txtUrl;
        public FeedItem feedItem;
        NetworkImageView profilePic;
        FeedImageView feedImage1;
        public FeedViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            timestamp = v.findViewById(R.id.timestamp);
            txtStatusMsg = v.findViewById(R.id.txtStatusMsg);
            //txtUrl = v.findViewById(R.id.txtUrl);
            profilePic = v.findViewById(R.id.profilePic);
            feedImage1 = v.findViewById(R.id.feedImage1);

            v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + feedItem.getName() + " \n "+feedItem.getId(),  Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}