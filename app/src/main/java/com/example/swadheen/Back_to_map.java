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


public class Back_to_map extends Fragment {
    RecyclerView rcv;
    List<Back_to_map_Model> testModelList = new ArrayList<Back_to_map_Model>();
    FirebaseAuth mAuth;
    DatabaseReference mHistroyDatabase;
    String userId="";
    public Back_to_map() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_back_to_map, container, false);


        rcv=view.findViewById(R.id.rcl);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();
        try {
            Log.i("ok","---------------------------Done---------------------"+userId+"----------");
            mHistroyDatabase = FirebaseDatabase.getInstance().getReference("request").child(userId).child("Worker_Selected");
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
                    String worker_Id=item.getKey();
                    String worker_type=item.getValue().toString();

                    testModelList.add(new Back_to_map_Model(worker_Id, worker_Id));
                    Back_to_map_Addapter testAdapter = new Back_to_map_Addapter(getContext(),testModelList);
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