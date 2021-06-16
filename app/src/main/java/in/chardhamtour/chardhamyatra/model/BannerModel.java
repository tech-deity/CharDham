package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

public class BannerModel {



    @SerializedName("id")
    private int id;

    @SerializedName("heading")
    private String heading;

    @SerializedName("sub_heading")
    private String sub_heading;

    @SerializedName("img")
    private String banner;



    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSub_heading() {
        return sub_heading;
    }

    public void setSub_heading(String sub_heading) {
        this.sub_heading = sub_heading;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
