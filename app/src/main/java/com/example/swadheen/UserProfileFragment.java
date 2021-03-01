package com.example.swadheen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserProfileFragment extends Fragment {

    ImageView profilePhoto;
    TextView name,adhaar,jobs, job_left,job_done,sQualification,sstream,syear,cQualification,cstream,cyear,email,phone;
    RatingBar ratingBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mCostumerDatabase;
    private String mProfieUrl;
    private String userId,mName,mPhone_Number,mEmail;
    TextView verify;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        //////Bottom Navigation View
        bottomNavigationView = view.findViewById(R.id.user_bottom_navigation_view_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_main_framelayout, this).commit();
        //////Bottom Navigation View
        profilePhoto=view.findViewById(R.id.image);
        name= view.findViewById(R.id.name);
        jobs=view.findViewById(R.id.jobs);
        adhaar=view.findViewById(R.id.adhaar);
        job_left=view.findViewById(R.id.job_left);
        job_done=view.findViewById(R.id.job_done);
        verify=view.findViewById(R.id.verify);

        sQualification=view.findViewById(R.id.squalifactio);
        sstream=view.findViewById(R.id.sstream);
        syear=view.findViewById(R.id.syear);



        cQualification=view.findViewById(R.id.cqualifactio);
        cstream=view.findViewById(R.id.cstream);
        cyear=view.findViewById(R.id.cyear);

        email=view.findViewById(R.id.email);
        phone=view.findViewById(R.id.phone);

        ratingBar=view.findViewById(R.id.ratingBar);
        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        mCostumerDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child("Costumers").child(userId);
        getUserInfo();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

            }
        });


        return view;
    }
    private void getUserInfo(){
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
                        phone.setText(mPhone_Number);
                    }
                    if(map.get("profileImageUri")!=null) {
                        mProfieUrl = map.get("profileImageUri").toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment=null;

            switch (menuItem.getItemId())
            {
                case R.id.user_home:
                    fragment = new UserHomeFragment();
                    break;

                case R.id.user_services:
                    fragment = new UserServicesFragment();
                    break;

                case R.id.user_bookings:
                    fragment = new UserBookingsFragment();
                    break;

                case R.id.user_profile:
                    fragment = new UserProfileFragment();
                    break;
            }
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_main_framelayout, fragment).commit();

            return true;
        }
    };


}