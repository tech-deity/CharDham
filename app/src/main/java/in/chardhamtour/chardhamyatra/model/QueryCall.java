package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.controller.ApiCall.StringModel;

public class QueryCall {

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private ArrayList<StringModel> data;

    public String getServ_res() {
        return serv_res;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<StringModel> getData() {
        return data;
    }

    public void setServ_res(String serv_res) {
        this.serv_res = serv_res;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(ArrayList<StringModel> data) {
        this.data = data;
    }
}
