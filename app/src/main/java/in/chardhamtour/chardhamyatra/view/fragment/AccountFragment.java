package in.chardhamtour.chardhamyatra.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;
import in.chardhamtour.chardhamyatra.BuildConfig;
import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.CustomAlertDialog;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.view.activity.HolderActivity;
import in.chardhamtour.chardhamyatra.view.activity.LoginAndRegisterActivity;
import in.chardhamtour.chardhamyatra.view.activity.ResetPassword;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    private static final String TAG ="Account Fragment" ;
    private View itemView,userDetailLay;
    private ChardhamPreference preference;
    private Context context;
    private TextView userloginLogoutBtn;

    public AccountFragment() {
        // Required empty public constructor
    }

    private void logoutUser(){
            String client=preference.getClient();
            if(client.equalsIgnoreCase("google")){
                signOutGoogle();
            }else if(client.equalsIgnoreCase("facebook")){
                signOutFacebook();
            }else{
                clearUser();
            }

    }

    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
        clearUser();
    }

    private void signOutGoogle() {
        GoogleSignInClient googleSignInClient;
        googleSignInClient=GoogleSignIn.getClient(context, ApiClient.getGso());
        googleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        clearUser();
                    }
                });
    }

    private void clearUser(){
        userloginLogoutBtn.setText("Login/Sign up");
        preference.setLoginStatus(false);
        preference.clearPreference();
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(getActivity().getIntent());
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        this.context=activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_account, container, false);
        preference=new ChardhamPreference(context);
        initViews();
        setAccountDetail();
        return itemView;
    }

    private void setAccountDetail() {

        if(preference.getLoginStatus()){
            userDetailLay.findViewById(R.id.header_section_detail).setVisibility(View.VISIBLE);
            TextView username=userDetailLay.findViewById(R.id.header_name);
            TextView email=userDetailLay.findViewById(R.id.header_email);
            CircleImageView profile=userDetailLay.findViewById(R.id.header_profile);
            username.setText(preference.getUserName());
            email.setText(preference.getEmail());
            String url=null;
            if(preference.getClient().equalsIgnoreCase("facebook")){
                Log.d(TAG,"facebook_url:"+preference.getProfileImage());
                url = "https://graph.facebook.com/"+preference.getClientId()+ "/picture?type=normal";
                Log.d(TAG,"facebook_url:"+url);
            }
            Glide.with(context).load(preference.getProfileImage()).placeholder(R.drawable.default_user).into(profile);
            userloginLogoutBtn.setText("Logout");
        }else{
            userloginLogoutBtn.setText("Login/Sign up");
            userDetailLay.findViewById(R.id.header_section_detail).setVisibility(View.GONE);
        }

    }

    private void initViews(){

        userDetailLay=itemView.findViewById(R.id.user_detail);
        userloginLogoutBtn=userDetailLay.findViewById(R.id.header_login_tv);

        itemView.findViewById(R.id.my_bookings).setOnClickListener(this);
        itemView.findViewById(R.id.my_queries).setOnClickListener(this);
        itemView.findViewById(R.id.my_whislist).setOnClickListener(this);
        itemView.findViewById(R.id.my_contact_us).setOnClickListener(this);
        itemView.findViewById(R.id.rate_app).setOnClickListener(this);
        itemView.findViewById(R.id.why_choose_us).setOnClickListener(this);
        itemView.findViewById(R.id.share_app).setOnClickListener(this);
        itemView.findViewById(R.id.faq).setOnClickListener(this);
        itemView.findViewById(R.id.t_c).setOnClickListener(this);
        itemView.findViewById(R.id.p_p).setOnClickListener(this);
        userloginLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preference.getLoginStatus()){
                    logoutUser();
                }else {
                    Intent startLogin=new Intent(context, LoginAndRegisterActivity.class);
                    startActivity(startLogin);
                    userloginLogoutBtn.setText("Login/Sign up");
                }
            }
        });
        itemView.findViewById(R.id.header_reset_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ResetPassword.class));
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.my_bookings:
                if(preference.getLoginStatus()){
                    Intent holderActivity=new Intent(context,HolderActivity.class);
                    holderActivity.putExtra("from","bookings");
                    startActivity(holderActivity);
                }else{
                    CustomAlertDialog dialog = new CustomAlertDialog(context,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
                    dialog.show(getFragmentManager(), "MyDialogFragmentTag");
                    return;
                }
                break;
            case R.id.my_queries:
                    if(preference.getLoginStatus()){
                        Intent holderActivity=new Intent(context,HolderActivity.class);
                        holderActivity.putExtra("from","queries");
                        startActivity(holderActivity);
                    }else{
                        CustomAlertDialog dialog = new CustomAlertDialog(context,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
                        dialog.show(getFragmentManager(), "MyDialogFragmentTag");
                        return;
                    }
                break;
            case R.id.my_whislist:
                if(preference.getLoginStatus()){
                    Intent holderActivity=new Intent(context,HolderActivity.class);
                    holderActivity.putExtra("from","wishlist");
                    startActivity(holderActivity);
                }else{
                    CustomAlertDialog dialog = new CustomAlertDialog(context,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
                    dialog.show(getFragmentManager(), "MyDialogFragmentTag");
                    return;
                }
                break;
            case R.id.my_contact_us:
                Function.makeCall(getContext());
                break;
            case R.id.rate_app:
                Function.rateAppOnStore(getContext());
                break;

            case R.id.share_app:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    e.toString();
                }
                break;

            case R.id.why_choose_us:
                Intent hiw=new Intent(getActivity(), HolderActivity.class);
                hiw.putExtra("from","hiw");
                startActivity(hiw);
                break;

            case R.id.t_c:
                Intent terms=new Intent(getActivity(), HolderActivity.class);
                terms.putExtra("from","t_c");
                startActivity(terms);
                break;

            case R.id.p_p:
                Intent privacy=new Intent(getActivity(),HolderActivity.class);
                privacy.putExtra("from","p_p");
                startActivity(privacy);
                break;

            case R.id.faq:
                Intent faq=new Intent(getActivity(),HolderActivity.class);
                faq.putExtra("from","faq");
                startActivity(faq);
                break;

        }
    }
}
