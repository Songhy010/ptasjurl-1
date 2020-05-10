package khay.dy.ptasjurl.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

import khay.dy.ptasjurl.R;
import khay.dy.ptasjurl.listener.VolleyCallback;
import khay.dy.ptasjurl.util.ConstantStatus;
import khay.dy.ptasjurl.util.EasyAES;
import khay.dy.ptasjurl.util.Global;
import khay.dy.ptasjurl.util.MyFunction;
import khay.dy.ptasjurl.util.Tools;

public class ActivityForgotPassword extends ActivityController {

    private final String TAG = "ForgotPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            finish();
        }
    }

    private void initView() {
        initLogin();
        initForgot();
    }

    private void initForgot() {
        final LinearLayout btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                getToken(new GetTokenListener() {
                    @Override
                    public void onGot(String token) {
                        try {
                            final EditText edt_phone = findViewById(R.id.edt_phone);
                            final HashMap<String,String> param = new HashMap<>();
                            param.put(Global.arData[26],edt_phone.getText().toString().trim());
                            param.put("token",token);

                            final String url = Global.arData[0] + Global.arData[1] + String.format(Global.arData[2], Global.arData[45], Global.arData[89]);
                            MyFunction.getInstance().requestString(Request.Method.POST, url, param, new VolleyCallback() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        final int code = Integer.parseInt(response);
                                        if(code == 1){
                                            MyFunction.getInstance().openActivityForResult(ActivityForgotPassword.this,ActivityNewPassword.class,param, ConstantStatus.ActivityNewPassword);
                                        }else{
                                            final TextView tv_phone = findViewById(R.id.tv_phone);
                                            tv_phone.setVisibility(View.VISIBLE);
                                        }

                                    } catch (Exception e) {
                                        MyFunction.getInstance().openActivityForResult(ActivityForgotPassword.this,ActivityNewPassword.class,param, ConstantStatus.ActivityNewPassword);
                                    }
                                    hideDialog();
                                }

                                @Override
                                public void onErrorResponse(VolleyError e) {
                                    Log.e(TAG, e.getMessage() + "");
                                    initForgot();
                                    hideDialog();
                                }
                            });
                        } catch (Exception e) {
                            hideDialog();
                            Log.e(TAG, "" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void initLogin() {
        final TextView tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getToken(final GetTokenListener getTokenListener) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("task", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        final String deviceId = MyFunction.getInstance().getAndroidID(ActivityForgotPassword.this);
                        Log.e("Device ID", deviceId);
                        final String token = task.getResult().getToken();
                        Log.e("Token", token);
                        getTokenListener.onGot(token);
                    }
                });
    }


    public interface GetTokenListener{
        void onGot(String token);
    }
}
