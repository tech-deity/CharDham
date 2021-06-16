package in.chardhamtour.chardhamyatra.view.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.BookingListener;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.InputValidation;
import in.chardhamtour.chardhamyatra.model.ItineraryModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepOneFragment extends Fragment {

    private static final String TAG = "StepOneFragment";
    private View itemView;
    private Context context;
    private IProgressBarListener interfaceProBar;
    private BookingListener bookingListener;

    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener date;


    public StepOneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_step_one, container, false);
        bookingListener = (BookingListener) context;
        interfaceProBar=(IProgressBarListener) context;
        EditText destinationEt = itemView.findViewById(R.id.enquiry_destination);
        destinationEt.setText(bookingListener.getSubPackage().getName());
        TextView toolTitle=itemView.findViewById(R.id.toolbar_back_title);
        ImageView backBtn=itemView.findViewById(R.id.toolbar_go_back_iv);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        toolTitle.setText("Booking");
        enableViewAndActions(itemView);
        setUpBookingDetail();
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
                getDataAndSend(itemView);
            }
        });


    }

    private void updateDate(EditText dateEt) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEt.setText(sdf.format(mCalendar.getTime()));
        TextView tourDate=itemView.findViewById(R.id.booking_detail_layout).findViewById(R.id.placeTourDate);
        tourDate.setText(sdf.format(mCalendar.getTime()));
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private void getDataAndSend(View itemView) {

        interfaceProBar.startProgressBar("Please Wait....");

        boolean allTrue=true;
        InputValidation validation=new InputValidation(getContext());
        EditText dateEt = itemView.findViewById(R.id.enquiry_dateEt);

        EditText departCityEt=itemView.findViewById(R.id.enquiry_city);
        EditText departStateEt=itemView.findViewById(R.id.enquiry_state);
        EditText departCountryEt=itemView.findViewById(R.id.enquiry_country);

        EditText phoneNoEt=itemView.findViewById(R.id.enquiry_phone);
        EditText userEmailEt=itemView.findViewById(R.id.enquiry_email);

        String departure=departCityEt.getText().toString()+","
                + departStateEt.getText().toString() +
                ","+ departCountryEt.getText().toString();

        String email=userEmailEt.getText().toString();
        String travelDate=dateEt.getText().toString();
        String phoneNo=phoneNoEt.getText().toString();

        if(validation.isETEmpty(dateEt)){
            allTrue=false;
            dateEt.setError("Please Select Travel Date");
        }

        if(validation.isETEmpty(userEmailEt) && !validation.isEmail(email)){
            allTrue=false;
            userEmailEt.setError("Please Enter valid Email address");
        }

        if(validation.isETEmpty(departCityEt) ||validation.isETEmpty(departStateEt) ||validation.isETEmpty(departCountryEt)){
            allTrue=false;
            departCityEt.setError("Required");
            departStateEt.setError("Required");
            departCountryEt.setError("Required");
        }

        if(validation.isETEmpty(phoneNoEt) && !validation.isPhoneNumber(phoneNo)){
            allTrue=false;
            phoneNoEt.setError("Phone Number Required");
        }

        if(allTrue){
            long user_id=new ChardhamPreference(context).getUserId();
            Call<CommonCall> createEnquiry = ApiClient.getRetrofit().create(ApiInterface.class).requestQuery(departure,user_id,phoneNo,
                    email,travelDate,bookingListener.getSubPackage().getId());
            createEnquiry.enqueue(new Callback<CommonCall>() {
                @Override
                public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                    if(response.isSuccessful()){
                        Function.fragTransaction(R.id.booking_frame,new StepTwoFragment(),context);
                        Log.d("ResponseCheck:",response.body().getMessage());
                        interfaceProBar.stopProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<CommonCall> call, Throwable t) {
                    Log.d("ResponseCheck:","Failed");
                    interfaceProBar.stopProgressBar();
                }
            });
            bookingListener.saveStepOneData(departure,dateEt.getText().toString(),email,phoneNo);
        }else{
            interfaceProBar.stopProgressBar();
            new AlertDialog.Builder(context)
                    .setTitle("Required Fields are Empty")
                    .setMessage("Please fill all the required details for processing your booking")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

   private void setUpBookingDetail(){
           View booking_detail_layout=itemView.findViewById(R.id.booking_detail_layout);
           final TextView tourName,tourPrice,tourDuration,tourDestination;
           tourName=booking_detail_layout.findViewById(R.id.placeName);
           tourDuration=booking_detail_layout.findViewById(R.id.placeDuration);
           tourPrice=booking_detail_layout.findViewById(R.id.placePrice);
           tourDestination=booking_detail_layout.findViewById(R.id.placeCovered);
       ItineraryModel program=bookingListener.getProgram();
       tourName.setText(program.getTourName());
       tourDuration.setText(program.getTourDuration());
       tourDestination.setText(program.getDestinationCovered());
       String priceString= Html.fromHtml("&#x20b9;")+program.getTourPrice()+"/ Person";
       tourPrice.setText(priceString);
    }


}
