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
import android.widget.ProgressBar;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.PackageCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.view.adapter.PackageAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackagesFragment extends Fragment {

    private static final String TAG = "PackagesFragment";
    View itemView;
    private ProgressBar progressBar;

    private static ApiInterface apiInterface;

    public PackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_packages, container, false);
        progressBar=itemView.findViewById(R.id.loading_pb);
        progressBar.setVisibility(View.VISIBLE);
        itemView.findViewById(R.id.noContent).setVisibility(View.GONE);
        itemView.findViewById(R.id.packages_rv).setVisibility(View.GONE);
        apiInterface= ApiClient.getRetrofit().create(ApiInterface.class);
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

        setPackages();
        return itemView;
    }


    private void setPackages() {

        final RecyclerView recyclerView=itemView.findViewById(R.id.packages_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        Call<PackageCall> packageCall=apiInterface.getPackages();
        packageCall.enqueue(new Callback<PackageCall>() {
            @Override
            public void onResponse(Call<PackageCall> call, Response<PackageCall> response) {
                Log.d(TAG,"response:packages:"+response.body().getServ_resp());
                if(response.isSuccessful()){
                    ArrayList<PackageModel> packages=response.body().getData();
                    progressBar.setVisibility(View.GONE);
                    if(packages!=null){
                        recyclerView.setAdapter(new PackageAdapter(getContext(),packages,R.layout.view_packages_list_item));
                        recyclerView.setVisibility(View.VISIBLE);
                    }else{
                        itemView.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PackageCall> call, Throwable t) {
                Log.d(TAG,"response:packages:Failure");
            }
        });
    }


}
