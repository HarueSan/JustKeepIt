package com.project.harue.projectdd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.harue.projectdd.Model.Contener;
import com.project.harue.projectdd.R;
import com.project.harue.projectdd.SubHomeActivity;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context mContext;
    private List<Contener> mData ;

    public ImageAdapter(Context mContext, List<Contener> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycle_image, parent, false);
        return new ImageAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.MyViewHolder holder, int position) {

        final Contener contener = mData.get(position);

        Glide.with(mContext).load(contener.getImgurl()).into(holder.imageurl);

        holder.imageurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imgurl", contener.getImgurl());
                intent.putExtra("priceid", contener.getPrice_object());
                intent.putExtra("dateid", contener.getDate_object());
                intent.putExtra("curdateid", contener.getCurdate_object());
                intent.putExtra("postid",contener.getPostid());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageurl;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageurl = itemView.findViewById(R.id.imageurl);

        }
    }
}
