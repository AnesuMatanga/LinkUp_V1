package com.example.linkupappv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Class To Setup Profile for firstTime Users
 */

public class ProfileSetupActivity extends AppCompatActivity {

    //Set up a Firestore DB to later store user profile details
    FirebaseFirestore db;
    //Get Current Authenticated user
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    //Declare the document to store userData as Firestore cloud saves in docs
    private DocumentReference userProfileDocRef;

    EditText pUsernameET, pBioET, pLocationET;
    Button pProfileSaveBtn;

    //For interests to use in autoCompleteTextView
    AutoCompleteTextView pInterestsET;
    //Adding as many interests that i can think of to try autoComplete for user
    String[] interestsList = {"football", "soccer", "cooking", "braiding", "gaming", "reading",
        "travelling", "hiking", "baking", "Photography", "Traveling", "Cooking", "Blogging", "Gaming", "Hiking", "Reading",
            "Writing", "Painting", "Drawing", "Dancing", "Yoga", "Meditation", "Running", "Swimming", "Cycling", "Gardening",
            "DIY Projects", "Music", "Playing Instruments", "Movies", "Theatre", "Fashion", "Shopping", "Crafts", "Pottery",
            "Bird Watching", "Fishing", "Hunting", "Surfing", "Skating", "Snowboarding", "Skiing", "Camping", "Backpacking",
            "Fitness", "Bodybuilding", "Martial Arts", "Collecting (e.g., stamps, coins, vintage items)", "Wine Tasting", "Brewing", "Knitting",
            "Crocheting", "Scuba Diving", "Rock Climbing", "Astronomy", "Puzzle Solving", "Board Games", "Singing", "Podcasting", "Volunteering",
            "Philanthropy", "Digital Art", "Graphic Design", "Web Development", "Animation", "Basketball", "Soccer", "Tennis", "Golf", "Rugby",
            "Cricket", "Pet Care", "Horse Riding", "Drones", "Robotics", "Programming", "Magic", "Stand-up Comedy", "Investing", "Stock Trading", "Travel Vlogging",
            "Photography Vlogging", "Cooking Vlogging", "Lifestyle Blogging", "Adventure Sports", "Motorcycling", "Car Racing", "Digital Marketing", "Startups",
            "Entrepreneurship", "Creative Writing", "Eco-friendly Living", "Sustainability", "Wildlife Conservation", "Renewable Energy", "Cultural Festivals", "Languages", "History", "Archaeology", "Urban Exploration", "Geocaching",
            "Metal Detecting", "Sculpting", "Museum Visits", "Theme Parks", "Aquariums", "Zoos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        //Initialise
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userProfileDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());

        pUsernameET = findViewById(R.id.profileUsername);
        pBioET = findViewById(R.id.profileStatus);
        pInterestsET = findViewById(R.id.profileInterests);
        pLocationET = findViewById(R.id.profileLocation);
        pProfileSaveBtn = findViewById(R.id.profileSaveBtn);

        //Set array adapter to use with the pInterestsET to populate from the interestsList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, interestsList);
        pInterestsET.setAdapter(adapter);


        //Check if User Not Logged in
        if (currentUser == null){
            //Sent to Login page
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        //Set OnClickListener for Button
        pProfileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });
    }

    //Save Profile Info To Database if save button is clicked
    public void saveProfile() {
        //Get current user Id
        String userID = currentUser.getUid();

        //Get the user inputs and turn into strings
        String username = pUsernameET.getText().toString();
        String bio = pBioET.getText().toString();
        String interests = pInterestsET.getText().toString();
        String location = pLocationET.getText().toString();

        //To validate interest input interest from user
        String[] userInterests = interests.split(",");
        if(userInterests.length > 3){
            Toast.makeText(ProfileSetupActivity.this, "Please enter a maximum of 3 Interests",
                    Toast.LENGTH_SHORT);
            return;
        }

        //Validate the inputs first before saving profile
        if(username.isEmpty()){
            Toast.makeText(ProfileSetupActivity.this, "Please enter a Username", Toast.LENGTH_SHORT);
            return;
        }
        if(location.isEmpty()){
            Toast.makeText(ProfileSetupActivity.this, "Please enter your Location(City)", Toast.LENGTH_SHORT);
            return;
        }
        if(interests.isEmpty()){
            Toast.makeText(ProfileSetupActivity.this, "Please enter an interest", Toast.LENGTH_SHORT);
            return;
        }

        //Create hashmap to save the profile data to save to cloud database
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("username", username);
        dataToSave.put("bio", bio);
        dataToSave.put("interests", interests);
        dataToSave.put("location", location);

        //Now save to the database using the doc we created at the start
        userProfileDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("ProfileData", "Document has been saved");
                Toast.makeText(ProfileSetupActivity.this, "Profile Saved!",
                        Toast.LENGTH_SHORT);

                //Create an Intent and direct user back to their profile page after
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);

                //Finish this activity
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Unable to save Profile", e);
                Toast.makeText(ProfileSetupActivity.this, "Could Not Save Profile!",
                        Toast.LENGTH_SHORT);

            }
        });
    }
}



















