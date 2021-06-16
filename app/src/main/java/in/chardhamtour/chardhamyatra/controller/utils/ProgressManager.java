package in.chardhamtour.chardhamyatra.controller.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

import in.chardhamtour.chardhamyatra.R;

public class ProgressManager {

        private Context context;
        private AlertDialog alertDialog;

        public ProgressManager(Context context){
            this.context=context;
        }

        public void startProgress(String title,boolean isCancelable){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.layout_smallprogressbar, null);
            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(Objects.requireNonNull(alertDialog.getWindow()).getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            alertDialog.getWindow().setAttributes(lp);
            dialogBuilder.setCancelable(isCancelable);
            alertDialog.show();
            TextView txt=dialogView.findViewById(R.id.loadingTxt);
            txt.setText(title);

        }
        public void stopProgress(){
            alertDialog.dismiss();
        }
    }
