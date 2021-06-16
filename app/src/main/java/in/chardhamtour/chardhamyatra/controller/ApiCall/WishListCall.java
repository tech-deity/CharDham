package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.model.PackageModel;

public class WishListCall {

    @SerializedName("msg")
    private String msg;

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("data")
    private ArrayList<PackageModel> data;

    public String getMsg() {
        return msg;
    }

    public String getServ_res() {
        return serv_res;
    }

    public ArrayList<PackageModel> getData() {
        return data;
    }
}
