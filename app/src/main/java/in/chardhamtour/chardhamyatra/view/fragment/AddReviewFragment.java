package in.chardhamtour.chardhamyatra.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.ApiCall.CommonCall;
import in.chardhamtour.chardhamyatra.controller.api.ApiClient;
import in.chardhamtour.chardhamyatra.controller.api.ApiInterface;
import in.chardhamtour.chardhamyatra.controller.listeners.EnquiryDetail;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.ProgressManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddReviewFragment extends Fragment {
    private static final String TAG = AddReviewFragment.class.getSimpleName();
    private Context context;
    private ChardhamPreference preference;
    private EnquiryDetail enquiryDetailListener;
    private ToolbarListerner toolbarListerner;
    private View itemView;
    private ProgressManager progressManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView = inflater.inflate(R.layout.fragment_add_review, container, false);
        progressManager=new ProgressManager(context);

        preference=new ChardhamPreference(context);
        TextView topText=itemView.findViewById(R.id.top_text);
        topText.setText("Welcome "+preference.getUserName());
        toolbarListerner=(ToolbarListerner) context;
        enquiryDetailListener=(EnquiryDetail) context;
        itemView.findViewById(R.id.add_review_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
                itemView.findViewById(R.id.add_review_btn).setEnabled(false);
            }
        });
        return itemView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.context=activity;
    }


    private void addReview(){
        progressManager.startProgress("Adding Review,Please Wait",true);
        EditText Comment=itemView.findViewById(R.id.comment);
        final RatingBar ratingBar=itemView.findViewById(R.id.review_ratebar);
        ratingBar.getRating();

        if(!Comment.getText().toString().isEmpty()){
            JSONObject reviewObject=new JSONObject();
           try{
               reviewObject.put("user_id",preference.getUserId());
               reviewObject.put("sub_package_id",enquiryDetailListener.getEnquiryId());
               reviewObject.put("comment",Comment.getText().toString());
               reviewObject.put("rating",ratingBar.getRating());
               Log.d(TAG,"Review Added:"+Comment.getText().toString()+ratingBar.getRating()+preference.getUserId());

            Call<CommonCall> addReview=ApiClient.getRetrofit().create(ApiInterface.class).addReview(reviewObject.toString());
            addReview.enqueue(new Callback<CommonCall>() {
                @Override
                public void onResponse(Call<CommonCall> call, Response<CommonCall> response) {
                    if(response.body().getServ_res().equalsIgnoreCase("ok")){
                        Log.d(TAG,"Review msg:"+response.body().getMessage());
                        if(response.body().getMessage().equalsIgnoreCase("201")){
                            Log.d(TAG,"Review Added:");
                            progressManager.stopProgress();
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Thank You")
                                    .setMessage("Your Review has Added.Hope you are enjoying with us.")
                                    .setCancelable(false)
                                    .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            toolbarListerner.onBackPressedForFragment();
                                        }
                                    })
                                    .setIcon(R.drawable.ic_message_24dp)
                                    .show();
                        }
                    }else{
                        progressManager.stopProgress();
                        Log.d(TAG,"Review failed:Failed");
                    }
                }
                @Override
                public void onFailure(Call<CommonCall> call, Throwable t) {
                    Log.d(TAG,"Review failed:Server failed");
                    progressManager.stopProgress();
                    itemView.findViewById(R.id.add_review_btn).setEnabled(true);
                }
            });
           }catch (Exception e){
               progressManager.stopProgress();
               itemView.findViewById(R.id.add_review_btn).setEnabled(true);
               Toast.makeText(context, "Something Went Wrong! Failed to add review to this package", Toast.LENGTH_SHORT).show();
           }

        }else{
            itemView.findViewById(R.id.add_review_btn).setEnabled(true);
            Toast.makeText(context, "Please add empty fields", Toast.LENGTH_SHORT).show();
            progressManager.stopProgress();
        }
    }
}
