package com.example.linkupappv1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<UserProfile> userProfileData = new MutableLiveData<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Fetches the user profile data from Firestore
    public void fetchUserProfileData(String userId) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    UserProfile data = document.toObject(UserProfile.class);
                    userProfileData.setValue(data);
                }
            }
        });
    }

    // Exposes the LiveData to observers
    public LiveData<UserProfile> getUserProfileData() {
        return userProfileData;
    }
}
