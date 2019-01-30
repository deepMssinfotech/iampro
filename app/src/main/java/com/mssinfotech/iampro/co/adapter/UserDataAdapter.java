package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 18/01/19.
 */

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
import android.content.Context;
import android.widget.TextView;
import com.mssinfotech.iampro.co.model.UserModel;
import java.util.ArrayList;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {
    ArrayList<UserModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    public UserDataAdapter(Context context, ArrayList<UserModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView,tv_category,tv_images,tv_videos,tv_users,tv_products,tv_provides,tv_demands;
        de.hdodenhof.circleimageview.CircleImageView imageView;
        UserModel item;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView =v.findViewById(R.id.imageView);

            tv_category=v.findViewById(R.id.tvcategory);
            tv_images=v.findViewById(R.id.tv_images);
            tv_videos=v.findViewById(R.id.tv_videos);
            tv_users=v.findViewById(R.id.tv_users);
            tv_products=v.findViewById(R.id.tv_products);
            tv_provides=v.findViewById(R.id.tv_provides);
            tv_demands=v.findViewById(R.id.tv_demands);

        }
        public void setData(UserModel item) {
            this.item = item;
            textView.setText(item.getName());
             tv_category.setText(item.getCategory());
            tv_images.setText(item.getTotal_image());
             tv_videos.setText(item.getTotal_video());
              tv_users.setText(item.getTotal_friend());
               tv_products.setText(item.getTotal_product());
               tv_provides.setText(item.getTotal_provide());
                tv_demands.setText(item.getTotal_demand());
            String url=item.getImage();
            //Toast.makeText(mContext,"image:"+url,Toast.LENGTH_LONG).show();

            Glide.with(mContext)
                    .load(url)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .fitCenter())
                    .into(imageView);
            //imageView.setImageResource(item.image);


        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(UserModel item);
    }
}