package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import in.chardhamtour.chardhamyatra.model.ChardhamModel;

public class ChardhamDataCall {

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("data")
    private ChardhamModel data;

    public String getServ_res() {
        return serv_res;
    }
    public ChardhamModel getData() {
        return data;
    }

}
