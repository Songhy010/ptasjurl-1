package khay.dy.ptasjurl.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.AlertListenner;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.EasyAES;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityChangePassword extends ActivityController {

    final String TAG = "Ac ChangePWD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        try {
            Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
            initView();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() + "");
        }
    }

    private void initView() {
        initBack();
        initChangePass();
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

    private void initChangePass() {
        final EditText old_password = findViewById(R.id.old_password);
        final EditText new_password = findViewById(R.id.new_password);
        final EditText con_password = findViewById(R.id.con_password);
        final ImageView iv_eye1 = findViewById(R.id.iv_eye1);
        final ImageView iv_eye2 = findViewById(R.id.iv_eye2);
        final ImageView iv_eye3 = findViewById(R.id.iv_eye3);
        MyFunction.getInstance().showHidePassword(this, iv_eye1, old_password);
        MyFunction.getInstance().showHidePassword(this, iv_eye2, new_password);
        MyFunction.getInstance().showHidePassword(this, iv_eye3, con_password);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String oldPass = old_password.getText().toString();
                final String newPass = new_password.getText().toString();
                final String conPass = con_password.getText().toString();
                if (!oldPass.isEmpty() && !newPass.isEmpty() && !conPass.isEmpty()) {
                    if (newPass.length() > 5) {
                        if (newPass.equals(conPass)) {
                            try {
                                final JSONObject obj = new JSONObject(MyFunction.getInstance().getText(ActivityChangePassword.this, Global.INFO_FILE));
                                final String uid = obj.getString(Global.arData[33]);
                                final String enOldPass = EasyAES.encryptString(oldPass);
                                final String enNewPass = EasyAES.encryptString(newPass);
                                loadChangePass(enOldPass, enNewPass, uid);
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage() + "");
                            }
                        } else
                            MyFunction.getInstance().alertMessage(ActivityChangePassword.this, getString(R.string.information), getString(R.string.ok), getString(R.string.pass_not_match), 1);
                    } else
                        MyFunction.getInstance().alertMessage(ActivityChangePassword.this, getString(R.string.information), getString(R.string.ok), getString(R.string.six_char), 1);
                } else
                    MyFunction.getInstance().alertMessage(ActivityChangePassword.this, getString(R.string.information), getString(R.string.ok), getString(R.string.required_field), 1);
            }
        });
    }

    private void loadChangePass(final String oldPass, final String newPass, final String uid) {
        final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[45], Global.arData[46]);
        final HashMap<String, String> param = new HashMap<>();
        param.put(Global.arData[33], uid);
        param.put(Global.arData[47], oldPass);
        param.put(Global.arData[48], newPass);
        showDialog();
        MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
            @Override
            public void onResponse(String response) {
                try {
                    final int code = Integer.parseInt(response);
                    if (code == 3) {
                        MyFunction.getInstance().finishActivity(ActivityChangePassword.this);
                    }else{
                        final TextView tv_incorrect = findViewById(R.id.tv_incorrect);
                        tv_incorrect.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                hideDialog();
            }

            @Override
            public void onErrorResponse(VolleyError e) {
                Log.e(TAG, e.getMessage() + "");
                hideDialog();
            }
        });
    }
}