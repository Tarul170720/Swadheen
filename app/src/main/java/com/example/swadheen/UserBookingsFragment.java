package com.example.swadheen;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserBookingsFragment extends Fragment {

    public UserBookingsFragment() {
        // Required empty public constructor
    }

    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_bookings, container, false);

        //////Bottom Navigation View
        bottomNavigationView = view.findViewById(R.id.user_bottom_navigation_view_bookings);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationMethod);
        //////Bottom Navigation View

        //////Setting ViewPager
        ViewPager2 viewPager2 = view.findViewById(R.id.bookings_viewPager);
        viewPager2.setAdapter(new BookingsPageAdapter(this));
        //////Setting ViewPager

        //////Setting TabLayout
        final TabLayout tabLayout = view.findViewById(R.id.bookings_tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position)
                {
                    case 0:
                    {
                        tab.setText("Pending");
                        tab.setIcon(R.drawable.user_bookings_icon);
                        break;
                    }
                    case 1:
                    {
                        tab.setText("Confirmed");
                        tab.setIcon(R.drawable.user_confirmed_booking_icon);
                        break;
                    }
                    case 2:
                    {
                        tab.setText("History");
                        tab.setIcon(R.drawable.user_history_booking_icon);
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();

        //////Setting TabLayout

        return view;
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