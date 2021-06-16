package in.chardhamtour.chardhamyatra.view.fragment;


import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.PackageCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.view.adapter.NotificationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";
    private View itemView;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_notification, container, false);
        final Fragment fragment=this;
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
        setNotification();
        return itemView;
    }

    private void setNotification(){

        final RecyclerView recyclerView=itemView.findViewById(R.id.notification_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        Call<PackageCall> notificationCall= ApiClient.getRetrofit().create(ApiInterface.class)
                .getNotification();
        notificationCall.enqueue(new Callback<PackageCall>() {
            @Override
            public void onResponse(Call<PackageCall> call, Response<PackageCall> response) {
                if(response.isSuccessful() && response.body()!=null){
                    if(response.body().getServ_resp().equalsIgnoreCase("ok")
                            && response.body().getData()!=null) {
                        if(response.body().getData().size()>0){
                            itemView.findViewById(R.id.no_notification_lay).setVisibility(View.GONE);
                        }else{
                            itemView.findViewById(R.id.no_notification_lay).setVisibility(View.VISIBLE);
                        }
                        recyclerView.setAdapter(new NotificationAdapter(getContext(), response.body().getData()));
                        Log.d(TAG, "response:" + response.body().getServ_resp());
                        Log.d(TAG, "response:" + response.body().getData().size());
                    }
                }
            }

            @Override
            public void onFailure(Call<PackageCall> call, Throwable t) {
                Log.d(TAG,"response:Failed");
            }
        });

    }

}
