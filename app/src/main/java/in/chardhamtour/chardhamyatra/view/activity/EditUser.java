package in.chardhamtour.chardhamyatra.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import in.chardhamtour.chardhamyatra.R;
import in.chardhamtour.chardhamyatra.controller.utils.ChardhamPreference;
import in.chardhamtour.chardhamyatra.controller.utils.Function;
import in.chardhamtour.chardhamyatra.model.Model;

public class EditUser extends AppCompatActivity {


    private EditText nameEt,mobileEt;
    private ImageView imageView;
    private static String TAG="EditUser";
    private static int IMG_REQUEST=111;
    private Context ctx;
    private ProgressDialog prog;
    private ChardhamPreference preference;
    private String profile_image=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        preference=new ChardhamPreference(this);
        ctx=this;
        init();
        setDataToField();
    }


    private void setDataToField(){

    }

    private void init() {

        View toolbar = findViewById(R.id.toolbar);
        TextView headToolbar = toolbar.findViewById(R.id.toolbar_back_title);
        headToolbar.setText("Edit Profile");
        ImageView backIv = toolbar.findViewById(R.id.toolbar_go_back_iv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.edit_change_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        imageView = findViewById(R.id.editUserImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        TextView updateTv = findViewById(R.id.updateUser);
        nameEt = findViewById(R.id.edit_name);

        mobileEt = findViewById(R.id.edit_mobile);

        updateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog=new ProgressDialog(EditUser.this);
                prog.show();
                //uploadData();
            }
        });

    }




    private void selectImage(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,IMG_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                profile_image= Function.bitmapToBase64String(bitmap);
                Log.d(TAG,profile_image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private Model getUserFromField(){
        String name,phone;
//        user=new UserModel();
//        user.setUser_id(new PreConfig(ctx).readUserId());
        name=nameEt.getText().toString();
        phone=mobileEt.getText().toString();
        if(profile_image!=null){
            //ImageUpdated
            //user.setProfile_img(UtilFun.UPLOAD_URL+"GKD_"+user.getUser_id()+".jpg");
            //Log.d(TAG,"Updation:"+user.getProfile_img());
        }else{
            //NoChange
           // user.setProfile_img(new PreConfig(ctx).readProfileImage());
            profile_image="404";
        }

//        if(!name.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !address.isEmpty()){
////            user.setName(name);
////            user.setAddress(address);
////            user.setPhone(phone);
////            user.setEmail(email);
//            Log.d(TAG,"Fields are OK");
//        }else{
//            Toast.makeText(this,"Fields Cannot be Empty",Toast.LENGTH_SHORT).show();
//            return null;
//        }

        return null;
        //return user;
    }


}
