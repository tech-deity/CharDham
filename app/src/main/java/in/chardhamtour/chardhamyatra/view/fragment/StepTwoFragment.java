package in.chardhamtour.chardhamyatra.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.listeners.BookingListener;
import in.chardhamtour.chardhamyatra.controller.listeners.IProgressBarListener;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.model.MemberModel;
import in.chardhamtour.chardhamyatra.view.activity.PaymentActivity;

public class StepTwoFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "StepTwoFragment";
    private View itemView;
    private TextView addTv,removeTv;
    private ViewGroup memberGroupView;
    private Context context;
    private List<View> views;
    private ChardhamPreference preference;
    private Button finalPaymentBtn;
    private BookingListener bookingListener;
    private List<MemberModel> memberList;
    private IProgressBarListener interfaceProBar;

    public StepTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView= inflater.inflate(R.layout.fragment_step_two, container, false);
        bookingListener=(BookingListener)context;
        finalPaymentBtn=itemView.findViewById(R.id.pay_button);
        finalPaymentBtn.setOnClickListener(this);
        preference=new ChardhamPreference(context);
        memberList=new ArrayList<>();
        interfaceProBar=(IProgressBarListener) context;

        TextView toolTitle=itemView.findViewById(R.id.toolbar_back_title);
        ImageView backBtn=itemView.findViewById(R.id.toolbar_go_back_iv);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        toolTitle.setText("Booking Details");

        memberGroupView=itemView.findViewById(R.id.member_parent_layout);
        views=new ArrayList<>();
        View member = LayoutInflater.from(context).inflate(R.layout.view_add_member_detail, memberGroupView, false);
        memberGroupView.addView(member);
        views.add(member);

        TextView name=member.findViewById(R.id.name);
        name.setText(preference.getUserName());
        Spinner spinner=views.get(0).findViewById(R.id.gender_spinner);
        setUpSpinner(spinner);
        initListeners();
        manageView();
        return itemView;
    }

    private void setUpSpinner(Spinner spinner){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.addMemberTv:
                addLayout();
                manageView();
            break;

            case R.id.removeMember:
                removeLayout();
                manageView();
                break;

            case R.id.pay_button:
                saveData();
                break;
        }
    }


    private void saveData() {
        boolean allField=true;
        memberList.clear();
        interfaceProBar.startProgressBar("Saving...");
        for(View layout:views){
            EditText nameEt,ageEt;
            Spinner genderSpinner;

            nameEt=layout.findViewById(R.id.name);
            ageEt=layout.findViewById(R.id.age);
            genderSpinner=layout.findViewById(R.id.gender_spinner);

            String name=nameEt.getText().toString();
            String age=ageEt.getText().toString();
            String gender=genderSpinner.getSelectedItem().toString();

            Log.d(TAG,"name:"+name+" age:"+age+"\n gender:"+gender+"\n");

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !gender.equalsIgnoreCase("Gender")){
                MemberModel model=new MemberModel();
                model.setName(name);
                model.setAge(age);
                model.setGender(gender);
                memberList.add(model);
                Log.d(TAG,"size:"+memberList.size());
            }else {
                allField=false;
            }
        }

        if(allField){
            Intent startPayment=new Intent(context,PaymentActivity.class);
            startPayment.putExtra("subpackage",bookingListener.getSubPackage());
            startPayment.putExtra("program",bookingListener.getProgram());
            Bundle args = new Bundle();
            args.putSerializable("memberlist",(Serializable)memberList);
            startPayment.putExtra("BUNDLE",args);
            startPayment.putExtra("step_one_data",bookingListener.getStepOneData());
            interfaceProBar.stopProgressBar();
            startActivity(startPayment);
        }else{
            new AlertDialog.Builder(getContext())
                    .setTitle("Required Fields are Empty")
                    .setMessage("Please fill all the member details for processing your booking")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            interfaceProBar.stopProgressBar();
            memberList.clear();
        }

    }


    private void manageView() {
        int size=views.size();
        if(size>1){
            removeTv.setVisibility(View.VISIBLE);
            if(size>5){
                addTv.setVisibility(View.GONE);
            }else {
                addTv.setVisibility(View.VISIBLE);
            }
        }else {
            removeTv.setVisibility(View.GONE);
        }
        String priceString="Total Payment "+Html.fromHtml("&#x20b9;")+(views.size()*Integer.parseInt(bookingListener.getProgram().getTourPrice()));
        finalPaymentBtn.setText(priceString);
    }

    private void initListeners() {
        addTv=itemView.findViewById(R.id.addMemberTv);
        addTv.setOnClickListener(this);
        removeTv=itemView.findViewById(R.id.removeMember);
        removeTv.setOnClickListener(this);
    }

    private void addLayout() {
        View member = LayoutInflater.from(context).inflate(R.layout.view_add_member_detail, memberGroupView, false);
        memberGroupView.addView(member);
        views.add(member);
        Spinner spinner=views.get(views.size()-1).findViewById(R.id.gender_spinner);
        setUpSpinner(spinner);
    }

    private void removeLayout(){
        int size=views.size();
        if(size>1){
            memberGroupView.removeView(views.get(size-1));
            views.remove(views.get(size-1));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context=context;
    }

}


