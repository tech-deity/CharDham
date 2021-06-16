package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("id")
    private long userId;
    @SerializedName("name")
    private String userName;
    @SerializedName("mobile")
    private String userContact;
    @SerializedName("email")
    private String userEmail;
    @SerializedName("image")
    private String userProfile;
    @SerializedName("city")
    private String userCity;
    @SerializedName("state")
    private String userState;
    @SerializedName("google_id")
    private String facebookId;
    @SerializedName("facebook_id")
    private String googleId;



    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getUserCity() {
        return userCity;
    }

    public String getUserState() {
        return userState;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
