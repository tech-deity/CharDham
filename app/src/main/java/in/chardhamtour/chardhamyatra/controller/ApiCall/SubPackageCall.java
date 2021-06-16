package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.model.SubPackagesModel;

public class SubPackageCall {

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("data")
    private ArrayList<SubPackagesModel> data;

    public String getServ_res() {
        return serv_res;
    }
    public ArrayList<SubPackagesModel> getData() {
        return data;
    }

}
