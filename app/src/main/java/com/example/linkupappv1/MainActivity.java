package com.example.linkupappv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Create Objects
    FirebaseAuth auth;
    FirebaseUser user;
    Button linkUpButton, findFriendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise HomePage buttons and get views using Ids from .xml
        linkUpButton = (Button) findViewById(R.id.linkUpButton);
        findFriendButton = (Button) findViewById(R.id.findFriendButton);

        //Initialise User by getting current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Check if User Not Logged in
        if (user == null){
            //Sent to Login page
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        //Button Click
        buttonClick(linkUpButton, findFriendButton);
    }

    /**
     * OnClickListener for LinkUp & FindFriend Button
     * LinkUp -Will take users to a dashboard after they have logged in
     * If not, redirect to login page
     * FindFriend -Will take users to a search page where they can discover
     * new friends
     */
    private void buttonClick (Button linkButton, Button friendButton) {
        Context context = this;

        //Set OnClickListener
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check which button was clicked and do something
                System.out.println("Clicked LinkUpButton");
                //Firebase.analytics.logEvent("linkUp clicked", null);

                //Intent to move to the desired activity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

            }
        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to move to the desired activity
                //Check which button was clicked and do something
                System.out.println("Clicked findFriendButton");
                //Intent intent = new Intent(MainActivity.this, FindFriendActivity.class);
                //startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                //Sent to Login page
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }

}














