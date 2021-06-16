package in.chardhamtour.chardhamyatra.view.activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.chardhamtour.chardhamyatra.BuildConfig;
import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.ChardhamDataCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.EnquiryDetail;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.CustomAlertDialog;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import in.chardhamtour.chardhamyatra.model.ChardhamModel;
import in.chardhamtour.chardhamyatra.view.adapter.TabMenuAdapter;
import in.chardhamtour.chardhamyatra.view.fragment.AccountFragment;
import in.chardhamtour.chardhamyatra.view.fragment.HomeFragment;
import in.chardhamtour.chardhamyatra.view.fragment.NotificationFragment;
import in.chardhamtour.chardhamyatra.view.fragment.PackagesFragment;
import in.chardhamtour.chardhamyatra.view.fragment.SearchFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IProgressBarListener, ToolbarListerner, EnquiryDetail {

    private static final String TAG = "MainActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ChardhamPreference preference;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressManager progressManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference=new ChardhamPreference(this);
        getChardhamData();
        setUpSideDrawer();
        setUpViews();

    }


    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(this.mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mConnReceiver);
    }

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);


            if(currentNetworkInfo.isConnected()){

            }else{
                new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Oops ! No Internet")
                        .setMessage("Please Connect your Internet and try again")
                        .setCancelable(false)
                        .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        }).show();

            }
        }
    };


    private void getChardhamData() {
        Call<ChardhamDataCall> dataCall=ApiClient.getRetrofit().create(ApiInterface.class).getChardhamData();
        dataCall.enqueue(new Callback<ChardhamDataCall>() {
            @Override
            public void onResponse(Call<ChardhamDataCall> call, Response<ChardhamDataCall> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getServ_res().equalsIgnoreCase("ok")){
                        ChardhamModel model=response.body().getData();
                        preference.setChardhamPhone(model.getPhone());
                        preference.setChardhamEmail(model.getEmail());
                        preference.setPP(model.getPp());
                        preference.setTC(model.getTc());
                        preference.setHIW(model.getCu_subtitle());
                        preference.setFaq(model.getFaq());
                        Log.d(TAG,"chardham data:"+model.getPhone());
                    }
                }
            }
            @Override
            public void onFailure(Call<ChardhamDataCall> call, Throwable t) {
                Log.d(TAG,"response:failure");
            }
        });
    }

    private void setUpSideDrawer() {

        TextView headerName,headerEmail;
        final TextView headerLoginBtn;
        CircleImageView headerProfile;

        navigationView=findViewById(R.id.main_slide_nav);
        View drawerHeader =navigationView.getHeaderView(0);
        headerName=drawerHeader.findViewById(R.id.header_name);
        headerEmail=drawerHeader.findViewById(R.id.header_email);
        headerProfile=drawerHeader.findViewById(R.id.header_profile);
        headerLoginBtn=drawerHeader.findViewById(R.id.header_login_tv);
        headerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preference.getLoginStatus()){
                    logoutUser();
                }else {
                    Intent startLogin=new Intent(v.getContext(), LoginAndRegisterActivity.class);
                    startActivity(startLogin);
                }

            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        if(preference.getLoginStatus()){
            drawerHeader.findViewById(R.id.header_section_detail).setVisibility(View.VISIBLE);
            headerLoginBtn.setText("Logout");
            headerName.setText(preference.getUserName());
            headerEmail.setText(preference.getEmail());
            Glide.with(this).load(preference.getProfileImage()).placeholder(R.drawable.default_user).into(headerProfile);
        }else {
            drawerHeader.findViewById(R.id.header_section_detail).setVisibility(View.GONE);
        }

        drawerLayout = findViewById(R.id.mDL);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

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



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
        clearUser();
    }

    private void signOutGoogle() {
        GoogleSignInClient googleSignInClient;
        googleSignInClient= GoogleSignIn.getClient(this, ApiClient.getGso());
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        clearUser();
                    }
                });
    }

    private void clearUser(){

        preference.setLoginStatus(false);
        preference.clearPreference();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void setUpViews() {
        viewPager=findViewById(R.id.main_viewpager);
        tabLayout=findViewById(R.id.main_tab_layout);
        setUpTabMenus();
    }

    private void setUpTabMenus() {
        String[] tabs={"Home","Packages","Search","Notify","Menu"};

        int[] drawable={R.drawable.ic_home_24dp,
                R.drawable.ic_tour_package_24dp,
                R.drawable.ic_search_new_24dp,
                R.drawable.ic_notification_24dp,
                R.drawable.ic_menu_24dp};

        ArrayList<Fragment> tabFrags=new ArrayList<>();
        tabFrags.add(new HomeFragment());
        tabFrags.add(new PackagesFragment());
        tabFrags.add(new SearchFragment());
        tabFrags.add(new NotificationFragment());
        tabFrags.add(new AccountFragment());
        TabMenuAdapter tabMenuAdapter=new TabMenuAdapter(getSupportFragmentManager());
        tabMenuAdapter.addValues(tabFrags);
        viewPager.setAdapter(tabMenuAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabMenuAdapter.modifyTabs(this,tabs,drawable,tabLayout);
    }

    private void setSideMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
        view.setText(count > 0 ? String.valueOf(count) : null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent=new Intent(MainActivity.this,HolderActivity.class);
        String from="something went wrong";
        switch (menuItem.getItemId()){
            case R.id.menu_my_bookings:
                if(preference.getLoginStatus()){
                    from="bookings";
                }else{
                    CustomAlertDialog dialog = new CustomAlertDialog(this,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    return true;
                }
                break;
            case R.id.menu_my_queries:
                if(preference.getLoginStatus()){
                from="queries";
                }else{
                CustomAlertDialog dialog = new CustomAlertDialog(this,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    return true;
                }
                break;
            case R.id.menu_my_wishList:
                if(preference.getLoginStatus()){
                    from="wishlist";
                }else{
                    CustomAlertDialog dialog = new CustomAlertDialog(this,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    return true;
                }
                break;
            case R.id.menu_share:
                from="share";
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
            case R.id.menu_contact_us:
                Log.d(TAG,"In WishList,Call");
                Function.makeCall(this);
                break;
            case R.id.menu_rate_us:
                Function.rateAppOnStore(this);
               break;
            case R.id.menu_tc:
                from="t_c";
                break;
            case R.id.menu_hiw:
                from="hiw";
                break;
            case R.id.menu_pp:
                from="p_p";
                break;
            case R.id.menu_faq:
                from="faq";
                break;

        }

        //Toast.makeText(getApplicationContext(),from,Toast.LENGTH_SHORT).show();
        if (menuItem.getItemId()!=R.id.menu_contact_us && menuItem.getItemId()!=R.id.menu_rate_us
                && menuItem.getItemId()!=R.id.menu_share){
            intent.putExtra("from",from);
            startActivity(intent);
        }
        drawerLayout.closeDrawers();
        return true;
    }


    @Override
    public void startProgressBar(String title) {
        progressManager=new ProgressManager(this);
        progressManager.startProgress(title,false);
    }

    @Override
    public void stopProgressBar() {
        progressManager.stopProgress();
    }


    @Override
    public void onBackPressedForFragment() {
        onBackPressed();
    }

    @Override
    public String getToolbarTitle() {
        return null;
    }

    @Override
    public long getEnquiryId() {
        return 0;
    }

    @Override
    public String getDestinationName() {
        return null;
    }

    @Override
    public void setEnquiryId(long id) {
    }

    @Override
    public void setDestinationName(String name) {
    }

    @Override
    public boolean isEnquiryFrag() {
        return false;
    }

    @Override
    public void setEnquiryFrag(boolean bool) {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
