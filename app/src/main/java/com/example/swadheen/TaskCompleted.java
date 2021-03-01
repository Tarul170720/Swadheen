   package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskCompleted extends AppCompatActivity {
    RecyclerView rcv;
    List<HistoryModel> testModelList = new ArrayList<>();
    FirebaseAuth mAuth;
    DatabaseReference mHistroyDatabase;
    String userId="";
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_completed);
        rcv=(RecyclerView)findViewById(R.id.rcl);
        rcv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(TaskCompleted.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv.setLayoutManager(layoutManager);
        toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        Toast.makeText(getApplicationContext(),"Home Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_profile:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(TaskCompleted.this,DriverProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_completed:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent1 = new Intent(TaskCompleted.this,TaskCompleted.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.menu_pending:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(TaskCompleted.this,QuestionAnswer.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.menu_out:
                        System.exit(0);
                        break;
                    case R.id.menu_call:
                        Toast.makeText(getApplicationContext(),"Call Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent inten3 = new Intent(TaskCompleted.this,WorkerSideSubCategory.class);
                        startActivity(inten3);
                        finish();

                        break;
                    case R.id.menu_settings:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent inten4 = new Intent(TaskCompleted.this,Payment.class);
                        startActivity(inten4);
                        finish();


                        break;
                }
                return true;
            }
        });


        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        try {
            Log.i("ok","---------------------------Done---------------------"+userId+"----------");
            mHistroyDatabase = FirebaseDatabase.getInstance().getReference().child("Histroy").child("Worker").child(userId);
            getHistory();
        }
        catch (Exception e){
            Log.i("error","--------------------No history found--------------");
            Toast.makeText(this,"" +e,Toast.LENGTH_SHORT).show();

        }
    }
    String n;
    String t;

    public void getHistory(){
        mHistroyDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Yes","--------------------Got inside--------------");

                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                        int counter = 0;
                        while (items.hasNext()) {
                            DataSnapshot item = items.next();
                            Log.i("Result", "key" + item.getKey());
                            String value=item.getValue().toString();

                            Map<String,Object> map=(Map<String, Object>)item.getValue();
                            String p="Worker type";
                            String q="Worker id";

                            if(map.get("Work type")!=null)
                            {
                                p=map.get("Work type").toString();

                            }
                            if(map.get("Worker Id")!=null) {
                                q = map.get("Worker Id").toString();
                            }

                            DatabaseReference Name = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(q);
                            Name.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if(snapshot!=null){

                                       n="Name";
                                       Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                                       if(map.get("Name")!=null)
                                       {
                                           n=map.get("Name").toString();

                                       }
                                   }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            DatabaseReference Imageref = FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(q);

                            Imageref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot!=null){
                                        t="";
                                        Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                                        if(map.get("Profile Image")!=null)
                                        {
                                            t=map.get("Profile Image").toString();


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                            testModelList.add(new HistoryModel(item.getKey(), p,n,t));
                            HistoryAdapter testAdapter = new HistoryAdapter(TaskCompleted.this,testModelList);
                            rcv.setAdapter(testAdapter);
                            testAdapter.notifyDataSetChanged();


                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}