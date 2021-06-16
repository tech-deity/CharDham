package in.chardhamtour.chardhamyatra.view.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.listeners.HeaderRvListeners;
import in.chardhamtour.chardhamyatra.controller.listeners.IResponseListener;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.Function;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackageListFragment extends Fragment{

    private View toolbar,viewsLay;
    private ToolbarListerner toolbarListerner;
    private HeaderRvListeners headerRvListeners;
    private ProgressBar progressBar;


    public PackageListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewsLay = inflater.inflate(R.layout.fragment_header_rv, container, false);
        toolbar=viewsLay.findViewById(R.id.header_rv_toolbar);
        toolbarListerner=(ToolbarListerner) getActivity();
        headerRvListeners=(HeaderRvListeners) getActivity();



        progressBar=viewsLay.findViewById(R.id.loading_pb);
        progressBar.setVisibility(View.VISIBLE);
        viewsLay.findViewById(R.id.noContent).setVisibility(View.GONE);
        viewsLay.findViewById(R.id.header_rv_recycle_view).setVisibility(View.GONE);

        setUpToolbarTheme(getContext(),toolbarListerner.getToolbarTitle());
        long id=headerRvListeners.getPackId();
        int isRelated=headerRvListeners.isRelated();
        if(id!=0L){
            populateRv(getContext(),id,isRelated);
        }
        return viewsLay;
    }


    private void setUpToolbarTheme(Context context, String title) {

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_back_title);
        toolbarTitle.setText(title);
        ImageView backBtnIv = toolbar.findViewById(R.id.toolbar_go_back_iv);
        toolbar.setBackgroundColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbarTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
            backBtnIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp, context.getTheme()));
        } else {
            toolbarTitle.setTextColor(getResources().getColor(R.color.black));
            backBtnIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        }
        backBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarListerner.onBackPressedForFragment();
            }
        });
    }

    private void populateRv(Context ctx,long pack_id,int is) {
        RecyclerView recyclerView;
        recyclerView=viewsLay.findViewById(R.id.header_rv_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setHasFixedSize(true);
        Function.setSubPackageToAdapter(ctx,pack_id,recyclerView,R.layout.view_sub_package_list,is);
    }


    public void onResponseSuccess(int size) {
        progressBar.setVisibility(View.GONE);
        if(size>0)
        viewsLay.findViewById(R.id.header_rv_recycle_view).setVisibility(View.VISIBLE);
        else {
            viewsLay.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
        }
    }


    public void onResponseFailed() {
        progressBar.setVisibility(View.GONE);
        viewsLay.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
    }
}
