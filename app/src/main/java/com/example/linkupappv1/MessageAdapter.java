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
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>  {

    //Initialize list to hold the message data to be displayed in the RecyclerView
    private List<Message> messages;
    FirebaseUser currentUser;
    //Document containing currentUser Details
    private DocumentReference mDocRef;

    private DocumentReference messagesDoc;
    //Constructor that gets the list and also current user
    public MessageAdapter(List<Message> messages){
        this.messages = messages;
    }

    //To help with inflating friend_reco.xml also
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        //Pass the layout to the ViewHolder
        return new ViewHolder(view);
    }

    //To help with populating data into the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        holder.messageContent.setText(currentMessage.getMessageContent());
    }

    @Override
    public int getItemCount() {
        //Return size of list
        return messages.size();
    }

    //Provides a direct reference to the views in each item layout to help quickly
    //update the content without any need to search the view hierachy
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //Initializing views
        TextView messageContent;
        //Constructor to do the view lookups to find each subview
        public ViewHolder(@NonNull View itemView) {
            //Stores the itemView in a public final member var
            //Used to access the context from any ViewHolder instance
            super(itemView);
            //Using R.id
            messageContent = itemView.findViewById(R.id.messageContent);
        }

    }
}
