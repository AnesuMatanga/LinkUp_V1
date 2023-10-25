package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * @navBarViewPageAdapter
 * To manage the Home, LinkUps, Prof and Settings Fragments for each item in
 * bottom navigation bar.
 */
public class navBarViewPagerAdapter extends FragmentStateAdapter {

    //Constructor
    public navBarViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public navBarViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        //Use a switch case to return the different fragments set in homePageActivity
        switch (position) {
            case 0:
                return new HomePageActivity.HomePageFragment();
            case 1:
                return new HomePageActivity.LinkUpPageFragment();
            case 2:
                return new HomePageActivity.ProfilePageFragment();
            default:
                return new HomePageActivity.SettingsPageFragment();
        }
    }

    @Override
    public int getItemCount() {
        //3 tabs in Profile Activity
        return 4;
    }
}