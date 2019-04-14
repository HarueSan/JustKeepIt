package com.project.harue.projectdd.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.harue.projectdd.Model.Contener;
import com.project.harue.projectdd.R;
import com.project.harue.projectdd.SubHomeActivity;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private List<Contener> mData;

    private String postid;

    public ImageAdapter(Context mContext, List<Contener> mData,Activity mActivity) {
        this.mContext = mContext;
        this.mData = mData;
        this.mActivity = mActivity;
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
        postid = contener.getPostid();

        Glide.with(mContext).load(contener.getImgurl()).into(holder.imageurl);

        holder.imageurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SubHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imgurl", contener.getImgurl());
                intent.putExtra("priceid", contener.getPrice_object());
                intent.putExtra("dateid", contener.getDate_object());
                intent.putExtra("curdateid", contener.getCurdate_object());
                intent.putExtra("postid", contener.getPostid());
                mContext.startActivity(intent);
            }
        });

        holder.imgdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("ลบรูปภาพ")
                        .setMessage("คุณแน่ใจใช่ไหมว่าจะลบ?")
                        .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletepost();
                            }
                        })
                        .setNegativeButton("ยกเลิก", null)
                        .show();
            }


        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageurl;
        ImageView imgdel;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageurl = itemView.findViewById(R.id.imageurl);
            imgdel = itemView.findViewById(R.id.imgdel);

        }
    }

    private void deletepost() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("listchild").child(postid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
