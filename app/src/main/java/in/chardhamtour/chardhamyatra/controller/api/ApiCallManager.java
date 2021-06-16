package in.chardhamtour.chardhamyatra.controller.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.UserCall;
import in.chardhamtour.chardhamyatra.controller.listeners.ILoginListener;
import in.chardhamtour.chardhamyatra.controller.utils.Function;

import in.chardhamtour.chardhamyatra.view.fragment.RegisterFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.chardhamtour.chardhamyatra.controller.utils.Function.fragTransaction;
import static in.chardhamtour.chardhamyatra.controller.utils.Function.toast;

public class ApiCallManager {

    private static String TAG=ApiCallManager.class.getSimpleName();
    private Context context;

    public ApiCallManager(Context context) {
        this.context = context;
    }

    public void connectUser(final ILoginListener iLoginListener,String name,String email, String password, final String client, final String clientId,final String image_url) {

        Call<UserCall> loginUser= ApiClient.getRetrofit().create(ApiInterface.class)
                .loginUser(name,client,email,password,clientId);
        loginUser.enqueue(new Callback<UserCall>() {
            @Override
            public void onResponse(Call<UserCall> call, Response<UserCall> response) {
                if(response.isSuccessful() && response.body()!=null && response.body().getServ_res()!=null){
                    Log.d(TAG,"Getting Response"+response.body().getServ_res());

                    if(response.body().getServ_res().equalsIgnoreCase("data")){
                        //Login Successful
                        Function.saveUserAndLoginStatus(context,response.body().getData(),client,clientId,image_url);
                        iLoginListener.onLoginSuccess();
                    }else if(response.body().getServ_res().equalsIgnoreCase("error")){
                        // User Error
                        if(response.body().getMsg()!=null){
                            String code = response.body().getMsg();
                            if(code.equalsIgnoreCase("404") || code.equalsIgnoreCase("403") || code.equalsIgnoreCase("406")){
                                fragTransaction(R.id.lr_frame_layout,new RegisterFragment(),context);
                                toast(context, "User Not Found,Please Register", Toast.LENGTH_SHORT);
                            }
                            if(code.equalsIgnoreCase("407")) {
                                toast(context,"Email and Password Don't match",Toast.LENGTH_SHORT);
                            }
                            if(code.equalsIgnoreCase("444")) {
                                toast(context,"Provider not found change login provider",Toast.LENGTH_SHORT);
                            }
                        }else {
                            toast(context, "No response,Please try later", Toast.LENGTH_SHORT);
                        }
                        iLoginListener.onLoginFailed(client);
                    }
                }else{
                    iLoginListener.onLoginFailed(client);
                }
            }

            @Override
            public void onFailure(Call<UserCall> call, Throwable t) {
                toast(context, "Server Error Try Later", Toast.LENGTH_SHORT);
                Log.d(TAG,"Failed connecting");
                iLoginListener.onLoginFailed(" ");
            }
        });
    }





}
