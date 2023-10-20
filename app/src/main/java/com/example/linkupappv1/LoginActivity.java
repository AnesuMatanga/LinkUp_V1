package com.example.linkupappv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Create objects
    TextInputEditText lLoginEmail, lLoginPassword;
    Button lLoginButton;
    FirebaseAuth mAuth;
    ProgressBar lProgressBar;
    TextView lClickToRegister;

    //Check if user is already logged in before asking in onStart(), Firebase
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //Open main activity if User already logged in using an Intent
            //Create Intent to open main activity
            Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
            //Start the activity
            startActivity(loginIntent);
            //Close this activity
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //Now using R.id to get views
        lLoginEmail = findViewById(R.id.email);
        lLoginPassword = findViewById(R.id.password);
        lLoginButton = findViewById(R.id.loginButton);
        //For firebase
        mAuth = FirebaseAuth.getInstance();
        //For Progress Bar
        lProgressBar = findViewById(R.id.loginProgressBar);
        //For Click To login after registering or before
        lClickToRegister = findViewById(R.id.clickToRegister);

        //Setting OnClickListener for the Login Button
        lLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set visibility of progress bar
                lProgressBar.setVisibility(View.VISIBLE);
                //Initialise email and password to read from the editText
                String email, password;

                //Get the email and password and get string values
                password = lLoginPassword.getText().toString();
                email = lLoginEmail.getText().toString();

                //Check if email and password is empty
                if(password.equals("")){
                    //Sent a toast message
                    Toast.makeText(LoginActivity.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.equals("")){
                    //Sent a toast message
                    Toast.makeText(LoginActivity.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Now sign in using email and password for a User using Firebase Library Docs
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Set visibility of progress bar
                                    lProgressBar.setVisibility(View.GONE);
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "Authentication Succeeded.",
                                            Toast.LENGTH_SHORT).show();
                                    //Create Intent to open main activity
                                    Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    //Start the activity
                                    startActivity(loginIntent);
                                    //Close this activity
                                    finish();
                                    //updateUI(user);
                                } else {
                                    //Set visibility of progress bar
                                    lProgressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });


            }
        });


        //Setting OnClickListener for regLogin TextView
        lClickToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to open LoginActivity
                Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                //Starting activity for the call passing the intent object
                startActivity(loginIntent);
                finish();
            }
        });

    }
}