package com.example.swadheen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    public UserHomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView testing;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        //// Bottom Navigation View

        bottomNavigationView = view.findViewById(R.id.user_bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.user_main_framelayout, this).commit();

        //// Bottom Navigation View


        ///////// Banner Slider

        List<SliderModel> sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_home_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_account_circle_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_notifications_24, "#077AE4"));

        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_favorite_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_restore_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.mipmap.ic_launcher_round, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_star_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_account_circle_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_home_24, "#077AE4"));

        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_home_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_notifications_24, "#077AE4"));
        sliderModelList.add(new SliderModel(R.drawable.ic_baseline_favorite_24, "#077AE4"));

        ///////// Banner Slider


        ///////// Horizontal Product Layout

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.architect, "Mason"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.beauty_care, "Beauty Care"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.carpenter, "Carpenter"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.electrician, "Electrician"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.plumber, "Plumber"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.painter, "Painter"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.home_cleaning, "Home Cleaning"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.labour, "Labours"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.mechanic, "Mechanic"));
        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(R.drawable.technician, "Technician"));

        ///////// Horizontal Product Layout


        //////////////////////////////////////

        testing = view.findViewById(R.id.home_page_recyclerview);
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(2,"Services",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3,"Services",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(3,"Services",horizontalProductScrollModelList));
        homePageModelList.add(new HomePageModel(2,"Services",horizontalProductScrollModelList));

        HomePageAdapter adapter = new HomePageAdapter(homePageModelList);
        testing.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //////////////////////////////////////

        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment = null;

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