package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BookingsPageAdapter extends FragmentStateAdapter {
    public BookingsPageAdapter(@NonNull UserBookingsFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new Back_to_map();
            case 1:
                return new Confirmed_bookings();
            default:
                return new History_bookings();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
