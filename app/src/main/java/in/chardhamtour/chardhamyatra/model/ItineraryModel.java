package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItineraryModel implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("sub_package_id")
    private long SubPackageId;

    @SerializedName("name")
    private String name;

    @SerializedName("tour_name")
    private String tourName;

    @SerializedName("destination_covered")
    private String destinationCovered;

    @SerializedName("tour_duration")
    private String tourDuration;

    @SerializedName("tour_price")
    private String tourPrice;

    @SerializedName("description")
    private String tourDescription;

    public long getId() {
        return id;
    }

    public long getSubPackageId() {
        return SubPackageId;
    }

    public String getName() {
        return name;
    }

    public String getTourName() {
        return tourName;
    }

    public String getDestinationCovered() {
        return destinationCovered;
    }

    public String getTourDuration() {
        return tourDuration;
    }

    public String getTourPrice() {
        return tourPrice;
    }

    public String getTourDescription() {
        return tourDescription;
    }
}
