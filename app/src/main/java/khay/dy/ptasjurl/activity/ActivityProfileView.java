package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.mtp.MtpConstants;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.my.MyImageLoader;

import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfileView extends ActivityController {

    private final String TAG = "Ac ProView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initView() {
        initToolbar();
        initProfile();
        initEdit();
    }

    private void initProfile() {
        try {
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final TextView tv_name = findViewById(R.id.tv_name);
            final TextView tv_username = findViewById(R.id.tv_username);
            final TextView tv_email = findViewById(R.id.tv_email);
            final TextView tv_phone = findViewById(R.id.tv_phone);
            final TextView tv_address = findViewById(R.id.tv_address);

            final String phone_dis = object.getString(Global.arData[26]).equals("null") ? "" : object.getString(Global.arData[26]);
            final String address_dis = object.getString(Global.arData[49]).equals("null") ? "" : object.getString(Global.arData[49]);
            final String username_dis = object.getString(Global.arData[28]).equals("null") ? "" : object.getString(Global.arData[28]);
            final String lname_dis = object.getString(Global.arData[31]).equals("null") ? "":object.getString(Global.arData[31]);
            final String fname_dis = object.getString(Global.arData[30]).equals("null") ? "":object.getString(Global.arData[30]);

            tv_name.setText(String.format("%s %s",fname_dis,lname_dis));
            tv_username.setText(username_dis);
            tv_email.setText(address_dis);
            tv_phone.setText(phone_dis);
            final ImageView iv_profile = findViewById(R.id.iv_profile);
            MyImageLoader.getInstance().setImage(iv_profile, object.getString(Global.arData[87]), null, 0, 0, -1, R.drawable.img_loading, R.drawable.img_loading);
            if (!object.getString(Global.arData[49]).equals("null"))
                tv_address.setText(object.getString(Global.arData[49]));
            else
                tv_address.setText("Phnom Penh");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initToolbar() {
        try {
            final ImageView iv_back = findViewById(R.id.iv_back);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyFunction.getInstance().finishActivity(ActivityProfileView.this);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }
    private void initEdit(){
        findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(ActivityProfileView.this,ActivityProfileEdit.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}