package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.StringModel;
import in.chardhamtour.chardhamyatra.controller.utils.Function;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.ViewHolder> {

    private ArrayList<StringModel> data;
    private Context context;
    private int resourceLay;

    public QueryAdapter(ArrayList<StringModel> model, Context context ,int resourceLayout) {
        this.data = model;
        this.context = context;
        resourceLay=resourceLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(resourceLay,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Function.addHtmlToTextView(holder.nameTv,data.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
        }
    }
}
