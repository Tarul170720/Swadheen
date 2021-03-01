package com.example.swadheen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class History_bookings extends Fragment {

    RecyclerView rcv;
    List<HistoryModel> testModelList = new ArrayList<>();
    FirebaseAuth mAuth;
    DatabaseReference mHistroyDatabase;
    String userId="";
    public History_bookings() {
        // Required empty public constructor
    }

    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_bookings, container, false);


        rcv=view.findViewById(R.id.rcl);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        try {
            Log.i("ok","---------------------------Done---------------------"+userId+"----------");
            mHistroyDatabase = FirebaseDatabase.getInstance().getReference().child("Histroy").child("Costumer").child(userId);
            getHistory();
        }
        catch (Exception e){
            Log.i("error","--------------------No history found--------------");
            Toast.makeText(getActivity(),"" +e,Toast.LENGTH_SHORT).show();

        }

        return view;
    }
    String name;
    String image;
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

                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("User_Info").child("Workers").child(q).child("ProfileImage");
                    DatabaseReference mref1 = FirebaseDatabase.getInstance().getReference().child("Users").child("Workers").child(q).child("Name");
                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                if(snapshot.getValue()!=null){
                                    name=snapshot.getValue().toString();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    mref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                if(snapshot.getValue()!=null){
                                    image =snapshot.getValue().toString();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    testModelList.add(new HistoryModel(item.getKey(), p,name,image));
                    HistoryAdapter testAdapter = new HistoryAdapter(getContext(),testModelList);
                    rcv.setAdapter(testAdapter);
                    testAdapter.notifyDataSetChanged();


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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