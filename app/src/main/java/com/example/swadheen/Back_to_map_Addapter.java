package com.example.swadheen;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Back_to_map_Addapter extends RecyclerView.Adapter<Back_to_map_Addapter.Viewholder> {

    private List<Back_to_map_Model> subCategoryList;
    private Context mContext;

    public Back_to_map_Addapter(Context mContext,List<Back_to_map_Model> subCategoryList) {
        this.subCategoryList = subCategoryList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public Back_to_map_Addapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row6_back_to_map, viewGroup, false);
        return new Back_to_map_Addapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Back_to_map_Addapter.Viewholder viewholder, int position) {
        String categoryName = subCategoryList.get(position).getSubCategoryName();
        String desc=subCategoryList.get(position).getDescription();
        viewholder.setSubCategoryName(categoryName,desc);
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView subCategoryName;

        private TextView Description;
        String workId="";
        String workerType="";
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            subCategoryName = itemView.findViewById(R.id.item1);
            Description=itemView.findViewById(R.id.item2);

            itemView.setClickable(true);
            itemView.setOnClickListener((View.OnClickListener) this);

        }

        private void setSubCategoryName(String categoryName,String description){
            subCategoryName.setText(categoryName);
            Description.setText(description);
            workerType=description;
            workId=categoryName;



        }
        public void onClick(View v) {

            Intent intent=new Intent(v.getContext(),CostumerMapsActivity.class);
            Log.i("WorkerId","Worker---------"+workId);
            intent.putExtra("worker_id",workId);
            intent.putExtra("worker_type",workerType);


            v.getContext().startActivity(intent);
            return;

        }

    }
}
