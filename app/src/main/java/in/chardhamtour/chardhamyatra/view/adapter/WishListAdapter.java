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

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.view.activity.ExploreActivity;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private ArrayList<PackageModel> data;
    private Context context;
    private int resourceLayout;

    public WishListAdapter(Context ctx, ArrayList<PackageModel> dataModel, int resource) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
         PackageModel model=data.get(position);
         holder.nameTv.setText(model.getName());
         Glide.with(context)
                .load(ApiClient.BASE_URL+"/images/"+model.getBackgroundImage())
                .placeholder(R.drawable.default_image).into(holder.imgIv);
        holder.favIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function.modifyWishlist(v.getContext(),new ChardhamPreference(v.getContext()).getUserId(),data.get(position).getId(),holder.favIv);
                Function.changeImageViewSrc(context,holder.favIv,R.drawable.baseline_favorite_border_24);
                removeAt(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv;
        private ImageView imgIv;
        private ImageView favIv;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.placeName);
            imgIv = itemView.findViewById(R.id.placeImage);
            favIv=itemView.findViewById(R.id.wish_fav_iv);
            Function.changeImageViewSrc(itemView.getContext(),favIv,R.drawable.baseline_favorite_24);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()!=R.id.wish_fav_iv){
                        Intent exploreIntent=new Intent(context, ExploreActivity.class);
                        exploreIntent.putExtra("sub_id",data.get(getAdapterPosition()).getId());
                        v.getContext().startActivity(exploreIntent);
                    }
                }
            });
        }
    }

    private void removeAt(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }
}
