package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageActivity extends AppCompatActivity {

    Handler uiHandler;

    //Initialize Bottom NavBar
    BottomNavigationView bottomNavigationView;

    //Initialize the bottom navbar fragments
    HomeFragment homeFragment = new HomeFragment();
    LinkUpFragment linkUpFragment = new LinkUpFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        uiHandler = new Handler(Looper.getMainLooper());

        //Initialise views
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();

        //Set onNavigationItemSelectedListener for bottom navigation created using Material Library
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Using if - else to set different navigation item functionalities
                if (item.getItemId() == R.id.homePage){
                    //What happens after someone selects homePage item
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.linkupPage){
                    //What happens after someone selects linkUpPage item
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, linkUpFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profilePage){
                    //What happens after someone selects profilePage item
                    //Update user profile data from firestore
                    profileFragment.updateData();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.settingsPage){
                    //What happens after someone selects settingsPage item
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, settingsFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}