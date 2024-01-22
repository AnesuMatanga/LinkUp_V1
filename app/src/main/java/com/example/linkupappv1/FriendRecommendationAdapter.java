package com.example.linkupappv1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.startActivity;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @FriendRecommendationAdapter Class
 * Extends RecyclerView.Adapter to help handle the data from firestore
 * and bind it onto the view
 */
public class FriendRecommendationAdapter extends RecyclerView.Adapter<FriendRecommendationAdapter.ViewHolder>  {

    //Initialize list to hold the friend recommendation data to be displayed in the RecyclerView
    private List<FriendRecommendation> friendRecommendations;
    FirebaseUser currentUser;
    //Document containing currentUser Details
    private DocumentReference fDocRef;

    //Instantiate DB
    FirebaseFirestore db;
    CollectionReference usersRef;

    private DocumentReference friendRequestsDoc;
    //Constructor that gets the list and also current user
    public FriendRecommendationAdapter(List<FriendRecommendation> friendRecommendations, FirebaseUser currentUser){
        this.friendRecommendations = friendRecommendations;
        this.currentUser = currentUser;
    }

    //To help with inflating friend_reco.xml also
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_recommendation_card, parent, false);
        //Pass the layout to the ViewHolder
        return new ViewHolder(view);
    }

    //To help with populating data into the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recomUsername.setText(friendRecommendations.get(position).username);
        holder.recomLocation.setText(friendRecommendations.get(position).location);
        holder.recomSimilarity.setText(friendRecommendations.get(position).interests);
        //Get profile image and show it in ImageView in using Glide Library
        Glide.with(holder.itemView.getContext())
                .load(friendRecommendations.get(position).profile_pic)
                .placeholder(R.drawable.black_girl_prof)
                .error(R.drawable.baseline_error_24)
                .into(holder.recomProfilePic);

        //Initialize variables to hold data
        String friendUsername;
        friendUsername = friendRecommendations.get(position).username;
        Log.d("Recommended Usernames: ", "FriendUsername: " + friendUsername);
        final String[] recomUserID = new String[1];

        //Set onClickListener for when Know Me Button is clicked to direct people to profile
        holder.recomKnowMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the UID of the recommended User from firestore
                db = FirebaseFirestore.getInstance();
                usersRef = db.collection("users");

                //Now search for the username, also using limit(1) to make sure only one doc is returned
                usersRef.whereEqualTo("username", friendUsername).limit(1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    //If doc has been found
                                    if (!task.getResult().isEmpty()){
                                        DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                        String userId = userDocument.getId();
                                        recomUserID[0] = userId;

                                        //Direct to profile of clicked user
                                        Intent intent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                                        //Put Extra to sent to activity
                                        intent.putExtra("recomUserId", recomUserID[0]);
                                        Log.d("Recommended UserID: ", "RECOMUSERID: " + recomUserID[0]);
                                        holder.itemView.getContext().startActivity(intent);
                                    } else {
                                        //No doc found with username
                                        Log.d("FriendRecomAdapter", "No user found with that username");
                                        //Toast for debugging purposes
                                        Toast.makeText(holder.itemView.getContext(), "No user found with that username!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("FriendRecomAdapter", "Task was unsuccessful");
                                }
                            }
                        });
            }
        });

        //Set onClickListener for when LinkUp Button is clicked
        holder.recomLinkUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the UID of the recommended User from firestore
                db = FirebaseFirestore.getInstance();
                usersRef = db.collection("users");
                final String[] recomUserID = new String[1];

                //Now search for the username, also using limit(1) to make sure only one doc is returned
                usersRef.whereEqualTo("username", friendUsername).limit(1).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    //If doc has been found
                                    if (!task.getResult().isEmpty()){
                                        DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                        String userId = userDocument.getId();
                                        recomUserID[0] = userId;
                                        //Get user document from firestore containing Current username
                                        final String[] currentUsername = new String[1];
                                        fDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());
                                        fDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                //Check if document exists first
                                                if (documentSnapshot.exists()) {
                                                    String currentUserDoc = documentSnapshot.getString("username");
                                                    currentUsername[0] = currentUserDoc;

                                                    //Direct to profile of clicked user
                                                    friendRequestsDoc = FirebaseFirestore.getInstance().document("users/" + recomUserID[0] + "/friendRequests/" + currentUsername[0]);
                                                    //Create data i want to save in my FriendRequestDocument
                                                    Map<String, Object> dataToSave = new HashMap<String, Object>();
                                                    //dataToSave.put("friendRequestUsername", friendUsername);
                                                    dataToSave.put("username", currentUsername[0]);Log.d("FriendRecomAdapter", "No user found with that username");
                                                    //Toast for debugging purposes
                                                    Log.d("Current Username", "CurrentUsername: " + currentUsername[0]);

                                                    friendRequestsDoc.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d("FriendRequest", "Friend Request Has been sent!");
                                                            //Make a toast to show user
                                                            Toast.makeText(holder.itemView.getContext(), "LinkUp Request Sent!",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Sorry, Couldn't send LinkUp Request", e);
                                                            //Make a toast to show user
                                                            Toast.makeText(holder.itemView.getContext(), "Sorry, Couldn't send LinkUp Request",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });


                                    } else {
                                        //No doc found with username
                                        Log.d("FriendRecomAdapter", "No user found with that username");
                                        //Toast for debugging purposes
                                    }
                                } else {
                                    Log.e("FriendRecomAdapter", "Task was unsuccessful");
                                }
                            }
                        });
                //Set friendRequestDoc
                //String friendUserID = getUniqueUserID(friendUsername, holder);
                //friendRequestsDoc = FirebaseFirestore.getInstance().document("users/" + friendUserID + "/friendRequests/" + friendUsername);

            }
        });
    }

    @Override
    public int getItemCount() {
        //Return size of list
        return friendRecommendations.size();
    }

    //Provides a direct reference to the views in each item layout to help quickly
    //update the content without any need to search the view hierachy
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Initializing views
        TextView recomUsername, recomLocation, recomSimilarity;
        ImageView  recomProfilePic;
        Button recomLinkUpBtn, recomKnowMeBtn;

        //Constructor to do the view lookups to find each subview
        public ViewHolder(@NonNull View itemView) {
            //Stores the itemView in a public final member var
            //Used to access the context from any ViewHolder instance
            super(itemView);
            //Using R.id
            recomUsername = itemView.findViewById(R.id.recomUsername);
            recomLocation = itemView.findViewById(R.id.recomLocation);
            recomSimilarity = itemView.findViewById(R.id.recomSimilarity);
            recomProfilePic = itemView.findViewById(R.id.recomProfilePic);
            recomLinkUpBtn = itemView.findViewById(R.id.recomLinkUpBtn);
            recomKnowMeBtn = itemView.findViewById(R.id.recomViewBtn);
        }

    }

    //Method to get username Unique Firestore UID
  /*  public void getUniqueUserID(String friendUsername, ViewHolder holder, DocumentReference friendRequestsDoc) {
        //Get the UID of the recommended User from firestore
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        final String[] recomUserID = new String[1];
        this.friendRequestsDoc = friendRequestsDoc;

        //Now search for the username, also using limit(1) to make sure only one doc is returned
        usersRef.whereEqualTo("username", friendUsername).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //If doc has been found
                            if (!task.getResult().isEmpty()){
                                DocumentSnapshot userDocument = task.getResult().getDocuments().get(0);
                                String userId = userDocument.getId();
                                recomUserID[0] = userId;
                                //Direct to profile of clicked user
                                friendRequestsDoc = FirebaseFirestore.getInstance().document("users/" + recomUserID[0] + "/friendRequests/" + friendUsername);
                            } else {
                                 //No doc found with username
                                Log.d("FriendRecomAdapter", "No user found with that username");
                                //Toast for debugging purposes
                                Toast.makeText(holder.itemView.getContext(), "No user found with that username!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("FriendRecomAdapter", "Task was unsuccessful");
                        }
                    }
                });
        return recomUserID[0];
    } */
}
