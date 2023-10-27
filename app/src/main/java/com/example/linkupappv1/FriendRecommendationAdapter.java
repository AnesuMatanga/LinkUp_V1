package com.example.linkupappv1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        //Set onClickListener for when LinkUp Button is clicked
        holder.recomLinkUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set friendRequestDoc
                friendRequestsDoc = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid() + "/friendRequests/" + friendUsername);
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
                        }
                    }
                });

                //Create data i want to save in my FriendRequestDocument
                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put("friendRequestUsername", friendUsername);
                dataToSave.put("username", currentUsername[0]);

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
        Button recomLinkUpBtn;

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
        }

    }
}
