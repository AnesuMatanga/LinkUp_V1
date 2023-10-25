package com.example.linkupappv1;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {

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
  /*  public void onStart() {
        super.onStart();
        System.out.println("****IN ON START()***");

        //Get document reference for currently signed in user utilising Firebase libraries
        pAuth = FirebaseAuth.getInstance();
        currentUser = pAuth.getCurrentUser();

        System.out.println("****Current User*** : " + currentUser);

        //Initialize doc ref
        pDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());

        //Use 'this' to make sure the listener is not used when needed to avoid over battery usage for user and also save cost
        pDocRef.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
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
                    Glide.with(ProfileFragment.this).load(profile_pic).into(pProfilePicIV);
                }
            }
        });

    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Initialize the view objects using R.id
        pRequestsCountTV = view.findViewById(R.id.profRequestsCount);
        pLinkUpsCountTV = view.findViewById(R.id.profLinkupsCount);
        pLinkUpsTV = view.findViewById(R.id.linkUpsText);
        pRequestsTV = view.findViewById(R.id.requestsText);
        pProfBio = view.findViewById(R.id.profBio);
        pProfLocation = view.findViewById(R.id.profLocation);
        pProfUsername = view.findViewById(R.id.profUsername);
        pProfilePicIV = view.findViewById(R.id.profileImageView);
        pProfInterests = view.findViewById(R.id.profInterests);
        //Get document reference for currently signed in user utilising Firebase libraries

        // Inflate the layout for this fragment

        return view;
    }

    //Method to help refresh and fetch current user data for their profile
    public void updateData() {

        pAuth = FirebaseAuth.getInstance();
        currentUser = pAuth.getCurrentUser();

        System.out.println("****Current User*** : " + currentUser);


        pDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());
        pDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                    Glide.with(ProfileFragment.this).load(profile_pic).into(pProfilePicIV);
                }
            }
        });
    }
}