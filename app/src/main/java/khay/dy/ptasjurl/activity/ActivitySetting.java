package khay.dy.ptasjurl.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivitySetting extends ActivityController {

    private final String TAG = "Ac Setting";
    private LinearLayout linear_km, linear_en;
    private SwitchCompat switch_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == 3){
                gotoMain();
            }else if(requestCode == ConstantStatus.ActivitySetting){
                if(MyFunction.getInstance().isHistory(ActivitySetting.this)){
                    MyFunction.getInstance().openActivity(ActivitySetting.this, ActivityChangePassword.class);
                }
            }
        }
    }

    private void findView() {
        linear_en = findViewById(R.id.linear_en);
        linear_km = findViewById(R.id.linear_km);
    }

    private void initView() {
        findView();
        initBack();
        initLogout();
        initLanguage();
        initChangeLang();
        setColorSwitch();
        initTurnOffNotification();
        initChangePassword();
    }

    private void initBack() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        final TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.setting));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initChangeLang() {
        linear_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunction.getInstance().saveText(ActivitySetting.this, Global.LANGUAGE, "km");
                gotoMain();
            }
        });
        linear_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunction.getInstance().saveText(ActivitySetting.this, Global.LANGUAGE, "en");
                gotoMain();
            }
        });
    }

    private void initLanguage() {
        try {
            final String lang = MyFunction.getInstance().getText(this, Global.LANGUAGE);
            if (lang.equals("km") || lang.isEmpty()) {
                linear_km.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                linear_en.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                linear_km.setBackgroundColor(getResources().getColor(R.color.grey));
                linear_en.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initLogout() {
        final RelativeLayout relative_logout = findViewById(R.id.relative_logout);
        final TextView tv_log = findViewById(R.id.tv_log);
        if (MyFunction.getInstance().isHistory(ActivitySetting.this))
            tv_log.setText(getString(R.string.logout));
        else
            tv_log.setText(getString(R.string.login));

        relative_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyFunction.getInstance().isHistory(ActivitySetting.this)) {
                    MyFunction.getInstance().saveText(ActivitySetting.this, Global.INFO_FILE, "");
                    gotoMain();
                } else {
                    MyFunction.getInstance().openActivityForResult(ActivitySetting.this,ActivityLogin.class,null,3);
                }

            }
        });
    }

    private void gotoMain() {
        MyFunction.getInstance().finishActivity(ActivitySetting.this);
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert intent != null;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void setColorSwitch() {

        switch_notification = findViewById(R.id.switch_notification);
        try {
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked},
            };

            int[] thumbColors = new int[]{
                    getResources().getColor(R.color.grey_4),
                    getResources().getColor(R.color.colorPrimary),
            };

            int[] trackColors = new int[]{
                    getResources().getColor(R.color.grey),
                    getResources().getColor(R.color.colorPrimaryDark),
            };

            DrawableCompat.setTintList(DrawableCompat.wrap(switch_notification.getThumbDrawable()), new ColorStateList(states, thumbColors));
            DrawableCompat.setTintList(DrawableCompat.wrap(switch_notification.getTrackDrawable()), new ColorStateList(states, trackColors));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initTurnOffNotification() {
        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck) {
                    turnOffNotification("1");
                } else {
                    turnOffNotification("0");
                }
            }
        });
    }

    private void turnOffNotification(final String status) {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[34], Global.arData[40]);
        final String deviceId = MyFunction.getInstance().getAndroidID(ActivitySetting.this);
        final HashMap<String, String> param = new HashMap<>();
        param.put(Global.arData[41], deviceId);
        param.put(Global.arData[43], status);
        MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    final int code = Integer.parseInt(response);
                    if (code == 3) {
                        Log.e("Success", response);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage() + "");
                turnOffNotification(status);
            }
        });
    }

    private void initChangePassword() {
        findViewById(R.id.relative_change_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!MyFunction.getInstance().isHistory(ActivitySetting.this)){
                    MyFunction.getInstance().alertMessage(ActivitySetting.this, getString(R.string.login_required), new AlertListenner() {
                        @Override
                        public void onSubmit() {
                            MyFunction.getInstance().openActivityForResult(ActivitySetting.this,ActivityLogin.class,null, ConstantStatus.ActivitySetting);
                        }
                    },1);
                }else{
                   MyFunction.getInstance().openActivity(ActivitySetting.this, ActivityChangePassword.class);
                }
            }
        });
    }
}
