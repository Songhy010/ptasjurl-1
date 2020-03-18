package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.EasyAES;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityLogin extends ActivityController {

    private final String TAG = "Ac Login";

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
            final EditText edt_username = findViewById(R.id.edt_username);
            final EditText edt_password = findViewById(R.id.edt_password);
            final HashMap<String,String> param = new HashMap<>();
            param.put(Global.arData[28],edt_username.getText().toString());
            param.put(Global.arData[29], EasyAES.encryptString(edt_password.getText().toString()));
            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[27], Global.arData[5]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e(TAG, response);
                        if(MyFunction.getInstance().isValidEmail(response)){
                            MyFunction.getInstance().saveText(ActivityLogin.this,Global.INFO_FILE,response);
                            MyFunction.getInstance().openActivity(ActivityLogin.this, ActivityHome.class);
                            MyFunction.getInstance().finishActivity(ActivityLogin.this);
                        }else{
                            Toast.makeText(ActivityLogin.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                        }
                        
                    } catch (Exception e) {
                        Log.e(TAG, "" + e.getMessage());
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            login();
            Log.e(TAG, "" + e.getMessage());
        }
    }

    private void initCreate() {
        findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyFunction.getInstance().openActivity(ActivityLogin.this,ActivityDonate.class);
            }
        });
    }
}
