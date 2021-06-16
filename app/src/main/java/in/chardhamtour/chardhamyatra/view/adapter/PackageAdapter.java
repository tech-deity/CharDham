package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.listeners.EnquiryDetail;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.view.activity.ExploreActivity;
import in.chardhamtour.chardhamyatra.view.activity.HolderActivity;
import in.chardhamtour.chardhamyatra.view.fragment.EnquiryFragment;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder> {

    private List<PackageModel> packages;
    private Context context;
    private EnquiryDetail enquiryDetailListener;
    private int rvLayout;

    public PackageAdapter(Context ctx,List<PackageModel> packages,int res){
        context=ctx;
        this.packages=packages;
        rvLayout=res;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(rvLayout,parent,false);
        enquiryDetailListener=(EnquiryDetail) context;
        return new PackageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {

        PackageModel packageModel =packages.get(position);

        if(rvLayout!=R.layout.view_search_item){
            Glide.with(context).load(ApiClient.BASE_URL+"images/"+packageModel.getBackgroundImage()).placeholder(R.drawable.default_image).into(holder.background_image);
            /*2563+ booking | 1632+ agents*/
        }
        holder.packageName.setText(packageModel.getName());

    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    public void clear(){
        packages.clear();
        notifyDataSetChanged();
    }

    class PackageViewHolder extends RecyclerView.ViewHolder {
        private TextView packageName;
        private ImageView background_image;
        /*2563+ booking | 1632+ agents*/

        PackageViewHolder(@NonNull View itemView){
            super(itemView);
            packageName=itemView.findViewById(R.id.pack_name);

            if(rvLayout!=R.layout.view_search_item){
                background_image=itemView.findViewById(R.id.pack_image);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rvLayout==R.layout.view_search_item){
                        if(enquiryDetailListener.isEnquiryFrag()){
                            enquiryDetailListener.setEnquiryId(packages.get(getAdapterPosition()).getId());
                            enquiryDetailListener.setDestinationName(packages.get(getAdapterPosition()).getName());
                            enquiryDetailListener.setEnquiryFrag(false);
                            Function.fragTransactionNoBackStack(R.id.holder_frame,new EnquiryFragment(),context);
                        }else{
                            Intent exploreIntent=new Intent(context, ExploreActivity.class);
                            exploreIntent.putExtra("sub_id",packages.get(getAdapterPosition()).getId());
                            v.getContext().startActivity(exploreIntent);
                        }
                    }else {
                        Intent holderIntent=new Intent(context, HolderActivity.class);
                        holderIntent.putExtra("from","package");
                        holderIntent.putExtra("object",packages.get(getAdapterPosition()));
                        v.getContext().startActivity(holderIntent);
                    }
                }
            });


        }
    }
}
