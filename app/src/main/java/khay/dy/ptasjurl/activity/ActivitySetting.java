package khay.dy.ptasjurl.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;


import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivitySetting extends ActivityController {

    private final String TAG = "Ac Setting";
    private LinearLayout linear_km,linear_en;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        try{
            Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
            initView();
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }

    private void findView(){
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
    }

    private void initBack(){
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
    private void initChangeLang(){
        linear_km.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunction.getInstance().saveText(ActivitySetting.this,Global.LANGUAGE,"km");
                gotoMain();
            }
        });
        linear_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunction.getInstance().saveText(ActivitySetting.this,Global.LANGUAGE,"en");
                gotoMain();
            }
        });
    }

    private void initLanguage(){
        try{
            final String lang = MyFunction.getInstance().getText(this,Global.LANGUAGE);
            if(lang.equals("km")){
                linear_km.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                linear_en.setBackgroundColor(getResources().getColor(R.color.grey));
            }else{
                linear_km.setBackgroundColor(getResources().getColor(R.color.grey));
                linear_en.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }

    private void initLogout(){
        final RelativeLayout relative_logout = findViewById(R.id.relative_logout);
        relative_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFunction.getInstance().saveText(ActivitySetting.this, Global.INFO_FILE,"");
                gotoMain();
            }
        });
    }

    private void gotoMain(){
        MyFunction.getInstance().finishActivity(ActivitySetting.this);
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        assert intent != null;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void setColorSwitch() {

        final SwitchCompat switch_notification = findViewById(R.id.switch_notification);
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
            Log.e(TAG, e.getMessage()+"");
        }
    }
}
