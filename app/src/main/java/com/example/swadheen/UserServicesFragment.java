package com.example.swadheen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserServicesFragment extends Fragment {


        DatabaseReference ref;
        ArrayList<Userdetails> list;
        RecyclerView recyclerView;
        SearchView searchView;


        public UserServicesFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_services, container, false);

        //////Bottom Navigation View
        bottomNavigationView = view.findViewById(R.id.user_bottom_navigation_view_services);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_main_framelayout, this).commit();
        //////Bottom Navigation View
        ref = FirebaseDatabase.getInstance().getReference().child("Works");
        recyclerView = view.findViewById(R.id.recycler_view);
        searchView = view.findViewById(R.id.search_view);
        return view;
    }
    public void onStart() {
        super.onStart();
        if(ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                int i=0;
                @Override

                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            list.add(ds.getValue(Userdetails.class));

                        }
                        AdapterClass adapterClass = new AdapterClass(getContext(),list);
                        recyclerView.setAdapter(adapterClass);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error ) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<Userdetails> mylist = new ArrayList<>();
        for (Userdetails object : mylist)
        {           // here getserviceworker is used for description -> serviceworker in the Userdetails.java

            if(object.getServicename().toLowerCase().contains(str.toLowerCase()))
            {
                mylist.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(getContext(),mylist);
        recyclerView.setAdapter(adapterClass);
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