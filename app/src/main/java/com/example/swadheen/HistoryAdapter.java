package com.example.swadheen;

import android.content.Context;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryModel> testModelList;
    private Context mContext;

    public HistoryAdapter(Context mContext,List<HistoryModel> rewardModelList) {
        this.testModelList = rewardModelList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row2, viewGroup, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewholder, int position) {
        String name = testModelList.get(position).getFullName();
        String number = testModelList.get(position).getDate_of_work();
        String workType=testModelList.get(position).getWorkerType();
        String photo=testModelList.get(position).getPhoto();

        viewholder.setData(name, number,workType,photo);
    }

    @Override
    public int getItemCount() {
        return testModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private TextView fullName;
        private TextView DateOfWork;
        private TextView WorkerType;
        private ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.item1);
            DateOfWork = itemView.findViewById(R.id.item2);
            WorkerType=itemView.findViewById(R.id.item3);
            imageView=itemView.findViewById(R.id.image);

        }

        private void setData(final String name, final String number,final String workerType,String photo){
            fullName.setText(name);
            DateOfWork.setText(number);
            WorkerType.setText(workerType);
            try {
                Picasso.get().load(photo).centerCrop().fit()
                        .into(imageView);
            }
            catch (Exception e){
                Log.i("Error","------------------"+e);
            }

        }


    }

}
