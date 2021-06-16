package in.chardhamtour.chardhamyatra.controller.api;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
  
    public static String BASE_URL="http://www.tourism-in-india.com/chardhamyatra/public/";
    private static GoogleSignInOptions gso=null;

    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static GoogleSignInOptions getGso(){
        if(gso==null){
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
        }
        return gso;
    }
}
