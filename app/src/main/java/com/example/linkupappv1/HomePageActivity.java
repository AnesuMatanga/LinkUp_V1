package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    //ViewPager and Tab view objects
    ViewPager2 hViewPager2;

    //Bottom Navigation using Material Library
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Initialize ViewPager2 to sort the tabs functionality and use adapter
        hViewPager2 = findViewById(R.id.navBarViewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigation);


        //Set ProfViewPagerAdapter
        hViewPager2.setAdapter(new navBarViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));

        //Set onNavigationItemSelectedListener for bottom navigation created using Material Library
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Using if - else to set different navigation item functionalities
                if (item.getItemId() == R.id.homePage){
                    //What happens after someone selects homePage item
                    hViewPager2.setCurrentItem(0, true);
                    return true;
                } else if (item.getItemId() == R.id.linkupPage){
                    //What happens after someone selects linkUpPage item
                    hViewPager2.setCurrentItem(1, true);
                    return true;
                } else if (item.getItemId() == R.id.profilePage){
                    //What happens after someone selects profilePage item
                    hViewPager2.setCurrentItem(2, true);
                    return true;
                } else if (item.getItemId() == R.id.settingsPage){
                    //What happens after someone selects settingsPage item
                    hViewPager2.setCurrentItem(3, true);
                    return true;
                }
                return false;
            }
        });

        //Handling ViewPager2 page changes in order to update selected items in bottom navBar usin a callback onPagehange
        hViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                //super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    /**
     * @Fragments
     * Opted to using more fragments and deleted the Activities in order to reduce the classes
     * but also for the numerous advantages they bring to the table.
     * e.g I noticed that the transition from one bottom navBar item like HomePage item to
     * profile is smoother, less reloading of the app, faster and also less Activity lifecycles
     * being made.
     *
     */
    //HomePage Bottom NavBar Fragment inner class
    public static class HomePageFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public HomePageFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.fragment_home_page, container, false);
        }
    }

    //LinkUps navbar item Fragment inner class (Also avoiding a lot of external classes) better for me to use Fragments
    public static class LinkUpPageFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public LinkUpPageFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.activity_profile, container, false);
        }
    }

    //ProfilePage Fragment inner class
    public static class ProfilePageFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public ProfilePageFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.activity_profile, container, false);


        }
    }

    //ProfilePage Fragment inner class
    public static class SettingsPageFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public SettingsPageFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.activity_profile, container, false);
        }
    }

}