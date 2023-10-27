package com.example.linkupappv1;

/**
 * @FriendRecommendation Class
 * To work with an adapter in order to get a list of recoms
 * to be populated onto the user screen using using RecyclerView.
 */
public class FriendRecommendation {
    public String username;
    public String location;
    public String interests;
    public String commonInterests;
    public String bio;
    public String profile_pic;


    //Getter and setter methods

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }


    public String getBio(){
        return this.bio;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public String getProfile_pic(){
        return this.profile_pic;
    }

    public void setProfile_pic(String profile_pic){
        this.profile_pic = profile_pic;
    }

    public String getInterests(){
        return this.interests;
    }

    public void setInterests(String interests){
        this.interests = interests;
    }

    //method to
}
