package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;

import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.SubPackagesModel;
import in.chardhamtour.chardhamyatra.view.activity.ExploreActivity;
import in.chardhamtour.chardhamyatra.view.activity.HolderActivity;

public class SubPackageAdapter extends RecyclerView.Adapter<SubPackageAdapter.ViewHolder>{

    private ArrayList<SubPackagesModel> data;
    private Context context;
    private int resourceLayout;

    public SubPackageAdapter(Context ctx, ArrayList<SubPackagesModel> dataModel, int resource) {
        context = ctx;
        data = dataModel;
        resourceLayout=resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resourceLayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubPackagesModel model=data.get(position);
        String name = model.getName();
        if(resourceLayout==R.layout.card_circular_cat_one){
            String[] arr = name.split(" ", 2);
            name=arr[0];
        }
            holder.nameTv.setText(name);

        holder.nameTv.setSelected(true);
        Log.d("sub-package",""+model.getImg());
        Glide.with(context).load(ApiClient.BASE_URL+"images/"+model.getImg()).placeholder(R.drawable.default_image).into(holder.imgIv);

        if(resourceLayout!=R.layout.card_circular_cat_one && resourceLayout!=R.layout.card_cat_three
                && resourceLayout!=R.layout.view_sub_package_list){
            String price="Starts "+Html.fromHtml("&#x20b9;")+model.getStarting_price()+"/-";
            Log.d("SubPackage","price:"+model.getStarting_price());
            if(model.getStarting_price()==null || model.getStarting_price().equalsIgnoreCase("0")
                    || model.getStarting_price().equalsIgnoreCase("")){
                price="NA "+Html.fromHtml("&#x20b9;")+"On Request";
            }holder.startPriceTv.setText(price);
        }

        if(resourceLayout==R.layout.view_sub_package_list){
            String price=Html.fromHtml("&#x20b9;")+""+model.getStarting_price()+"/-";
            if(model.getStarting_price()==null || model.getStarting_price().equalsIgnoreCase("0")
                    || model.getStarting_price().equalsIgnoreCase("")){
                price = Html.fromHtml("&#x20b9;")+" On Request";
            }holder.startPriceTv.setText(price);
        }

        if(resourceLayout==R.layout.view_sub_package_list || resourceLayout==R.layout.view_related_pack ){
           holder.placeDurationTv.setText(model.getTour_duration());
           holder.placeCoveredTv.setText(model.getDestination_covered());
        }

        if(resourceLayout==R.layout.view_related_pack){

            // R.id.helicopter, R.id.hotels,R.id.pickup,R.id.meal,R.id.sightSeeing
            int[] accommodationArray={
                    model.getHelicopter(),
                    model.getHotel(),
                    model.getPickup(),
                    model.getBreak_fast(),
                    model.getView()
            };
            Function.accommodationHandler(holder.mAccomadationView,accommodationArray);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv,startPriceTv;
        private TextView placeDurationTv,placeCoveredTv;
        private ImageView imgIv;
        private View mAccomadationView;
        private Button getQuoteBtn;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.placeName);
            imgIv = itemView.findViewById(R.id.placeImage);
            //for trending
            if(resourceLayout!=R.layout.card_circular_cat_one && resourceLayout!=R.layout.card_cat_three){
                startPriceTv=itemView.findViewById(R.id.placePrice);
            }
            if(resourceLayout==R.layout.view_related_pack){
                mAccomadationView=itemView.findViewById(R.id.accomadation);
                getQuoteBtn=itemView.findViewById(R.id.get_quotes);
                getQuoteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent holderIntent=new Intent(context, HolderActivity.class);
                        holderIntent.putExtra("from","quotes");
                        holderIntent.putExtra("object",data.get(getAdapterPosition()));
                        v.getContext().startActivity(holderIntent);
                    }
                });
            }
            if(resourceLayout==R.layout.view_sub_package_list || resourceLayout==R.layout.view_related_pack ){
                placeDurationTv=itemView.findViewById(R.id.placeDuration);
                placeCoveredTv=itemView.findViewById(R.id.placeCovered);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent exploreIntent=new Intent(context, ExploreActivity.class);
                    exploreIntent.putExtra("sub_id",data.get(getAdapterPosition()).getId());
                    exploreIntent.putExtra("pack_id",data.get(getAdapterPosition()).getPackage_id());
                    v.getContext().startActivity(exploreIntent);
                }
            });
        }
    }
}
