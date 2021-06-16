package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubPackagesModel implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("package_id")
    private long package_id;

    @SerializedName("agents")
    private int agents;

    @SerializedName("trending")
    private int trending;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("exclusions")
    private String exclusions;

    @SerializedName("inclusions")
    private String inclusions;

    @SerializedName("starting_price")
    private String starting_price;

    @SerializedName("img")
    private String img;

    @SerializedName("tour_duration")
    private String tour_duration;

    @SerializedName("destination_covered")
    private String destination_covered;

    @SerializedName("helicopter")
    private int helicopter;

    @SerializedName("hotel")
    private int hotel;

    @SerializedName("break_fast")
    private int break_fast;

    @SerializedName("pickup")
    private int pickup;

    @SerializedName("view")
    private int view;

    public int getHelicopter() {
        return helicopter;
    }

    public int getHotel() {
        return hotel;
    }

    public int getBreak_fast() {
        return break_fast;
    }

    public int getPickup() {
        return pickup;
    }

    public int getView() {
        return view;
    }

    public String getTour_duration() {
        return tour_duration;
    }

    public String getDestination_covered() {
        return destination_covered;
    }

    public String getStarting_price() {
        return starting_price;
    }

    public long getId() {
        return id;
    }

    public long getPackage_id() {
        return package_id;
    }

    public int getAgents() {
        return agents;
    }

    public int getTrending() {
        return trending;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExclusions() {
        return exclusions;
    }

    public String getInclusions() {
        return inclusions;
    }

    public String getImg() {
        return img;
    }
}
