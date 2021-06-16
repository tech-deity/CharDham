package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.view.fragment.HomeFragment;

public class StaticDataAdapter extends PagerAdapter {

    private Context context;
    private List<HomeFragment.StaticBannerObject> objectData;

    public StaticDataAdapter(Context context, List<HomeFragment.StaticBannerObject> objectData) {
        this.context = context;
        this.objectData = objectData;
    }

    @Override
    public int getCount() {
        return objectData.size();
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
        TextView titleView;
        TextView subTitleView;

        LayoutInflater mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        itemView = mLayoutInflater.inflate(R.layout.view_static_pager_view, container, false);
        imageView = itemView.findViewById(R.id.image);
        titleView=itemView.findViewById(R.id.title);
        subTitleView=itemView.findViewById(R.id.sub_title);
        Function.changeImageViewSrc(context,imageView,objectData.get(position).getDrawable());
        titleView.setText(objectData.get(position).getTitle());
        subTitleView.setText(objectData.get(position).getSubTitle());
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}
