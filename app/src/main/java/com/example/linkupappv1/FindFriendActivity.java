package com.example.linkupappv1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FindFriendActivity extends AppCompatActivity {

    //Declare viewPager2 (Used viewPager2 for its numerous benefits with helping make this
    //page(activity) interactive and provide good user interface
    private ViewPager2 viewPager;
    //Initialise Firestore database

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;

    FirebaseAuth auth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);

        //Initialize views
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Get currentUser to put in the adapter constructor
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        //Fetch friend recommendations from firestore and set the adapter
        db.collection("friendRecommendations").get()
                .addOnCompleteListener(task -> {
                    //Check if task is successful
                    if(task.isSuccessful()) {
                        //Initialise new List of friendRecommendations
                        List<FriendRecommendation> friendRecommendationList = new ArrayList<>();
                        //Loop through data and add to List
                        for (QueryDocumentSnapshot doc :task.getResult()){
                            //Create a FriendReco instance and get data from firestore
                            FriendRecommendation friendRecommendation = doc.toObject(FriendRecommendation.class);
                            //Add to list
                            friendRecommendationList.add(friendRecommendation);
                        }

                        //Set the adapter instance and put the list and currentUser
                        FriendRecommendationAdapter friendRecommendationAdapter = new FriendRecommendationAdapter(friendRecommendationList, currentUser);
                        viewPager.setAdapter(friendRecommendationAdapter);
                    } else {
                        //Error
                        Log.w(TAG, "Error fetching FriendRecommendations from Firestore");
                    }
                });

        //Set onNavigationItemSelectedListener for bottom navigation created using Material Library
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Using if - else to set different navigation item functionalities
                if (item.getItemId() == R.id.homePage){
                    //What happens after someone selects homePage item
                    return true;
                } else if (item.getItemId() == R.id.linkupPage){
                    //What happens after someone selects linkUpPage item
                    startActivity(new Intent(getApplicationContext(), LinkUpActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.profilePage){
                    //What happens after someone selects profilePage item
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.settingsPage){
                    //What happens after someone selects settingsPage item
                    return true;
                }
                return false;
            }
        });
    }


}
















