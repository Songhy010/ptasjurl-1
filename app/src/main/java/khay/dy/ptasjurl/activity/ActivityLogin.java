package khay.dy.ptasjurl.activity;


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

public class ActivityLogin extends ActivityController {

    private final String TAG = "Ac Login";
    private EditText edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        initLogin();
        initCreate();
        initHidePassword();
    }

    private void initHidePassword() {
        edt_password = findViewById(R.id.edt_password);
        final ImageView iv_eye = findViewById(R.id.iv_eye);
        MyFunction.getInstance().showHidePassword(this,iv_eye,edt_password);
    }

    private void initLogin() {
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        try {
            showDialog();
            final TextView tv_incorrect = findViewById(R.id.tv_incorrect);
            final EditText edt_username = findViewById(R.id.edt_username);
            final HashMap<String,String> param = new HashMap<>();
            param.put(Global.arData[28],edt_username.getText().toString());
            param.put(Global.arData[29], EasyAES.encryptString(edt_password.getText().toString()));
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[27], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        if(MyFunction.getInstance().isValidJSON(response)){
                            tv_incorrect.setVisibility(View.GONE);
                            MyFunction.getInstance().saveText(ActivityLogin.this,Global.INFO_FILE,response);
                            MyFunction.getInstance().openActivity(ActivityLogin.this, ActivityHome.class);
                            MyFunction.getInstance().finishActivity(ActivityLogin.this);
                        }else{
                            tv_incorrect.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    login();
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    private void initCreate() {
        findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFunction.getInstance().openActivity(ActivityLogin.this,ActivityRegister.class);
            }
        });
    }
}
