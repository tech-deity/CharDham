package in.chardhamtour.chardhamyatra.controller.utils;

import android.content.Context;
import android.content.SharedPreferences;

import in.chardhamtour.chardhamyatra.R;

public class ChardhamPreference {


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context ctx;

    public ChardhamPreference(Context ctx){
        this.ctx=ctx;
        sp=ctx.getSharedPreferences(ctx.getString(R.string.config_file), Context.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean status){
        editor=sp.edit();
        editor.putBoolean(ctx.getString(R.string.config_login),status);
        editor.apply();
    }

    public boolean getLoginStatus(){
        return sp.getBoolean(ctx.getString(R.string.config_login),false);
    }



    public void setUserId(long id){
        editor=sp.edit();
        editor.putLong(ctx.getString(R.string.config_user_id),id);
        editor.apply();
    }

    public long getUserId(){
        return sp.getLong(ctx.getString(R.string.config_user_id),0);
    }




    public void setProfileImage(String imageUrl){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_profile),imageUrl);
        editor.apply();
    }

    public String getProfileImage(){
        return sp.getString(ctx.getString(R.string.config_profile),null);
    }

    public String getEmail(){
        return sp.getString(ctx.getString(R.string.config_user_email),"");
    }

    public void setEmail(String email){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_user_email),email);
        editor.apply();
    }

    public String getFaq(){
        return sp.getString(ctx.getString(R.string.config_faq),"");
    }

    public void setFaq(String email){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_faq),email);
        editor.apply();
    }


    public String getUserName(){
        return sp.getString(ctx.getString(R.string.config_user_name),"user");
    }

    public void setUserName(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_user_name),user);
        editor.apply();
    }



    public String getClient(){
        return sp.getString(ctx.getString(R.string.config_provider),"manual");
    }

    public void setClient(String provider){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_provider),provider);
        editor.apply();
    }


    public void setClientId(String id){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_provider_id),id);
        editor.apply();
    }

    public String getClientId(){
        return sp.getString(ctx.getString(R.string.config_provider_id),null);
    }

    public String getPP(){
        return sp.getString(ctx.getString(R.string.config_pp),"def");
    }

    public void setPP(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_pp),user);
        editor.apply();
    }


    public String getTC(){
        return sp.getString(ctx.getString(R.string.config_tc),"def");
    }

    public void setTC(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_tc),user);
        editor.apply();
    }

    public String getHIW(){
        return sp.getString(ctx.getString(R.string.config_hiw),"def");
    }

    public void setHIW(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_hiw),user);
        editor.apply();
    }


    public String getChardhamEmail(){
        return sp.getString(ctx.getString(R.string.config_chardham_email),"def");
    }

    public void setChardhamEmail(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_chardham_email),user);
        editor.apply();
    }

    public String getUserPhone(){
        return sp.getString(ctx.getString(R.string.config_user_phone),"def");
    }

    public void setUserPhone(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_user_phone),user);
        editor.apply();
    }

    public String getChardhamPhone(){
        return sp.getString(ctx.getString(R.string.config_chardham_phone),"Call Us");
    }

    public void setChardhamPhone(String user){
        editor=sp.edit();
        editor.putString(ctx.getString(R.string.config_chardham_phone),user);
        editor.apply();
    }


    public void clearPreference(){
        editor=sp.edit();
        editor.clear();
        editor.apply();
    }


}
