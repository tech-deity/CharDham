package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.PackageModel;
import in.chardhamtour.chardhamyatra.view.activity.ExploreActivity;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NVH> {

    private ArrayList<PackageModel> data;
    private Context ctx;

    public NotificationAdapter(Context ctx, ArrayList<PackageModel> notification){
        this.ctx=ctx;
        data=notification;
    }

    @NonNull
    @Override
    public NVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_notification_layout,parent,false);
        return new NVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NVH holder, int position) {
        Function.addHtmlToTextView(holder.htmlText,data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NVH extends RecyclerView.ViewHolder{
        private TextView htmlText;
        NVH(@NonNull View itemView) {
            super(itemView);
            htmlText=itemView.findViewById(R.id.notify_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(getAdapterPosition()).getId()!=0){
                        Intent explore=new Intent(ctx, ExploreActivity.class);
                        explore.putExtra("sub_id",data.get(getAdapterPosition()).getId());
                        ctx.startActivity(explore);
                    }else {
                        Function.toast(v.getContext(),"Nothing to show", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }
}
