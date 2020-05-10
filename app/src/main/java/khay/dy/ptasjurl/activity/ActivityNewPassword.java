package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.EasyAES;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityNewPassword extends ActivityController {
    final String TAG = "Ac NewPass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    private void initView() {
        initBack();
        initNewPass();
    }
    private void initBack() {
        final ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void initNewPass() {
        final LinearLayout btn_confirm = findViewById(R.id.btn_confirm);
        final EditText edt_code = findViewById(R.id.edt_code);
        final EditText new_password = findViewById(R.id.new_password);
        final EditText con_password = findViewById(R.id.con_password);
        final ImageView iv_eye2 = findViewById(R.id.iv_eye2);
        final ImageView iv_eye3 = findViewById(R.id.iv_eye3);
        MyFunction.getInstance().showHidePassword(this, iv_eye2, new_password);
        MyFunction.getInstance().showHidePassword(this, iv_eye3, con_password);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_code.getText().toString().trim().equals("") && !new_password.getText().toString().trim().equals("") && !con_password.getText().toString().trim().equals("")) {
                    if (new_password.getText().toString().trim().equals(con_password.getText().toString().trim())) {
                        if (new_password.getText().toString().trim().length() > 5) {
                            final HashMap<String,String> param = MyFunction.getInstance().getIntentHashMap(getIntent());
                            final String phone = param.get(Global.arData[26]);
                            final String new_pass = EasyAES.encryptString(new_password.getText().toString().trim());
                            final String code = edt_code.getText().toString().trim();
                            initPass(phone,new_pass,code);
                        } else
                            MyFunction.getInstance().alertMessage(ActivityNewPassword.this, getString(R.string.information), getString(R.string.ok), getString(R.string.six_char), 1);
                    } else
                        MyFunction.getInstance().alertMessage(ActivityNewPassword.this, getString(R.string.information), getString(R.string.ok), getString(R.string.pass_not_match), 1);
                } else
                    MyFunction.getInstance().alertMessage(ActivityNewPassword.this, getString(R.string.information), getString(R.string.ok), getString(R.string.required_field), 1);

            }
        });
    }

    private void initPass(final String phone, final String new_pass, final String code) {
        try {
            showDialog();
            final HashMap<String,String> param = new HashMap<>();
            param.put(Global.arData[26],phone);
            param.put(Global.arData[48],new_pass);
            param.put(Global.arData[91],code);

            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[45], Global.arData[92]);
            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                @Override
                public void onResponse(String response) {
                    try {
                        if(MyFunction.getInstance().isValidJSON(response)){
                            setResult(RESULT_OK);
                            MyFunction.getInstance().finishActivity(ActivityNewPassword.this);
                        }else{
                            MyFunction.getInstance().alertMessage(ActivityNewPassword.this,getString(R.string.information),getString(R.string.ok),getString(R.string.wrong_code),1);
                        }

                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage()+"");
                    }
                    hideDialog();
                }

                @Override
                public void onErrorResponse(VolleyError e) {
                    Log.e(TAG, e.getMessage() + "");
                    initPass(phone,new_pass,code);
                    hideDialog();
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.e(TAG, "" + e.getMessage());
        }
    }
}
