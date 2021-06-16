package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.listeners.TopBannerTextListener;
import in.chardhamtour.chardhamyatra.model.BannerModel;

public class HomeTopVpAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<BannerModel> topBannerList;
    private TopBannerTextListener textListener;


    public HomeTopVpAdapter(Context context, ArrayList<BannerModel> topBannerList) {
        this.mContext = context;
        this.topBannerList = topBannerList;
        //this.screenPos=screenPos;
    }

    @Override
    public int getCount() {
        return this.topBannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView;
        ImageView imageView;

        this.mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        itemView = mLayoutInflater.inflate(R.layout.content_top_banner, container, false);
        imageView = itemView.findViewById(R.id.home_top_banner_iv);

        BannerModel topBanner = topBannerList.get(position);
        Glide.with(mContext).load(ApiClient.BASE_URL+"/images/"+topBanner.getBanner()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

}
