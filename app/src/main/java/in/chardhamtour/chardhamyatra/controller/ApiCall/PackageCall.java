package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.model.PackageModel;

public class PackageCall implements Serializable {

    @SerializedName("serv_res")
    private String serv_resp;

    @SerializedName("data")
    private ArrayList<PackageModel> data;

    public String getServ_resp() {
        return serv_resp;
    }

    public ArrayList<PackageModel> getData() {
        return data;
    }
}
