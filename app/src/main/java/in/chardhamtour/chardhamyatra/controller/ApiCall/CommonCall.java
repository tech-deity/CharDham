package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

public class CommonCall {

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("msg")
    private String message;

    public String getServ_res() {
        return serv_res;
    }

    public void setServ_res(String serv_res) {
        this.serv_res = serv_res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
