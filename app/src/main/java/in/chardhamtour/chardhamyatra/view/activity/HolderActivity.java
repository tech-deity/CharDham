package in.chardhamtour.chardhamyatra.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import in.chardhamtour.chardhamyatra.controller.listeners.EnquiryDetail;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.listeners.IResponseListener;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.listeners.HeaderRvListeners;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import in.chardhamtour.chardhamyatra.view.fragment.AddReviewFragment;
import in.chardhamtour.chardhamyatra.view.fragment.EnquiryFragment;
import in.chardhamtour.chardhamyatra.view.fragment.MyBookingFragment;
import in.chardhamtour.chardhamyatra.view.fragment.MyQueriesFragment;
import in.chardhamtour.chardhamyatra.view.fragment.PackageListFragment;
import in.chardhamtour.chardhamyatra.view.fragment.SearchFragment;
import in.chardhamtour.chardhamyatra.view.fragment.ToolbarFragment;
import in.chardhamtour.chardhamyatra.view.fragment.WishlistFragment;

public class HolderActivity extends AppCompatActivity implements ToolbarListerner, IResponseListener,EnquiryDetail, HeaderRvListeners, IProgressBarListener {


    private static final String TAG = "HolderActivity";
    private String toolbarTitle;
    private long currentPackId;
    private String currentDestination;
    private Fragment attachedFragment;
    private SubPackagesModel enquiryModel;
    private PackageModel model;
    private int isRelated=0;
    private AlertDialog dialog;
    private ProgressManager progressManager;
    private boolean isEnquiry=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        String from=getIntent().getStringExtra("from");
        Log.d(TAG,"from:"+from);
        if(from!=null && !from.equals("")){
            checkAndStartFragment(from);
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
                new androidx.appcompat.app.AlertDialog.Builder(HolderActivity.this)
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
    public void startProgressBar(String title) {
        progressManager=new ProgressManager(this);
        progressManager.startProgress(title,false);
    }

    @Override
    public void stopProgressBar() {
        progressManager.stopProgress();
    }

    private void checkAndStartFragment(String from) {

        switch (from) {
            case "search":
                attachedFragment = new SearchFragment();
               // Function.fragTransactionNoBackStack(R.id.holder_frame, attachedFragment, this);
                break;

            case "package":
                model = (PackageModel) getIntent().getSerializableExtra("object");
                Log.d(TAG, "object_name:" + model.getName());
                if (model != null) {
                    toolbarTitle = model.getName();
                    currentPackId = model.getId();
                    attachedFragment = new PackageListFragment();
                }
                break;

            case "quotes":
                attachedFragment = new EnquiryFragment();
                toolbarTitle = "Enquiry Us";
                enquiryModel = (SubPackagesModel) getIntent().getSerializableExtra("object");
                if (enquiryModel != null) {
                    currentPackId = enquiryModel.getId();
                    currentDestination = enquiryModel.getName();
                }
                Log.d(TAG, "quotes:isEnquiry->" + isEnquiry + ",pack_id:" + currentPackId + " currentDest:" + currentDestination);
                break;

            case "related":
                model = (PackageModel) getIntent().getSerializableExtra("object");
                Log.d(TAG, "object_name:" + model.getName());
                if (model != null) {
                    toolbarTitle = "Related Packages";
                    currentPackId = model.getId();
                    isRelated = 1;
                    attachedFragment = new PackageListFragment();
                }
                break;


            case "bookings":
                Log.d(TAG,"In Booking");
                toolbarTitle = "My Bookings";
                attachedFragment=new MyBookingFragment();
                break;

            case "queries":
                Log.d(TAG,"In Queries");
                toolbarTitle = "My Queries";
                attachedFragment=new MyQueriesFragment();
                break;

            case "wishlist":
                    Log.d(TAG,"In WishList");
                    toolbarTitle = "My WishList";
                    attachedFragment=new WishlistFragment();
                    break;

             case "review":
                    Log.d(TAG,"In Review");
                    toolbarTitle = "null";
                    long id=getIntent().getLongExtra("id",0);
                    currentPackId=id;
                    attachedFragment=new AddReviewFragment();
                    break;

            default:
                toolbarTitle = from;
                attachedFragment=new ToolbarFragment();
                break;
        }

        Function.fragTransactionNoBackStack(R.id.holder_frame, attachedFragment, this);
    }

    @Override
    public void onBackPressedForFragment() {
        onBackPressed();
    }

    @Override
    public String getToolbarTitle() {
        return toolbarTitle;
    }

    @Override
    public long getPackId() {
        return currentPackId;
    }

    @Override
    public int isRelated() {
        return isRelated;
    }

    @Override
    public long getEnquiryId() {
        return currentPackId;
    }

    @Override
    public String getDestinationName() {
        return currentDestination;
    }

    @Override
    public void setEnquiryId(long id) {
        currentPackId=id;
    }

    @Override
    public void setDestinationName(String name) {
        currentDestination=name;
    }

    @Override
    public boolean isEnquiryFrag() {
        return isEnquiry;
    }

    @Override
    public void setEnquiryFrag(boolean bool) {
        isEnquiry=bool;
    }

    @Override
    public void onResponseSuccess(int dataSize) {
        ((PackageListFragment)attachedFragment).onResponseSuccess(dataSize);
    }

    @Override
    public void onResponseFailed() {
        ((PackageListFragment)attachedFragment).onResponseFailed();
    }
}
