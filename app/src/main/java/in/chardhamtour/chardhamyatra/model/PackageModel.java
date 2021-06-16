package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackageModel implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String backgroundImage;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

}
