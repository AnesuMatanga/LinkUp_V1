package com.example.linkupappv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LinkUpActivity extends AppCompatActivity {

    //Make this constant
    public static final String AUTHOR_KEY = "author";
    public static final String QUOTE_KEY = "quote";

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");

    //Create Objects
    EditText lUserEmail, lUserName;
    Button lSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_up);

        lUserEmail = findViewById(R.id.userEmail);
        lUserName = findViewById(R.id.userName);
        lSaveButton = findViewById(R.id.saveButton);

        lSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatUser();
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















