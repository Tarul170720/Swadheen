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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

public class NearbyworkerAdaptere extends RecyclerView.Adapter<NearbyworkerAdaptere.ViewHolder> {
    private List<NearbyworkerModel> testModelList;
    private Context mContext;

    public NearbyworkerAdaptere(Context mContext,List<NearbyworkerModel> rewardModelList) {
        this.testModelList = rewardModelList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        String name = testModelList.get(position).getFullName();
        String number = testModelList.get(position).getMobileNumber();
        String workType=testModelList.get(position).getWorkerType();
        String Image=testModelList.get(position).getWorkerType();
        viewholder.setData(name, number,workType,Image);
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fullName;
        private TextView mobileNumber;
        private String workerType;
        private String workId;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.Name);
            mobileNumber = itemView.findViewById(R.id.Number);
            image=itemView.findViewById(R.id.profile);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        private void setData(final String name, final String number,final String workerType,final String i) {
            fullName.setText(name);
            workId=number;
            mobileNumber.setText(number);
            this.workerType = workerType;
            try{
                Picasso.get()
                        .load(i).fit().centerCrop()
                        .into(image);
                }
            catch (Exception e){

            }


        }
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(v.getContext(),SubCategoryActivity.class);
            Log.i("WorkerId","Worker---------"+workId);
            intent.putExtra("Worker_key",workId);
            intent.putExtra("Woker_Type",workerType);
            v.getContext().startActivity(intent);
            return;

        }
    }
}
