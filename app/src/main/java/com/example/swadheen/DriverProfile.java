package com.example.swadheen;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DriverProfile extends AppCompatActivity {

    ImageView profilePhoto;
    TextView name,adhaar,jobs, job_left,job_done,sQualification,sstream,syear,cQualification,cstream,cyear,email,phone;
    RatingBar ratingBar;
    private String userId,mName,mPhone_Number,mEmail,mjobs,msQualification,msstream,msyear,mcQualification,mcstream,mcyear;
    private FirebaseAuth mAuth;
    private DatabaseReference mWorkerDatabase;
    private DatabaseReference mCostumerDatabase;
    private String mProfieUrl;

    TextView verify;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

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
                        Intent intent = new Intent(DriverProfile.this,DriverProfile.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_completed:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent1 = new Intent(DriverProfile.this,TaskCompleted.class);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.menu_pending:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent2 = new Intent(DriverProfile.this,QuestionAnswer.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.menu_out:
                        System.exit(0);
                        break;
                    case R.id.menu_call:
                        Toast.makeText(getApplicationContext(),"Call Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent inten3 = new Intent(DriverProfile.this,WorkerSideSubCategory.class);
                        startActivity(inten3);
                        finish();

                        break;
                    case R.id.menu_settings:
                        Toast.makeText(getApplicationContext(),"Setting Panel is Open",Toast.LENGTH_LONG).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent inten4 = new Intent( DriverProfile.this,Payment.class);
                        startActivity(inten4);
                        finish();


                        break;
                }
                return true;
            }
        });



        profilePhoto=(ImageView)findViewById(R.id.image);
        name=(TextView) findViewById(R.id.name);
        jobs=(TextView)findViewById(R.id.jobs);
        adhaar=(TextView)findViewById(R.id.adhaar);
        job_left=(TextView)findViewById(R.id.job_left);
        job_done=(TextView)findViewById(R.id.job_done);
        verify=(TextView)findViewById(R.id.verify);

        sQualification=(TextView)findViewById(R.id.squalifactio);
        sstream=(TextView)findViewById(R.id.sstream);
        syear=(TextView)findViewById(R.id.syear);



        cQualification=(TextView)findViewById(R.id.cqualifactio);
        cstream=(TextView)findViewById(R.id.cstream);
        cyear=(TextView)findViewById(R.id.cyear);

        email=(TextView)findViewById(R.id.email);
        phone=(TextView)findViewById(R.id.phone);

        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        mCostumerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(userId);
        mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(userId);



        getUserInfo();
        getMoreUserInfo();
        downloadWithBytes();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DriverProfile.this,Face_detection_and_recogition.class);
                startActivity(intent);
                intent.putExtra("Type","High");
                finish();
                return;


            }
        });
    }
    private void getMoreUserInfo(){
        mWorkerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();

                    if(map.get("sQualification")!=null)
                    {
                        msQualification=map.get("sQualification").toString();
                        sQualification.setText(msQualification);
                    }
                    if(map.get("sstrea")!=null) {
                        msstream= map.get("sstrea").toString();
                        sstream.setText(msstream);
                    }
                    if(map.get("syear")!=null) {
                        msyear = map.get("syear").toString();
                        syear.setText(msyear);
                    }
                    if(map.get("cQualification")!=null) {
                        mcQualification = map.get("cQualification").toString();
                        cQualification.setText(mcQualification);
                    }
                    if(map.get("cstream")!=null) {
                        mcstream = map.get("cstream").toString();
                        cstream.setText(mcstream);
                    }
                    if(map.get("cyear")!=null) {
                        mcyear = map.get("cyear").toString();
                        cyear.setText(mcyear);
                    }
                    if(map.get("Job_type")!=null) {
                        mjobs = map.get("Job_type").toString();
                        jobs.setText(mjobs);
                    }
                    if(map.get("image")!=null) {
                        mProfieUrl = map.get("image").toString();
                        Picasso.get()
                                .load(mProfieUrl)
                                .into(profilePhoto);                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private  void getUserInfo(){
        mCostumerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()&&snapshot.getChildrenCount()>0){
                    Map<String,Object> map=(Map<String, Object>)snapshot.getValue();
                    if(map.get("Name")!=null)
                    {
                        mName=map.get("Name").toString();
                        name.setText(mName);
                    }
                    if(map.get("Mobile Number")!=null) {
                        mPhone_Number = map.get("Mobile Number").toString();
                        phone.setText(mPhone_Number);
                    }
                    if(map.get("Email")!=null) {
                        mEmail = map.get("Email").toString();
                        email.setText(mEmail);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void downloadWithBytes(){
        try{
            StorageReference imageRef1=storageReference.child("Profile_Image/"+userId+"/image");

            imageRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(DriverProfile.this)
                    .load(uri)
                    .error(R.drawable.ic_launcher_background)
                    .into(profilePhoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }catch (Exception e){

        }
    }


}