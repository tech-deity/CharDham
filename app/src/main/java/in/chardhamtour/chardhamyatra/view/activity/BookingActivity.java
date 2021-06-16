package in.chardhamtour.chardhamyatra.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.listeners.BookingListener;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import in.chardhamtour.chardhamyatra.model.ItineraryModel;
import in.chardhamtour.chardhamyatra.model.StepOneModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import in.chardhamtour.chardhamyatra.view.fragment.StepOneFragment;

public class BookingActivity extends AppCompatActivity implements BookingListener, IProgressBarListener {

    private static final String TAG = "BookingActivity";
    private ItineraryModel program;
    private SubPackagesModel packModel;
    private StepOneModel model;
    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        program=(ItineraryModel) getIntent().getSerializableExtra("program");
        packModel=(SubPackagesModel) getIntent().getSerializableExtra("packModel");
        Log.d(TAG,"program"+program.getName()+" pack"+packModel.getName());

        if(program!=null){
            if(packModel!=null){
                model=new StepOneModel();
                Function.fragTransactionNoBackStack(R.id.booking_frame,new StepOneFragment(),this);
            }else {
                this.finish();
            }
        }else {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mConnReceiver);
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);


            if(currentNetworkInfo.isConnected()){

            }else{
                new androidx.appcompat.app.AlertDialog.Builder(BookingActivity.this)
                        .setTitle("Oops ! No Internet")
                        .setMessage("Please Connect your Internet and try again")
                        .setCancelable(false)
                        .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .show();
            }
        }
    };

    @Override
    public SubPackagesModel getSubPackage() {
        return packModel;
    }

    @Override
    public ItineraryModel getProgram() {
        return program;
    }

    @Override
    public void saveStepOneData(String departure,String departureDate,String email,String phoneNumber) {
            model.setEmail(email);
            model.setDeparture(departure);
            model.setContact(phoneNumber);
            model.setTravelDate(departureDate);
    }

    @Override
    public StepOneModel getStepOneData() {
        return model;
    }

    @Override
    public void startProgressBar(String title) {
        progressManager=new ProgressManager(this);
        progressManager.startProgress(title,false);
    }

    @Override
    public void stopProgressBar() {
        progressManager.stopProgress();
    }


}
