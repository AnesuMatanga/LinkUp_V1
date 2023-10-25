package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    //Constants for keys to help query the Firestore
    public static final String PROFILE_USERNAME = "username";
    public static final String PROFILE_BIO = "bio";
    public static final String PROFILE_INTERESTS = "interests";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String PROFILE_LOCATION = "location";

    //Create view objects
    TextView pRequestsCountTV, pLinkUpsCountTV, pRequestsTV, pLinkUpsTV,
            pProfUsername, pProfBio, pProfLocation, pProfInterests;
    //For menu ids
    final int homePage = R.id.homePage;
    final int linkUpPage = R.id.linkupPage;
    final int profilePage = R.id.profilePage;
    final int settingsPage = R.id.settingsPage;

    BottomNavigationView bottomNavigationView;
    ImageView pProfilePicIV;
    Uri pProfPicURI;

    //For current User
    FirebaseAuth pAuth;
    FirebaseUser currentUser;

    //Firebase Document Reference
    private DocumentReference pDocRef;

    //Real Time data retrieval to keep profile page always updated using onStart()
  /*  protected void onStart() {
        super.onStart();
        System.out.println("****IN ON START()***");

        //Get document reference for currently signed in user utilising Firebase libraries
        pAuth = FirebaseAuth.getInstance();
        currentUser = pAuth.getCurrentUser();

        System.out.println("****Current User*** : " + currentUser);

        //Initialize doc ref
        pDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());

        //Use 'this' to make sure the listener is not used when needed to avoid over battery usage for user and also save cost
        pDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //First check if document exists before trying to access it
                if (documentSnapshot.exists()) {
                    //Get the document Info and set it in user profile in real time
                    String username = documentSnapshot.getString(PROFILE_USERNAME);
                    String bio = documentSnapshot.getString(PROFILE_BIO);
                    String profile_pic = documentSnapshot.getString(PROFILE_PIC);
                    String interests = documentSnapshot.getString(PROFILE_INTERESTS);
                    String location = documentSnapshot.getString(PROFILE_LOCATION);

                    //Set the username, bio and Location in the TextViews
                    pProfUsername.setText("@" + username);
                    pProfBio.setText(bio);
                    pProfLocation.setText(location);
                    pProfInterests.setText("[ " + interests + " ]");

                    //Get profile image and show it on Profile page in real Time using Glide Library
                    Glide.with(ProfileActivity.this).load(profile_pic).into(pProfilePicIV);
                }
            }
        });

    } */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initialize the view objects using R.id
        pRequestsCountTV = findViewById(R.id.profRequestsCount);
        pLinkUpsCountTV = findViewById(R.id.profLinkupsCount);
        pLinkUpsTV = findViewById(R.id.linkUpsText);
        pRequestsTV = findViewById(R.id.requestsText);
        pProfBio = findViewById(R.id.profBio);
        pProfLocation = findViewById(R.id.profLocation);
        pProfUsername = findViewById(R.id.profUsername);
        pProfilePicIV = findViewById(R.id.profileImageView);
        pProfInterests = findViewById(R.id.profInterests);
        //bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Get Menu ids to use in the onNavigationItemSelected listener

        //Create onSetListeners for the TextViews so when a user clicks they can view their requests
        pLinkUpsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pRequestsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pRequestsCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        pLinkUpsCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Set onNavigationItemSelectedListener for bottom navigation created using Material Library
      /*  bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Using if - else to set different navigation item functionalities
                if (item.getItemId() == R.id.homePage){
                    //What happens after someone selects homePage item
                    return true;
                } else if (item.getItemId() == R.id.linkupPage){
                    //What happens after someone selects linkUpPage item
                    return true;
                } else if (item.getItemId() == R.id.profilePage){
                    //What happens after someone selects profilePage item
                    return true;
                } else if (item.getItemId() == R.id.settingsPage){
                    //What happens after someone selects settingsPage item
                    return true;
                }
                return false;
            }
        });*/
    }
}















