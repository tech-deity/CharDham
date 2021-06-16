package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.ItineraryModel;

public class ProgramVpAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ItineraryModel> programs;


    public ProgramVpAdapter(Context context, ArrayList<ItineraryModel> program) {
        this.mContext = context;
        this.programs = program;
    }

    @Override
    public int getCount() {
        return this.programs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        View itemView;
        final ImageView scroll_down;
        final TextView tourName,tourPrice,tourDesc,tourDuration,tourDestination;

        this.mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert mLayoutInflater != null;
        itemView = mLayoutInflater.inflate(R.layout.content_program, container, false);

        tourDesc=itemView.findViewById(R.id.placeDesc);
        scroll_down=itemView.findViewById(R.id.scroll_down_up);
        scroll_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.modifyViewVisibility(tourDesc,scroll_down,mContext);
            }
        });
        tourName=itemView.findViewById(R.id.placeName);
        tourDuration=itemView.findViewById(R.id.placeDuration);
        tourPrice=itemView.findViewById(R.id.placePrice);
        tourDestination=itemView.findViewById(R.id.placeCovered);

        ItineraryModel model=programs.get(position);
        tourName.setText(model.getTourName());
        tourPrice.setText(model.getTourPrice());
        tourDuration.setText(model.getTourDuration());
        tourDestination.setText(model.getDestinationCovered());
        tourDesc.setText(model.getTourDescription());

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


}
