package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.EasyAES;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityRegister extends ActivityController {

    private final String TAG = "Ac Register";
    private EditText edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try{
            Tools.setSystemBarColor(this,R.color.colorPrimaryDark);
            initView();
        }catch (Exception e){
            Log.e(TAG,e.getMessage()+"");
        }
    }

    private void initView(){
        hidePassword();
        initBackToLogin();
        initRegister();
    }

    private void hidePassword() {
        edt_password = findViewById(R.id.edt_password);
        final ImageView iv_eye = findViewById(R.id.iv_eye);
        MyFunction.getInstance().showHidePassword(this,iv_eye,edt_password);
    }

    private void initRegister() {
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_password.getText().length()>5){
                    register();
                }else{
                    MyFunction.getInstance().alertMessage(ActivityRegister.this,getString(R.string.information),getString(R.string.ok),getString(R.string.six_char),1);
                }
            }
        });
    }

    private void initBackToLogin() {
        findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().finishActivity(ActivityRegister.this);
            }
        });
    }

    private void register(){
        try {
            showDialog();
            final EditText edt_phone = findViewById(R.id.edt_phone);
            final EditText edt_username = findViewById(R.id.edt_username);
            final HashMap<String,String> param = new HashMap<>();
            param.put(Global.arData[28],edt_username.getText().toString());
            param.put(Global.arData[26],edt_phone.getText().toString());
            param.put(Global.arData[29], edt_password.getText().toString());
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[38], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        final int code = Integer.parseInt(response);
                        if(code == 3){
                            MyFunction.getInstance().alertMessage(ActivityRegister.this,getString(R.string.information),getString(R.string.ok),getString(R.string.register_success),1);
                            MyFunction.getInstance().openActivity(ActivityRegister.this, ActivityLogin.class);
                            MyFunction.getInstance().finishActivity(ActivityRegister.this);
                        }else{
                            MyFunction.getInstance().alertMessage(ActivityRegister.this,getString(R.string.information),getString(R.string.ok),getString(R.string.user_existed),1);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    register();
                    Log.e(TAG, e.getMessage() + "");
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }
}
