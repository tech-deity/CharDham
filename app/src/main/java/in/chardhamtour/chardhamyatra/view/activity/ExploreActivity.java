package in.chardhamtour.chardhamyatra.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.ReviewCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.SubPackDetailCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.CustomAlertDialog;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.ItineraryModel;
import in.chardhamtour.chardhamyatra.model.ReviewModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import in.chardhamtour.chardhamyatra.view.adapter.ExploreVPAdapter;
import in.chardhamtour.chardhamyatra.view.adapter.ReviewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ExploreActivity";
    private View similarPackLayout,reviewLay,overviewLay,faqLay;
    private ImageView favIcon;
    private Button bookBtn;
    private TextView nextTv,prevTv,pageDetailTv;
    private TextView placePrice,tourDuration;
    private List<TextView> planTextViews;
    private ItineraryModel bookingProgram;
    private ChardhamPreference preference;
    private SubPackagesModel subPackagesModel;
    private int currentPage;
    private int maxSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        startLoading(true);
        preference=new ChardhamPreference(this);
        initViews();
        bookingProgram=new ItineraryModel();
        long id=getIntent().getLongExtra("sub_id",0L);
        long pack_id=getIntent().getLongExtra("pack_id",0L);
        Log.d(TAG,"id:"+id);
        Log.d(TAG,"pack_id:"+pack_id);
        if(id!=0L ){
            getItemDetail(id);
        }
        if(pack_id!=0L){
            setRelatedPackages(pack_id);
        }else {
            similarPackLayout.setVisibility(View.GONE);
        }
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

            Log.d(TAG, "onReceive: Reason"+reason);

            if(currentNetworkInfo.isConnected()){
            }else{
                new androidx.appcompat.app.AlertDialog.Builder(ExploreActivity.this)
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
                        })
                        .show();
            }
        }
    };


    private void startLoading(boolean b) {
        ScrollView explore_activity = findViewById(R.id.explore_activity);
        ProgressBar loading_pb = findViewById(R.id.loading_pb);
        if(b){
            explore_activity.setVisibility(View.GONE);
            loading_pb.setVisibility(View.VISIBLE);
        }else{
            explore_activity.setVisibility(View.VISIBLE);
            loading_pb.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        ImageView goBack;
        goBack=findViewById(R.id.explore_back_btn);
        favIcon=findViewById(R.id.explore_fav_iv);
        similarPackLayout=findViewById(R.id.similar_pack_lay);
        reviewLay=findViewById(R.id.review_pack_lay);
        faqLay=findViewById(R.id.faq);
        overviewLay=findViewById(R.id.overview);
        View bottomView=similarPackLayout.findViewById(R.id.bottom_lay);
        bottomView.setVisibility(View.GONE);
        // bottomView.setBackgroundColor(Color.BLACK);
        reviewLay.setVisibility(View.GONE);

        prevTv=findViewById(R.id.prev);
        nextTv=findViewById(R.id.next);
        pageDetailTv=findViewById(R.id.page_detail);

        prevTv.setOnClickListener(this);
        nextTv.setOnClickListener(this);

        goBack.setOnClickListener(this);
        favIcon.setOnClickListener(this);
    }

    private void getItemDetail(long id){
        Call<SubPackDetailCall> call= ApiClient.getRetrofit().create(ApiInterface.class).getSubPackageDetail(id);
        call.enqueue(new Callback<SubPackDetailCall>() {
            @Override
            public void onResponse(Call<SubPackDetailCall> call, Response<SubPackDetailCall> response) {
                if(response.isSuccessful()){
                    SubPackDetailCall packDetail=response.body();
                    if(packDetail!=null && packDetail.getServ_res()!=null
                            && packDetail.getImageModels()!=null  && packDetail.getData()!=null && packDetail.getItineraryModels()!=null){
                        if(packDetail.getServ_res().equalsIgnoreCase("ok")){
                            updateUI(packDetail);
                        }}}
            }
            @Override
            public void onFailure(Call<SubPackDetailCall> call, Throwable t) {
                Log.d(TAG,"ConnectionFailure");
            }
        });
    }

    private void updateUI(final SubPackDetailCall packDetail) {
        ViewPager imageVp;
        TextView placeName;
        Button enquiryBtn;

        final TextView overViewDataTV=overviewLay.findViewById(R.id.scroll_data_tv);
        final TextView faqLayTV=faqLay.findViewById(R.id.scroll_data_tv);
        final ImageView faq_expandIV=faqLay.findViewById(R.id.scroll_down_up);
        TextView faqLayTitleTV=faqLay.findViewById(R.id.scroll_title);
        faqLayTitleTV.setText("FaQ");

        placeName=findViewById(R.id.explore_placeName);
        placePrice=findViewById(R.id.explore_placePrice);
        tourDuration=findViewById(R.id.explore_placeDuration);
        imageVp=findViewById(R.id.explore_img_vp);


        imageVp.setAdapter(new ExploreVPAdapter(this,packDetail.getImageModels()));
        enquiryBtn = findViewById(R.id.explore_enquiry_btn);
        bookBtn=findViewById(R.id.explore_book_btn);
        enquiryBtn.setOnClickListener(this);
        bookBtn.setOnClickListener(this);

        subPackagesModel=packDetail.getData();
        placeName.setText(packDetail.getData().getName());

        Function.addHtmlToTextView(faqLayTV,packDetail.getFaqHtmlString());
        Function.addHtmlToTextView(overViewDataTV,packDetail.getData().getDescription());

        final ImageView expandIV=overviewLay.findViewById(R.id.scroll_down_up);
        expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.modifyViewVisibility(overViewDataTV,expandIV,v.getContext());
            }
        });

        faq_expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.modifyViewVisibility(faqLayTV,faq_expandIV,v.getContext());
            }
        });

        setReviews(this,packDetail.getData().getId());
        setInclusionExclusion(packDetail.getData());
        setPrograms(packDetail.getItineraryModels());


        if(preference.getLoginStatus()){
            checkItemInWishlist();
        }


        TextView viewAll=findViewById(R.id.view_all_tv);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preference.getLoginStatus()){
                    Intent intent=new Intent(ExploreActivity.this,HolderActivity.class);
                    intent.putExtra("from","review");
                    intent.putExtra("id",packDetail.getData().getId());
                    startActivity(intent);
                }else{
                    CustomAlertDialog dialog = new CustomAlertDialog(ExploreActivity.this,"Login Required!","You are not Logged In ,Please Login to start your booking",true);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
            }
        });

        maxSize=packDetail.getImageModels().size();
        --maxSize;
        currentPage=0;
        pageDetailTv.setText((currentPage+1)+" of "+(maxSize+1));
        imageVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPage=position;
                pageDetailTv.setText((currentPage+1)+" of "+(maxSize+1));
                if(currentPage==maxSize){
                    nextTv.setVisibility(View.INVISIBLE);
                }else{
                    nextTv.setVisibility(View.VISIBLE);
                }
                if(currentPage==0){
                    prevTv.setVisibility(View.INVISIBLE);
                }else {
                    prevTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable = getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp);

        final int width = Math.round(18* density);
        final int height = Math.round(18 * density);

        drawable.setBounds(0, 0, width, height);

        prevTv.setCompoundDrawables(drawable, null, null, null);

        final Drawable drawable2 = getResources().getDrawable(R.drawable.ic_navigate_next_white_24dp);
        drawable2.setBounds(0, 0, width, height);

        nextTv.setCompoundDrawables(null, null, drawable2, null);

    }

    private void setRelatedPackages(final long pack_id){
        RecyclerView rv=similarPackLayout.findViewById(R.id.recycler_view);
        TextView tv=similarPackLayout.findViewById(R.id.title_tv);
        tv.setText("Related Packages");
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        rv.setClipToPadding(false);
        rv.setPadding(10,20,120,10);
        if(pack_id!=0L){
            Function.setSubPackageToAdapter(this,pack_id,rv,R.layout.view_related_pack,1);
        }

    }

    private void setInclusionExclusion(final SubPackagesModel subPackagesModel) {
        View IELayout=findViewById(R.id.include_exclude);
        final TextView inclusionExclusion=IELayout.findViewById(R.id.inc_exc_fill_tv);
        final TextView includeBtn=IELayout.findViewById(R.id.inclusion_tv);
        final TextView excludeBtn=IELayout.findViewById(R.id.exclusion_tv);
        Function.addHtmlToTextView(inclusionExclusion,subPackagesModel.getInclusions());

        includeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excludeBtn.setTextColor(getResources().getColor(R.color.black_252525));
                includeBtn.setTextColor(getResources().getColor(R.color.orange));
                Function.addHtmlToTextView(inclusionExclusion,subPackagesModel.getInclusions());
            }
        });

        excludeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                includeBtn.setTextColor(getResources().getColor(R.color.black_252525));
                excludeBtn.setTextColor(getResources().getColor(R.color.orange));
                Function.addHtmlToTextView(inclusionExclusion,subPackagesModel.getExclusions());
            }
        });
    }

    private void setPrograms(ArrayList<ItineraryModel> programs){
        View program_lay=findViewById(R.id.program_lay);
        Log.d(TAG,"index+"+programs.size());
        if(programs.size()>0){
            Log.d(TAG,"index+notnull"+programs.size());
            ViewGroup planViewGroup = program_lay.findViewById(R.id.planLayout);
            planTextViews=new ArrayList<>();

            for(final ItineraryModel program : programs){
                final TextView plan=new TextView(this);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                plan.setLayoutParams(param);
                plan.setTextSize(TypedValue.COMPLEX_UNIT_SP ,15);
                plan.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
                plan.setTextColor(getResources().getColor(R.color.black_252525));
                plan.setPadding(16,16,16,16);
                plan.setGravity(Gravity.CENTER);
                plan.setText(" | "+program.getTourDuration()+" | ");

                planTextViews.add(plan);
                plan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpProgramData(program);
                        plan.setTextColor(getResources().getColor(R.color.orange));
                        plan.setTypeface(plan.getTypeface(), Typeface.BOLD);
                        plan.setTextSize(TypedValue.COMPLEX_UNIT_SP ,16);
                        bookingProgram=program;
                    }
                });
                planViewGroup.addView(plan);
            }
            setUpProgramData(programs.get(0));
            bookingProgram=programs.get(0);
            planTextViews.get(0).setTextColor(getResources().getColor(R.color.orange));
        }else{
            placePrice.setText(Html.fromHtml("&#x20b9;")+" On Request to Get Quote");
            this.tourDuration.setText("Not Available");
            bookBtn.setVisibility(View.GONE);
            bookBtn.setEnabled(false);
            program_lay.setVisibility(View.GONE);
        }
    }

     private void setTvBlackColor(){
        for (TextView tv:planTextViews){
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,15);
            tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
        }
     }

    private void setUpProgramData(ItineraryModel program) {
        final ImageView scroll_down;
        View programLayout=findViewById(R.id.program_lay);
        final TextView tourName,tourPrice,tourDesc,tourDuration,tourDestination;
        setTvBlackColor();
        tourDesc=programLayout.findViewById(R.id.placeDesc);
        scroll_down=programLayout.findViewById(R.id.program_scroll_down_up);
        Function.modifyViewVisibility(tourDesc,scroll_down,ExploreActivity.this);
        scroll_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.modifyViewVisibility(tourDesc,scroll_down,ExploreActivity.this);
            }
        });

        tourName=programLayout.findViewById(R.id.placeName);
        tourDuration=programLayout.findViewById(R.id.placeDuration);
        tourPrice=programLayout.findViewById(R.id.placePrice);
        tourDestination=programLayout.findViewById(R.id.placeCovered);

        tourName.setText(program.getTourName());
        tourDuration.setText(program.getTourDuration());
        tourDestination.setText(program.getDestinationCovered());
        Function.addHtmlToTextView(tourDesc,program.getTourDescription());

        String price=program.getTourPrice();
        String priceString=Html.fromHtml("&#x20b9;")+price+"/ Person";
        Log.d("program","price:"+price);
        if(price==null || price.equalsIgnoreCase("0")
                || price.equalsIgnoreCase("")){
            priceString=Html.fromHtml("&#x20b9;")+" On Request";
            bookBtn.setVisibility(View.GONE);
            bookBtn.setEnabled(false);
        }
        tourPrice.setText(priceString);
        placePrice.setText(priceString);
        this.tourDuration.setText(program.getTourDuration());
    }



    private void setReviews(final Context ctx,long id){
        TextView title=reviewLay.findViewById(R.id.title_tv);
        final RecyclerView rv=reviewLay.findViewById(R.id.recycler_view);
        title.setText("Reviews");
        Call<ReviewCall> reviews = ApiClient.getRetrofit().create(ApiInterface.class).getReviews(id);
        reviews.enqueue(new Callback<ReviewCall>() {
            @Override
            public void onResponse(Call<ReviewCall> call, Response<ReviewCall> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null && response.body().getServ_resp()!=null &&
                            response.body().getServ_resp().equalsIgnoreCase("ok") && response.body().getData()!=null){
                        Log.d("Review","size:"+response.body().getData().size());
                        if(response.body().getData().size()>0){
                            reviewLay.setVisibility(View.VISIBLE);
                            ArrayList<ReviewModel> reviewModels=response.body().getData();
                            rv.setHasFixedSize(true);
                            rv.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false));
                            rv.setAdapter(new ReviewAdapter(ctx,reviewModels));
                        }
                        startLoading(false);
                    }else{
                        Log.d("Review","Failed:"+response.body().getData().size());
                    }
                }
            }
            @Override
            public void onFailure(Call<ReviewCall> call, Throwable t) {
                Log.d("Review","Failed:");
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.explore_back_btn:
                onBackPressed();
                finish();
                System.gc();
                break;
            case R.id.explore_book_btn:
                if(preference.getLoginStatus()){
                    Intent bookIntent=new Intent(this,BookingActivity.class);
                    bookIntent.putExtra("program",bookingProgram);
                    bookIntent.putExtra("packModel",subPackagesModel);
                    startActivity(bookIntent);
                }else{
                    CustomAlertDialog dialog = new CustomAlertDialog(this,"Login Required!","You are not Logged In ,Please Login to start your booking",true);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                }
                break;
            case R.id.explore_enquiry_btn:
                Intent holderIntent=new Intent(this, HolderActivity.class);
                holderIntent.putExtra("from","quotes");
                holderIntent.putExtra("object",subPackagesModel);
                v.getContext().startActivity(holderIntent);
                break;

            case R.id.explore_fav_iv:
                 resetFav();
                 break;
            case R.id.next:
                if(currentPage == maxSize){
                    currentPage = 0; }
                else{
                    ++currentPage ; }
                ((ViewPager)findViewById(R.id.explore_img_vp)).setCurrentItem(currentPage, true);
                break;

            case R.id.prev:
                if(currentPage == 0){
                    currentPage = maxSize; }
                else{
                    --currentPage ; }
                ((ViewPager)findViewById(R.id.explore_img_vp)).setCurrentItem(currentPage, true);
                break;
        }
    }




    private void resetFav() {
        if(preference.getLoginStatus()){
            Function.modifyWishlist(this,preference.getUserId(),subPackagesModel.getId(),favIcon);
        }else{
            CustomAlertDialog dialog = new CustomAlertDialog(this,"Login Required!","You are not Logged In ,Please Login to start this feature",true);
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }
    }


    private void checkItemInWishlist() {
        ApiClient.getRetrofit().create(ApiInterface.class)
                .isFav(preference.getUserId(),subPackagesModel.getId())
                .enqueue(new Callback<CommonCall>() {
                    @Override
                    public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                        if(response.isSuccessful() && response.body()!=null ){
                            String code=response.body().getMessage();
                            Log.d(TAG,"code:"+code);
                            Log.d(TAG,"responseFav:"+response.body().getServ_res());
                            if(code.equalsIgnoreCase("200")){
                                Log.d(TAG,"modcode:"+code);
                                Function.changeImageViewSrc(ExploreActivity.this,favIcon,R.drawable.baseline_favorite_24);
                            }
                            else if(code.equalsIgnoreCase("204")){
                                Log.d(TAG,"204");
                                Function.changeImageViewSrc(ExploreActivity.this,favIcon,R.drawable.baseline_favorite_border_24);
                            }
                            else {
                                Function.toast(ExploreActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT);
                            }
                        }else {
                            Function.toast(ExploreActivity.this,"Server Response Failed",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonCall> call, Throwable t) {
                        Function.toast(ExploreActivity.this,"Connection ERror",Toast.LENGTH_SHORT);
                    }
                });
    }
}
