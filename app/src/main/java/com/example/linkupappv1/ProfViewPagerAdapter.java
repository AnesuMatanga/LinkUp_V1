package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

/**
 * @ProfViewPageAdapter
 * To manage the Quests, LinkUps, and UpTos Fragments for each tab in ProfileActivity
 */
public class ProfViewPagerAdapter extends FragmentStateAdapter {

    //Constructor
    public ProfViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        //Use a switch case
        switch (position) {
            case 0:
                return new ProfileActivity.QuestFragment();
            case 1:
                return new ProfileActivity.LinkUpsFragment();
            default:
                return new ProfileActivity.UpTosFragment();
        }
    }

    @Override
    public int getItemCount() {
        //3 tabs in Profile Activity
        return 3;
    }
}






















