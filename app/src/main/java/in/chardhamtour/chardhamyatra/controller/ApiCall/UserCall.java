package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import in.chardhamtour.chardhamyatra.model.Model;

public class UserCall{

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("data")
    private Model data;

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }
    public String getServ_res() {
        return serv_res;
    }
    public Model getData() {
        return data;
    }
}
