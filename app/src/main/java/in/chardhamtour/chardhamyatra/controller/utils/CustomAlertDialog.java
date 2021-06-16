package in.chardhamtour.chardhamyatra.controller.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import in.chardhamtour.chardhamyatra.view.activity.LoginAndRegisterActivity;

public class CustomAlertDialog extends DialogFragment {

    private Context ctx;
    private String title,msg;
    private boolean isLoginBtn;

    public CustomAlertDialog(Context ctx,String title,String msg,boolean isLoginBtn){
            this.ctx=ctx;
            this.title=title;
            this.msg=msg;
            this.isLoginBtn=isLoginBtn;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        String positiveBtnTxt="Ok";
        String negativeBtnTxt="Cancel";

        if(isLoginBtn){
            positiveBtnTxt="Login Now";
            negativeBtnTxt="Not now";
        }

        builder.setPositiveButton(positiveBtnTxt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(isLoginBtn){
                    startActivity(new Intent(ctx, LoginAndRegisterActivity.class));
                }
            }
        });

        if(isLoginBtn){
            builder.setNegativeButton(negativeBtnTxt ,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }

        return builder.create();
    }
}
