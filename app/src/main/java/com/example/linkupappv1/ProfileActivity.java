package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
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

    //Query the database only the first time a user accesses app to avoid unnecessary querying using a Flag
    public static boolean hasQueriedFirestore = false;
    public static boolean hasNewMessages = false;
    public static boolean hasQueriedOtherUserFireStore = false;
    // Declare variables to hold strings for views as static to help with the above cause
    private static String username = "";
    private static String bio = "";
    private static String interests = "";
    private static String interests2 = "";
    private static String location = "";
    private static String profile_pic = "";

    private static String otherUsername = "";
    private static String otherBio = "";
    private static String otherInterests = "";

    private static String otherInterests2 = "";
    private static String otherLocation = "";
    private static String otherProfile_pic = "";

    //Constants for keys to help query the Firestore
    public static final String PROFILE_USERNAME = "username";
    public static final String PROFILE_BIO = "bio";
    public static final String PROFILE_INTERESTS = "interests";
    public static final String PROFILE_INTERESTS2 = "interests2";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String PROFILE_LOCATION = "location";

    //Create view objects
    TextView pRequestsCountTV, pLinkUpsCountTV, pRequestsTV, pLinkUpsTV,
            pProfUsername, pProfBio, pProfLocation, pProfInterests, pProfInterests2;
    MaterialTextView profileNewMessageBadge;

    Button pEditProfileBtn, pLinkUpBtn, pMessageBtn;
    Button[] profileButtons = new Button[3];
    //For menu ids
    final int homePage = R.id.homePage;
    final int linkUpPage = R.id.linkupPage;
    final int profilePage = R.id.profilePage;
    final int settingsPage = R.id.settingsPage;

    //From FriendRecommendationAdapter, for intent extras
    String recomUserId;

    BottomNavigationView bottomNavigationView;
    ShapeableImageView pProfilePicIV;
    Uri pProfPicURI;

    //For current User
    FirebaseAuth pAuth;
    FirebaseUser currentUser;

    //Firebase Document Reference
    private DocumentReference pDocRef;

    //Real Time data retrieval to keep profile page always updated using onStart()
    protected void onStart() {
        super.onStart();
        System.out.println("****IN ON START()***");
        Log.d("IN Current HASQUERIED", "IN Current HasQueried: " + hasQueriedFirestore);


        System.out.println("****Current User*** : " + currentUser);
        //Get document reference for currently signed in user utilising Firebase libraries
        pAuth = FirebaseAuth.getInstance();
        currentUser = pAuth.getCurrentUser();

        Log.d("Recommended User Id: ", "B4 RecomUserId: " + recomUserId);


        if (recomUserId != null && (!recomUserId.equals("")) && (!recomUserId.equals(currentUser.getUid()))) {
            //Initialize doc ref
            pDocRef = FirebaseFirestore.getInstance().document("users/" + recomUserId);


            Log.d("Recommended User Id: ", "After RecomUserId: " + recomUserId);

            //Use 'this' to make sure the listener is not used when needed to avoid over battery usage for user and also save cost
            pDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    //First check if document exists before trying to access it
                    if (documentSnapshot.exists()) {
                        //Get the document Info and set it in user profile in real time
                        otherUsername = documentSnapshot.getString(PROFILE_USERNAME);
                        otherBio = documentSnapshot.getString(PROFILE_BIO);
                        otherProfile_pic = documentSnapshot.getString(PROFILE_PIC);
                        otherInterests = documentSnapshot.getString(PROFILE_INTERESTS);
                        otherLocation = documentSnapshot.getString(PROFILE_LOCATION);
                        otherInterests2 = documentSnapshot.getString(PROFILE_INTERESTS2);

                        //Set the username, bio and Location in the TextViews
                        pProfUsername.setText("@" + otherUsername);
                        pProfBio.setText(otherBio);
                        pProfLocation.setText(otherLocation);
                        pProfInterests.setText("[ " + otherInterests + ", " + interests2 + " ]");

                        //Get profile image and show it on Profile page in real Time using Glide Library
                        Glide.with(ProfileActivity.this).load(otherProfile_pic).into(pProfilePicIV);
                    }
                }
            });
        } else {
        //Initialize doc ref
        pDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());
        Log.d("Current User Id: ", "Current UserId: " + currentUser.getUid());


        //Check if its a currentUser Checking out their own profile
        if (!hasQueriedFirestore) {
            hasQueriedFirestore = true;
            //Use 'this' to make sure the listener is not used when needed to avoid over battery usage for user and also save cost
            pDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    //First check if document exists before trying to access it
                    if (documentSnapshot.exists()) {
                        //Get the document Info and set it in user profile in real time
                        username = documentSnapshot.getString(PROFILE_USERNAME);
                        bio = documentSnapshot.getString(PROFILE_BIO);
                        profile_pic = documentSnapshot.getString(PROFILE_PIC);
                        interests = documentSnapshot.getString(PROFILE_INTERESTS);
                        interests2 = documentSnapshot.getString(PROFILE_INTERESTS2);
                        location = documentSnapshot.getString(PROFILE_LOCATION);
                        Log.d("Current HasQUERIED", "Current HasQueried: " + hasQueriedFirestore);

                        //Set the username, bio and Location in the TextViews
                        pProfUsername.setText(username);
                        pProfBio.setText(bio);
                        pProfLocation.setText(location);
                        pProfInterests.setText("[ " + interests + ", " + interests2 + " ]");

                        //Get profile image and show it on Profile page in real Time using Glide Library
                        Glide.with(ProfileActivity.this).load(profile_pic).into(pProfilePicIV);
                    }
                }
            });
        } else {
            //Set the username, bio and Location in the TextViews
            Log.d("ELSE Current HasQUERIED", "ELSE Current HasQueried: " + hasQueriedFirestore);
            pProfUsername.setText(username);
            Log.d("Current Username", "ELSE Current Username: " + username);
            pProfBio.setText(bio);
            pProfLocation.setText(location);
            pProfInterests.setText("[ " + interests + ", " + interests2 + " ]");

            //Get profile image and show it on Profile page in real Time using Glide Library
            Glide.with(ProfileActivity.this).load(profile_pic).into(pProfilePicIV);
        }
    }

}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        pAuth = FirebaseAuth.getInstance();
        currentUser = pAuth.getCurrentUser();

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
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        pEditProfileBtn = findViewById(R.id.editProfile);
        pLinkUpBtn = findViewById(R.id.profileLinkUpBtn);
        pMessageBtn = findViewById(R.id.profileMessageBtn);
        profileNewMessageBadge = findViewById(R.id.profileChatNewMessage);
        //Get recipientUserId(User the message is being sent to) sent through intent as extra to create unique chatId for users
        recomUserId = getIntent().getStringExtra("recomUserId");

        Log.d("Recommended User Id: ", "RecomUserId onCreate: " + recomUserId);
        Toast.makeText(ProfileActivity.this, "RecomUserId onCreate: " + recomUserId,
                Toast.LENGTH_SHORT).show();

        //Set visibility of buttons based if profile belongs to currentUser or not
        if (recomUserId != null && (!recomUserId.equals("")) && (!recomUserId.equals(currentUser.getUid()))){
            pEditProfileBtn.setVisibility(View.GONE);
            //pLinkUpBtn.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.GONE);
            if(hasNewMessages){
                profileNewMessageBadge.setVisibility(View.VISIBLE);
            }
        } else {
            pMessageBtn.setVisibility(View.GONE);
        }

        //Add buttons to button array
        profileButtons[0] = pEditProfileBtn;
        profileButtons[1] = pLinkUpBtn;
        profileButtons[2] = pMessageBtn;

        //Set Home Selected listener
        bottomNavigationView.setSelectedItemId(R.id.profilePage);

        //Set onClicklistener for buttons
        for (Button btn : profileButtons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (btn == pEditProfileBtn) {

                    } else if (btn == pMessageBtn){
                        //Start an intent for messaging
                        Intent intent = new Intent(ProfileActivity.this, MessageActivity.class);
                        //Send extras for recepientId == recomUserId (Who they are sending to)
                        intent.putExtra("recipientUserId", recomUserId);
                        startActivity(intent);
                        finish();
                    } else {

                    }
                }
            });
        }

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

        //Set onClickListeners for the buttons

        //Set onNavigationItemSelectedListener for bottom navigation created using Material Library
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Using if - else to set different navigation item functionalities
                if (item.getItemId() == R.id.homePage){
                    //What happens after someone selects homePage item
                    startActivity(new Intent(getApplicationContext(), FindFriendActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.linkupPage){
                    //What happens after someone selects linkUpPage item
                    startActivity(new Intent(getApplicationContext(), LinkUpActivity.class));
                    overridePendingTransition(0, 0);
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
        });
    }
}















