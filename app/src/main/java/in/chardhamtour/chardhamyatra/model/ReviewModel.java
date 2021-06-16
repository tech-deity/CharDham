package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

public class ReviewModel {

    @SerializedName("id")
    private int review_id;

    @SerializedName("sub_package_id")
    private int sub_package_id;

    @SerializedName("rating")
    private String ratings;

    @SerializedName("comment")
    private String comment;

    @SerializedName("date")
    private String c_datetime;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("name")
    private String user_name;

    @SerializedName("image")
    private String user_image;


    public int getReview_id() {
        return review_id;
    }

    public int getSub_package_id() {
        return sub_package_id;
    }

    public String getRatings() {
        return ratings;
    }

    public String getComment() {
        return comment;
    }

    public String getC_datetime() {
        return c_datetime;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_image() {
        return user_image;
    }
}
