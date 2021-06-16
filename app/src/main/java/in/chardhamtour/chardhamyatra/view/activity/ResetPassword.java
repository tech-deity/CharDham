package in.chardhamtour.chardhamyatra.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import in.chardhamtour.chardhamyatra.view.fragment.LoginFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.*;

public class ResetPassword extends AppCompatActivity {

    private static final String TAG = ResetPassword.class.getSimpleName();
    private EditText emailEt,OtpEt,PasswordEt;
    TextView passwordTv;
    private String userEmail;
    private Button sendOtpBtn,verifyBtn;
    private String randomOtp;
    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        randomOtp=null;
        emailEt=findViewById(R.id.email);
        PasswordEt=findViewById(R.id.password);
        findViewById(R.id.text_enter).setVisibility(View.GONE);
        PasswordEt.setVisibility(View.GONE);

        OtpEt=findViewById(R.id.otp);
        OtpEt.setVisibility(View.GONE);

        sendOtpBtn=findViewById(R.id.send_otp);
        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressManager=new ProgressManager(ResetPassword.this);
                String email=emailEt.getText().toString().trim();
                sendEmail(email);
            }
        });
        passwordTv=findViewById(R.id.password_tv);
        passwordTv.setVisibility(View.GONE);
        verifyBtn=findViewById(R.id.verify_otp);
        verifyBtn.setEnabled(false);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });
        String email=new ChardhamPreference(this).getEmail();
        Log.d(TAG, "onCreate: Chardham" +
                "Username:"+email+"old"+new ChardhamPreference(this).getEmail());
        if(email!=null && email.length()>16){
            emailEt.setText(email);
        }
    }

    public void sendEmail(final String email){

             if(!TextUtils.isEmpty(email)){
                 progressManager.startProgress("Sending OTP please wait",false);
                 generateOtp();
                 ApiClient.getRetrofit().create(ApiInterface.class).resetPassword(email,randomOtp,1).enqueue(new Callback<CommonCall>() {
                     @Override
                     public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                         if(response.isSuccessful() && response.body()!=null && response.body().getServ_res()!=null){
                             Log.d(TAG,"Getting Response"+response.body().getServ_res());
                             if(response.body().getServ_res().equalsIgnoreCase("ok")){
                                 if(response.body().getMessage()!=null){
                                     String code = response.body().getMessage();
                                     if(code.equalsIgnoreCase("201")){
                                         enableOtpVerification(true);
                                         handleOtpButton(30);
                                         userEmail=email;
                                         progressManager.stopProgress();
                                         Function.toast(ResetPassword.this,"Enter Password and OTP",Toast.LENGTH_SHORT);
                                     }
                                     if(code.equalsIgnoreCase("208")) {
                                           randomOtp=null;
                                           progressManager.stopProgress();
//                                         Function.fragTransaction(R.id.lr_frame_layout,new LoginFragment(),ResetPassword.this);
                                           Function.toast(ResetPassword.this,"User don't Exist",Toast.LENGTH_SHORT);
                                     }
                                 }else {
                                     progressManager.stopProgress();
                                     Function.toast(ResetPassword.this, "Message Null", Toast.LENGTH_SHORT);
                                 }}}}
                     @Override
                     public void onFailure(Call<CommonCall> call, Throwable t) {
                         progressManager.stopProgress();
                         Function.toast(ResetPassword.this,"Connection Error",Toast.LENGTH_SHORT);
                     }
                 });
             }else{
                 Function.toast(this,"Email can't be empty", Toast.LENGTH_SHORT);
             }
    }

    private void enableOtpVerification(boolean bool) {

        if(bool){
            sendOtpBtn.setText("Resend Otp");
            verifyBtn.setEnabled(true);
            passwordTv.setVisibility(View.VISIBLE);
            PasswordEt.setVisibility(View.VISIBLE);
            OtpEt.setVisibility(View.VISIBLE);
            findViewById(R.id.text_enter).setVisibility(View.VISIBLE);

        }else{
            sendOtpBtn.setText("Send Otp");
            verifyBtn.setEnabled(false);
            PasswordEt.setVisibility(View.GONE);
            OtpEt.setVisibility(View.GONE);
            passwordTv.setVisibility(View.GONE);
            findViewById(R.id.text_enter).setVisibility(View.GONE);
        }

    }

    private void generateOtp(){
        randomOtp=String.valueOf(Function.getRandomNumber(1000,9999));
        Log.d(TAG,randomOtp);
    }

    private void verifyOtp(){
        String otpValue=OtpEt.getText().toString();
        String newPassword=PasswordEt.getText().toString().trim();
        if(!TextUtils.isEmpty(newPassword) && newPassword.length()>7){
            if((otpValue.length()==4)){
                if(otpValue.equals(randomOtp)){
                    updatePassword(newPassword);
                }else {
                    Function.toast(this,"Incorrect OTP",Toast.LENGTH_SHORT);
                }
            }else{
                Function.toast(this,"Enter a correct 4 digit Otp",Toast.LENGTH_SHORT);
            }
        }else{
            Function.toast(this,"Enter a valid password, minimum 8 characters ",Toast.LENGTH_SHORT);
        }
    }

    private void handleOtpButton(final int maxSize)
    {
        final Timer timer = new Timer();
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            int loop=maxSize;
            public void run()
            {
                if(loop==0){
                    timer.cancel();
                    sendOtpBtn.setText("Resend Otp");
                    sendOtpBtn.setEnabled(true);
                }else{
                    sendOtpBtn.setEnabled(false);
                    sendOtpBtn.setText("wait 00:"+String.format("%02d", loop));
                    --loop;
                }
            }
        };



        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 1000);
    }


    private void updatePassword(String newPassword) {
        final ProgressManager progress=new ProgressManager(this);
        progress.startProgress("Updating Your Password",false);
            ApiClient.getRetrofit().create(ApiInterface.class).resetPassword(userEmail,newPassword,2).enqueue(new Callback<CommonCall>() {
                @Override
                public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                    if(response.isSuccessful() && response.body()!=null && response.body().getServ_res()!=null){
                        Log.d(TAG,"Getting Response"+response.body().getServ_res());
                        if(response.body().getServ_res().equalsIgnoreCase("ok")){
                            if(response.body().getMessage()!=null){
                                String code = response.body().getMessage();
                                if(code.equalsIgnoreCase("201")){
                                    Function.toast(ResetPassword.this,"Password Updated",Toast.LENGTH_SHORT);
                                    progress.stopProgress();
                                    //Function.fragTransaction(R.id.lr_frame_layout,new LoginFragment(),ResetPassword.this);
                                    Intent startLogin=new Intent(ResetPassword.this, LoginAndRegisterActivity.class);
                                    startLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startLogin);
                                }
                                if(code.equalsIgnoreCase("208")) {
                                    progress.stopProgress();
                                    Function.toast(ResetPassword.this,"Password updation fail",Toast.LENGTH_SHORT);
                                }
                            }else {
                                progress.stopProgress();
                                Function.toast(ResetPassword.this, "Message Null", Toast.LENGTH_SHORT);
                            }}}}
                @Override
                public void onFailure(Call<CommonCall> call, Throwable t) {
                    progress.stopProgress();
                    Function.toast(ResetPassword.this,"Connection Error",Toast.LENGTH_SHORT);
                }
            });
    }

}
