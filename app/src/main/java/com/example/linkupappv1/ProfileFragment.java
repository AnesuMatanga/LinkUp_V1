package com.example.linkupappv1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    //Create view objects
    TextView pRequestsCountTV, pLinkUpsCountTV, pRequestsTV, pLinkUpsTV,
            pProfUsername, pProfBio, pProfLocation, pProfInterests;

    //For current User
    FirebaseAuth pAuth;
    FirebaseUser currentUser;
    ImageView pProfilePicIV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get document reference for currently signed in user utilising Firebase libraries
        pAuth = FirebaseAuth.getInstance();
        currentUser = pAuth.getCurrentUser();

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

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getUserProfileData().observe(getViewLifecycleOwner(), userProfile -> {
            // Update UI elements with the observed data
            //Glide.with(getContext())
                   // .load(userProfile.getProfilePicUrl())
                    //.into(pProfilePicIV);

            pProfBio.setText(String.valueOf(userProfile.getBio()));
        });

        // Fetch data
        viewModel.fetchUserProfileData(currentUser.getUid());
    }
}
