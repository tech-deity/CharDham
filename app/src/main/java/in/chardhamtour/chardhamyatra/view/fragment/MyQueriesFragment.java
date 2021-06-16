package in.chardhamtour.chardhamyatra.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.StringModel;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.model.QueryCall;
import in.chardhamtour.chardhamyatra.view.adapter.QueryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyQueriesFragment extends Fragment {

    private RecyclerView queries;
    private Context context;
    private ChardhamPreference preference;
    private static String TAG=MyQueriesFragment.class.getSimpleName();


    public MyQueriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        context=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View itemView = inflater.inflate(R.layout.fragment_my_queries, container, false);
        queries=itemView.findViewById(R.id.query_rv);
        preference=new ChardhamPreference(context);
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
        View toolbar=itemView.findViewById(R.id.toolbar);
        ToolbarListerner toolbarListerner=(ToolbarListerner) getActivity();
        TextView text=toolbar.findViewById(R.id.toolbar_back_title);
        text.setText(toolbarListerner.getToolbarTitle());
        toolbar.findViewById(R.id.toolbar_go_back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        Log.d(TAG,"Id:"+preference.getUserId());
        ApiClient.getRetrofit().create(ApiInterface.class).getQueries(preference.getUserId())
                .enqueue(new Callback<QueryCall>() {
                    @Override
                    public void onResponse(Call<QueryCall> call, Response<QueryCall> response) {
                        Log.d(TAG,response.body().getServ_res());
                        Log.d(TAG,response.body().getMsg());
                        if(response.isSuccessful() && response.body()!=null && response.body().getServ_res()
                                .equalsIgnoreCase("ok")){
                            Log.d(TAG,response.body().getMsg());
                            if(response.body().getMsg().equals("200")){
                                ArrayList<StringModel> data=response.body().getData();
                                if(data.size()>0){
                                    itemView.findViewById(R.id.no_queries_lay).setVisibility(View.GONE);
                                }else{
                                    itemView.findViewById(R.id.no_queries_lay).setVisibility(View.VISIBLE);
                                }
                                Log.d(TAG,"datasize:"+data.size());
                                QueryAdapter adapter=new QueryAdapter(data,getContext(),R.layout.view_queries);
                                queries.setLayoutManager(new LinearLayoutManager(getActivity()));
                                queries.setAdapter(adapter);
                                Log.d(TAG,response.body().getServ_res());
                            }
                            if(response.body().getMsg().equals("400")){
                                Log.d(TAG,"Empty 404");
                                itemView.findViewById(R.id.no_queries_lay).setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QueryCall> call, Throwable t) {
                        Log.d(TAG,"NETWORK ERROR");
                        itemView.findViewById(R.id.no_queries_lay).setVisibility(View.VISIBLE);
                    }
                });

        return itemView;
    }

}
