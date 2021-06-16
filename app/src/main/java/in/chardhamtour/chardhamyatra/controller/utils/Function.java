package in.chardhamtour.chardhamyatra.controller.utils;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.SubPackageCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.UserCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.IResponseListener;
import in.chardhamtour.chardhamyatra.model.Model;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import in.chardhamtour.chardhamyatra.view.activity.MainActivity;
import in.chardhamtour.chardhamyatra.view.adapter.SubPackageAdapter;
import in.chardhamtour.chardhamyatra.view.fragment.RegisterFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Function {

    public static String TAG=Function.class.getSimpleName();

    public static void fragTransaction(int id, Fragment fragment, Context ctx){
        FragmentTransaction ft;
        ft = ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment).addToBackStack(null);
        ft.commit();
    }

    public static void fragTransactionNoBackStack(int id, Fragment fragment, Context ctx){
        FragmentTransaction ft;
        ft = ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment).commit();
    }

    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void saveUserAndLoginStatus(Context ctx,Model user,String client,String clientId,String image_url){
        ChardhamPreference pref=new ChardhamPreference(ctx);
        pref.setUserId(user.getUserId());
        pref.setEmail(user.getUserEmail());
        String url;
        if(client.equalsIgnoreCase("manaul")){
            url=ApiClient.BASE_URL+"/images/profile/"+user.getUserProfile();
        }else{
            url=image_url;
        }
        pref.setProfileImage(url);
        pref.setUserName(user.getUserName());
        if(!TextUtils.isEmpty(user.getUserContact())){
            pref.setUserPhone(user.getUserContact());
        }
        pref.setClient(client);
        pref.setClientId(clientId);
        pref.setLoginStatus(true);
    }



    public static void accommodationHandler(View accommodationView,int[] accommodationArray){
       int[] layIds={
              R.id.helicopter, R.id.hotels,R.id.pickup,R.id.meal,R.id.sightSeeing
       };
       Log.i("Accomadation","Called");
       LinearLayout[] hotelLay=new LinearLayout[layIds.length];
        Log.i("Accomadation","size:"+layIds.length);
       for (int i=0;i<layIds.length;i++){
           hotelLay[i]=accommodationView.findViewById(layIds[i]);
           Log.i("Accomadation","isTrue:"+accommodationArray[i]);

           if(accommodationArray[i]==0){
               hotelLay[i].setAlpha(0.4F);
           }
       }
    }

    public static void makeCall(Context ctx){
        String phone=new ChardhamPreference(ctx).getChardhamPhone();
        if(phone!=null){
            ctx.startActivity(new Intent(Intent.ACTION_DIAL)
                    .setData(Uri.parse("tel:"+phone))
            );
        }}

    public static void addHtmlToTextView(TextView textView,String htmlString){
        if(htmlString!=null && textView!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textView.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT));
            } else {
                textView.setText(Html.fromHtml(htmlString));
            }
        }
    }

    public static void modifyWishlist(final Context ctx,long userId,long packId,final ImageView favIcon) {
        ApiClient.getRetrofit().create(ApiInterface.class)
                .resetFav(userId,packId)
                .enqueue(new Callback<CommonCall>() {
                    @Override
                    public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                        if(response.isSuccessful() && response.body()!=null ){
                            String code=response.body().getMessage();
                            Log.d(TAG,"code:"+code);
                            Log.d(TAG,"responseFav:"+response.body().getServ_res());
                            if(code.equalsIgnoreCase("201")){Log.d(TAG,"code:"+code);
                                Function.changeImageViewSrc(ctx,favIcon,R.drawable.baseline_favorite_24);
                                Function.toast(ctx,"Added to whishlist",Toast.LENGTH_SHORT);
                            }else if(code.equalsIgnoreCase("202")){
                                Function.changeImageViewSrc(ctx,favIcon,R.drawable.baseline_favorite_border_24);
                                Function.toast(ctx,"Removed from whishlist",Toast.LENGTH_SHORT);
                            }else if(code.equalsIgnoreCase("404")){Log.d(TAG,"Server Error");}else {
                                Function.toast(ctx,"Something Went Wrong",Toast.LENGTH_SHORT);}
                        }else {
                            Function.toast(ctx,"Server Response Failed",Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onFailure(Call<CommonCall> call, Throwable t) {
                        Function.toast(ctx,"Connection ERror",Toast.LENGTH_SHORT);
                    }
                });
    }

    public static void rateAppOnStore(Context context){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        }catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void setSubPackageToAdapter(final Context ctx,  final long id, final RecyclerView rv, final int rvLayout,int isRelatedPack){


        Call<SubPackageCall> subPackageCall= ApiClient.getRetrofit().create(ApiInterface.class).getAssociatedSubPackages(id,isRelatedPack);
        subPackageCall.enqueue(new Callback<SubPackageCall>() {
            @Override
            public void onResponse(Call<SubPackageCall> call, Response<SubPackageCall> response) {
                Log.d(TAG,"isSresponse"+response.isSuccessful());
                if(response.isSuccessful()){
                    SubPackageCall responseData=response.body();
                    if(responseData!=null && responseData.getData()!=null && responseData.getServ_res().equalsIgnoreCase("ok")){
                        ArrayList<SubPackagesModel> data=responseData.getData();
                        SubPackageAdapter adapter=new SubPackageAdapter(ctx,data,rvLayout);
                        rv.setAdapter(adapter);
                        if(rvLayout==R.layout.view_sub_package_list){
                            ((IResponseListener) ctx).onResponseSuccess(data.size());
                        }
                    }else{
                        if(rvLayout==R.layout.view_sub_package_list){
                            ((IResponseListener) ctx).onResponseFailed();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<SubPackageCall> call, Throwable t) {
                Toast.makeText(ctx, "Chardham Not Responding", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Failure");
            }
        });
    }

    public static void changeImageViewSrc(Context ctx,ImageView view,int res){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setImageDrawable(ctx.getResources().getDrawable(res, ctx.getTheme()));
        } else {
            view.setImageDrawable(ctx.getResources().getDrawable(res));
        }
    }

    public static void handleLogRegProgress(Button btn, ProgressBar prog, String fieldText, boolean show){
        btn.setText(fieldText);
        if(show){
            btn.setEnabled(false);
            prog.setVisibility(View.VISIBLE);
        }else{
            btn.setEnabled(true);
            prog.setVisibility(View.GONE);
        }
    }


    public static void toast(Context context, String message, int duration){
        Toast toast = Toast.makeText(context, message, duration);
        View view = toast.getView();

        view.getBackground().setColorFilter(context.getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_IN);

        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        toast.show();
    }


    public static void modifyViewVisibility(View view,ImageView iv,Context ctx){
            if(view.getVisibility()==View.VISIBLE){
                view.setVisibility(View.GONE);
                changeImageViewSrc(ctx,iv,R.drawable.ic_keyboard_arrow_right_black_24dp);
            }else{
                view.setVisibility(View.VISIBLE);
                changeImageViewSrc(ctx,iv,R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
    }

    public static void hideKeyboardFromActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getRandomNumber(int min,int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }

    public static String bitmapToBase64String(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(image,Base64.DEFAULT);
    }



}
