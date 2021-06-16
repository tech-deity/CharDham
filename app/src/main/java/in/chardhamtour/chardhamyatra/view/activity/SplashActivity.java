package in.chardhamtour.chardhamyatra.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.utils.Function;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private int REQUEST_CODE=112;

    private String[] REQUIRED_PERMISSIONS={
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startAnimations();
        if(!hasRequiredPermission(this,REQUIRED_PERMISSIONS)){
            ActivityCompat.requestPermissions(this,REQUIRED_PERMISSIONS,REQUEST_CODE);
        }else{
            setupThings();
        }
    }

    private void startAnimations() {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1800);
        rotate.setInterpolator(new LinearInterpolator());
        findViewById(R.id.logo_rotate).startAnimation(rotate);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.top_sky).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.top_to_bottom_anim));
                findViewById(R.id.top_left_heli).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.left_to_right_anim));
                findViewById(R.id.top_right_heli).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.right_to_left_anim));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.progress).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void setupThings() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Function.isNetworkConnected(SplashActivity.this)){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();}
                else{
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("Oops ! No Internet")
                            .setMessage("Please Connect your Internet and try again")
                            .setCancelable(false)
                            .setPositiveButton("Retry",
                                    new DialogInterface.OnClickListener() {
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
        },3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CODE==112) {
                if(hasRequiredPermission(this,REQUIRED_PERMISSIONS)){
                    setupThings();
                }else {
                    openPermissionAlert();
                }

            } else {
            openPermissionAlert();
            }
    }

    private void openPermissionAlert(){
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Permissions Required")
                .setMessage("Grant All Permission to work chardham tour properly")
                .setCancelable(false)
                .setPositiveButton("Grant Permission", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SplashActivity.this,REQUIRED_PERMISSIONS,REQUEST_CODE);
                    }
                }).setNegativeButton("cancle",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        })
                .show();
    }


    private boolean hasRequiredPermission(Context ctx, String[] permissions) {
        boolean isPermission=true;
        for(String per : permissions){
            int result=ctx.checkCallingOrSelfPermission(per);
            if(result!=PackageManager.PERMISSION_GRANTED){
                isPermission=false;
            }
        }
        return isPermission;
    }

}
