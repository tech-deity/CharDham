package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.model.BannerModel;


public class BannerCall {

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("data")
    private ArrayList<BannerModel> data;

    public String getServ_res() {
        return serv_res;
    }

    public void setServ_res(String serv_res) {
        this.serv_res = serv_res;
    }

    public ArrayList<BannerModel> getData() {
        return data;
    }

    public void setData(ArrayList<BannerModel> data) {
        this.data = data;
    }
}
