package com.example.linkupappv1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {

    //Create objects
    TextInputEditText rRegEmail, rRegPassword;
    Button rRegButton;
    FirebaseAuth mAuth;
    ProgressBar rProgressBar;
    TextView rClickToLogin;

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
        setContentView(R.layout.activity_register);

        //Now using R.id to get views
        rRegEmail = findViewById(R.id.regEmail);
        rRegPassword = findViewById(R.id.regPassword);
        rRegButton = findViewById(R.id.regButton);
        //For firebase
        mAuth = FirebaseAuth.getInstance();
        //For Progress Bar
        rProgressBar = findViewById(R.id.regProgressBar);
        //For Click To login after registering or before
        rClickToLogin = findViewById(R.id.regLogin);

        //Set OnclickListener for Register button
        rRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set visibility of progress bar
                rProgressBar.setVisibility(View.VISIBLE);
                //Initialise email and password to read from the editText
                String email, password;

                //Password & Email validation at client side
                //Get the email and password and get string values, trim any white spaces
                password = rRegPassword.getText().toString().trim();
                email = rRegEmail.getText().toString().trim();

                //Check if email and password is empty
                if(password.equals("")){
                    //Sent a toast message
                    Toast.makeText(RegisterActivity.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(email.equals("")){
                    //Sent a toast message
                    Toast.makeText(RegisterActivity.this, "Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Email format validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    rProgressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Enter a valid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check password strength by length
                if (password.length() < 6){
                    rProgressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Password should be atleast 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Implement Code for creating User using the Firebase Library and their Docs
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    rProgressBar.setVisibility(View.GONE);
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(RegisterActivity.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(user);

                                    //Intent to open Login page after
                                    //Create an intent to open LoginActivity
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    //Starting activity for the call passing the intent object
                                    startActivity(intent);
                                    finish();
                                } else {
                                    rProgressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });

            }
        });

        //Setting OnClickListener for regLogin TextView
        rClickToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to open LoginActivity
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                //Starting activity for the call passing the intent object
                startActivity(loginIntent);
                finish();
            }
        });

    }
}