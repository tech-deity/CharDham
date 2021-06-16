package in.chardhamtour.chardhamyatra.view.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.PackageCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.SubPackageCall;
import in.chardhamtour.chardhamyatra.controller.ApiCall.BannerCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.BannerModel;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import in.chardhamtour.chardhamyatra.view.activity.HolderActivity;
import in.chardhamtour.chardhamyatra.view.activity.WebView;
import in.chardhamtour.chardhamyatra.view.adapter.HomeTopVpAdapter;
import in.chardhamtour.chardhamyatra.view.adapter.StaticDataAdapter;
import in.chardhamtour.chardhamyatra.view.adapter.SubPackageAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View itemView, trending ,category;
    private ViewPager viewPager;
    private static String TAG=HomeFragment.class.getSimpleName();
    private ArrayList<BannerModel> banners;
    private static ApiInterface apiInterface;
    private IProgressBarListener interfaceProBar;
    private ChardhamPreference preference;
    private int currentPage = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final Fragment fragment=this;
        interfaceProBar=(IProgressBarListener) getContext();
        preference=new ChardhamPreference(getActivity());
        itemView = inflater.inflate(R.layout.fragment_home, container, false);
        interfaceProBar.startProgressBar("Loading Chardham Tour...");
        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);

        final SwipeRefreshLayout pullToRefresh = itemView.findViewById(R.id.refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    getFragmentManager().beginTransaction().detach(fragment).commitNow();
                    getFragmentManager().beginTransaction().attach(fragment).commitNow();
                } else {
                    getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                }
                pullToRefresh.setRefreshing(false);
            }
        });

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTrending();
                setBanners();
                enableSearching();
                enableQueryCalling();
                setHomeLayout();
                setStaticBanner();
            }
        },200);


        itemView.findViewById(R.id.home_plan_button_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent holderActivity=new Intent(getActivity(),HolderActivity.class);
                holderActivity.putExtra("from","quotes");
                startActivity(holderActivity);
            }
        });

        FloatingActionButton fab = itemView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webView=new Intent(getContext(), WebView.class);
                webView.putExtra("web_view_url",getString(R.string.bot_link));
                webView.putExtra("title","Ask about your package");
                startActivity(webView);
            }
        });
        return itemView;
    }

    private void enableSearching() {
        EditText searchET=itemView.findViewById(R.id.home_search_et);
        searchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent holderIntent=new Intent(getActivity(), HolderActivity.class);
                holderIntent.putExtra("from","search");
                startActivity(holderIntent);
            }
        });
    }

    @Override
    public void onPause() {
        interfaceProBar.stopProgressBar();
        super.onPause();
    }

    private void enableQueryCalling() {
        Button btn=itemView.findViewById(R.id.home_call_us_layout)
                .findViewById(R.id.call_btn);
        btn.setText(preference.getChardhamPhone());
        btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Function.makeCall(getContext());
                    }
                });
    }

    private void setHomeLayout() {

        final int[] categories={
                R.id.home_layout_one,
                R.id.home_layout_two,
                R.id.home_layout_three,
                R.id.home_layout_four
        };

        final int[] rvIds={
                R.layout.card_cat_two,
                R.layout.card_cat_two,
                R.layout.card_cat_three,
                R.layout.card_cat_two,
        };

        final Call<PackageCall> packageCall=apiInterface.getHomePackages();
        packageCall.enqueue(new Callback<PackageCall>() {
            @Override
            public void onResponse(Call<PackageCall> call, Response<PackageCall> response) {
                Log.d(TAG,"response:packages:"+response.body().getServ_resp());
                if(response.isSuccessful()){
                    final ArrayList<PackageModel> packages=response.body().getData();
                    if(packages!=null && packages.size()>0){
                        Log.d(TAG,"response:Size Greator than 0:Size="+packages.size());
                        TextView categoryTitle;
                        RecyclerView categoryRv;
                        TextView view_all;
                        for(int i=0;i<packages.size();i++){
                            final int index=i;
//                            Intent openViewAll = new Intent(getActivity(), MainActivity.class);
                            Log.d(TAG,"response:Size at:"+i+", package Name:"+packages.get(i).getName());
                            category=itemView.findViewById(categories[i]);
                            categoryTitle=category.findViewById(R.id.vct_color_title);
                            view_all=category.findViewById(R.id.vct_view_all);
                            view_all.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent openViewAll = new Intent(getActivity(), HolderActivity.class);
                                    openViewAll.putExtra("from","package");
                                    Log.d(TAG,"id:"+index);
                                    openViewAll.putExtra("object",packages.get(index));
                                    startActivity(openViewAll);
                                }
                            });
//                            categorySubTitle=category.findViewById(R.id.vct_title);
                            categoryRv=category.findViewById(R.id.vct_rv);
                            categoryTitle.setText(packages.get(i).getName());
                            categoryRv.setHasFixedSize(true);
                            categoryRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
                            categoryRv.setClipToPadding(false);
                            categoryRv.setPadding(20,0,120,20);
                            Function.setSubPackageToAdapter(getContext(),packages.get(i).getId(),categoryRv,rvIds[i],0);
                        }

                        interfaceProBar.stopProgressBar();
                    }
                }
            }

            @Override
            public void onFailure(Call<PackageCall> call, Throwable t) {
                Log.d(TAG,"response:packages:Failure");
            }
        });
    }

    private void setTrending() {

        TextView trendingTitle;
        final RecyclerView trendingRv;

        trending=itemView.findViewById(R.id.home_trending_layout);
        trendingTitle=trending.findViewById(R.id.vct_title);
        trendingRv=trending.findViewById(R.id.vct_rv);
        trendingTitle.setText("Trending Chardham Packages");
        // setTrendingData();
        trendingRv.setHasFixedSize(true);
        trendingRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        trendingRv.setClipToPadding(false);
        trendingRv.setPadding(20,0,120,20);

        Call<SubPackageCall> trending = apiInterface.getTrending();
        trending.enqueue(new Callback<SubPackageCall>() {
            @Override
            public void onResponse(Call<SubPackageCall> call, Response<SubPackageCall> response) {
                Log.d(TAG,"response:trending:"+response.body().getServ_res());
                if(response.isSuccessful() && response.body().getServ_res().equalsIgnoreCase("ok")){
                    Log.d(TAG,"response:trending:"+response.body().getServ_res());
                    ArrayList<SubPackagesModel> subPackages=response.body().getData();
                    if(subPackages!=null)
                        trendingRv.setAdapter(new SubPackageAdapter(getContext(),subPackages,R.layout.card_circular_cat_one));
                        interfaceProBar.stopProgressBar();

                }
            }

            @Override
            public void onFailure(Call<SubPackageCall> call, Throwable t) {
                Log.d(TAG,"response:trending:failed");
            }
        });
    }



    private void setBanners() {

        viewPager=itemView.findViewById(R.id.home_banner_vp);
        banners=new ArrayList<>();

        Call<BannerCall> bannerCall=apiInterface.getBanner();
        bannerCall.enqueue(new Callback<BannerCall>() {
            @Override
            public void onResponse(Call<BannerCall> call, Response<BannerCall> response) {
                Log.d(TAG,"response:"+response.body().getServ_res());
                if(response.isSuccessful() && response.body().getServ_res().equalsIgnoreCase("ok")){
                    banners = response.body().getData();
                    viewPager.setAdapter(new HomeTopVpAdapter(getActivity(),banners));
                    setTitle(banners.get(0).getHeading());
                    setSubTitle(banners.get(0).getSub_heading());
                    setupAutoPager(viewPager,(banners.size()));

                }
            }
            @Override
            public void onFailure(Call<BannerCall> call, Throwable t) { Log.d(TAG,"response:Failure");}
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTitle(banners.get(position).getHeading());
                setSubTitle(banners.get(position).getSub_heading());
            }

            @Override
            public void onPageSelected(int position) {

            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }



    private void setupAutoPager(final ViewPager pager, final int maxSize)
    {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run()
            {
                pager.setCurrentItem(currentPage, true);
                if(currentPage == maxSize)
                {
                    currentPage = 0;
                }
                else
                {
                    ++currentPage ;
                }
            }
        };


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 6000);
    }


    private void setStaticBanner(){

        ViewPager viewPager=itemView.findViewById(R.id.static_view_pager);

        StaticBannerObject bannerObject=new StaticBannerObject();
        StaticBannerObject bannerObject1=new StaticBannerObject();
        StaticBannerObject bannerObject2=new StaticBannerObject();
        StaticBannerObject bannerObject3=new StaticBannerObject();
        StaticBannerObject bannerObject4=new StaticBannerObject();

        bannerObject.setDrawable(R.drawable.ic_save_time);
        bannerObject.setTitle("SAVE TIME");
        bannerObject.setSubTitle(getString(R.string.no_need_to_surf_multiple_sites_for_packages_quotes_travel_plans));

        bannerObject1.setDrawable(R.drawable.ic_save_money);
        bannerObject1.setTitle("SAVE MONEY");
        bannerObject1.setSubTitle(getString(R.string.compare_negotiate_choose_the_best_from_multiple_options));

        bannerObject2.setDrawable(R.drawable.ic_multiple_option);
        bannerObject2.setTitle("MULTIPLE OPTIONS");
        bannerObject2.setSubTitle(getString(R.string.get_multiple_itineraries_personalised_suggestions_from_our_travel_agents));

        bannerObject3.setDrawable(R.drawable.ic_online_booking);
        bannerObject3.setTitle("ONLINE BOOKING");
        bannerObject3.setSubTitle(getString(R.string.you_can_book_online_chardham_tour_package_with_all_payment_getway));

        bannerObject4.setDrawable(R.drawable.ic_network);
        bannerObject4.setTitle("TRUSTED NETWORK");
        bannerObject4.setSubTitle(getString(R.string.of_500_hotels_reliable_authentic_travel_guides_in_chardham));

        List<StaticBannerObject> dataObject = new ArrayList<>();
        dataObject.add(bannerObject);
        dataObject.add(bannerObject1);
        dataObject.add(bannerObject2);
        dataObject.add(bannerObject3);
        dataObject.add(bannerObject4);

        StaticDataAdapter adapter=new StaticDataAdapter(getContext(),dataObject);
        viewPager.setAdapter(adapter);
        setupAutoPager(viewPager,(dataObject.size()));
    }

    private void setTitle(String title) {
        TextView bannerTitle = itemView.findViewById(R.id.bannerTitle);
        bannerTitle.setText(title);
    }

    private void setSubTitle(String subTitle) {
        TextView bannerSubTitle = itemView.findViewById(R.id.bannerSubTitle);
        bannerSubTitle.setText(subTitle);
    }

    public class StaticBannerObject{
        private String title,subTitle;
        private int drawable;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }
    }



}
