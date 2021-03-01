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

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.Viewholder> {

    private List<SubCategoryModel> subCategoryList;
    private Context mContext;

    public SubCategoryAdapter(Context mContext,List<SubCategoryModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row3, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
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
        private EditText price;
        private TextView Description;
        private Button add;
        private ImageView photo1;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            subCategoryName = itemView.findViewById(R.id.item1);
            price=itemView.findViewById(R.id.price);
            Description=itemView.findViewById(R.id.item2);
            add=itemView.findViewById(R.id.add);
            photo1=itemView.findViewById(R.id.image);

        }

        private void setSubCategoryName(String categoryName,String description,String photo){
            subCategoryName.setText(categoryName);
            Description.setText(description);
            try {


                Glide.with(mContext).load(photo).centerCrop().into(photo1);
            }
            catch (Exception e){
                Log.i("Error","------------------"+e);
            }

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String p=price.getText().toString();
                    FirebaseAuth mAuth;
                    DatabaseReference mWorkerDatabase;
                    mAuth = FirebaseAuth.getInstance();
                    String userId=mAuth.getCurrentUser().getUid();
                    mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(userId).child("Sub Category");
                    Map userdata = new HashMap<>();
                    userdata.put(categoryName,p);
                    mWorkerDatabase.updateChildren(userdata)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
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
