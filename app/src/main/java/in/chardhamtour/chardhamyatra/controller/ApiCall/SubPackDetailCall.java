package in.chardhamtour.chardhamyatra.controller.ApiCall;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.model.ItineraryModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;

public class SubPackDetailCall {

    @SerializedName("serv_res")
    private String serv_res;

    @SerializedName("data")
    private SubPackagesModel data;


    @SerializedName("images")
    private ArrayList<ImageModel> imageModels;

    @SerializedName("itinerary")
    private ArrayList<ItineraryModel> itineraryModels;

    public SubPackDetailCall() {
    }

    public class ImageModel{

        @SerializedName("img")
        private String img;

        public String getImg() {
            return img;
        }
    }

    @SerializedName("faq")
    private String faqHtmlString;

    public String getFaqHtmlString() {
        return faqHtmlString;
    }

    public String getServ_res() {
        return serv_res;
    }

    public SubPackagesModel getData() {
        return data;
    }

    public ArrayList<ImageModel> getImageModels() {
        return imageModels;
    }

    public ArrayList<ItineraryModel> getItineraryModels() {
        return itineraryModels;
    }
}
