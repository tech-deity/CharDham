package in.chardhamtour.chardhamyatra.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import in.chardhamtour.chardhamyatra.R;

public class WebView extends AppCompatActivity {

    private android.webkit.WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        String url=getIntent().getStringExtra("web_view_url");

        if(url!=null){
            webView = findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
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
                new androidx.appcompat.app.AlertDialog.Builder(WebView.this)
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

}
