package com.example.linkupappv1;

public class UserProfile {

    private String username;
    private String bio;
    private String interests;

    private String profile_pic;
    private String location;
    // ... any other fields ...

    // Empty constructor required for Firestore
    public UserProfile() {}

    // Getter and setter methods

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicUrl() {
        return profile_pic;
    }

    public void setProfilePicUrl(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests){
        this.interests = interests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }
    // ... any other getter and setter methods ...
}
