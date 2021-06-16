package in.chardhamtour.chardhamyatra.view.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.WishListCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.view.adapter.WishListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFragment extends Fragment {

    private RecyclerView wishlist;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View itemView=inflater.inflate(R.layout.fragment_wishlist, container, false);
        wishlist=itemView.findViewById(R.id.wishlist_rv);
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

        ApiClient.getRetrofit().create(ApiInterface.class).getWishList(new ChardhamPreference(getContext()).getUserId())
                .enqueue(new Callback<WishListCall>() {
                    @Override
                    public void onResponse(Call<WishListCall> call, Response<WishListCall> response) {
                        Log.d("WISTLIST",response.body().getServ_res());
                        Log.d("WISTLIST",response.body().getMsg());
                        if(response.isSuccessful() && response.body()!=null && response.body().getServ_res()
                                .equalsIgnoreCase("ok")){
                            Log.d("WISTLIST",response.body().getMsg());
                            if(response.body().getMsg().equals("200")){
                                ArrayList<PackageModel> data=response.body().getData();
                                if(response.body().getData().size()>0){
                                    itemView.findViewById(R.id.no_wishlist).setVisibility(View.GONE);
                                }else{
                                    itemView.findViewById(R.id.no_wishlist).setVisibility(View.VISIBLE);
                                }
                                WishListAdapter adapter=new WishListAdapter(getContext(),data,R.layout.view_whislist);
                                wishlist.setLayoutManager(new LinearLayoutManager(getActivity()));
                                wishlist.setAdapter(adapter);
                                Log.d("WISTLIST",response.body().getServ_res());
                            }
                            if(response.body().getMsg().equals("400")){
                                Log.d("WISTLIST","Empty 404");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WishListCall> call, Throwable t) {
                        Log.d("WISTLIST","NETWORK ERROR");
                    }
                });

        return itemView;
    }

}
