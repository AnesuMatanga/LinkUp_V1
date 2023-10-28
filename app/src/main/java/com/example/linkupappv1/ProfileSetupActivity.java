package com.example.linkupappv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class To Setup Profile for firstTime Users
 */

public class ProfileSetupActivity extends AppCompatActivity {

    //Make the profile data keys public static for global access
    public static final String PROFILE_PIC_KEY = "profile_pic";
    public static final String USERNAME_KEY = "username";
    public static final String BIO_KEY = "bio";
    public static final String INTERESTS_KEY = "interests";
    public static final String INTERESTS_KEY2 = "interests2";
    public static final String LOCATION_KEY = "location";

    //Set up a Firestore DB to later store user profile details
    FirebaseFirestore db;
    //Get Current Authenticated user
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    //Declare the document to store userData as Firestore cloud saves in docs
    private DocumentReference userProfileDocRef;

    //When user registers, add them to a friendRecommendations doc already to help later with
    //recommending friends in FindFriendActivity
    private DocumentReference friendRecommendations;
    EditText pUsernameET, pBioET, pLocationET;
    Button pProfileSaveBtn, pProfilePicUploadBtn;

    //1 for yes for request to pick an image
    private static final int PICK_IMAGE_REQUEST = 1;
    ShapeableImageView pProfileImage;
    ProgressBar pProfileUploadPB;

    //URL string for profile image after uploading to storage to save in firestore
    String pProfilePicURL;
    Uri pFirestorePicURI;

    //Using Firebase storage for uploading image to storage for User profiles
    private StorageReference pStorageReference;

    //For interests to use in autoCompleteTextView
    AutoCompleteTextView pInterestsET, pInterestsET2;
    //Adding as many interests that i can think of to try autoComplete for user
    String[] interestsList = {"football", "soccer", "cooking", "braiding", "gaming", "reading",
        "travelling", "hiking", "baking", "Photography", "Traveling", "Cooking", "Blogging", "Gaming", "Hiking", "Reading",
            "Writing", "Painting", "Drawing", "Dancing", "Yoga", "Meditation", "Running", "Swimming", "Cycling", "Gardening",
            "DIY Projects", "Music", "Playing Instruments", "Movies", "Theatre", "Fashion", "Shopping", "Crafts", "Pottery",
            "Bird Watching", "Fishing", "Hunting", "Surfing", "Skating", "Snowboarding", "Skiing", "Camping", "Backpacking",
            "Fitness", "Bodybuilding", "Martial Arts", "Collecting (e.g., stamps, coins, vintage items)", "Wine Tasting", "Brewing", "Knitting",
            "Crocheting", "Scuba Diving", "Rock Climbing", "Astronomy", "Puzzle Solving", "Board Games", "Singing", "Podcasting", "Volunteering",
            "Philanthropy", "Digital Art", "Graphic Design", "Web Development", "Animation", "Basketball", "Soccer", "Tennis", "Golf", "Rugby",
            "Cricket", "Pet Care", "Horse Riding", "Drones", "Robotics", "Programming", "Magic", "Stand-up Comedy", "Investing", "Stock Trading", "Travel Vlogging",
            "Photography Vlogging", "Cooking Vlogging", "Lifestyle Blogging", "Adventure Sports", "Motorcycling", "Car Racing", "Digital Marketing", "Startups",
            "Entrepreneurship", "Creative Writing", "Eco-friendly Living", "Sustainability", "Wildlife Conservation", "Renewable Energy", "Cultural Festivals", "Languages", "History", "Archaeology", "Urban Exploration", "Geocaching",
            "Metal Detecting", "Sculpting", "Museum Visits", "Theme Parks", "Aquariums", "Zoos"};


    //Get data in real time from firestore through onStart()
    protected void onStart() {
        super.onStart();

        //Use 'this' to make sure the listener is not used when needed to avoid over battery usage for user and also save cost
        userProfileDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
           //Mainly for showing the profile pic
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //Check if document already exists
                if (documentSnapshot.exists()) {
                    //Get the imageURL from documentSnapshot
                    String imageURL = documentSnapshot.getString(PROFILE_PIC_KEY);

                    //Use Glide library for easy image load and display in imageView
                    Glide.with(ProfileSetupActivity.this).load(imageURL)
                            .placeholder(R.drawable.profile_icon) //Shown when image is loading
                            .error(R.drawable.baseline_error_24) //If there is an error in loading image
                            .into(pProfileImage);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        //Initialise
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userProfileDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());
        friendRecommendations = FirebaseFirestore.getInstance().document("friendRecommendations/" + currentUser.getUid());

        //Get views using R.id
        pUsernameET = findViewById(R.id.profileUsername);
        pBioET = findViewById(R.id.profileStatus);
        pInterestsET = findViewById(R.id.profileInterests);
        pInterestsET2 = findViewById(R.id.profileInterests2);
        pLocationET = findViewById(R.id.profileLocation);
        pProfileSaveBtn = findViewById(R.id.profileSaveBtn);
        pProfilePicUploadBtn = findViewById(R.id.profilePicUploadBtn);
        pProfileImage = findViewById(R.id.profileImageView);
        pProfileUploadPB = findViewById(R.id.profileSaveProgressBar);
        //pProfilePicURL = "";

        //Initialise firebase storage bucket
        pStorageReference = FirebaseStorage.getInstance().getReference("profile_images");

        //Set array adapter to use with the pInterestsET to populate from the interestsList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.favourite_item, interestsList);
        pInterestsET.setAdapter(adapter);
        pInterestsET2.setAdapter(adapter);


        //Check if User Not Logged in
        if (currentUser == null){
            //Sent to Login page
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        //Set OnClickListener for save Button
        pProfileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });

        //Set OnClickListener for upload button
        pProfilePicUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfilePic(pFirestorePicURI);
            }
        });

        //Set onClickListener on ImageView
        pProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickProfileImage();
            }
        });
    }

    //Save Profile Info To Database if save button is clicked
    public void saveProfile() {
        //Get current user Id
        String userID = currentUser.getUid();

        //Get the user inputs and turn into strings
        String username = "@" + pUsernameET.getText().toString();
        String bio = pBioET.getText().toString();
        String interests = pInterestsET.getText().toString();
        String location = pLocationET.getText().toString();
        String profilePic = pProfilePicURL;
        String interests2 = pInterestsET2.getText().toString();

        //To validate interest input interest from user
        String[] userInterests = interests.split(",");
        if(userInterests.length > 3){
            Toast.makeText(ProfileSetupActivity.this, "Please enter a maximum of 3 Interests",
                    Toast.LENGTH_SHORT);
            return;
        }

        //Validate the inputs first before saving profile
        if(username.isEmpty()){
            Toast.makeText(ProfileSetupActivity.this, "Please enter a Username",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(location.isEmpty()){
            Toast.makeText(ProfileSetupActivity.this, "Please enter your Location(City)",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(interests.isEmpty()){
            Toast.makeText(ProfileSetupActivity.this, "Please enter an interest",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //Create hashmap to save the profile data to save to cloud database
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(PROFILE_PIC_KEY, profilePic);
        dataToSave.put(USERNAME_KEY, username);
        dataToSave.put(BIO_KEY, bio);
        dataToSave.put(INTERESTS_KEY, interests);
        dataToSave.put(LOCATION_KEY, location);
        dataToSave.put(INTERESTS_KEY2, interests2);

        //Now save to the database using the doc we created at the start
        userProfileDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("ProfileData", "Document has been saved");
                Toast.makeText(ProfileSetupActivity.this, "Profile Saved!",
                        Toast.LENGTH_SHORT).show();

                //Now save to the database friendRecommendations Doc using the doc we created at the start
                friendRecommendations.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("ProfileData", "Document has been saved");
                        //Toast.makeText(ProfileSetupActivity.this, "Profile Saved!",
                        //Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Unable to save Profile", e);
                        Toast.makeText(ProfileSetupActivity.this, "Could Not Save into FriendRecommendations!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                //Create an Intent and direct user back to their profile page after
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);

                //Finish this activity
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Unable to save Profile", e);
                Toast.makeText(ProfileSetupActivity.this, "Could Not Save Profile!",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }

    //Method to launch the image chooser for user to pick profile image
    private void pickProfileImage() {
        //Initialise an intent object
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //Override onActivityResult and get the selected image URL
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //Check if image was successfully picked
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            //Get the picked image URI
            System.out.println("DEBUG In onActivity Image URI = " + data.getData());
            Uri imageURI = data.getData();

            //Save to a variable in case to access later to save in FireStore under user profile
            pFirestorePicURI = imageURI;

            //Use try catch block
            try {
                //Setting the image bitmap on imageView for user to see
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                pProfileImage.setImageBitmap(bitmap);

                //Calling the uploadProfilePic Method using the URI to save to firebase storage
                //uploadProfilePic(imageURI);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    //Method to upload image to firebase storage using the image Uri
    private void uploadProfilePic(Uri imagePath){
        //If image picked
        if (imagePath != null){
            System.out.println("DEBUG Uploading Image to Firebase");
            //Show progress bar
            pProfileUploadPB.setVisibility(View.VISIBLE);

            //Defining the child of storageRef (the image saved under current user)
            StorageReference ref = pStorageReference.child(currentUser.getUid() + ".jpg");

           //String path = "profile_images/" + UUID.randomUUID() + ".jpg";
            //StorageReference ref = pStorageReference.child(path);

            //Add to storage and give a Toast message to user if upload was a success
            ref.putFile(imagePath).addOnSuccessListener(taskSnapshot -> {
                //Stop showing progress bar
                pProfileUploadPB.setVisibility(View.GONE);

                //Give toast message
                Toast.makeText(ProfileSetupActivity.this, "Profile Image uploaded Successfully",
                        Toast.LENGTH_SHORT).show();

                //Get the URL of the uploaded image to save to firestore with other user data
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageURL = uri.toString();
                    pProfilePicURL = imageURL;
                });

            }).addOnFailureListener(e -> {
                pProfileUploadPB.setVisibility(View.GONE);
                Toast.makeText(ProfileSetupActivity.this, "Failed to Upload Profile Image!",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }


}



















