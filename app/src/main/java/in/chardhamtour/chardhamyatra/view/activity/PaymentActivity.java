package in.chardhamtour.chardhamyatra.view.activity;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import in.chardhamtour.chardhamyatra.model.ItineraryModel;
import in.chardhamtour.chardhamyatra.model.MemberModel;
import in.chardhamtour.chardhamyatra.model.StepOneModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentActivity extends Activity implements PaymentResultListener {

    private static final String TAG = PaymentActivity.class.getSimpleName();
    private Context context;
    private String bookingid;
    private android.app.AlertDialog dialog;
    private ArrayList<MemberModel> memberlist;
    private ItineraryModel program;
    private SubPackagesModel subPackagesModel;
    private StepOneModel stepOneModel;
    private ProgressManager progressManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        startProgressBar("Processing Payment...");
        subPackagesModel= (SubPackagesModel) getIntent().getSerializableExtra("subpackage");
        program= (ItineraryModel) getIntent().getSerializableExtra("program");
        Bundle args = getIntent().getBundleExtra("BUNDLE");
        memberlist = (ArrayList<MemberModel>) args.getSerializable("memberlist");
        Log.d(TAG,"size:"+memberlist.size());
        stepOneModel= (StepOneModel) getIntent().getSerializableExtra("step_one_data");
        Log.d(TAG,stepOneModel.getContact());
        context=this;
        startPayment();
    }






    public void startProgressBar(String title) {
        progressManager=new ProgressManager(this);
        progressManager.startProgress(title,false);
    }


    public void stopProgressBar() {
        progressManager.stopProgress();
    }

    private String createJSON(String successfulPaymentId){

        ChardhamPreference preference=new ChardhamPreference(context);
        JSONObject jObjectData = new JSONObject();
        JSONArray membersArray=new JSONArray();

        try {
            jObjectData.put("user_id", preference.getUserId());
            jObjectData.put("sub_package_id", subPackagesModel.getId());
            jObjectData.put("program_id", program.getId());
            jObjectData.put("transaction_id", successfulPaymentId);
            jObjectData.put("status", "1");
            jObjectData.put("departure", stepOneModel.getDeparture());
            jObjectData.put("price", program.getTourPrice());
            jObjectData.put("departure_date", stepOneModel.getTravelDate());

            for (MemberModel member: memberlist){
                JSONObject membersJsonObject = new JSONObject();
                membersJsonObject.put("name", member.getName());
                membersJsonObject.put("gender", member.getGender());
                membersJsonObject.put("age", member.getAge());
                membersArray.put(membersJsonObject);
            }
            jObjectData.put("members", membersArray);
        }catch (Exception e){
            Log.d(TAG,"Exception:"+e);
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            overridePendingTransition(0, 0);
        }
        Log.d(TAG,"original_data"+jObjectData.toString());
        return jObjectData.toString();
    }

    public void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.merchant_name));
            String desc=program.getTourDuration()+" package";
            options.put("description", desc);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            Integer price=memberlist.size()*Integer.parseInt(program.getTourPrice())*100;
            options.put("amount", price);
            JSONObject preFill = new JSONObject();
            preFill.put("email", stepOneModel.getEmail());
            preFill.put("contact", stepOneModel.getContact());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG,"startPayment:"+e.getMessage());
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            overridePendingTransition(0, 0);
            e.printStackTrace();
            stopProgressBar();
        }
    }
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            bookingid=razorpayPaymentID;
            savePackageToServer(createJSON(razorpayPaymentID));
            Function.toast(this, "Payment Successful",Toast.LENGTH_SHORT);
        } catch (Exception e) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            overridePendingTransition(0, 0);
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Function.toast(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT);
            new AlertDialog.Builder(this)
                    .setTitle("Payment unsuccessful")
                    .setMessage("Something went wrong with the payment")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
                            overridePendingTransition(0, 0);
                        }
                    })
                    .setIcon(R.drawable.ic_online_booking)
                    .show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            overridePendingTransition(0, 0);
        }
    }

    private void savePackageToServer(String jsonData) {
        Log.d(TAG,"final calling data:"+jsonData);
        if(!TextUtils.isEmpty(jsonData)){
            final Call<CommonCall> book = ApiClient.getRetrofit().create(ApiInterface.class).startBooking(jsonData);
            book.enqueue(new Callback<CommonCall>() {
                @Override
                public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                    Log.d(TAG,"res:"+response.body().getServ_res());
                    if(response.isSuccessful()){
                        stopProgressBar();
                        new AlertDialog.Builder(PaymentActivity.this)
                                .setTitle("Booking Confirmed")
                                .setMessage("Your Payment for package "+program.getTourDuration()+" is successfully completed with Booking Id:"+bookingid)
                                .setCancelable(false)
                                .setPositiveButton("Go back", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        overridePendingTransition(0, 0);
                                        startActivity(new Intent(PaymentActivity.this,MainActivity.class));
                                        overridePendingTransition(0, 0);
                                    }
                                })
                                .setIcon(R.drawable.ic_online_booking)
                                .show();
                        Log.d(TAG,"message:"+response.body().getMessage());
                    }
                    Log.d(TAG,"message:"+response.body().getMessage());
                    stopProgressBar();
                }
                @Override
                public void onFailure(Call<CommonCall> call, Throwable t) {
                    Toast.makeText(context, "Server Failed", Toast.LENGTH_SHORT).show();
                    stopProgressBar();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(new Intent(PaymentActivity.this,MainActivity.class));
                    overridePendingTransition(0, 0);
                }});
        }else {
            Toast.makeText(context, "Booking Data Error", Toast.LENGTH_SHORT).show();
            stopProgressBar();
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(PaymentActivity.this,MainActivity.class));
            overridePendingTransition(0, 0);
        }

    }
}

