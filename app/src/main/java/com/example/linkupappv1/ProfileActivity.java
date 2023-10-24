package com.example.linkupappv1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

    //Dialog Post menu items (Quests and UpTo) Things a user can post
    String[] postOptions = {"Quest", "UpTo"};

    //Create view objects
    TextView pRequestsCountTV, pLinkUpsCountTV, pRequestsTV, pLinkUpsTV,
            pProfUsername, pProfBio, pProfLocation, pProfInterests;

    Button pPostButton, pEditProfileButton;
    ImageView pProfilePicIV;
    Uri pProfPicURI;

    //ViewPager and Tab view objects
    ViewPager2 pViewPager2;
    TabLayout pTabLayout;

    //For current User
    FirebaseAuth pAuth;
    FirebaseUser currentUser;

    //Firebase Document Reference
    private DocumentReference pDocRef;

    //Real Time data retrieval to keep profile page always updated using onStart()
    protected void onStart() {
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

    }


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
        pPostButton = findViewById(R.id.profPostBtn);
        pEditProfileButton = findViewById(R.id.profEditProfileBtn);
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

        //If User presses the Post button on their profile a dialog should appear with options
        pPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickWhatToPost();
            }
        });

        //Initialize ViewPager2 to sort the tabs functionality and use adapter
        pViewPager2 = findViewById(R.id.profViewPager);
        pTabLayout = findViewById(R.id.profileTabLayout);

        //Set ProfViewPagerAdapter
        pViewPager2.setAdapter(new ProfViewPagerAdapter(getSupportFragmentManager(), getLifecycle()));

        new TabLayoutMediator(pTabLayout,pViewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    //Set tab text and icon
                    tab.setText(R.string.quests);
                    tab.setIcon(R.drawable.icons_compass_64);
                    break;
                case 1:
                    tab.setText(R.string.linkUps);
                    tab.setIcon(R.drawable.icons_chain_50);
                    break;
                case 2:
                    tab.setText(R.string.upTos);
                    tab.setIcon(R.drawable.icons_arrow_up_50);
                    break;
            }
        }).attach();
    }

    //Quests Tab Fragment inner class
    public static class QuestFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public QuestFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.fragment_quest, container, false);
        }
    }

    //LinkUps Tab Fragment inner class (Also avoiding a lot of external classes)
    public static class LinkUpsFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public LinkUpsFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.fragment_quest, container, false);
        }
    }

    //Quests Tab Fragment inner class
    public static class UpTosFragment extends Fragment {
        //Fragment Usage

        //Constructor as default constructor is needed for Fragments
        public UpTosFragment() {
            //It can be empty
        }

        //Override onCreateView for inflating fragment
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //Inflate the Fragment using the specified .xml layout
            return inflater.inflate(R.layout.fragment_quest, container, false);
        }
    }

    //Fragments for Quest and UpTo after post btn and choice has been clicked by user for them to post
   

    //Method using Material library to create a simple dialog that pops when user clicks post with options
    public void pickWhatToPost() {

        new MaterialAlertDialogBuilder(ProfileActivity.this) //Profile activity context
                .setTitle(getResources().getString(R.string.post)) //set title post from resources string.xml file
                .setItems(postOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int option) {

                        //Using switch case for the different options(items) a user can choose to post
                        switch (option) {
                            case 0: //Quest
                                //If user clicks Quest, what next?
                                break;
                            case 1: //UpTo
                                //If user clicks UpTo, what next?
                                break;
                        }

                    }
                }).show();  //Finally show the dialog
    }


}















