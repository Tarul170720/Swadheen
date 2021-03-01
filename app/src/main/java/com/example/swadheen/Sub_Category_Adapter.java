package com.example.swadheen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class
Sub_Category_Adapter extends RecyclerView.Adapter<Sub_Category_Adapter.Viewholder> {

    private List<Sub_Category_Model1> subCategoryList;
    private Context mContext;

    public Sub_Category_Adapter(Context mContext,List<Sub_Category_Model1> subCategoryList) {
        this.subCategoryList = subCategoryList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public Sub_Category_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row4, viewGroup, false);
        return new Sub_Category_Adapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Sub_Category_Adapter.Viewholder viewholder, int position) {
        String categoryName = subCategoryList.get(position).getSubCategoryName();
        String desc=subCategoryList.get(position).getDescription();
        String photo=subCategoryList.get(position).getPhoto();
        String price=subCategoryList.get(position).getPrice();
        String workerID=subCategoryList.get(position).getCostumerId();
        viewholder.setSubCategoryName(categoryName,desc,photo,price,workerID);
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
            priced=itemView.findViewById(R.id.item3);
            Description=itemView.findViewById(R.id.item2);
            add=itemView.findViewById(R.id.add);
            photo1=itemView.findViewById(R.id.image);

        }

        private void setSubCategoryName(String categoryName,String description,String photo,String price,String WorkerId){
            subCategoryName.setText(categoryName);
            Description.setText(description);
            priced.setText(price);
            try {


                Glide.with(mContext).load(photo).centerCrop().into(photo1);
            }
            catch (Exception e){
                Log.i("Error","------------------"+e);
            }

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mWorkerDatabase;
                    mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(WorkerId).child("costumerRequest").child(categoryName);
                    Map userdata = new HashMap<>();
                    userdata.put("subCategoryName",categoryName);
                    userdata.put("Description",description);
                    userdata.put("Photo",photo);

                    mWorkerDatabase.updateChildren(userdata)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
                                        add.setEnabled(false);
                                    }
                                    else {

                                        String error = task.getException().getMessage();
                                        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            });

        }

    }
}

