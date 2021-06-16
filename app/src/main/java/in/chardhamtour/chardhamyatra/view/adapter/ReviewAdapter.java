package in.chardhamtour.chardhamyatra.view.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.model.ReviewModel;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CommentViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private ArrayList<ReviewModel> review_data;
    private Context ctx;

    public ReviewAdapter(Context ctx, ArrayList<ReviewModel> reviews){
        this.ctx=ctx;
        review_data=reviews;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Log.d(TAG,"review:User"+review_data.get(position).getUser_name());
        holder.com_username.setText(review_data.get(position).getUser_name());
        holder.com_rating_bar.setRating(Float.valueOf(review_data.get(position).getRatings()));
        holder.com_ratings_txt.setText(""+Float.parseFloat(review_data.get(position).getRatings()));
        holder.com_comment.setText(review_data.get(position).getComment());
        holder.com_date.setText(review_data.get(position).getC_datetime());
        Glide.with(ctx).load(ApiClient.BASE_URL+"images/profile/"+review_data.get(position).getUser_image()).placeholder(R.drawable.default_user).into(holder.com_pro);
    }

    @Override
    public int getItemCount() {
        return review_data.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView com_username;
        private TextView com_ratings_txt;
        private CircleImageView com_pro;
        private TextView com_comment;
        private RatingBar com_rating_bar;
        private TextView com_date;


        CommentViewHolder(View itemView) {
            super(itemView);
            com_username=itemView.findViewById(R.id.review_reviewer_name);
            com_comment=itemView.findViewById(R.id.review);
            com_ratings_txt=itemView.findViewById(R.id.review_reviewer_rating);
            com_rating_bar=itemView.findViewById(R.id.review_ratebar);
            com_pro=itemView.findViewById(R.id.review_profile);
            com_date=itemView.findViewById(R.id.review_time);
        }
    }
}
