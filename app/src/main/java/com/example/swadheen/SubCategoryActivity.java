package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubCategoryActivity extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<Sub_Category_Model1> list;
    RecyclerView rcv;
    private FirebaseAuth mAuth;
    private DatabaseReference mWorkerDatabase;
    private String userId="",Price="",subCat="",desc="",photo="",workerId="",workerJob="";

    private Button button;
    ArrayList<String> prices=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ref = FirebaseDatabase.getInstance().getReference().child("Works");

        button=(Button)findViewById(R.id.Done);
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        Intent intent=getIntent();
        workerId=intent.getStringExtra("Worker_key");
        workerJob=intent.getStringExtra("Woker_Type");
        Log.i("WorkerId","--------------------------------"+workerId);


        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(workerId).child("Sub Category");
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

                            Map<String,Object> map=(Map<String, Object>)ds.getValue();

                            if(map.get("subCategoryName")!=null) {
                                subCat= map.get("subCategoryName").toString();
                            }
                            if(map.get("Description")!=null){
                                desc=map.get("Description").toString();
                            }
                            if(map.get("Photo")!=null){
                                photo=map.get("Photo").toString();
                            }
                            Price=prices.get(i);
                            i++;


                            list.add(new Sub_Category_Model1(subCat,desc,photo,Price,workerId));

                        }
                        Sub_Category_Adapter adapterClass = new Sub_Category_Adapter(SubCategoryActivity.this,list);
                        rcv.setAdapter(adapterClass);
                        adapterClass.notifyDataSetChanged();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error ) {
                    Toast.makeText(SubCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }






    }
    private void mainIntent() {
        Intent mainIntent = new Intent(SubCategoryActivity.this,NearbyWorkerProfile.class);
        mainIntent.putExtra("Worker_key",workerId);
        mainIntent.putExtra("Woker_Type",workerJob);
        startActivity(mainIntent);

        finish();
    }



    private void getJob(){
        mWorkerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();

                    for (Map.Entry<String,Object> entry : map.entrySet()){
                        System.out.println("Key = " + entry.getKey() +
                                ", Value = " + entry.getValue());
                        prices.add(entry.getValue().toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}