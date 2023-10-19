package com.example.linkupappv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Initialise
    Button linkUpButton, findFriendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise HomePage buttons and get views using Ids from .xml
        linkUpButton = (Button) findViewById(R.id.linkUpButton);
        findFriendButton = (Button) findViewById(R.id.findFriendButton);
    }

    /**
     * OnClickListener for LinkUp & FindFriend Button
     * LinkUp -Will take users to a dashboard after they have logged in
     * If not, redirect to login page
     * FindFriend -Will take users to a search page where they can discover
     * new friends
     */
    private void buttonClick (Button button) {
        Context context = this;

        //Set OnClickListener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check which button was clicked and do something
                if (button == linkUpButton){
                    //Intent to move to the desired activity
                    Intent intent = new Intent(MainActivity.this, LinkUpActivity.class);
                    startActivity(intent);
                }

                if (button == findFriendButton){
                    //Intent to move to the desired activity
                    Intent intent = new Intent(MainActivity.this, FindFriendActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}














