package in.chardhamtour.chardhamyatra.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.listeners.ToolbarListerner;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolbarFragment extends Fragment {

    private View toolbar,itemView;
    private Context context;
    private ToolbarListerner toolbarListerner;

    public ToolbarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       itemView = inflater.inflate(R.layout.fragment_toolbar, container, false);
        setUpFragment();
       return itemView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
       this.context=context;
        super.onAttach(context);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        this.context= activity;
        super.onAttach(activity);
    }

    private void setUpFragment() {

            toolbarListerner=(ToolbarListerner) context;
            toolbar=itemView.findViewById(R.id.fragment_toolbar);
            toolbar.findViewById(R.id.toolbar_go_back_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toolbarListerner.onBackPressedForFragment();
                }
            });
            ChardhamPreference preference=new ChardhamPreference(context);
            TextView toolbarTitle=toolbar.findViewById(R.id.toolbar_back_title);
            String from=toolbarListerner.getToolbarTitle();

            TextView htmlTv=itemView.findViewById(R.id.fragment_htmlTv);
            htmlTv.setMovementMethod(new ScrollingMovementMethod());

        if(from.equalsIgnoreCase("t_c")){
                toolbarTitle.setText(getResources().getText(R.string.tc));
                Function.addHtmlToTextView(htmlTv,preference.getTC());
            }
            if(from.equalsIgnoreCase("p_p")){
                toolbarTitle.setText(getResources().getText(R.string.pp));
                Function.addHtmlToTextView(htmlTv,preference.getPP());
            }
        if(from.equalsIgnoreCase("hiw")){
            toolbarTitle.setText(getResources().getText(R.string.wcu));
            Function.addHtmlToTextView(htmlTv,preference.getHIW());
        }
        if(from.equalsIgnoreCase("faq")){
            toolbarTitle.setText(getResources().getText(R.string.faq));
            Function.addHtmlToTextView(htmlTv,preference.getFaq());
        }
    }

}
