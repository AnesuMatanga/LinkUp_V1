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

/**
 * @LinkUp App Idea Explanation
 * @Author: Anesu Maxwell Matanga
 * @ID: 20010613
 * @Avatars I have made 3 avatars for you to use to test( Already made accounts)
 * @Avatar1 Login Email: amaxkraals@gmail.com  Password: Anesu88
 * @Avatar2 Login Email: hillaryjoy18@gmail.com  Password: Hills88
 * @Avatar3 Login Email: bmtzrecords@gmail.com   Password: Bmtz88
 * @Explanation
 *
 * The LinkUp App was inspired by a clear digital gap: while many people have access to multiple social media platforms,
 * meaningful friendships remain elusive. Popular, platforms such as Instagram and TikTok frequently
 * fall short of creating genuine connections.
 *
 * LinkUp's current version(V1) allows users to swipe through profiles that highlight interests and location
 * (restricted to city for safety). Users can read detailed profiles and participate in real-time chats.
 * I used Firebase to implement strong authentication, and the Material library was crucial in keeping to
 * Android's design requirements. Some icons were obtained from Icons.
 *
 * My vision for LinkUp's future is Ambitious. I plan to implement an algorithm that incorporates psychological
 * insights to improve friendship suggestions based on profile compatibilities, creating genuine friendships.
 *
 * Certain envisioned functionality are still pending due to the constraint of 10 classes.
 * Nonetheless, as a solo project, this software is a huge accomplishment and monument to my dedication.
 * I hope you acknowledge the potential and effort behind LinkUp and also enjoy it :).
 */
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
            Intent loginIntent = new Intent(getApplicationContext(), ProfileActivity.class);
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
                password = lLoginPassword.getText().toString().trim();
                email = lLoginEmail.getText().toString().trim();

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

                //Email format validation
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    lProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Enter a valid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check password strength by length
                if (password.length() < 6){
                    lProgressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Password should be atleast 6 characters", Toast.LENGTH_SHORT).show();
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
                                    //Reset has queried
                                    ProfileActivity.hasQueriedFirestore = false;
                                    //Create Intent to open main activity
                                    Intent loginIntent = new Intent(getApplicationContext(), ProfileActivity.class);
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