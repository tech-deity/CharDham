package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

public class StringModel {

    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
