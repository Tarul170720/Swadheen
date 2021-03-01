package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class WorkerSideSubCategory extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<SubCategoryModel> list;
    RecyclerView rcv;
    private FirebaseAuth mAuth;
    private DatabaseReference mWorkerDatabase;
    private String userId="",Job="";

    private Button button;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_side_sub_category);
        ref = FirebaseDatabase.getInstance().getReference().child("Works");

        button=(Button)findViewById(R.id.Done);
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(userId);
        getJob();
        rcv=(RecyclerView)findViewById(R.id.rcl);
        rcv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv.setLayoutManager(layoutManager);
        ref = FirebaseDatabase.getInstance().getReference("Works/Plumber/SubCategory/ActivityTasks");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();


            }
        });


        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                int i=0;
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        Log.i("Got in","-------------------------------------------------------------");
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            list.add(ds.getValue(SubCategoryModel.class));

                        }
                        SubCategoryAdapter adapterClass = new SubCategoryAdapter(WorkerSideSubCategory.this,list);
                        rcv.setAdapter(adapterClass);
                        adapterClass.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error ) {
                    Toast.makeText(WorkerSideSubCategory.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }






    }
    private void mainIntent() {
        Intent mainIntent = new Intent(WorkerSideSubCategory.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }



        private void getJob(){
        mWorkerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();

                    if(map.get("Job_type")!=null) {
                        Log.i("Got in job","------------------------------------------------------------------"+Job);
                        Job= map.get("Job_type").toString();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}