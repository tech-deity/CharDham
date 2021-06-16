package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.model.ReviewModel;

public class ReviewCall {

    @SerializedName("serv_res")
    private String serv_resp;

    @SerializedName("data")
    private ArrayList<ReviewModel> data;

    public String getServ_resp() {
        return serv_resp;
    }

    public ArrayList<ReviewModel> getData() {
        return data;
    }

}
