package in.chardhamtour.chardhamyatra.model;

import com.google.gson.annotations.SerializedName;

public class ChardhamModel {

    @SerializedName("id")
    private int id;

    @SerializedName("p_p")
    private String pp;

    @SerializedName("t_c")
    private String tc;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("title")
    private String cu_title;

    @SerializedName("subtitle")
    private String cu_subtitle;

    @SerializedName("faq")
    private String faq;

    public String getFaq() {
        return faq;
    }

    public String getCu_title() {
        return cu_title;
    }

    public String getCu_subtitle() {
        return cu_subtitle;
    }

    public int getId() {
        return id;
    }

    public String getPp() {
        return pp;
    }

    public String getTc() {
        return tc;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

  }
