package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityProfileView extends AppCompatActivity {

    private final String  TAG = "Ac ProView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        try{
            Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
            initView();
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }

    private void initView(){
        initToolbar();
        initProfile();
    }

    private void initProfile(){
        try{
            final JSONObject object = new JSONObject(MyFunction.getInstance().getText(this, Global.INFO_FILE));
            final TextView tv_name = findViewById(R.id.tv_name);
            final TextView tv_username = findViewById(R.id.tv_username);
            final TextView tv_email = findViewById(R.id.tv_email);
            final TextView tv_phone = findViewById(R.id.tv_phone);
            tv_name.setText(object.getString(Global.arData[30])+" "+object.getString(Global.arData[31]));
            tv_username.setText(object.getString(Global.arData[28]));
            tv_email.setText(object.getString(Global.arData[24]));
            tv_phone.setText(object.getString(Global.arData[26]));
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }

    private void initToolbar(){
        try{
            final ImageView iv_back = findViewById(R.id.iv_back);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyFunction.getInstance().finishActivity(ActivityProfileView.this);
                }
            });
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }
}