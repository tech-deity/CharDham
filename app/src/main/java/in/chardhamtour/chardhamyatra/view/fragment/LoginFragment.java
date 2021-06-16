package in.chardhamtour.chardhamyatra.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.listeners.ILoginListener;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.view.activity.ResetPassword;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    private View layViews;
    private Button loginBtn;
    private TextView resetPasswordTv;
    private Context context;
    private ILoginListener iLoginListener;


    public LoginFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layViews= inflater.inflate(R.layout.fragment_login, container, false);
        iLoginListener= (ILoginListener) context;
        initViews();
        return layViews;
    }

    private void initViews() {

        loginBtn=layViews.findViewById(R.id.lu_login_btn);
        resetPasswordTv=layViews.findViewById(R.id.lu_reset_password);
        TextView notMember = layViews.findViewById(R.id.lu_non_member);

        /* Initialize Listeners*/
        loginBtn.setOnClickListener(this);
        resetPasswordTv.setOnClickListener(this);
        notMember.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
            //manual Login
            case R.id.lu_login_btn:
                if(Function.isNetworkConnected(getActivity())){
                    loginUser();
                    Function.hideKeyboardFromActivity(getActivity());
                }else{
                    Function.toast(context,getString(R.string.no_internet),Toast.LENGTH_SHORT);
                }
                break;

                //Skip_button
            case R.id.lu_non_member:
                Function.fragTransaction(R.id.lr_frame_layout,new RegisterFragment(),getContext());
                break;

            case R.id.lu_reset_password:
                startActivity(new Intent(context, ResetPassword.class));
                break;

        }
    }


    private void loginUser() {

        TextInputLayout usernameTIL,passwordTIL;
        ProgressBar loginProg = layViews.findViewById(R.id.login_progressBar);
        usernameTIL = layViews.findViewById(R.id.lu_username);
        passwordTIL=layViews.findViewById(R.id.lu_password);

        Log.d(TAG,"Count:"+usernameTIL.getChildCount());

        String username,password;

        Function.handleLogRegProgress(loginBtn,loginProg,"",true);

        username = Objects.requireNonNull(usernameTIL.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(passwordTIL.getEditText()).getText().toString().trim();


        if( !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) ) {
            Log.d(TAG,"username:"+username+"pass:"+password);
            iLoginListener.onLoginStart(null,username,password,"manual",null,null);
            //Function.connectUser(context,null,username,password,"manual",null,null);
            try {
                Thread.sleep(500);
                Function.handleLogRegProgress(loginBtn,loginProg,"Login",false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            Function.toast(context,"Please Fill correct email and password",Toast.LENGTH_SHORT);
            Function.handleLogRegProgress(loginBtn,loginProg,"Login",false);
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        context=activity;
        super.onAttach(activity);
    }

}
