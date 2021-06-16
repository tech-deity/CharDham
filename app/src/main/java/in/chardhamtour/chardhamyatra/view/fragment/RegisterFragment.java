package in.chardhamtour.chardhamyatra.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.ILoginListener;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = RegisterFragment.class.getSimpleName();
    private Button registerBtn;
    private TextView alreadyRegister;
    private View layViews;
    private ProgressBar regProg;
    private ChardhamPreference preference;
    private Context context;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layViews = inflater.inflate(R.layout.fragment_register, container, false);
        preference=new ChardhamPreference(context);


        registerBtn=layViews.findViewById(R.id.ru_register_btn);
        regProg = layViews.findViewById(R.id.reg_progressBar);

        registerBtn.setOnClickListener(this);
        alreadyRegister=layViews.findViewById(R.id.ru_already_member);
        alreadyRegister.setOnClickListener(this);

        return layViews;
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
            case R.id.ru_register_btn:
                if(Function.isNetworkConnected(getActivity())){
                    getUserDetail();
                    Function.hideKeyboardFromActivity(getActivity());
                }else{
                    Function.toast(context,getString(R.string.no_internet),Toast.LENGTH_SHORT);
                }
                break;

            //Skip_button
            case R.id.ru_already_member:
                Function.fragTransaction(R.id.lr_frame_layout,new LoginFragment(),getContext());
                break;
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.context=activity;
    }

    private void getUserDetail(){

        Function.handleLogRegProgress(registerBtn,regProg,"",true);
        TextInputLayout nameTIL,usernameTIL,passwordTIL;

        nameTIL = layViews.findViewById(R.id.ru_name_til);
        usernameTIL = layViews.findViewById(R.id.ru_email_til);
        passwordTIL = layViews.findViewById(R.id.ru_password_til);

        String username,password,name;
        name = Objects.requireNonNull(nameTIL.getEditText()).getText().toString().trim();
        username = Objects.requireNonNull(usernameTIL.getEditText()).getText().toString().trim();
        password = Objects.requireNonNull(passwordTIL.getEditText()).getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) ) {
            createUser(name,username,password,"manual",null);
        }else{
            Function.toast(context,"Please Fill All Required Fields",Toast.LENGTH_SHORT);
            Function.handleLogRegProgress(registerBtn,regProg,"Create user",false);
        }

    }

    private void createUser(final String name, final String username, final String password, final String client, final String client_id) {
        ((IProgressBarListener)context).startProgressBar("Signing you in..");

        Call<CommonCall> registerCall= ApiClient.getRetrofit().create(ApiInterface.class)
                .registerUser(name,username,password,client,client_id);
        registerCall.enqueue(new Callback<CommonCall>() {
            @Override
            public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                if(response.body().getServ_res().equalsIgnoreCase("Ok")){
                    // User Error
                    if(response.body().getMessage()!=null){
                        String code = response.body().getMessage();
                        Log.d(TAG,"response?:"+code);
                           switch (code){
                               case "201":
                                   preference.clearPreference();
                                   ((ILoginListener) context).onLoginStart(name,username,password,client,client_id,null);
                                   break;
                               case "206":
                                   Function.toast(context, "User already registered, login with password", Toast.LENGTH_SHORT);
                                   break;
                               case "208":
                                   Function.toast(context, "Please try again later", Toast.LENGTH_SHORT);
                                   break;
                           }
                    }else {
                        Function.toast(context, "Message Null", Toast.LENGTH_SHORT);
                    }
                    Function.handleLogRegProgress(registerBtn,regProg,"Create user",false);
                }
                ((IProgressBarListener)context).stopProgressBar();
            }
            @Override
            public void onFailure(Call<CommonCall> call, Throwable t) {
                Log.d(TAG,"Failure Connection");
                ((IProgressBarListener)context).stopProgressBar();
            }
        });
    }

}
