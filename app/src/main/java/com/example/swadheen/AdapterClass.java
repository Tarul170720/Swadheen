package com.example.swadheen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    ArrayList<Userdetails> list;
    private Context mContext;

    public AdapterClass(Context mContext,ArrayList<Userdetails> list){
        this.list = list;
        this.mContext=mContext;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setProductTitle(list.get(position).getServicename(),position);
        String photo=list.get(position).getPhoto();
        Log.i("phoyo","-----------------------------------------------"+photo);

        Glide.with(mContext).load(photo).into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdealid, txtdescription;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            txtdealid = itemView.findViewById(R.id.txtdealid);
            txtdescription = itemView.findViewById(R.id.txtdescription);
            imageView=itemView.findViewById(R.id.image);

        }
        private void setProductTitle(final String title, final int position){
            txtdealid.setText(title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent categoryIntent = new Intent(itemView.getContext(), WorkerAtNearestLocation.class);
                    categoryIntent.putExtra("CategoryName",title);
                    itemView.getContext().startActivity(categoryIntent);
                }
            });

        }
}}