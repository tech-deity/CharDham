package in.chardhamtour.chardhamyatra.view.fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.EnquiryDetail;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.InputValidation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class EnquiryFragment extends Fragment{

    private ToolbarListerner toolbarListerner;
    private Calendar mCalendar;
    private EnquiryDetail enquiryDetailListener;
    private DatePickerDialog.OnDateSetListener date;
    private IProgressBarListener interfaceProBar;
    private long destinationPackId;



    public EnquiryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        toolbarListerner=(ToolbarListerner) getContext();
        View itemView =  inflater.inflate(R.layout.fragment_enquiry, container, false);
        TextView toolTitle=itemView.findViewById(R.id.toolbar_back_title);
        itemView.findViewById(R.id.toolbar_go_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarListerner.onBackPressedForFragment();
            }
        });
        toolTitle.setText(toolbarListerner.getToolbarTitle());
        interfaceProBar=(IProgressBarListener) getContext();
        enquiryDetailListener=(EnquiryDetail) getContext();

        enableViewAndActions(itemView);
        assert enquiryDetailListener != null;
        if(enquiryDetailListener.getEnquiryId()!=0){
            if(enquiryDetailListener.getDestinationName()!=null ){
                EditText destinationEt = itemView.findViewById(R.id.enquiry_destination);
                destinationPackId=enquiryDetailListener.getEnquiryId();
                destinationEt.setText(enquiryDetailListener.getDestinationName());
                destinationPackId=enquiryDetailListener.getEnquiryId();
                enquiryDetailListener.setEnquiryId(0);
                enquiryDetailListener.setDestinationName(null);
            }
        }

        return itemView;
     }

     private void enableViewAndActions(final View itemView){

         final EditText datPickerEt=itemView.findViewById(R.id.enquiry_dateEt);
         mCalendar = Calendar.getInstance();
         date = new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker view, int year, int monthOfYear,
                                   int dayOfMonth) {
                 // TODO Auto-generated method stub
                 mCalendar.set(Calendar.YEAR, year);
                 mCalendar.set(Calendar.MONTH, monthOfYear);
                 mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                 updateDate(datPickerEt);
             }
         };

         datPickerEt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, mCalendar
                         .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                         mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                updateDate(datPickerEt);
             }
         });

         itemView.findViewById(R.id.enquiry_send_btn).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 itemView.findViewById(R.id.enquiry_send_btn).setEnabled(false);
                 getDataAndSend(itemView);
             }
         });

         itemView.findViewById(R.id.enquiry_destination).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 enquiryDetailListener.setEnquiryFrag(true);
                 Function.fragTransactionNoBackStack(R.id.holder_frame,new SearchFragment(),getContext());
             }
         });

         itemView.findViewById(R.id.enquiry_call_btn).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Function.makeCall(getContext());
             }
         });
     }

    private void getDataAndSend(final View itemView) {
        boolean allTrue=true;
        interfaceProBar.startProgressBar("Sending your Enquiry...");
        InputValidation validation=new InputValidation(getContext());
        EditText destinationEt = itemView.findViewById(R.id.enquiry_destination);
        EditText dateEt = itemView.findViewById(R.id.enquiry_dateEt);
        EditText departLocationEt=itemView.findViewById(R.id.enquiry_daparture);
        EditText phoneNoEt=itemView.findViewById(R.id.enquiry_phone);
        EditText userEmailEt=itemView.findViewById(R.id.enquiry_email);

        String departure=departLocationEt.getText().toString();
        String email=userEmailEt.getText().toString();
        String travelDate=dateEt.getText().toString();
        String phoneNo=phoneNoEt.getText().toString();

        if(!validation.isETEmpty(dateEt)){

        }else {
            allTrue=false;
            dateEt.setError("Please Select Travel Date");
        }

        if(!validation.isETEmpty(userEmailEt) && validation.isEmail(email)){

        }else {
            allTrue=false;
            userEmailEt.setError("Please Enter valid Email address");
        }

        if(!validation.isETEmpty(departLocationEt)){

        }else{
            allTrue=false;
            departLocationEt.setError("Please Choose a Destination");
        }

        if(!validation.isETEmpty(phoneNoEt) && validation.isPhoneNumber(phoneNo)){

        }else {
            phoneNo=null;
        }

        if(allTrue){

            long user_id=0;
            if(new ChardhamPreference(getContext()).getLoginStatus()){
                user_id=new ChardhamPreference(getContext()).getUserId();
            }
            Call<CommonCall> createEnquiry = ApiClient.getRetrofit().create(ApiInterface.class).requestQuery(departure,user_id,phoneNo,email,travelDate,destinationPackId);
            createEnquiry.enqueue(new Callback<CommonCall>() {
                @Override
                public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                    if(response.isSuccessful()){
                        if(response.body().getMessage().equalsIgnoreCase("200")){
                            try {
                                interfaceProBar.stopProgressBar();
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Enquiry Sent")
                                        .setMessage("Your Query has been sent to us.We will reach you within 24-48 hours")
                                        .setCancelable(false)
                                        .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                toolbarListerner.onBackPressedForFragment();
                                            }
                                        })
                                        .setIcon(R.drawable.ic_message_24dp)
                                        .show();
                            }catch(Exception e){
                                itemView.findViewById(R.id.enquiry_send_btn).setEnabled(true);
                                interfaceProBar.stopProgressBar();
                            }
                            Log.d("ResponseCheck:",response.body().getMessage());

                        }
                        interfaceProBar.stopProgressBar();
                      }
                }
                @Override
                public void onFailure(Call<CommonCall> call, Throwable t) {
                    Log.d("ResponseCheck:","Failed");
                    interfaceProBar.stopProgressBar();
                    toolbarListerner.onBackPressedForFragment();
                    itemView.findViewById(R.id.enquiry_send_btn).setEnabled(true);
                }
            });
        }else{
            interfaceProBar.stopProgressBar();
            new AlertDialog.Builder(getContext())
                    .setTitle("Required Fields are Empty")
                    .setMessage("Please fill all the required details for processing your booking")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.ic_message_24dp)
                    .show();
            itemView.findViewById(R.id.enquiry_send_btn).setEnabled(true);
        }
    }

    private void updateDate(EditText dateEt) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEt.setText(sdf.format(mCalendar.getTime()));
    }

}
