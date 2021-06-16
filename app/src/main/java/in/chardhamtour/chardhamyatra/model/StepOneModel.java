package in.chardhamtour.chardhamyatra.model;

import java.io.Serializable;

public class StepOneModel implements Serializable {


    private String travelDate;
    private String departure;
    private String contact;
    private String email;
    private long pack_id;
    private long program_id;



    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
