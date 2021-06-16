package in.chardhamtour.chardhamyatra.controller.listeners;

import in.chardhamtour.chardhamyatra.model.ItineraryModel;
import in.chardhamtour.chardhamyatra.model.StepOneModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;

public interface BookingListener {

    SubPackagesModel getSubPackage();
    ItineraryModel getProgram();
    void saveStepOneData(String departure,String departureDate,String email,String phoneNumber);
    StepOneModel getStepOneData();


}
