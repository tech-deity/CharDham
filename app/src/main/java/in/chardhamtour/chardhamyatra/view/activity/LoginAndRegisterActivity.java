package in.chardhamtour.chardhamyatra.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.api.ApiCallManager;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.listeners.ILoginListener;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import in.chardhamtour.chardhamyatra.view.fragment.LoginFragment;

public class LoginAndRegisterActivity extends AppCompatActivity implements ILoginListener, IProgressBarListener,View.OnClickListener {

    private LoginButton fbRegisterBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private ChardhamPreference preference;
    private TextView skipBtn;
    private ProgressManager progressManager;
    private ApiCallManager apiCallManager;
    private SignInButton googleSignInButton;
    private CallbackManager callbackManager;
    private final int RC_SIGN_IN = 10123;
    private static final String TAG = LoginAndRegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        Function.fragTransactionNoBackStack(R.id.lr_frame_layout,new LoginFragment(),this);
        preference=new ChardhamPreference(this);
        apiCallManager=new ApiCallManager(this);

        findViewById(R.id.login_with_facebook).setOnClickListener(this);


        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable = getResources().getDrawable(R.drawable.facebook_icon);

        final int width = Math.round(18* density);
        final int height = Math.round(18 * density);


        drawable.setBounds(0, 0, width, height);

        //Custom Facebook Button
        ((Button)findViewById(R.id.login_with_facebook)).setCompoundDrawables(drawable, null, null, null);


        /*Google signIN btn*/
        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText("Google");
        googleSignInButton.setOnClickListener(this);

        //facebook main button
        fbRegisterBtn=findViewById(R.id.login_button);
        fbRegisterBtn.setOnClickListener(this);
        skipBtn=findViewById(R.id.skip_btn);
        skipBtn.setOnClickListener(this);
    }

    private void loginWithGoogle(){
        mGoogleSignInClient= GoogleSignIn.getClient(this, ApiClient.getGso());
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateGoogleUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Oops ! Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGoogleUI(GoogleSignInAccount acct) {
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personId = acct.getId();
            String personEmail=acct.getEmail();
            String image_url=acct.getPhotoUrl().toString();
            Log.d(TAG,personName+personId+personGivenName+"data:"+personFamilyName);
            if(personEmail!=null && personId!=null){
                onLoginStart(personName,personEmail,null,"google",personId,image_url);
                //Function.connectUser(this,personName,personEmail,null,"google",personId,image_url);
            }else{
                Function.toast(this,"Oops! Google Login Failed", Toast.LENGTH_SHORT);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*Facebook signIN btn*/
    private void registerWithFacebook(){
        callbackManager = CallbackManager.Factory.create();
        fbRegisterBtn.setPermissions(Arrays.asList("email","public_profile"));
        fbRegisterBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken currentAccessToken=loginResult.getAccessToken();
                if(currentAccessToken==null){
                    Toast.makeText(LoginAndRegisterActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
                }
                else{
                    loadUserProfile(currentAccessToken);
                }
            }
            @Override
            public void onCancel() {
                Toast.makeText(LoginAndRegisterActivity.this, "Oops! Facebook Login Failed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,error.toString());
            }
        });
    }

//    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
//        {
//            if(currentAccessToken==null){
//                Toast.makeText(LoginAndRegisterActivity.this,"User Logged out",Toast.LENGTH_LONG).show();
//            }
//            else{
//                loadUserProfile(currentAccessToken);
//            }
//        }
//    };

    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";
                    preference.setProfileImage(image_url);
                    Log.d(TAG,"LoginSuccessFul:"+first_name+last_name+email+id);
                    onLoginStart(first_name+" "+last_name,email,null,"facebook",id,image_url);
                    //Function.connectUser(LoginAndRegisterActivity.this,first_name+" "+last_name,email,null,"facebook",id,image_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch(id){
            case R.id.sign_in_button:
                if(Function.isNetworkConnected(this)){
                    loginWithGoogle();
                }else{
                    Function.toast(this,getString(R.string.no_internet),Toast.LENGTH_SHORT);
                }
                break;
            case R.id.login_button:
                if(Function.isNetworkConnected(this)){
                    registerWithFacebook();
                }else{
                    Function.toast(this,getString(R.string.no_internet),Toast.LENGTH_SHORT);
                }
                break;
            case R.id.skip_btn:
                Intent i=new Intent(this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                break;

            case R.id.login_with_facebook:
                fbRegisterBtn.performClick();
                break;

        }

    }




    public void startProgressBar(String title) {
        progressManager=new ProgressManager(LoginAndRegisterActivity.this);
        progressManager.startProgress(title,false);

    }

    public void stopProgressBar() {
        progressManager.stopProgress();
    }

    @Override
    public void onLoginSuccess() {
        stopProgressBar();
        Intent i=new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onLoginFailed(String client) {
        if (client.equalsIgnoreCase("facebook")){
            LoginManager.getInstance().logOut();
        }
        if(client.equalsIgnoreCase("google")){
            mGoogleSignInClient.signOut();
        }
        preference.clearPreference();
        stopProgressBar();

    }

    @Override
    public void onLoginStart(String name,String email, String password, final String client, final String clientId,final String image_url) {
        startProgressBar("Logging you in...");
        apiCallManager.connectUser(this,name,email,password,client,clientId,image_url);
    }
}
