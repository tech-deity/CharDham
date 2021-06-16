package in.chardhamtour.chardhamyatra.view.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.PackageCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.view.adapter.PackageAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private boolean isCross=false;
    private View itemView;
    private ToolbarListerner toolbarListerner;

    private static String TAG="SearchFragment";

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_search, container, false);
        init();
        toolbarListerner=(ToolbarListerner) getActivity();
        return itemView;
    }

    private void init(){

        ImageView searchBtn=itemView.findViewById(R.id.search_btn);
        final ImageView backBtn=itemView.findViewById(R.id.search_back_btn);
        recyclerView=itemView.findViewById(R.id.search_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final EditText et = itemView.findViewById(R.id.search_et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    Function.changeImageViewSrc(getContext(),backBtn,R.drawable.ic_arrow_back_black_24dp);
                    isCross=false;
                }
                if(s.length()!=0){
                    Function.changeImageViewSrc(getContext(),backBtn,R.drawable.ic_clear_black_24dp);
                    isCross=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et.getText().length()>0){
                    searchItem(et.getText().toString());
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCross){
                    et.setText("");
                    isCross=false;
                }else {
                    toolbarListerner.onBackPressedForFragment();
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_item=et.getText().toString().trim();
                if(search_item.length()>0){
                    searchItem(search_item);
                }else {
                    Toast.makeText(view.getContext(),"Enter search item to search",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchItem(final String item){

        Call<PackageCall> call= ApiClient.getRetrofit().create(ApiInterface.class).getSearch(item);
        call.enqueue(new Callback<PackageCall>() {
            @Override
            public void onResponse(Call<PackageCall> call, Response<PackageCall> response) {
                Log.d(TAG,response.isSuccessful()+""+response.body().getServ_resp());
                ArrayList<PackageModel> model=new ArrayList<>();
                PackageAdapter adapter=new PackageAdapter(getActivity(),model,R.layout.view_search_item);
                if(response.isSuccessful()){
                    Log.d(TAG,response.body().getServ_resp());
                    if(response.body().getServ_resp().equalsIgnoreCase("OK")){
                        if(response.body().getData().size()>0){
                            itemView.findViewById(R.id.no_result_lay).setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter=new PackageAdapter(getActivity(),response.body().getData(),R.layout.view_search_item);
                            recyclerView.setAdapter(adapter);
                        }else {
                            itemView.findViewById(R.id.no_result_lay).setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            adapter.clear();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PackageCall> call, Throwable t) {
                Toast.makeText(getContext(), "Failed To Search Item", Toast.LENGTH_SHORT).show();
                itemView.findViewById(R.id.no_result_lay).setVisibility(View.VISIBLE);
            }
        });
    }

}
