package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.SubPackDetailCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
public class ExploreVPAdapter  extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<SubPackDetailCall.ImageModel> images;


    public ExploreVPAdapter(Context context, ArrayList<SubPackDetailCall.ImageModel> images) {
        this.mContext = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView;
        ImageView imageView;

        this.mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert mLayoutInflater != null;
        itemView = mLayoutInflater.inflate(R.layout.content_top_banner, container, false);
        imageView = itemView.findViewById(R.id.home_top_banner_iv);
        itemView.findViewById(R.id.view_shadow).setVisibility(View.GONE);
        String url = ApiClient.BASE_URL+"/images/"+images.get(position).getImg();
        Glide.with(mContext).load(url).placeholder(R.drawable.default_image).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }


}
