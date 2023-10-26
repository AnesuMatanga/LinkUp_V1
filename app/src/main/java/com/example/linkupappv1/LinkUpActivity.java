package com.example.linkupappv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class LinkUpActivity extends AppCompatActivity {

    //Make this constant
    public static final String AUTHOR_KEY = "author";
    public static final String QUOTE_KEY = "quote";

    TextView lQuoteFetched;

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");

    //Create Objects
    EditText lUserEmail, lUserName;
    Button lSaveButton;
    Button lFetchButton;

    //Get data in real time using onStart()
    protected void onStart() {
        super.onStart();

        //Since it happens so fast, if you want to notify exactly when it happens
        //DocumentListenOptions verboseOptions = new DocumentListenOptions();
        //verboseOptions.includeMetadataChanges();

        //Use 'this' to make sure the listener is not used when needed to avoid over battery usage for user and also save cost
        mDocRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    String quoteText = documentSnapshot.getString(QUOTE_KEY);
                    String authorText = documentSnapshot.getString(AUTHOR_KEY);

                    //Set the text Value into the textView
                    lQuoteFetched.setText("\"" + quoteText + "\" -- " + authorText);

                    Toast.makeText(LinkUpActivity.this, "Updated Quote From Cloud!",
                            Toast.LENGTH_SHORT).show();

                } else if (error != null){
                    Log.w(TAG, "Got an exception!", error);
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_up);

        lUserEmail = findViewById(R.id.userEmail);
        lUserName = findViewById(R.id.userName);
        lSaveButton = findViewById(R.id.saveButton);
        lFetchButton = findViewById(R.id.fetchButton);

        lQuoteFetched = findViewById(R.id.fetchedQuote);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Set Home Selected listener
        bottomNavigationView.setSelectedItemId(R.id.linkupPage);


        lSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatUser();
            }
        });

        lFetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchQuote();
            }
        });

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
                    return true;
                } else if (item.getItemId() == R.id.profilePage){
                    //What happens after someone selects profilePage item
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.settingsPage){
                    //What happens after someone selects settingsPage item
                    return true;
                }
                return false;
            }
        });

    }

    public void fetchQuote() {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String quoteText = documentSnapshot.getString(QUOTE_KEY);
                    String authorText = documentSnapshot.getString(AUTHOR_KEY);

                    //Set the text Value into the textView
                    lQuoteFetched.setText("\"" + quoteText + "\" -- " + authorText);

                    Toast.makeText(LinkUpActivity.this, "Fetched Quote From Cloud!",
                            Toast.LENGTH_SHORT).show();

                    //If want to get all the data fetch at once
                    //Map<String, Object> myData = documentSnapshot.getData();

                    //For more sophisticated projects toObject get data and set as fields
                    //InspiringQuote myQuote = documentSnapshot.toObject(InspiringQuote.class);
                }
            }
        });
    }

    public void whatUser() {
        String quoteEmail = lUserEmail.getText().toString();
        String quoteName = lUserName.getText().toString();

        //Check if empty
        if (quoteEmail.isEmpty() || quoteName.isEmpty()) {
            return;
        }
        //Create data i want to save in my document
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(QUOTE_KEY, quoteEmail);
        dataToSave.put(AUTHOR_KEY, quoteName);

        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("InspiringQuote", "Document has been saved!");
                Toast.makeText(LinkUpActivity.this, "Document saved to Cloud!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document not saved to Cloud!", e);
            }
        });
    }
}















