package in.chardhamtour.chardhamyatra.controller.listeners;

public interface EnquiryDetail {
    long getEnquiryId();
    String getDestinationName();

    void setEnquiryId(long id);
    void setDestinationName(String name);

    boolean isEnquiryFrag();
    void setEnquiryFrag(boolean bool);
}
