package com.example.swadheen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcceptingAdapter extends RecyclerView.Adapter<AcceptingAdapter.Viewholder> {

private List<AcceptingModel> subCategoryList;
private Context mContext;

public AcceptingAdapter(Context mContext,List<AcceptingModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
        this.mContext=mContext;
        }

@NonNull
@Override
public AcceptingAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row5, viewGroup, false);
        return new AcceptingAdapter.Viewholder(view);
        }

@Override
public void onBindViewHolder(@NonNull AcceptingAdapter.Viewholder viewholder, int position) {
        String categoryName = subCategoryList.get(position).getSubCategoryName();
        String desc=subCategoryList.get(position).getDescription();
        String photo=subCategoryList.get(position).getPhoto();
        viewholder.setSubCategoryName(categoryName,desc,photo);
        }

@Override
public int getItemCount() {
        return subCategoryList.size();
        }

public class Viewholder extends RecyclerView.ViewHolder{

    private TextView subCategoryName;
    private TextView priced;
    private TextView Description;
    private Button add;
    private ImageView photo1;

    public Viewholder(@NonNull View itemView) {
        super(itemView);
        subCategoryName = itemView.findViewById(R.id.item1);
        Description=itemView.findViewById(R.id.item2);

        photo1=itemView.findViewById(R.id.image);

    }

    private void setSubCategoryName(String categoryName,String description,String photo){
        subCategoryName.setText(categoryName);
        Description.setText(description);

        try {
            Picasso.get()
                    .load(photo).fit().centerCrop()
                    .into(photo1);
        }
        catch (Exception e){
            Log.i("Error","------------------"+e);
        }


    }

}
}

