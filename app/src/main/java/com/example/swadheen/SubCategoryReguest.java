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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategoryReguest extends AppCompatActivity {
    TextView name,phone,email,job_type;
    Button done,reject;
    ImageView image;
    RecyclerView rcl;

    ArrayList<AcceptingModel> list=new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mWorkerDatabase,ref;
    private String userId="",Price="",subCat="",desc="",photo="",workerId="",workerJob="";
    String customerId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_reguest);

        name=(TextView)findViewById(R.id.item1);
        phone=(TextView)findViewById(R.id.item2);
        email=(TextView)findViewById(R.id.item3);
        job_type=(TextView)findViewById(R.id.item4);

        done=(Button)findViewById(R.id.Accept);
        reject=(Button)findViewById(R.id.Reject);
        image=(ImageView)findViewById(R.id.image);
        rcl=(RecyclerView)findViewById(R.id.rcl);

        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        getAssignedCustomer();


        rcl.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcl.setLayoutManager(layoutManager);
        ref = FirebaseDatabase.getInstance().getReference().child("Works");


        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(userId).child("costumerRequest");
        mWorkerDatabase.addValueEventListener(new ValueEventListener() {
            int i=0;
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.i("Got in","-------------------------------------------------------------");
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        list.add(ds.getValue(AcceptingModel.class));

                    }
                    AcceptingAdapter adapterClass = new AcceptingAdapter(SubCategoryReguest.this,list);
                    rcl.setAdapter(adapterClass);
                    adapterClass.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error ) {
                Toast.makeText(SubCategoryReguest.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DatabaseReference driverAsking= FirebaseDatabase.getInstance().getReference().child("Asked").child(userId).child("Accepted");
                    driverAsking.setValue("Yes");
                    DatabaseReference complete=FirebaseDatabase.getInstance().getReference("Completed").child(userId);
                    complete.setValue("No");

                    Intent intent=new Intent(SubCategoryReguest.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }
                catch (Exception e){
                    Toast.makeText(SubCategoryReguest.this,""+e,Toast.LENGTH_SHORT).show();
                }




            }
        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DatabaseReference driverRef1=FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(userId).child("costumerRequest");
                    driverRef1.setValue(true);
                    DatabaseReference driverAsking= FirebaseDatabase.getInstance().getReference().child("Asked").child(userId).child("Accepted");
                    driverAsking.setValue("No");
                    DatabaseReference mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(userId).child("costumerRequest");
                    mWorkerDatabase.setValue(true);
                    Intent intent=new Intent(SubCategoryReguest.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }
                catch (Exception e){
                    Toast.makeText(SubCategoryReguest.this,""+e,Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    private void getJob(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();

                    for (Map.Entry<String,Object> entry : map.entrySet()){
                        System.out.println("Key = " + entry.getKey() +
                                ", Value = " + entry.getValue());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void getAssignedCustomer(){
        String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerId=FirebaseDatabase.getInstance().getReference().child("Users/Workers").child(driverId).child("costumerRequest/costumerRideId");
        assignedCustomerId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    customerId=snapshot.getValue().toString();

                    getCustomerInfo();
                }else{

                    customerId="";
                    phone.setText("");
                    name.setText("");
                    email.setText("");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getCustomerInfo(){
        DatabaseReference mCostumerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Costumers").child(customerId);

        mCostumerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                    if(map.get("Name")!=null)
                    {
                        name.setText(map.get("Name").toString());
                    }
                    if(map.get("Mobile Number")!=null) {

                        phone.setText(map.get("Mobile Number").toString());
                    }
                    if(map.get("Email")!=null) {

                        email.setText(map.get("Email").toString());
                    }
                    getMoreInfo();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void getMoreInfo(){
        DatabaseReference mCostumerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Costumers").child(customerId);

        mCostumerDatabase.child("Job").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                    if(map.get("Entrepreneur")!=null)
                    {
                        job_type.setText(map.get("Entrepreneur").toString());
                    }
                    if(map.get("image")!=null) {

                        String url=map.get("Mobile Number").toString();
                        Picasso.get()
                                .load(url)
                                .into(image);

                }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}